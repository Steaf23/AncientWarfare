package io.github.steaf23.ancientwarfare.client.datagen;

import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
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

	public FactionNpcProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.output = output;
		this.registries = registries;
	}

	public void generate(HolderLookup.Provider reg) {

		saveNpc(FactionNpcData.builder()
				.faction(AncientWarfare.id("empire"))
				.npcType(AncientWarfare.id("soldier"))
				.equipment(Items.IRON_SPEAR, null)
				.buildAutoId());

		FactionNpcData amazon = FactionNpcData.builder()
				.faction(AncientWarfare.id("amazon"))
				.buildAutoId();
		saveNpc(FactionNpcData.builder().setFromData(amazon)
				.npcType(AncientWarfare.id("soldier"))
				.equipment(Items.GOLDEN_SPEAR, Items.SHIELD)
				.buildAutoId());
		saveNpc(FactionNpcData.builder().setFromData(amazon)
				.npcType(AncientWarfare.id("elite_soldier"))
				//TODO: golden halberd .equipment()
				.buildAutoId());
		saveNpc(FactionNpcData.builder().setFromData(amazon)
				.npcType(AncientWarfare.id("cavalry"))
				.equipment(Items.GOLDEN_SPEAR, Items.SHIELD)
				.horseMount()
				.buildAutoId());
		saveNpc(FactionNpcData.builder().setFromData(amazon)
				.npcType(AncientWarfare.id("leader"))
				//TODO: golden halberd .equipment()
				.buildAutoId());
		saveNpc(FactionNpcData.builder().setFromData(amazon)
				.npcType(AncientWarfare.id("elite_leader"))
				.equipment(Items.GOLDEN_SWORD, null)
				.buildAutoId());
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		generate(registries.join());
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public String getName() {
		return "Ancient Warfare Faction Npcs registry";
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
