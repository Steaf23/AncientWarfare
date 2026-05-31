package io.github.steaf23.ancientwarfare.npc.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWResources;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record Faction(
		Identifier id,
		int color,
		Set<Identifier> hostileTowards,
		int defaultReputation,
		Map<Identifier, Integer> reputationModifiers) {

	public static final Codec<Faction> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("id").forGetter(Faction::id),
			Codec.INT.fieldOf("color").forGetter(Faction::color),
			Identifier.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("hostile_towards").forGetter(Faction::hostileTowards),
			Codec.INT.fieldOf("default_reputation").forGetter(Faction::defaultReputation),
			Codec.unboundedMap(Identifier.CODEC, Codec.INT).fieldOf("reputation_modifiers").forGetter(Faction::reputationModifiers)
	).apply(i, Faction::new));

	public static @Nullable Faction fromItem(ItemStack stack) {
		TypedEntityData<?> data = stack.get(DataComponents.ENTITY_DATA);
		if (data == null) {
			return null;
		}
		if (data.type() != AWEntities.FACTION_NPC) {
			return null;
		}

		FactionNpcData npcData = AWResources.npc(data.copyTagWithoutId().read("npc_data", Identifier.CODEC).orElseThrow());
		return npcData == null ? AWResources.faction(AncientWarfare.id("neutral")) : AWResources.faction(npcData.faction().identifier());
	}

	public String getDescription() {
		return "faction." + id.getNamespace() + "." + id.getPath().replace("/", ".");
	}
}
