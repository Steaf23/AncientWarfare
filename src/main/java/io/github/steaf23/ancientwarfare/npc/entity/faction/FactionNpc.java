package io.github.steaf23.ancientwarfare.npc.entity.faction;

import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.registry.AWResources;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.core.util.FactionOwned;
import io.github.steaf23.ancientwarfare.core.versioned.BrainFactory;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpcAi;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentEmpty;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentFixed;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentTable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class FactionNpc extends BaseNpc implements FactionOwned {

	private FactionNpcData npcData;
	private boolean spawnCompleted;

	public FactionNpc(EntityType<? extends PathfinderMob> entityType, Level world) {
		super(entityType, world);
		this.npcData = FactionNpcData.INVALID_DEFAULT;
	}

	@Override
	public BrainFactory<BaseNpc> initBrainMaker() {
		return new BrainFactory<>(
				PlayerOwnedNpcAi.MEMORY_MODULES,
				PlayerOwnedNpcAi.SENSORS,
				npc -> PlayerOwnedNpcAi.getActivities());

	}

	@Override
	protected void addAdditionalSaveData(ValueOutput values) {
		super.addAdditionalSaveData(values);
		values.putBoolean("spawn_completed", spawnCompleted);
		values.store("npc_data", Identifier.CODEC, npcData.id());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput values) {
		super.readAdditionalSaveData(values);

		spawnCompleted = values.getBooleanOr("spawn_completed", false);
		npcData = AWResources.npc(values.read("npc_data", Identifier.CODEC).orElseThrow());

		if (!spawnCompleted) {
			spawnCompleted = true;
			setupNpcFromData();
		}
	}

	public static ItemStack itemFromNpcData(HolderLookup.Provider registries, FactionNpcData data) {
		TagValueOutput input = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
		input.store("npc_data", Identifier.CODEC, data.id());

		Identifier npcId = data.id();
		ItemStack stack = new ItemStack(AWItems.FACTION_NPC_SPAWNER);
		stack.applyComponents(DataComponentMap.builder()
				.set(DataComponents.ENTITY_DATA,
						TypedEntityData.of(AWEntities.FACTION_NPC, input.buildResult()))
				.set(DataComponents.ITEM_NAME,
						Component.translatable(data.getDescription()))
				.build());
		return stack;
	}

	@Override
	public ItemStack item() {
		return itemFromNpcData(level().registryAccess(), npcData);
	}

	public void setupNpcFromData() {
		equipNpc();

		npcData.mount().ifPresent(this::addMount);
	}

	public void addMount(TypedEntityData<EntityType<?>> mount) {
		if (isPassenger()) {
			return;
		}

		if (!(level() instanceof ServerLevel level)) {
			return;
		}

		Entity entity = mount.type().create(level, EntitySpawnReason.JOCKEY);
		if (entity != null) {
			entity.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
			mount.loadInto(entity);
			if (entity instanceof Mob mob) {
				mob.finalizeSpawn(level, level.getCurrentDifficultyAt(blockPosition()), EntitySpawnReason.JOCKEY, null);
			}
			this.startRiding(entity);
			level.addFreshEntity(entity);
		}
	}

	public void equipNpc() {
		switch (npcData.equipment()) {
			case NpcEquipmentTable table -> {
				//TODO: implement equipment from equipment table
			}
			case NpcEquipmentFixed fixed -> {
				if (fixed.mainHandItemId() != null) {
					setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BuiltInRegistries.ITEM.getValue(fixed.mainHandItemId())));
				}
				if (fixed.offHandItemId() != null) {
					setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(BuiltInRegistries.ITEM.getValue(fixed.offHandItemId())));
				}
			}
			case NpcEquipmentEmpty _ -> {
				// NOOP
			}
		}
	}

	public static String getNpcDescriptionIdFromData(CompoundTag tag) {
		FactionNpcData npcData = AWResources.npc(Identifier.parse(tag.getString("npc_data").orElse("")));
		if (npcData == null) {
			return AWEntities.FACTION_NPC.getDescriptionId();
		}

		return "ancientwarfare:npc.faction." + npcData.faction().identifier().getPath() + "." + npcData.npcType().getPath();
	}

	@Override
	public @Nullable ResourceKey<Faction> getFactionKey() {
		return npcData.faction();
	}
}
