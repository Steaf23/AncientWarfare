package io.github.steaf23.ancientwarfare.core.util;

import io.github.steaf23.ancientwarfare.core.registry.AWResources;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FactionOwned {

	@Nullable ResourceKey<Faction> getFactionKey();

	default @NotNull Faction getFaction() {
		if (getFactionKey() == null) {
			return Factions.NEUTRAL;
		}

		Faction faction = AWResources.faction(getFactionKey().identifier());
		return faction == null ? Factions.NEUTRAL : faction;
	}
}
