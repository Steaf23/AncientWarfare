package io.github.steaf23.ancientwarfare.npc.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipment;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentEmpty;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentFixed;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentTable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentTable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TypedEntityData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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
		Optional<TypedEntityData<EntityType<?>>> mount,
		AdditionalAttributes additional) {

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
					.forGetter(FactionNpcData::mount),
			AdditionalAttributes.CODEC.fieldOf("additional_attributes")
					.forGetter(FactionNpcData::additional)
	).apply(i, FactionNpcData::new));

	public static final FactionNpcData INVALID_DEFAULT = new FactionNpcData(
			AncientWarfare.id(""),
			ResourceKey.create(Factions.FACTION_REGISTRY_KEY, AncientWarfare.id("neutral")),
			AWEntities.NPC_SUBTYPE_SOLDIER,
			new HashMap<>(),
			0,
			false, false, false,
			new NpcEquipmentEmpty(), Identifier.withDefaultNamespace("empty"),
			true,
			Set.of(),
			Optional.empty(),
			new AdditionalAttributes(false, false, 0.0, AncientWarfare.id("player"))
	);

	public static Builder builder() {
		Builder builder = new Builder();
		builder.setFromData(FactionNpcData.INVALID_DEFAULT);
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
		private boolean burnsInSun;
		private boolean undead;
		private double healPerTry;
		private Identifier soundSet = AncientWarfare.id("player");

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
			// Additional
			this.burnsInSun = data.additional.burnsInSun();
			this.undead = data.additional.undead();
			this.healPerTry = data.additional.healPerTry();
			this.soundSet = data.additional.soundSet();
			return this;
		}

		public Builder copy() {
			Builder copy = new Builder();
			copy.faction = this.faction;
			copy.npcType = this.npcType;
			copy.defaultAttributes = new HashMap<>(this.defaultAttributes);
			copy.experienceDropped = this.experienceDropped;
			copy.canSwim = this.canSwim;
			copy.canBreakDoors = this.canBreakDoors;
			copy.canOpenDoors = this.canOpenDoors;
			copy.equipment = this.equipment;
			copy.lootTable = this.lootTable;
			copy.enabled = this.enabled;
			copy.spells = this.spells;
			// Additional
			copy.burnsInSun = this.burnsInSun;
			copy.undead = this.undead;
			copy.healPerTry = this.healPerTry;
			copy.soundSet = this.soundSet;
			return copy;
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

		public Builder experienceDropped(int expPoints) {
			this.experienceDropped = expPoints;
			return this;
		}

		public Builder navigation(boolean canSwim, boolean canBreakDoors, boolean canOpenDoors) {
			this.canSwim = canSwim;
			this.canBreakDoors = canBreakDoors;
			this.canOpenDoors = canOpenDoors;
			return this;
		}

		public Builder canSwim(boolean canSwim) {
			this.canSwim = canSwim;
			return this;
		}

		public Builder lootTable(Identifier lootTable) {
			this.lootTable = lootTable;
			return this;
		}

		public Builder burnsInSun(boolean burnsInSun) {
			this.burnsInSun = burnsInSun;
			return this;
		}

		public Builder undead(boolean undead) {
			this.undead = undead;
			return this;
		}

		public Builder healPerTry(double healPerTry) {
			this.healPerTry = healPerTry;
			return this;
		}

		public Builder soundSet(Identifier soundSet) {
			this.soundSet = soundSet;
			return this;
		}

		public Builder equipmentTable(EquipmentTable table) {
			this.equipment = new NpcEquipmentTable(table);
			return this;
		}

		public Builder addAttribute(Holder<Attribute> attribute, double defaultValue) {
			this.defaultAttributes.put(attribute.unwrapKey().map(ResourceKey::identifier).orElseThrow(), defaultValue);
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

		public Builder noEquipment() {
			this.equipment = new NpcEquipmentEmpty();
			return this;
		}

		public Builder simpleMount(EntityType<?> entity) {
			this.mount = TypedEntityData.of(entity, new CompoundTag());
			return this;
		}

		public Builder horseMount() {
			CompoundTag tag = new CompoundTag();
			CompoundTag saddleSlot = new CompoundTag();
			CompoundTag saddle = new CompoundTag();
			saddle.putInt("count", 1);
			saddle.putString("id", BuiltInRegistries.ITEM.getKey(Items.SADDLE).toString());

			tag.put("equipment", saddleSlot);
			saddleSlot.put("saddle", saddle);

			this.mount = TypedEntityData.of(EntityType.HORSE, tag);
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
					Optional.ofNullable(mount),
					new AdditionalAttributes(burnsInSun, undead, healPerTry, soundSet)
			);
		}

		public FactionNpcData buildAutoId() {
			return build(AncientWarfare.id((faction == null ? "" : faction.identifier().getPath() + "/") + npcType.getPath()));
		}
	}
}
