package io.github.steaf23.ancientwarfare.npc.entity.playerowned;

import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWActivities;
import io.github.steaf23.ancientwarfare.core.util.EntityHelper;
import io.github.steaf23.ancientwarfare.core.versioned.BrainFactory;
import io.github.steaf23.ancientwarfare.npc.command.NpcCommand;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.faction.FactionNpc;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.upkeep.NpcUpkeep;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.upkeep.UpkeepValueProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerOwnedNpc extends BaseNpc implements TraceableEntity {

	public static BrainFactory<BaseNpc> BRAIN_MAKER = new BrainFactory<>(
			PlayerOwnedNpcAi.MEMORY_MODULES,
			PlayerOwnedNpcAi.SENSORS,
			npc -> PlayerOwnedNpcAi.getActivities());

	//FIXME: REFACTOR Command into memory module
	NpcCommand.Command currentPlayerCommand = NpcCommand.Command.NONE;

	boolean followingPlayer;
	UUID owner;
	NpcJob job;

	NpcUpkeep upkeep;
	// Block to use for upkeep if no order has been set.
	BlockPos autoUpkeepBlock = null;

	public PlayerOwnedNpc(EntityType<? extends PathfinderMob> entityType, Level world) {
		super(entityType, world);

		this.upkeep = new NpcUpkeep(6000, new UpkeepValueProvider.FoodProvider());
	}

	@Override
	public BrainFactory<BaseNpc> initBrainMaker() {
		return BRAIN_MAKER;
	}

	public void handlePlayerCommand(NpcCommand.Command command) {
		if (command.type == NpcCommand.CommandType.ATTACK) {
			Entity e = command.getEntityTarget(level());
			if (e instanceof LivingEntity living) {
				if (canAttack(living)) //only attacked allowed targets
				{
					setTarget(living);
				}
			}
			setPlayerCommand(NpcCommand.Command.NONE);
			return;
		}
		setPlayerCommand(command);
	}

	public NpcUpkeep upkeep() {
		return upkeep;
	}

	public NpcCommand.Command getCurrentCommand() {
		return currentPlayerCommand;
	}

	public void setPlayerCommand(NpcCommand.Command command) {
		currentPlayerCommand = command;
	}

	public void setAutoUpkeepBlock(@Nullable BlockPos pos) {
		autoUpkeepBlock = pos;
		getBrain().setMemory(MemoryModuleType.HOME, GlobalPos.of(level().dimension(), pos));
		getBrain().setActiveActivityIfPossible(AWActivities.UPKEEP);
	}

	public void followPlayer(ServerPlayer player, boolean follow) {
		this.followingPlayer = follow;
		if (follow) {
			this.owner = player.getUUID();
			PlayerOwnedNpcAi.followCommand(this);
		} else {
			PlayerOwnedNpcAi.stopFollowCommand(this);
		}
	}

	public boolean isFollowingPlayer() {
		return followingPlayer;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput values) {
		super.readAdditionalSaveData(values);

		upkeep.read(values);
		this.followingPlayer = values.getBooleanOr("follow_player", false);
		this.autoUpkeepBlock = values.read("auto_upkeep_block", BlockPos.CODEC).orElse(null);
		this.currentPlayerCommand = values.read("command", NpcCommand.Command.CODEC).orElse(null);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput values) {
		super.addAdditionalSaveData(values);

		upkeep.write(values);
		values.putBoolean("follow_player", this.followingPlayer);
		if (autoUpkeepBlock != null) {
			values.store("auto_upkeep_block", BlockPos.CODEC, autoUpkeepBlock);
		}
		if (currentPlayerCommand != null) {
			values.store("command", NpcCommand.Command.CODEC, currentPlayerCommand);
		}
	}

	@Override
	public @Nullable Entity getOwner() {
		return level().getEntity(owner);
	}

	@Override
	protected @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return super.mobInteract(player, hand);
		}

		if (!EntityHelper.getItemFromEitherHand(serverPlayer, AWItems.COMMAND_BATONS).isEmpty()) {
			return InteractionResult.PASS;
		}

		ItemStack handStack = player.getItemInHand(hand);
		EquipmentSlot preferredSlot = getEquipmentSlotForItem(handStack);
		if (preferredSlot.isArmor()) {
			if (swapEquipment(serverPlayer, hand, preferredSlot, handStack)) {
				return InteractionResult.SUCCESS_SERVER;
			}
		}

		if (player.isShiftKeyDown()) {
			followPlayer(serverPlayer, !followingPlayer);
			return InteractionResult.SUCCESS_SERVER;
		}
		return super.mobInteract(player, hand);
	}

	private boolean swapEquipment(ServerPlayer player, InteractionHand hand, EquipmentSlot slot, ItemStack stack) {
		return equip(player, slot, stack, hand);
	}

	// Copied from ArmorStandEntity!
	private boolean equip(Player player, EquipmentSlot slot, ItemStack stack, InteractionHand hand) {
		ItemStack itemStack = this.getItemBySlot(slot);

		if (player.hasInfiniteMaterials() && itemStack.isEmpty() && !stack.isEmpty()) {
			this.setItemSlot(slot, stack.copyWithCount(1));
			return true;
		}
		if (!stack.isEmpty() && stack.getCount() > 1) {
			if (!itemStack.isEmpty()) {
				return false;
			}
			this.setItemSlot(slot, stack.split(1));
			return true;
		}
		this.setItemSlot(slot, stack);
		player.setItemInHand(hand, itemStack);
		return true;
	}

	public void doUpkeepFromBlock(BlockPos pos) {
		if (level().getBlockEntity(pos) instanceof Container container) {
			boolean success = upkeep.refillFromContainer(container);
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();

		upkeep.tick();
	}

	@Override
	public ItemStack item() {
		TagValueOutput input = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level().registryAccess());
		addAdditionalSaveData(input);

		ItemStack stack = new ItemStack(AWItems.NPC_SPAWNER);
		stack.applyComponents(DataComponentMap.builder().set(DataComponents.ENTITY_DATA, TypedEntityData.of(
				getType(), input.buildResult()
		)).build());
		return stack;
	}
}
