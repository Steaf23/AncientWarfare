package io.github.steaf23.ancientwarfare.client.datagen;

import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
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
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
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

		var empire = factionBuilder("empire");
		saveNpc(buildSoldier(empire)
				.equipment(Items.IRON_SPEAR, null));

		var amazon = factionBuilder("amazon");
		saveNpc(buildSoldier(amazon)
				.equipment(Items.GOLDEN_SPEAR, Items.SHIELD));
		saveNpc(buildEliteSoldier(amazon)
				//TODO: golden halberd .equipment()
				);
		saveNpc(buildCavalry(amazon)
				.equipment(Items.GOLDEN_SPEAR, Items.SHIELD));
		saveNpc(buildLeader(amazon)
				//TODO: golden halberd .equipment()
				);
		saveNpc(buildEliteLeader(amazon)
				.equipment(Items.GOLDEN_SWORD, null));
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


	public void saveNpc(FactionNpcData.Builder dataBuilder) {
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

	public FactionNpcData.Builder buildCavalry(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("cavalry"))
				.horseMount()
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MAX_HEALTH, 2)
				.experienceDropped(40)
				.equipment(Items.IRON_SWORD, null)
				.lootTable(AncientWarfare.id("entities/cavalry"));
	}

	public FactionNpcData.Builder buildSoldier(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("soldier"))
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20)
				.equipment(Items.IRON_SWORD, null)
				.lootTable(AncientWarfare.id("entities/soldier"));
	}

	public FactionNpcData.Builder buildEliteSoldier(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("elite_soldier"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0.325)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.experienceDropped(30)
				.equipment(Items.IRON_SWORD, null)
				.lootTable(AncientWarfare.id("entities/elite_soldier"));
	}

	public FactionNpcData.Builder buildArcher(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("archer"))
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.FOLLOW_RANGE, 60)
				.experienceDropped(20)
				.equipment(Items.BOW, null)
				.lootTable(AncientWarfare.id("entities/archer"));
	}

	public FactionNpcData.Builder buildMountedArcher(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("mounted_archer"))
				.horseMount()
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Items.BOW, null)
				.lootTable(AncientWarfare.id("entities/mounted_archer"));
	}

	public FactionNpcData.Builder buildEliteArcher(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("elite_archer"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.FOLLOW_RANGE, 80)
				.experienceDropped(30)
				.equipment(Items.BOW, null)
				.lootTable(AncientWarfare.id("entities/elite_archer"));
	}

	public FactionNpcData.Builder buildLeader(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("leader"))
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.experienceDropped(50)
				.equipment(Items.DIAMOND_SWORD, null)
				.lootTable(AncientWarfare.id("entities/leader"));
	}

	public FactionNpcData.Builder buildEliteLeader(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("elite_leader"))
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.experienceDropped(60)
				.equipment(Items.DIAMOND_SWORD, null)
				.lootTable(AncientWarfare.id("entities/elite_leader"));
	}

	public FactionNpcData.Builder buildSiegeEngineer(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("siege_engineer"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.FOLLOW_RANGE, 120)
				.experienceDropped(30)
				.equipment((Identifier) null, null)
				.lootTable(AncientWarfare.id("entities/siege_engineer"));
	}

	public FactionNpcData.Builder buildPriest(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("priest"))
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0.375)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.experienceDropped(40)
				.healPerTry(0.5)
				.equipment(Items.BOOK, null)
				.lootTable(AncientWarfare.id("entities/priest"));
	}

	public FactionNpcData.Builder buildSpellcaster(FactionNpcData.Builder builder, int variant) {
		return builder.copy()
				.npcType(AncientWarfare.id(variant > 1 ? "spell_caster_" + variant : "spell_caster"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(40)
				.equipment((Identifier) null, null)
				.lootTable(AncientWarfare.id("entities/soldier"));
	}

	public FactionNpcData.Builder buildBard(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("bard"))
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.experienceDropped(40)
				.equipment(AWItems.STEEL_INGOT, null);
	}

	public FactionNpcData.Builder buildMaleCivilian(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("male_civilian"))
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.equipment((Identifier) null, null);
	}

	public FactionNpcData.Builder buildFemaleCivilian(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("female_civilian"))
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.equipment((Identifier) null, null);
	}

	/**
	 * Default npc parameters for any faction
	 */
	public FactionNpcData.Builder factionBuilder(String faction) {
		return FactionNpcData.builder()
				.faction(AncientWarfare.id(faction))
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0.3)
				.addAttribute(Attributes.FOLLOW_RANGE, 35)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.experienceDropped(30)
				.navigation(true, false, true)
				.burnsInSun(false)
				.lootTable(Identifier.withDefaultNamespace("empty"));
	}
}
