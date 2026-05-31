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
