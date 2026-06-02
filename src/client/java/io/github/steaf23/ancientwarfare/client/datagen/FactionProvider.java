package io.github.steaf23.ancientwarfare.client.datagen;

import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FactionProvider implements DataProvider {

	private static final Logger log = LogManager.getLogger(FactionProvider.class);
	private final FabricPackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;

	public FactionProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.output = output;
		this.registries = registries;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		registries.join();

		saveFaction(Factions.NEUTRAL);
		saveFaction(new Faction(
				AncientWarfare.id("empire"),
				0xff800080,
				Set.of(),
				0,
				Map.of(
						AncientWarfare.id("kill"), -10,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("norska"),
				0xff9999ff,
				Set.of(),
				0,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("sarkonid"),
				0xffb20000,
				Set.of(),
				0,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("xoltec"),
				0xffffa500,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("witchbane"),
				0xffffffff,
				Set.of(),
				-100,
				Map.of(
						AncientWarfare.id("kill"), -10,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("nogg"),
				0xff00cd00,
				Set.of(),
				50,
				Map.of(
						AncientWarfare.id("kill"), -25,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("buffloka"),
				0xff008000,
				Set.of(),
				0,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 1)
		));
		saveFaction(new Faction(
				AncientWarfare.id("reiksgard"),
				0xff22cd22,
				Set.of(),
				50,
				Map.of(
						AncientWarfare.id("kill"), -15,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("zimba"),
				0xffffff00,
				Set.of(),
				0,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("kong"),
				0xff004000,
				Set.of(),
				-100,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("orc"),
				0xff404040,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), 1,
						AncientWarfare.id("trade"), 1)
		));
		saveFaction(new Faction(
				AncientWarfare.id("brigand"),
				0xff964b00,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("pirate"),
				0xffcc7722,
				Set.of(),
				-100,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("evil"),
				0xff000000,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("good"),
				0xff66b266,
				Set.of(),
				50,
				Map.of(
						AncientWarfare.id("kill"), -25,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("undead"),
				0xffb2b2b2,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("demon"),
				0xffff2400,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("coven"),
				0xff000000,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("elf"),
				0xff66b266,
				Set.of(),
				50,
				Map.of(
						AncientWarfare.id("kill"), -50,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("barbarian"),
				0xff802b00,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("dwarf"),
				0xff77b266,
				Set.of(),
				50,
				Map.of(
						AncientWarfare.id("kill"), -25,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("shakayana"),
				0xff88b266,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -10,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("smingol"),
				0xffffd700,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("lizardman"),
				0xff576500,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("mindflayer"),
				0xff777500,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("zamurai"),
				0xff376500,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -10,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("rakshasa"),
				0xffd7bf00,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("klown"),
				0xffffc0cb,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("ishtari"),
				0xfffed000,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("icelord"),
				0xff3c9f9c,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("guild"),
				0xff007cc9,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("wizardly"),
				0xffaa3fd8,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("amazon"),
				0xffd34848,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("minossian"),
				0xff456689,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("sealsker"),
				0xff6666ff,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("vyncan"),
				0xff6666ff,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("hobbit"),
				0xff6666ff,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("monster"),
				0xffffc0cb,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("beast"),
				0xffffc0cb,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("ent"),
				0xff6666ff,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("gnome"),
				0xff6666ff,
				Set.of(),
				20,
				Map.of(
						AncientWarfare.id("kill"), -20,
						AncientWarfare.id("trade"), 2)
		));
		saveFaction(new Faction(
				AncientWarfare.id("gremlin"),
				0xffffc0cb,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("vampire"),
				0xff320000,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));
		saveFaction(new Faction(
				AncientWarfare.id("giant"),
				0xffffc0cb,
				Set.of(),
				-1000,
				Map.of(
						AncientWarfare.id("kill"), -100,
						AncientWarfare.id("trade"), 0)
		));

		return CompletableFuture.completedFuture(null);
	}

	public void saveFaction(Faction data) {
		Identifier name = data.id();
		Path file = output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(name.getNamespace() + "/factions/" + name.getPath() + ".json");

		var json = Faction.CODEC.encodeStart(JsonOps.INSTANCE, data)
				.getOrThrow();
		try {
			Files.createDirectories(file.getParent());
			Files.writeString(file, new GsonBuilder().setPrettyPrinting().create().toJson(json));
		} catch (IOException e) {
			System.out.println("Cannot save file for faction " + name);
			log.error("exception: ", e);
		}
	}

	@Override
	public String getName() {
		return "Ancient Warfare Factions registry";
	}
}
