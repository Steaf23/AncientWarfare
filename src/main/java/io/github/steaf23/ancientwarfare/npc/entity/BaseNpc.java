package io.github.steaf23.ancientwarfare.npc.entity;

import com.google.common.collect.ImmutableList;
import io.github.steaf23.ancientwarfare.core.item.ItemConvertible;
import io.github.steaf23.ancientwarfare.core.menu.EntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.EntityScreenProvider;
import io.github.steaf23.ancientwarfare.core.menu.ScreenData;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWActivities;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpcAi;
import io.github.steaf23.ancientwarfare.npc.menu.NpcContainerMenu;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class BaseNpc extends PathfinderMob implements EntityScreenProvider, ItemConvertible {

	public static int MIN_MOVE_RANGE_SQUARED = 9;
	private static final Brain.Provider<BaseNpc> BRAIN_PROVIDER = Brain.provider(PlayerOwnedNpcAi.SENSORS, e -> PlayerOwnedNpcAi.getActivities());

	private final NpcHome home;
	private final NpcSkin skin; // FIXME: IMPLEMENT

	private String name = "";

	private final SimpleContainer items;


	public BaseNpc(EntityType<? extends PathfinderMob> entityType, Level world) {
		super(entityType, world);
		this.home = new NpcHome(this);
		this.skin = new NpcSkin();
		items = new SimpleContainer(2);
		setCustomNameVisible(true);
	}

	public static AttributeSupplier.Builder createNpcAttributes() {
		return PathfinderMob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.1D);
	}

	@Override
	protected @NotNull Brain<?> makeBrain(Brain.@NonNull Packed packed) {
		return BRAIN_PROVIDER.makeBrain(this, packed);
	}

	@SuppressWarnings("unchecked")
	@Override
	public @NonNull Brain<BaseNpc> getBrain() {
		return (Brain<BaseNpc>) super.getBrain();
	}

	@Override
	protected void customServerAiStep(ServerLevel world) {
		getBrain().tick(world, this);
		getBrain().setActiveActivityToFirstValid(ImmutableList.of(AWActivities.UPKEEP, Activity.WORK, Activity.IDLE));
		super.customServerAiStep(world);
	}

	@Override
	protected @NotNull PathNavigation createNavigation(Level world) {
		GroundPathNavigation navigation = new GroundPathNavigation(this, world);
		navigation.setCanOpenDoors(true);
		return navigation;
	}

	@Override
	protected @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
//		player.openMenu(this);

		if (level() instanceof ServerLevel serverLevel) {
			List<PoiRecord> records = serverLevel.getPoiManager()
					.getInRange(p -> {
						boolean val = p.is(PoiTypes.ARMORER);
						return val;
					}, blockPosition(), 48, PoiManager.Occupancy.ANY)
					.toList();
			boolean t = false;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected float getDamageAfterArmorAbsorb(DamageSource source, float amount) {
		return super.getDamageAfterArmorAbsorb(source, amount);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput values) {
		super.addAdditionalSaveData(values);

		values.putString("name", getNpcName());
		home.write(values);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput values) {
		super.readAdditionalSaveData(values);

		setNpcName(values.getStringOr("name", ""));
		home.read(values);
	}

	public NpcHome getHome() {
		return home;
	}

	//FIXME: Refactor this to not use raw parsing but instead the data appliers from the container.
	@Override
	public @NonNull EntityScreenData getScreenOpeningData(@NonNull ServerPlayer player) {
		ScreenData compound = new ScreenData();
		ByteBuf buf = Unpooled.buffer();
		ByteBufCodecs.STRING_UTF8.encode(buf, getNpcName());
		compound.put("name", buf.array());
		return new EntityScreenData(this, compound);
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
		return new NpcContainerMenu(syncId, playerInventory, this);
	}

	public SimpleContainer getItems() {
		return items;
	}

	public void setNpcName(String name) {
		this.name = name;

		setCustomName(Component.empty().append(Component.literal("NPC").withStyle(ChatFormatting.BOLD, ChatFormatting.RED)).append(" " + name));
	}

	public String getNpcName() {
		return this.name;
	}


	public void recall(@NotNull ServerPlayer toPlayer) {
		ItemStack item = item();
		if (!toPlayer.addItem(item)) {
			toPlayer.drop(item, true);
		}
		toPlayer.closeContainer();

		discard();
	}

	@Override
	public ItemStack item() {
		TagValueOutput input = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level().registryAccess());
		addAdditionalSaveData(input);

		ItemStack stack = new ItemStack(AWItems.NPC_SPAWNER);
		stack.applyComponents(DataComponentMap.builder().set(DataComponents.ENTITY_DATA, TypedEntityData.of(
				AWEntities.BASE_NPC, input.buildResult()
		)).build());
		return stack;
	}
}
