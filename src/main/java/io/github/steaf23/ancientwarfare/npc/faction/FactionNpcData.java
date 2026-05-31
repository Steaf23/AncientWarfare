package io.github.steaf23.ancientwarfare.npc.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipment;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentFixed;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentTable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentTable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TypedEntityData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Data to create a single faction npc.
 */
public record FactionNpcData(
		Identifier id,
		ResourceKey<Faction> faction,
		Identifier npcType,
		Map<Identifier, Double> defaultAttributes,
		int experienceDropped,
		boolean canSwim,
		boolean canBreakDoors,
		boolean canOpenDoors,
		@NotNull NpcEquipment equipment,
		@NotNull Identifier lootTable,
		boolean enabled,
		Set<Identifier> spells,
		Optional<TypedEntityData<EntityType<?>>> mount) {

	public static final Codec<FactionNpcData> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("id").forGetter(FactionNpcData::id),
			ResourceKey.codec(Factions.FACTION_REGISTRY_KEY).fieldOf("faction")
					.forGetter(FactionNpcData::faction),
			Identifier.CODEC.fieldOf("npc_type")
					.forGetter(FactionNpcData::npcType),
			Codec.unboundedMap(Identifier.CODEC, Codec.DOUBLE).fieldOf("default_attributes")
					.forGetter(FactionNpcData::defaultAttributes),
			Codec.INT.fieldOf("experience_dropped")
					.forGetter(FactionNpcData::experienceDropped),
			Codec.BOOL.fieldOf("can_swim")
					.forGetter(FactionNpcData::canSwim),
			Codec.BOOL.fieldOf("can_break_boors")
					.forGetter(FactionNpcData::canBreakDoors),
			Codec.BOOL.fieldOf("can_open_doors")
					.forGetter(FactionNpcData::canOpenDoors),
			NpcEquipment.CODEC.fieldOf("equipment")
					.forGetter(FactionNpcData::equipment),
			Identifier.CODEC.fieldOf("loot_table")
					.forGetter(FactionNpcData::lootTable),
			Codec.BOOL.fieldOf("enabled")
					.forGetter(FactionNpcData::enabled),
			Identifier.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("spells")
					.forGetter(FactionNpcData::spells),
			TypedEntityData.codec(EntityType.CODEC).optionalFieldOf("mount")
					.forGetter(FactionNpcData::mount)
	).apply(i, FactionNpcData::new));

	public static final FactionNpcData DEFAULT = new FactionNpcData(
			AncientWarfare.id(""),
			ResourceKey.create(Factions.FACTION_REGISTRY_KEY, AncientWarfare.id("neutral")),
			AWEntities.NPC_SUBTYPE_SOLDIER,
			Map.of(),
			0,
			false, false, false,
			new NpcEquipmentTable(new EquipmentTable(ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("empty")), 0.0f)), Identifier.withDefaultNamespace("empty"),
			true,
			Set.of(),
			Optional.empty()
	);

	public static Builder builder() {
		Builder builder = new Builder();
		builder.setFromData(FactionNpcData.DEFAULT);
		return builder;
	}

	public String getDescription() {
		return "npc." + id.getNamespace() + "." + id.getPath().replace("/", ".");
	}

	public static class Builder {
		private ResourceKey<Faction> faction;
		private Identifier npcType;
		private Map<Identifier, Double> defaultAttributes;
		private int experienceDropped;
		private boolean canSwim;
		private boolean canBreakDoors;
		private boolean canOpenDoors;
		private @Nullable NpcEquipment equipment;
		private @Nullable Identifier lootTable;
		private boolean enabled;
		private Set<Identifier> spells = new HashSet<>();
		private @Nullable TypedEntityData<EntityType<?>> mount;

		public Builder setFromData(FactionNpcData data) {
			this.faction = data.faction;
			this.npcType = data.npcType;
			this.defaultAttributes = data.defaultAttributes;
			this.experienceDropped = data.experienceDropped;
			this.canSwim = data.canSwim;
			this.canBreakDoors = data.canBreakDoors;
			this.canOpenDoors = data.canOpenDoors;
			this.equipment = data.equipment;
			this.lootTable = data.lootTable;
			this.enabled = data.enabled;
			this.spells = data.spells;
			return this;
		}

		private Builder() {}

		public static Builder fromData(FactionNpcData data) {
			Builder builder = new Builder();
			builder.setFromData(data);
			return builder;
		}

		public Builder npcType(Identifier type) {
			this.npcType = type;
			return this;
		}

		public Builder faction(Identifier faction) {
			this.faction = ResourceKey.create(Factions.FACTION_REGISTRY_KEY, faction);
			return this;
		}

		public Builder spells(Set<Identifier> spells) {
			this.spells = spells;
			return this;
		}

		public Builder navigation(boolean canSwim, boolean canBreakDoors, boolean canOpenDoors) {
			this.canSwim = canSwim;
			this.canBreakDoors = canBreakDoors;
			this.canOpenDoors = canOpenDoors;
			return this;
		}

		public Builder equipmentTable(EquipmentTable table) {
			this.equipment = new NpcEquipmentTable(table);
			return this;
		}

		public Builder equipment(Identifier mainHand, Identifier offHand) {
			this.equipment = new NpcEquipmentFixed(mainHand, offHand);
			return this;
		}

		public Builder equipment(Item mainHand, Item offHand) {
			this.equipment = new NpcEquipmentFixed(mainHand == null ? null : BuiltInRegistries.ITEM.getKey(mainHand), offHand == null ? null : BuiltInRegistries.ITEM.getKey(offHand));
			return this;
		}

		public Builder simpleMount(EntityType<?> entity) {
			this.mount = TypedEntityData.of(entity, new CompoundTag());
			return this;
		}

		public Builder horseMount() {
			this.mount = TypedEntityData.of(EntityType.HORSE, new CompoundTag());
			return this;
		}

		public FactionNpcData build(Identifier id) {
			return new FactionNpcData(
					id,
					faction,
					npcType,
					defaultAttributes,
					experienceDropped,
					canSwim,
					canBreakDoors,
					canOpenDoors,
					equipment,
					lootTable,
					enabled,
					spells,
					Optional.ofNullable(mount)
			);
		}

		public FactionNpcData buildAutoId() {
			return build(AncientWarfare.id((faction == null ? "" : faction.identifier().getPath() + "/") + npcType.getPath()));
		}
	}
}
