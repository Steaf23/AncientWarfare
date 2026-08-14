package io.github.steaf23.ancientwarfare.client.datagen.faction;

import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
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
import java.util.concurrent.CompletableFuture;

public class FactionNpcProvider implements DataProvider {

	private static final Logger log = LogManager.getLogger(FactionNpcProvider.class);
	private final FabricPackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final FactionNpcDefinitions npcs;

	public FactionNpcProvider(FactionNpcDefinitions npcs, FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.output = output;
		this.registries = registries;
		this.npcs = npcs;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		registries.join();
		npcs.forEach(this::saveNpc);
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public String getName() {
		return "Ancient Warfare Faction Npcs registry";
	}


	public void saveNpc(FactionNpcBuilder dataBuilder) {
		saveNpc(dataBuilder.buildAutoId());
	}

	public void saveNpc(FactionNpcData data) {
		Identifier name = data.id();
		Path file = output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(name.getNamespace() + "/npcs/" + name.getPath() + ".json");

		var json = FactionNpcData.CODEC.encodeStart(JsonOps.INSTANCE, data)
				.getOrThrow();
		try {
			Files.createDirectories(file.getParent());
			Files.writeString(file, new GsonBuilder().setPrettyPrinting().create().toJson(json));
		} catch (IOException e) {
			System.out.println("Cannot save file for npc " + name);
			log.error("exception: ", e);
		}
	}
}
