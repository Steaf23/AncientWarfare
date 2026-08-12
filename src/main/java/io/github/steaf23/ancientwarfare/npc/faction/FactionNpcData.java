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

	public String getDescription() {
		return "npc." + id.getNamespace() + "." + id.getPath().replace("/", ".");
	}
}
