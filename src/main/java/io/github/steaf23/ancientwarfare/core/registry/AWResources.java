package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.core.util.AWResourceLoader;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AWResources {

	private static final AWResources INSTANCE = new AWResources();

	private AWResourceLoader<Faction> factions = null;
	private AWResourceLoader<FactionNpcData> npcs = null;
	private Map<Identifier, Set<FactionNpcData>> npcsPerFaction = new HashMap<>();

	public static Collection<FactionNpcData> npcs() {
		return INSTANCE.npcs.allValues();
	}

	public static Map<Identifier, Faction> factionEntries() {
		return INSTANCE.factions.entries();
	}

	public static Map<Identifier, FactionNpcData> npcEntries() {
		return INSTANCE.npcs.entries();
	}

	public static @Nullable FactionNpcData npc(Identifier id) {
		return INSTANCE.npcs.byId(id);
	}

	public static Set<FactionNpcData> npcsInFaction(Identifier factionId) {
		INSTANCE.setupNpcs();
		return INSTANCE.npcsPerFaction.getOrDefault(factionId, Set.of());
	}

	public static Collection<Faction> factions() {
		return INSTANCE.factions.allValues();
	}

	public static Collection<Identifier> factionIds() {
		return INSTANCE.factions.entries().keySet();
	}

	public static @Nullable Faction faction(Identifier id) {
		return INSTANCE.factions.byId(id);
	}

	public static void setNpcs(AWResourceLoader<FactionNpcData> npcs) {
		INSTANCE.npcs = npcs;
	}

	public static void setFactions(AWResourceLoader<Faction> factions) {
		INSTANCE.factions = factions;
	}

	private void setupNpcs() {
		npcsPerFaction.clear();

		for (FactionNpcData npc : npcs()) {
			Identifier factionId = npc.faction().identifier();
			if (!npcsPerFaction.containsKey(factionId))
			{
				npcsPerFaction.put(factionId, new HashSet<>());
			}
			npcsPerFaction.get(factionId).add(npc);
		}
	}

}
