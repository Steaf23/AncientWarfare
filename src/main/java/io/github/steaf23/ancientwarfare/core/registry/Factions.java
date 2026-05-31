package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.util.AWResourceLoader;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.Set;

public class Factions {

	public static final ResourceKey<Registry<Faction>> FACTION_REGISTRY_KEY =
			ResourceKey.createRegistryKey(AncientWarfare.id("factions"));

	public static final ResourceKey<Registry<FactionNpcData>> FACTION_NPCS_REGISTRY_KEY =
			ResourceKey.createRegistryKey(AncientWarfare.id("npcs"));

	public static final Faction NEUTRAL = new Faction(
			AncientWarfare.id("neutral"),
			0xff8e8989,
			Set.of(),
			0,
			Map.of()
	);

//	//~ if <=1.21.11 'create(' -> 'createSimple('
//	public static final Registry<Faction> FACTION_REGISTRY = FabricRegistryBuilder.createDefaulted(FACTION_REGISTRY_KEY, AncientWarfare.id("neutral"))
//			.attribute(RegistryAttribute.OPTIONAL)
//			.buildAndRegister();

	public static void initialize() {

		//~ if <=1.21.11 'registerReloadListener' -> 'registerReloader' {
		DataResourceLoader.get().registerReloadListener(AncientWarfare.id("factions"), _ -> {
			var loader = new AWResourceLoader<>(Faction.CODEC, "factions");
			AWResources.setFactions(loader);
			return loader;
		});
		DataResourceLoader.get().registerReloadListener(AncientWarfare.id("npcs"), _ -> {
			var loader = new AWResourceLoader<>(FactionNpcData.CODEC, "npcs");
			AWResources.setNpcs(loader);
			return loader;
		});
		//~}
	}

}
