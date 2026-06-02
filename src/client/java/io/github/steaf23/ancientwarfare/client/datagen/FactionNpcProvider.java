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
import net.minecraft.world.entity.EntityType;
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
import java.util.Set;
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
		var amazon = factionBuilder("amazon")
				.soundSet(AncientWarfare.id("human_female"));
		saveNpc(buildSoldier(amazon)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_spear"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildEliteSoldier(amazon)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null));
		saveNpc(buildCavalry(amazon)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_spear"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildLeader(amazon)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null));
		saveNpc(buildEliteLeader(amazon)
				.equipment(Identifier.parse("minecraft:golden_sword"), null));

		var barbarian = factionBuilder("barbarian")
				.soundSet(AncientWarfare.id("barbarian"));
		saveNpc(buildSoldier(barbarian)
				.equipment(Identifier.parse("minecraft:stone_axe"), Identifier.parse("ancientwarfarenpc:shield_round_1")));
		saveNpc(buildLeader(barbarian)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:stone_cleaver"), Identifier.parse("ancientwarfarenpc:iron_shield"))
				.experienceDropped(60));
		saveNpc(buildPriest(barbarian)
				.equipment(Identifier.parse("minecraft:skull"), Identifier.parse("minecraft:stone_axe")));

		var beast = factionBuilder("beast");
		saveNpc(buildSoldier(beast)
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.soundSet(AncientWarfare.id("kobold"))
				.equipment(Identifier.parse("minecraft:stone_axe"), null));
		saveNpc(buildEliteSoldier(beast)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.soundSet(AncientWarfare.id("gargoyle")));
		saveNpc(buildLeader(beast)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 6)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.soundSet(AncientWarfare.id("owlbear")));
		saveNpc(buildEliteLeader(beast)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 16)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.soundSet(AncientWarfare.id("ent")));

		var brigand = factionBuilder("brigand")
				.soundSet(AncientWarfare.id("brigand"));
		saveNpc(buildSoldier(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.equipment(Identifier.parse("minecraft:iron_axe"), null)
				.experienceDropped(15));
		saveNpc(buildEliteSoldier(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.equipment(Identifier.parse("minecraft:iron_sword"), null)
				.experienceDropped(20));
		saveNpc(buildArcher(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.experienceDropped(15));
		saveNpc(buildEliteArcher(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.experienceDropped(15));
		saveNpc(buildCavalry(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildMountedArcher(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildLeader(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.equipment(Identifier.parse("minecraft:iron_sword"), null)
				.experienceDropped(40));
		saveNpc(buildEliteLeader(brigand)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null)
				.experienceDropped(60));
		saveNpc(buildFemaleCivilian(brigand)
				.soundSet(AncientWarfare.id("human_female")));

		var buffloka = factionBuilder("buffloka")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(buffloka)
				.equipment(Identifier.parse("ancientwarfarenpc:stone_spear"), Identifier.parse("ancientwarfarenpc:shield_buffloka")));
		saveNpc(buildEliteSoldier(buffloka)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.equipment(Identifier.parse("minecraft:stone_axe"), null)
				.experienceDropped(30));
		saveNpc(buildCavalry(buffloka)
				.equipment(Identifier.parse("ancientwarfarenpc:stone_spear"), Identifier.parse("ancientwarfarenpc:shield_buffloka")));
		saveNpc(buildLeader(buffloka)
				.equipment(Identifier.parse("ancientwarfarenpc:diamond_spear"), null));
		saveNpc(buildEliteLeader(buffloka)
				.equipment(Identifier.parse("minecraft:diamond_axe"), null));
		saveNpc(buildPriest(buffloka)
				.equipment(Identifier.parse("minecraft:stick"), null));
		saveNpc(buildTrader(buffloka)
				.equipment(Identifier.parse("minecraft:feather"), null));
		saveNpc(buildFemaleCivilian(buffloka)
				.soundSet(AncientWarfare.id("human_female")));

		var coven = factionBuilder("coven");
		saveNpc(buildArcher(coven)
				.soundSet(AncientWarfare.id("coven_satyr")));
		saveNpc(buildEliteArcher(coven)
				.soundSet(AncientWarfare.id("coven_dryad")));
		saveNpc(buildSoldier(coven)
				.equipment(Identifier.parse("ancientwarfarenpc:sickle"), null)
				.soundSet(AncientWarfare.id("coven_scarecrow")));
		saveNpc(buildEliteSoldier(coven)
				.addAttribute(Attributes.MAX_HEALTH, 55)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 6)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.soundSet(AncientWarfare.id("coven_puppet")));
		saveNpc(buildCavalry(coven)
				.equipment(Identifier.parse("ancientwarfarenpc:death_scythe"), null)
				.simpleMount(EntityType.SKELETON_HORSE)
				.soundSet(AncientWarfare.id("coven_puppetd")));
		saveNpc(buildLeader(coven)
				.soundSet(AncientWarfare.id("coven_familiar"))
				.addAttribute(Attributes.MAX_HEALTH, 45)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2));
		saveNpc(buildEliteLeader(coven)
				.addAttribute(Attributes.MAX_HEALTH, 75)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 5)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.soundSet(AncientWarfare.id("coven_witch"))
				.equipment(Identifier.parse("ancientwarfarenpc:sickle"), null));
		saveNpc(buildSpellcaster(coven, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(coven, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(40)
				.spells(Set.of(Identifier.parse("ebwizardry:smoke_bomb"), Identifier.parse("ebwizardry:dart"), Identifier.parse("ebwizardry:poison_bomb"), Identifier.parse("ebwizardry:snare"))));
		saveNpc(buildSpellcaster(coven, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(50)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:summon_skeleton"))));
		saveNpc(buildSpellcaster(coven, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(60)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:cobwebs"), Identifier.parse("ebwizardry:poison"), Identifier.parse("ebwizardry:spider_swarm"))));
		saveNpc(buildSpellcaster(coven, 4)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(80)
				.spells(Set.of(Identifier.parse("ebwizardry:lightning_web"), Identifier.parse("ebwizardry:entrapment"), Identifier.parse("ebwizardry:greater_ward"), Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:summon_shadow_wraith"), Identifier.parse("ebwizardry:curse_of_undeath"))));

		var demon = factionBuilder("demon");
		saveNpc(buildSoldier(demon)
				.addAttribute(Attributes.MAX_HEALTH, 35)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_spear"), null)
				.experienceDropped(35));
		saveNpc(buildEliteSoldier(demon)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null)
				.experienceDropped(60));
		saveNpc(buildCavalry(demon)
				.addAttribute(Attributes.MAX_HEALTH, 55)
				.equipment(Identifier.parse("minecraft:golden_sword"), null)
				.experienceDropped(55));
		saveNpc(buildMountedArcher(demon)
				.addAttribute(Attributes.MAX_HEALTH, 45)
				.experienceDropped(45));
		saveNpc(buildLeader(demon)
				.addAttribute(Attributes.MAX_HEALTH, 75)
				.experienceDropped(75));
		saveNpc(buildEliteLeader(demon)
				.addAttribute(Attributes.MAX_HEALTH, 150)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 6)
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
				.experienceDropped(150));
		saveNpc(buildSiegeEngineer(demon)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildPriest(demon)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.equipment(Identifier.parse("minecraft:enchanted_book"), null)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(demon)
				.addAttribute(Attributes.MAX_HEALTH, 10));
		saveNpc(buildMaleCivilian(demon)
				.addAttribute(Attributes.MAX_HEALTH, 10));
		saveNpc(buildSpellcaster(demon, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(demon, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(demon, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(demon, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(demon, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var dwarf = factionBuilder("dwarf");
		saveNpc(buildArcher(dwarf)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30));
		saveNpc(buildSoldier(dwarf)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null));
		saveNpc(buildEliteSoldier(dwarf)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(60)
				.equipment(Identifier.parse("minecraft:iron_axe"), null));
		saveNpc(buildLeader(dwarf)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.experienceDropped(70)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null));
		saveNpc(buildEliteLeader(dwarf)
				.addAttribute(Attributes.MAX_HEALTH, 120)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:diamond_axe"), null));
		saveNpc(buildPriest(dwarf)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(dwarf)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(dwarf, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(dwarf, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(dwarf, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(dwarf, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(dwarf, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var elf = factionBuilder("elf")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(elf)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_spear"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildEliteSoldier(elf)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.equipment(Identifier.parse("minecraft:golden_sword"), Identifier.parse("ancientwarfarenpc:gold_shield"))
				.experienceDropped(70));
		saveNpc(buildArcher(elf)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildCavalry(elf)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_lance"), Identifier.parse("ancientwarfarenpc:gold_shield"))
				.experienceDropped(70));
		saveNpc(buildLeader(elf)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.equipment(Identifier.parse("ancientwarfarenpc:diamond_halberd"), null)
				.experienceDropped(70));
		saveNpc(buildEliteLeader(elf)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.equipment(Identifier.parse("minecraft:diamond_sword"), Identifier.parse("minecraft:diamond_sword"))
				.experienceDropped(100));
		saveNpc(buildPriest(elf)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(elf)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(elf, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(elf, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(elf, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(elf, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(elf, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var empire = factionBuilder("empire")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildEliteSoldier(empire)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null)
				.experienceDropped(30));
		saveNpc(buildSoldier(empire)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildCavalry(empire)
				.addAttribute(Attributes.MAX_HEALTH, 65)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_lance"), Identifier.parse("ancientwarfarenpc:iron_shield"))
				.experienceDropped(65));
		saveNpc(buildLeader(empire)
				.equipment(Identifier.parse("minecraft:diamond_axe"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildFemaleCivilian(empire)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(empire, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(empire, 1)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:thunderbolt"), Identifier.parse("ebwizardry:ignite"), Identifier.parse("ebwizardry:freeze"), Identifier.parse("ebwizardry:ward"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:smoke_bomb"))));
		saveNpc(buildSpellcaster(empire, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:frost_ray"), Identifier.parse("ebwizardry:flame_ray"), Identifier.parse("ebwizardry:lightning_ray"), Identifier.parse("ebwizardry:iceball"), Identifier.parse("ebwizardry:lightning_arrow"), Identifier.parse("ebwizardry:ward"), Identifier.parse("ebwizardry:oakflesh"))));
		saveNpc(buildSpellcaster(empire, 3)
				.addAttribute(Attributes.MAX_HEALTH, 35)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:arcane_jammer"), Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:lightning_disc"), Identifier.parse("ebwizardry:iceball"), Identifier.parse("ebwizardry:greater_fireball"), Identifier.parse("ebwizardry:ice_charge"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:summon_ice_wraith"), Identifier.parse("ebwizardry:summon_lightning_wraith"), Identifier.parse("ebwizardry:oakflesh"), Identifier.parse("ebwizardry:blink"))));
		saveNpc(buildSpellcaster(empire, 4)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:ice_statue"), Identifier.parse("ebwizardry:arcane_jammer"), Identifier.parse("ebwizardry:ring_of_fire"), Identifier.parse("ebwizardry:blizzard"), Identifier.parse("ebwizardry:greater_fireball"), Identifier.parse("ebwizardry:ice_charge"), Identifier.parse("ebwizardry:lightning_disc"), Identifier.parse("ebwizardry:summon_storm_elemental"), Identifier.parse("ebwizardry:summon_phoenix"), Identifier.parse("ebwizardry:summon_ice_giant"), Identifier.parse("ebwizardry:greater_ward"), Identifier.parse("ebwizardry:diamondflesh"), Identifier.parse("ebwizardry:blink"), Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:paralysis"), Identifier.parse("ebwizardry:decoy"))));

		var ent = factionBuilder("ent")
				.soundSet(AncientWarfare.id("ent"));
		saveNpc(buildSoldier(ent)
				.addAttribute(Attributes.MAX_HEALTH, 35)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2));
		saveNpc(buildEliteSoldier(ent)
				.addAttribute(Attributes.MAX_HEALTH, 85)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4));
		saveNpc(buildLeader(ent)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 20)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1));
		saveNpc(buildEliteLeader(ent)
				.addAttribute(Attributes.MAX_HEALTH, 150)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 30)
				.addAttribute(Attributes.ATTACK_DAMAGE, 6)
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1));

		var evil = factionBuilder("evil")
				.soundSet(AncientWarfare.id("malice"));
		saveNpc(buildSoldier(evil)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildEliteSoldier(evil)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("minecraft:iron_axe"), Identifier.parse("minecraft:iron_sword"))
				.experienceDropped(60));
		saveNpc(buildCavalry(evil)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.equipment(Identifier.parse("ancientwarfarenpc:diamond_lance"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildMountedArcher(evil)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0));
		saveNpc(buildLeader(evil)
				.addAttribute(Attributes.MAX_HEALTH, 75)
				.experienceDropped(75));
		saveNpc(buildEliteLeader(evil)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.equipment(Identifier.parse("ancientwarfarenpc:diamond_halberd"), null)
				.experienceDropped(100));
		saveNpc(buildPriest(evil)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(evil)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(evil, 0)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"))));
		saveNpc(buildSpellcaster(evil, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:smoke_bomb"))));
		saveNpc(buildSpellcaster(evil, 2)
				.addAttribute(Attributes.MAX_HEALTH, 35)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:cobwebs"), Identifier.parse("ebwizardry:poison"))));
		saveNpc(buildSpellcaster(evil, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:slime"), Identifier.parse("ebwizardry:greater_fireball"), Identifier.parse("ebwizardry:wither"), Identifier.parse("ebwizardry:summon_skeleton"))));
		saveNpc(buildSpellcaster(evil, 4)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(80)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:black_hole"), Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:darkness_orb"), Identifier.parse("ebwizardry:disintegration"), Identifier.parse("ebwizardry:summon_wither_skeleton"))));

		var giant = factionBuilder("giant")
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
				.soundSet(AncientWarfare.id("giant"));
		saveNpc(buildSoldier(giant)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 7)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:giant_club"), null));
		saveNpc(buildEliteSoldier(giant)
				.addAttribute(Attributes.MAX_HEALTH, 150)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.equipment(Identifier.parse("ancientwarfarenpc:ice_spear"), null));
		saveNpc(buildLeader(giant)
				.addAttribute(Attributes.MAX_HEALTH, 150)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 15)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5));
		saveNpc(buildEliteLeader(giant)
				.addAttribute(Attributes.MAX_HEALTH, 250)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 20)
				.addAttribute(Attributes.ATTACK_DAMAGE, 6)
				.equipment(Identifier.parse("ancientwarfarenpc:giant_club"), null));

		var gnome = factionBuilder("gnome")
				.soundSet(AncientWarfare.id("gnome"));
		saveNpc(buildArcher(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildSoldier(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), null));
		saveNpc(buildEliteSoldier(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.equipment(Identifier.parse("minecraft:iron_sword"), null)
				.experienceDropped(40));
		saveNpc(buildCavalry(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:iron_shield"))
				.experienceDropped(20)
				.simpleMount(EntityType.CHICKEN));
		saveNpc(buildMountedArcher(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.simpleMount(EntityType.CHICKEN));
		saveNpc(buildLeader(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3));
		saveNpc(buildEliteLeader(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2));
		saveNpc(buildTrader(gnome)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20)
				.equipment(Identifier.parse("minecraft:brown_mushroom"), null));

		var good = factionBuilder("good")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(good)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_spear"), null));
		saveNpc(buildEliteSoldier(good)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.experienceDropped(70));
		saveNpc(buildArcher(good)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30));
		saveNpc(buildCavalry(good)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_lance"), Identifier.parse("ancientwarfarenpc:diamond_shield"))
				.experienceDropped(60));
		saveNpc(buildEliteLeader(good)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.experienceDropped(100));
		saveNpc(buildPriest(good)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(good)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(good, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(good, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(good, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(good, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(good, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var gremlin = factionBuilder("gremlin")
				.soundSet(AncientWarfare.id("gremlin"));
		saveNpc(buildArcher(gremlin)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildSoldier(gremlin)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20)
				.equipment(Identifier.parse("minecraft:iron_sword"), null));
		saveNpc(buildCavalry(gremlin)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.equipment(Identifier.parse("minecraft:iron_axe"), null)
				.experienceDropped(20)
				.simpleMount(EntityType.SPIDER));
		saveNpc(buildMountedArcher(gremlin)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20)
				.simpleMount(EntityType.SPIDER));
		saveNpc(buildLeader(gremlin)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.equipment(Identifier.parse("minecraft:iron_sword"), null));
		saveNpc(buildEliteLeader(gremlin)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.equipment(Identifier.parse("minecraft:diamond_sword"), null));
		saveNpc(buildPriest(gremlin)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20)
				.equipment(Identifier.parse("minecraft:torch"), null));
		saveNpc(buildSpellcaster(gremlin, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30));
		saveNpc(buildSpellcaster(gremlin, 1)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:dart"), Identifier.parse("ebwizardry:snare"))));
		saveNpc(buildSpellcaster(gremlin, 2)
				.addAttribute(Attributes.MAX_HEALTH, 25)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:poison"), Identifier.parse("ebwizardry:blink"))));

		var guild = factionBuilder("guild");
		saveNpc(buildSoldier(guild)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildLeader(guild)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null));

		var hobbit = factionBuilder("hobbit")
				.soundSet(AncientWarfare.id("hobbit"));
		saveNpc(buildArcher(hobbit)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30));
		saveNpc(buildSoldier(hobbit)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50)
				.equipment(Identifier.parse("ancientwarfarenpc:pitchfork"), null));
		saveNpc(buildEliteSoldier(hobbit)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(60)
				.equipment(Identifier.parse("ancientwarfarenpc:scythe"), null));
		saveNpc(buildLeader(hobbit)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.experienceDropped(70)
				.equipment(Identifier.parse("minecraft:iron_sword"), null));
		saveNpc(buildPriest(hobbit)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(hobbit)
				.soundSet(AncientWarfare.id("human_female")));

		var icelord = factionBuilder("icelord")
				.soundSet(AncientWarfare.id("zombie"));
		saveNpc(buildSoldier(icelord));
		saveNpc(buildEliteSoldier(icelord)
				.addAttribute(Attributes.MAX_HEALTH, 45)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3));
		saveNpc(buildCavalry(icelord)
				.equipment(Identifier.parse("ancientwarfarenpc:ice_spear"), Identifier.parse("ancientwarfarenpc:iron_shield"))
				.simpleMount(EntityType.SKELETON_HORSE));
		saveNpc(buildLeader(icelord)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 6)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4));
		saveNpc(buildEliteLeader(icelord)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("minecraft:diamond_axe"), null));
		saveNpc(buildPriest(icelord));
		saveNpc(buildSpellcaster(icelord, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30));
		saveNpc(buildSpellcaster(icelord, 1)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(60)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:frost_ray"), Identifier.parse("ebwizardry:ice_charge"), Identifier.parse("ebwizardry:iceball"), Identifier.parse("ebwizardry:ice_lance"), Identifier.parse("ebwizardry:ice_shroud"), Identifier.parse("ebwizardry:ironflesh"))));
		saveNpc(buildSpellcaster(icelord, 2)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:iceball"), Identifier.parse("ebwizardry:ice_charge"), Identifier.parse("ebwizardry:ice_lance"), Identifier.parse("ebwizardry:ice_shroud"), Identifier.parse("ebwizardry:blizzard"), Identifier.parse("ebwizardry:hailstorm"), Identifier.parse("ebwizardry:diamondflesh"))));

		var ishtari = factionBuilder("ishtari")
				.soundSet(AncientWarfare.id("ishtari_mummy"));
		saveNpc(buildSoldier(ishtari));
		saveNpc(buildEliteSoldier(ishtari)
				.equipment(Identifier.parse("minecraft:golden_sword"), null)
				.soundSet(AncientWarfare.id("zombie")));
		saveNpc(buildLeader(ishtari)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 5)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.soundSet(AncientWarfare.id("ishtari_anubite"))
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null));
		saveNpc(buildEliteLeader(ishtari)
				.soundSet(AncientWarfare.id("ishtari_pharoah"))
				.equipment(Identifier.parse("minecraft:diamond_sword"), null));

		var klown = factionBuilder("klown");
		saveNpc(buildSoldier(klown)
				.addAttribute(Attributes.MAX_HEALTH, 6)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_cleaver"), null));
		saveNpc(buildEliteSoldier(klown)
				.soundSet(AncientWarfare.id("klown"))
				.equipment(Identifier.parse("minecraft:iron_axe"), null));
		saveNpc(buildCavalry(klown)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_cleaver"), null)
				.simpleMount(EntityType.ZOMBIE_HORSE)
				.soundSet(AncientWarfare.id("norska")));
		saveNpc(buildMountedArcher(klown)
				.addAttribute(Attributes.MAX_HEALTH, 6)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.equipment(Identifier.parse("minecraft:bow"), null)
				.simpleMount(EntityType.PIG));
		saveNpc(buildLeader(klown)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 2)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.soundSet(AncientWarfare.id("norska"))
				.equipment(Identifier.parse("ancientwarfarenpc:giant_club"), null));
		saveNpc(buildEliteLeader(klown)
				.addAttribute(Attributes.MAX_HEALTH, 120)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.soundSet(AncientWarfare.id("klown"))
				.equipment(Identifier.parse("ancientwarfarenpc:iron_cleaver"), null));
		saveNpc(buildPriest(klown)
				.soundSet(AncientWarfare.id("human")));
		saveNpc(buildSpellcaster(klown, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(klown, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(klown, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(klown, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(klown, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var kong = factionBuilder("kong")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(kong)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.equipment(Identifier.parse("ancientwarfarenpc:wooden_spear"), null)
				.experienceDropped(15));
		saveNpc(buildEliteSoldier(kong)
				.equipment(Identifier.parse("minecraft:wooden_axe"), null));
		saveNpc(buildArcher(kong)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.experienceDropped(15));
		saveNpc(buildEliteArcher(kong)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildLeader(kong)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5));
		saveNpc(buildEliteLeader(kong)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_cleaver"), null)
				.experienceDropped(60));
		saveNpc(buildPriest(kong)
				.equipment(Identifier.parse("minecraft:rotten_flesh"), null));
		saveNpc(buildTrader(kong)
				.equipment(Identifier.parse("minecraft:bone"), null));
		saveNpc(buildFemaleCivilian(kong)
				.soundSet(AncientWarfare.id("human_female")));

		var lizardman = factionBuilder("lizardman")
				.soundSet(AncientWarfare.id("lizardman"));
		saveNpc(buildSoldier(lizardman)
				.equipment(Identifier.parse("ancientwarfarenpc:stone_cleaver"), null));
		saveNpc(buildEliteSoldier(lizardman)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 8)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1));
		saveNpc(buildLeader(lizardman)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 5)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_cleaver"), null));
		saveNpc(buildEliteLeader(lizardman)
				.addAttribute(Attributes.MAX_HEALTH, 120)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 6)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4));
		saveNpc(buildPriest(lizardman)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 2)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2));
		saveNpc(buildSpellcaster(lizardman, 0)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.experienceDropped(30));
		saveNpc(buildSpellcaster(lizardman, 1)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:poison"), Identifier.parse("ebwizardry:snare"))));

		var mindflayer = factionBuilder("mindflayer");
		saveNpc(buildSoldier(mindflayer)
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.soundSet(AncientWarfare.id("kobold"))
				.equipment(Identifier.parse("minecraft:stone_axe"), null));
		saveNpc(buildEliteSoldier(mindflayer)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.soundSet(AncientWarfare.id("gargoyle"))
				.equipment(Identifier.parse("minecraft:iron_sword"), null));
		saveNpc(buildLeader(mindflayer)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 6)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.soundSet(AncientWarfare.id("owlbear")));
		saveNpc(buildEliteLeader(mindflayer)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 16)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.soundSet(AncientWarfare.id("ent")));
		saveNpc(buildSpellcaster(mindflayer, 0)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"))));
		saveNpc(buildSpellcaster(mindflayer, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:smoke_bomb"))));
		saveNpc(buildSpellcaster(mindflayer, 2)
				.addAttribute(Attributes.MAX_HEALTH, 35)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:cobwebs"), Identifier.parse("ebwizardry:poison"))));
		saveNpc(buildSpellcaster(mindflayer, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:slime"), Identifier.parse("ebwizardry:greater_fireball"), Identifier.parse("ebwizardry:wither"), Identifier.parse("ebwizardry:summon_skeleton"))));
		saveNpc(buildSpellcaster(mindflayer, 4)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(80)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:black_hole"), Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:darkness_orb"), Identifier.parse("ebwizardry:disintegration"), Identifier.parse("ebwizardry:summon_wither_skeleton"))));

		var minossian = factionBuilder("minossian");
		saveNpc(buildSoldier(minossian)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 4)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1));
		saveNpc(buildEliteSoldier(minossian)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 2)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.equipment(Identifier.parse("minecraft:iron_axe"), null));
		saveNpc(buildLeader(minossian)
				.addAttribute(Attributes.MAX_HEALTH, 75)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 3)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1));
		saveNpc(buildEliteLeader(minossian)
				.addAttribute(Attributes.MAX_HEALTH, 65)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 4)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("minecraft:diamond_axe"), null));
		saveNpc(buildSpellcaster(minossian, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(minossian, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(40)
				.spells(Set.of(Identifier.parse("ebwizardry:smoke_bomb"), Identifier.parse("ebwizardry:dart"), Identifier.parse("ebwizardry:poison_bomb"), Identifier.parse("ebwizardry:snare"))));
		saveNpc(buildSpellcaster(minossian, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(50)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:summon_skeleton"))));
		saveNpc(buildSpellcaster(minossian, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(60)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:cobwebs"), Identifier.parse("ebwizardry:poison"), Identifier.parse("ebwizardry:spider_swarm"))));
		saveNpc(buildSpellcaster(minossian, 4)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(80)
				.spells(Set.of(Identifier.parse("ebwizardry:lightning_web"), Identifier.parse("ebwizardry:entrapment"), Identifier.parse("ebwizardry:greater_ward"), Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:summon_shadow_wraith"), Identifier.parse("ebwizardry:curse_of_undeath"))));

		var monster = factionBuilder("monster")
				.soundSet(AncientWarfare.id("monster"));
		saveNpc(buildSoldier(monster)
				.addAttribute(Attributes.MAX_HEALTH, 65)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 4)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3));
		saveNpc(buildEliteSoldier(monster)
				.addAttribute(Attributes.MAX_HEALTH, 85)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 8)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4));
		saveNpc(buildLeader(monster)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 12)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5));
		saveNpc(buildEliteLeader(monster)
				.addAttribute(Attributes.MAX_HEALTH, 150)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 16)
				.addAttribute(Attributes.ATTACK_DAMAGE, 6));

		var nogg = factionBuilder("nogg")
				.soundSet(AncientWarfare.id("norska"));
		saveNpc(buildSoldier(nogg)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null)
				.experienceDropped(30));
		saveNpc(buildEliteSoldier(nogg)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildArcher(nogg)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30));
		saveNpc(buildEliteArcher(nogg)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildCavalry(nogg)
				.addAttribute(Attributes.MAX_HEALTH, 65)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_lance"), Identifier.parse("ancientwarfarenpc:shield_round_5"))
				.experienceDropped(65));
		saveNpc(buildSiegeEngineer(nogg)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildPriest(nogg)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(nogg)
				.soundSet(AncientWarfare.id("human_female")));

		var norska = factionBuilder("norska")
				.soundSet(AncientWarfare.id("norska"));
		saveNpc(buildSoldier(norska)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.equipment(Identifier.parse("minecraft:iron_sword"), Identifier.parse("ancientwarfarenpc:shield_round_2"))
				.experienceDropped(30));
		saveNpc(buildEliteSoldier(norska)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.equipment(Identifier.parse("minecraft:iron_axe"), Identifier.parse("minecraft:iron_axe"))
				.experienceDropped(50));
		saveNpc(buildCavalry(norska)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:shield_round_3"))
				.simpleMount(EntityType.POLAR_BEAR));
		saveNpc(buildLeader(norska)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.equipment(Identifier.parse("minecraft:diamond_axe"), Identifier.parse("ancientwarfarenpc:shield_round_3"))
				.experienceDropped(70));
		saveNpc(buildEliteLeader(norska)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.equipment(Identifier.parse("minecraft:diamond_axe"), Identifier.parse("ancientwarfarenpc:shield_round_4"))
				.experienceDropped(100));
		saveNpc(buildFemaleCivilian(norska)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(norska, 0)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(norska, 1)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40)
				.spells(Set.of(Identifier.parse("ebwizardry:fire_sigil"), Identifier.parse("ebwizardry:frost_sigil"), Identifier.parse("ebwizardry:ironflesh"), Identifier.parse("ebwizardry:ring_of_fire"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:font_of_vitality"))));
		saveNpc(buildSpellcaster(norska, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.soundSet(AncientWarfare.id("human_female"))
				.experienceDropped(50)
				.spells(Set.of(Identifier.parse("ebwizardry:whirlwind"), Identifier.parse("ebwizardry:tornado"), Identifier.parse("ebwizardry:font_of_vitality"), Identifier.parse("ebwizardry:greater_ward"))));
		saveNpc(buildSpellcaster(norska, 3)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(60)
				.spells(Set.of(Identifier.parse("ebwizardry:arc"), Identifier.parse("ebwizardry:ironflesh"), Identifier.parse("ebwizardry:thunderstorm"), Identifier.parse("ebwizardry:lightning_bolt"), Identifier.parse("ebwizardry:lightning_hammer"))));
		saveNpc(buildSpellcaster(norska, 4)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.experienceDropped(80)
				.spells(Set.of(Identifier.parse("ebwizardry:fireskin"))));

		var orc = factionBuilder("orc")
				.soundSet(AncientWarfare.id("orc"));
		saveNpc(buildSoldier(orc)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.equipment(null, Identifier.parse("ancientwarfarenpc:wooden_shield"))
				.experienceDropped(15));
		saveNpc(buildEliteSoldier(orc)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.soundSet(AncientWarfare.id("orc_uruk"))
				.equipment(Identifier.parse("ancientwarfarenpc:iron_cleaver"), Identifier.parse("ancientwarfarenpc:iron_cleaver"))
				.experienceDropped(50));
		saveNpc(buildArcher(orc)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.experienceDropped(15));
		saveNpc(buildCavalry(orc)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), null)
				.experienceDropped(50));
		saveNpc(buildMountedArcher(orc)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildLeader(orc)
				.soundSet(AncientWarfare.id("orc_uruk"))
				.equipment(Identifier.parse("ancientwarfarenpc:iron_cleaver"), null));
		saveNpc(buildEliteLeader(orc)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.soundSet(AncientWarfare.id("orc_uruk")));
		saveNpc(buildPriest(orc)
				.equipment(Identifier.parse("minecraft:skull"), null));
		saveNpc(buildTrader(orc)
				.equipment(Identifier.parse("minecraft:feather"), null));
		saveNpc(buildSpellcaster(orc, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(orc, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:whirlwind"), Identifier.parse("ebwizardry:oakflesh"))));
		saveNpc(buildSpellcaster(orc, 2)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:lightning_arrow"), Identifier.parse("ebwizardry:chain_lightning"), Identifier.parse("ebwizardry:paralysis"), Identifier.parse("ebwizardry:summon_lightning_wraith"), Identifier.parse("ebwizardry:static_aura"))));
		saveNpc(buildSpellcaster(orc, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:darkness_orb"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:mind_trick"), Identifier.parse("ebwizardry:summon_skeleton"), Identifier.parse("ebwizardry:intimidate"))));

		var pirate = factionBuilder("pirate")
				.soundSet(AncientWarfare.id("pirate"));
		saveNpc(buildSoldier(pirate)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.experienceDropped(15));
		saveNpc(buildArcher(pirate)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.experienceDropped(15));
		saveNpc(buildEliteArcher(pirate)
				.addAttribute(Attributes.MAX_HEALTH, 15)
				.experienceDropped(15));
		saveNpc(buildCavalry(pirate)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildMountedArcher(pirate)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20));
		saveNpc(buildLeader(pirate)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildEliteLeader(pirate)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(60));
		saveNpc(buildFemaleCivilian(pirate)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(pirate, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(pirate, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(pirate, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(pirate, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(pirate, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var rakshasa = factionBuilder("rakshasa");
		saveNpc(buildEliteSoldier(rakshasa)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_cleaver"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildEliteLeader(rakshasa)
				.addAttribute(Attributes.MAX_HEALTH, 75)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_cleaver"), null));
		saveNpc(buildSpellcaster(rakshasa, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(rakshasa, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(rakshasa, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(rakshasa, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(rakshasa, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var reiksgard = factionBuilder("reiksgard")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildEliteSoldier(reiksgard)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null)
				.experienceDropped(30));
		saveNpc(buildSoldier(reiksgard)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildCavalry(reiksgard)
				.addAttribute(Attributes.MAX_HEALTH, 65)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_lance"), Identifier.parse("ancientwarfarenpc:iron_shield"))
				.experienceDropped(65));
		saveNpc(buildLeader(reiksgard)
				.equipment(Identifier.parse("minecraft:diamond_sword"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildFemaleCivilian(reiksgard)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(reiksgard, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(reiksgard, 1)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:thunderbolt"), Identifier.parse("ebwizardry:ignite"), Identifier.parse("ebwizardry:freeze"), Identifier.parse("ebwizardry:ward"), Identifier.parse("ebwizardry:snare"), Identifier.parse("ebwizardry:smoke_bomb"))));
		saveNpc(buildSpellcaster(reiksgard, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:frost_ray"), Identifier.parse("ebwizardry:flame_ray"), Identifier.parse("ebwizardry:lightning_ray"), Identifier.parse("ebwizardry:iceball"), Identifier.parse("ebwizardry:lightning_arrow"), Identifier.parse("ebwizardry:ward"), Identifier.parse("ebwizardry:oakflesh"))));
		saveNpc(buildSpellcaster(reiksgard, 3)
				.addAttribute(Attributes.MAX_HEALTH, 35)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:arcane_jammer"), Identifier.parse("ebwizardry:fireball"), Identifier.parse("ebwizardry:lightning_disc"), Identifier.parse("ebwizardry:iceball"), Identifier.parse("ebwizardry:greater_fireball"), Identifier.parse("ebwizardry:ice_charge"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:summon_ice_wraith"), Identifier.parse("ebwizardry:summon_lightning_wraith"), Identifier.parse("ebwizardry:oakflesh"), Identifier.parse("ebwizardry:blink"))));
		saveNpc(buildSpellcaster(reiksgard, 4)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:ice_statue"), Identifier.parse("ebwizardry:arcane_jammer"), Identifier.parse("ebwizardry:ring_of_fire"), Identifier.parse("ebwizardry:blizzard"), Identifier.parse("ebwizardry:greater_fireball"), Identifier.parse("ebwizardry:ice_charge"), Identifier.parse("ebwizardry:lightning_disc"), Identifier.parse("ebwizardry:summon_storm_elemental"), Identifier.parse("ebwizardry:summon_phoenix"), Identifier.parse("ebwizardry:summon_ice_giant"), Identifier.parse("ebwizardry:greater_ward"), Identifier.parse("ebwizardry:diamondflesh"), Identifier.parse("ebwizardry:blink"), Identifier.parse("ebwizardry:banish"), Identifier.parse("ebwizardry:paralysis"), Identifier.parse("ebwizardry:decoy"))));

		var sarkonid = factionBuilder("sarkonid")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(sarkonid)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_spear"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildEliteSoldier(sarkonid)
				.equipment(Identifier.parse("minecraft:golden_sword"), Identifier.parse("minecraft:golden_sword")));
		saveNpc(buildCavalry(sarkonid)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_lance"), null)
				.experienceDropped(50));
		saveNpc(buildLeader(sarkonid)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null));
		saveNpc(buildEliteLeader(sarkonid)
				.equipment(Identifier.parse("minecraft:golden_sword"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildFemaleCivilian(sarkonid)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(sarkonid, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(sarkonid, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(sarkonid, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(sarkonid, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(sarkonid, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var sealsker = factionBuilder("sealsker")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(sealsker)
				.equipment(Identifier.parse("ancientwarfarenpc:ice_spear"), null));
		saveNpc(buildEliteSoldier(sealsker)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.equipment(Identifier.parse("ancientwarfarenpc:ice_spear"), null)
				.experienceDropped(50));
		saveNpc(buildEliteArcher(sealsker)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildLeader(sealsker)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:ice_spear"), null)
				.experienceDropped(80));
		saveNpc(buildEliteLeader(sealsker)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.experienceDropped(40));
		saveNpc(buildPriest(sealsker)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(sealsker)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(sealsker, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(sealsker, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(sealsker, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(sealsker, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(sealsker, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var shakayana = factionBuilder("shakayana")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(shakayana)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_spear"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildEliteSoldier(shakayana)
				.equipment(Identifier.parse("minecraft:golden_sword"), Identifier.parse("minecraft:golden_axe")));
		saveNpc(buildCavalry(shakayana)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_lance"), null)
				.experienceDropped(50));
		saveNpc(buildLeader(shakayana)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null));
		saveNpc(buildEliteLeader(shakayana)
				.equipment(Identifier.parse("minecraft:golden_sword"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildFemaleCivilian(shakayana)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(shakayana, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(shakayana, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(shakayana, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(shakayana, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(shakayana, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var smingol = factionBuilder("smingol");
		saveNpc(buildEliteSoldier(smingol)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.equipment(Identifier.parse("minecraft:iron_sword"), Identifier.parse("minecraft:iron_sword"))
				.experienceDropped(30));
		saveNpc(buildSoldier(smingol)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildCavalry(smingol)
				.addAttribute(Attributes.MAX_HEALTH, 65)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_lance"), Identifier.parse("ancientwarfarenpc:iron_shield"))
				.experienceDropped(65));
		saveNpc(buildLeader(smingol)
				.equipment(Identifier.parse("minecraft:diamond_sword"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildPriest(smingol)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(smingol)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(smingol, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(smingol, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(smingol, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(smingol, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(smingol, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var undead = factionBuilder("undead")
				.soundSet(AncientWarfare.id("zombie"));
		saveNpc(buildSoldier(undead)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildEliteSoldier(undead)
				.equipment(Identifier.parse("ancientwarfarenpc:scythe"), null)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildArcher(undead)
				.burnsInSun(true)
				.soundSet(AncientWarfare.id("zombie"))
				.undead(true));
		saveNpc(buildEliteArcher(undead)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildCavalry(undead)
				.burnsInSun(true)
				.undead(true)
				.simpleMount(EntityType.SKELETON_HORSE));
		saveNpc(buildMountedArcher(undead)
				.burnsInSun(true)
				.undead(true)
				.simpleMount(EntityType.SKELETON_HORSE));
		saveNpc(buildLeader(undead)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildEliteLeader(undead)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.equipment(Identifier.parse("ancientwarfarenpc:death_scythe"), null)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildSiegeEngineer(undead)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildPriest(undead)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildBard(undead)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildTrader(undead)
				.equipment(Identifier.parse("minecraft:bone"), null)
				.burnsInSun(true)
				.undead(true));
		saveNpc(buildSpellcaster(undead, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.burnsInSun(true)
				.undead(true)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(undead, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(40)
				.spells(Set.of(Identifier.parse("ebwizardry:lightning_arrow"), Identifier.parse("ebwizardry:summon_zombie"), Identifier.parse("ebwizardry:summon_skeleton"))));
		saveNpc(buildSpellcaster(undead, 2)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.spells(Set.of(Identifier.parse("ebwizardry:wither"), Identifier.parse("ebwizardry:poison"), Identifier.parse("ebwizardry:reversal"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_zombie"), Identifier.parse("ebwizardry:summon_skeleton"))));
		saveNpc(buildSpellcaster(undead, 3)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(60)
				.spells(Set.of(Identifier.parse("ebwizardry:darkness_orb"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:decay"), Identifier.parse("ebwizardry:blink"), Identifier.parse("ebwizardry:summon_wither_skeleton"))));
		saveNpc(buildSpellcaster(undead, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(80)
				.spells(Set.of(Identifier.parse("ebwizardry:darkness_orb"), Identifier.parse("ebwizardry:wither_skull"), Identifier.parse("ebwizardry:decay"), Identifier.parse("ebwizardry:summon_skeleton_legion"), Identifier.parse("ebwizardry:summon_shadow_wraith"))));

		var vampire = factionBuilder("vampire");
		saveNpc(buildSoldier(vampire)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 6)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.burnsInSun(false)
				.canSwim(false)
				.soundSet(AncientWarfare.id("monster")));
		saveNpc(buildEliteSoldier(vampire)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ARMOR, 1)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.burnsInSun(true)
				.canSwim(false)
				.undead(true)
				.soundSet(AncientWarfare.id("vampire_bride")));
		saveNpc(buildArcher(vampire)
				.burnsInSun(false)
				.soundSet(AncientWarfare.id("malice")));
		saveNpc(buildEliteArcher(vampire)
				.burnsInSun(false)
				.soundSet(AncientWarfare.id("malice")));
		saveNpc(buildCavalry(vampire)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.addAttribute(Attributes.ARMOR, 1)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("minecraft:diamond_sword"), null)
				.experienceDropped(80)
				.burnsInSun(false));
		saveNpc(buildMountedArcher(vampire)
				.burnsInSun(false)
				.soundSet(AncientWarfare.id("malice")));
		saveNpc(buildLeader(vampire)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.addAttribute(Attributes.ARMOR, 2)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.experienceDropped(80)
				.burnsInSun(true)
				.canSwim(false)
				.undead(true)
				.soundSet(AncientWarfare.id("vampire")));
		saveNpc(buildEliteLeader(vampire)
				.addAttribute(Attributes.MAX_HEALTH, 120)
				.addAttribute(Attributes.ARMOR, 4)
				.addAttribute(Attributes.ATTACK_DAMAGE, 5)
				.experienceDropped(120)
				.equipment(Identifier.parse("minecraft:diamond_sword"), null)
				.burnsInSun(true)
				.undead(true)
				.soundSet(AncientWarfare.id("vampire_boss"))
				.canSwim(false));
		saveNpc(buildSiegeEngineer(vampire)
				.burnsInSun(false)
				.soundSet(AncientWarfare.id("malice")));
		saveNpc(buildPriest(vampire)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.burnsInSun(true)
				.undead(true)
				.canSwim(false)
				.soundSet(AncientWarfare.id("vampire")));
		saveNpc(buildTrader(vampire)
				.equipment(Identifier.parse("minecraft:bone"), null)
				.burnsInSun(true)
				.undead(true)
				.soundSet(AncientWarfare.id("vampire")));
		saveNpc(buildFemaleCivilian(vampire)
				.burnsInSun(false)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildMaleCivilian(vampire)
				.burnsInSun(false)
				.soundSet(AncientWarfare.id("human")));
		saveNpc(buildSpellcaster(vampire, 0)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.burnsInSun(true)
				.undead(true)
				.soundSet(AncientWarfare.id("vampire_bride"))
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(vampire, 1)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(40)
				.spells(Set.of(Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:invisibility"), Identifier.parse("ebwizardry:blink"))));
		saveNpc(buildSpellcaster(vampire, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(50)
				.spells(Set.of(Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:blink"), Identifier.parse("ebwizardry:decoy"), Identifier.parse("ebwizardry:oakflesh"))));
		saveNpc(buildSpellcaster(vampire, 3)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(60)
				.spells(Set.of(Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:ironflesh"), Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:font_of_vitality"))));
		saveNpc(buildSpellcaster(vampire, 4)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.experienceDropped(80)
				.spells(Set.of(Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:diamondflesh"), Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:font_of_vitality"))));

		var vyncan = factionBuilder("vyncan")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(vyncan)
				.equipment(Identifier.parse("ancientwarfarenpc:obsidian_spear"), Identifier.parse("ancientwarfarenpc:stone_shield")));
		saveNpc(buildEliteSoldier(vyncan)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.equipment(Identifier.parse("ancientwarfarenpc:macuahuitl"), null)
				.experienceDropped(50));
		saveNpc(buildEliteArcher(vyncan)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildLeader(vyncan)
				.addAttribute(Attributes.MAX_HEALTH, 80)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.experienceDropped(80));
		saveNpc(buildPriest(vyncan)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(vyncan)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(vyncan, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(vyncan, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(vyncan, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(vyncan, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(vyncan, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var witchbane = factionBuilder("witchbane")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(witchbane)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:shield_witchbane_1"))
				.experienceDropped(40));
		saveNpc(buildEliteSoldier(witchbane)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null)
				.experienceDropped(60));
		saveNpc(buildCavalry(witchbane)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_lance"), Identifier.parse("ancientwarfarenpc:shield_witchbane_2"))
				.experienceDropped(60));
		saveNpc(buildLeader(witchbane)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.equipment(Identifier.parse("minecraft:golden_sword"), Identifier.parse("ancientwarfarenpc:shield_witchbane_2"))
				.experienceDropped(70));
		saveNpc(buildEliteLeader(witchbane)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.equipment(Identifier.parse("minecraft:diamond_sword"), Identifier.parse("ancientwarfarenpc:shield_witchbane_2"))
				.experienceDropped(100));
		saveNpc(buildSiegeEngineer(witchbane)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildPriest(witchbane)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(witchbane)
				.soundSet(AncientWarfare.id("human_female")));

		var wizardly = factionBuilder("wizardly")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Identifier.parse("ancientwarfarenpc:golden_halberd"), null));
		saveNpc(buildEliteSoldier(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30));
		saveNpc(buildArcher(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30));
		saveNpc(buildLeader(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 55)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.experienceDropped(55));
		saveNpc(buildEliteLeader(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 70)
				.addAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
				.equipment(Identifier.parse("minecraft:iron_axe"), null)
				.experienceDropped(70));
		saveNpc(buildPriest(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.equipment(Identifier.parse("minecraft:golden_apple"), null)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.soundSet(AncientWarfare.id("human_female"))
				.equipment(Identifier.parse("minecraft:book"), null)
				.experienceDropped(10));
		saveNpc(buildMaleCivilian(wizardly)
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.equipment(Identifier.parse("minecraft:book"), null)
				.experienceDropped(10));
		saveNpc(buildSpellcaster(wizardly, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(wizardly, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(wizardly, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(wizardly, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(wizardly, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var xoltec = factionBuilder("xoltec")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(xoltec)
				.equipment(Identifier.parse("ancientwarfarenpc:obsidian_spear"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildEliteSoldier(xoltec)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.equipment(Identifier.parse("ancientwarfarenpc:macuahuitl"), null)
				.experienceDropped(40));
		saveNpc(buildEliteArcher(xoltec)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(40));
		saveNpc(buildCavalry(xoltec)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.equipment(Identifier.parse("ancientwarfarenpc:obsidian_spear"), Identifier.parse("ancientwarfarenpc:gold_shield"))
				.experienceDropped(60)
				.simpleMount(EntityType.LLAMA));
		saveNpc(buildMountedArcher(xoltec)
				.simpleMount(EntityType.LLAMA));
		saveNpc(buildLeader(xoltec)
				.equipment(Identifier.parse("ancientwarfarenpc:macuahuitl"), null));
		saveNpc(buildEliteLeader(xoltec)
				.addAttribute(Attributes.MAX_HEALTH, 100)
				.equipment(Identifier.parse("ancientwarfarenpc:macuahuitl"), Identifier.parse("ancientwarfarenpc:macuahuitl"))
				.experienceDropped(100));
		saveNpc(buildPriest(xoltec)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.experienceDropped(50));
		saveNpc(buildFemaleCivilian(xoltec)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(xoltec, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(xoltec, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(xoltec, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(xoltec, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(xoltec, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var zamurai = factionBuilder("zamurai")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(zamurai)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:iron_shield")));
		saveNpc(buildEliteSoldier(zamurai)
				.equipment(Identifier.parse("minecraft:iron_sword"), null));
		saveNpc(buildCavalry(zamurai)
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.equipment(Identifier.parse("minecraft:iron_sword"), null)
				.experienceDropped(50));
		saveNpc(buildLeader(zamurai)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_halberd"), null));
		saveNpc(buildEliteLeader(zamurai)
				.equipment(Identifier.parse("minecraft:golden_sword"), Identifier.parse("ancientwarfarenpc:gold_shield")));
		saveNpc(buildFemaleCivilian(zamurai)
				.soundSet(AncientWarfare.id("human_female")));
		saveNpc(buildSpellcaster(zamurai, 0)
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of()));
		saveNpc(buildSpellcaster(zamurai, 1)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(30)
				.equipment(Identifier.parse("minecraft:book"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:fireball"))));
		saveNpc(buildSpellcaster(zamurai, 2)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.experienceDropped(40)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"))));
		saveNpc(buildSpellcaster(zamurai, 3)
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.experienceDropped(50)
				.equipment(Identifier.parse("minecraft:stick"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:life_drain"), Identifier.parse("ebwizardry:summon_blaze"))));
		saveNpc(buildSpellcaster(zamurai, 4)
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.experienceDropped(100)
				.equipment(Identifier.parse("minecraft:skull"), null)
				.spells(Set.of(Identifier.parse("ebwizardry:curse_of_enfeeblement"), Identifier.parse("ebwizardry:firebolt"), Identifier.parse("ebwizardry:summon_blaze"), Identifier.parse("ebwizardry:fireskin"), Identifier.parse("ebwizardry:ring_of_fire"))));

		var zimba = factionBuilder("zimba")
				.soundSet(AncientWarfare.id("human"));
		saveNpc(buildSoldier(zimba)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:shield_tribal_1")));
		saveNpc(buildEliteSoldier(zimba)
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.equipment(Identifier.parse("minecraft:iron_sword"), Identifier.parse("ancientwarfarenpc:wooden_shield"))
				.experienceDropped(30));
		saveNpc(buildCavalry(zimba)
				.equipment(Identifier.parse("ancientwarfarenpc:iron_spear"), Identifier.parse("ancientwarfarenpc:shield_tribal_2")));
		saveNpc(buildLeader(zimba)
				.equipment(Identifier.parse("minecraft:diamond_sword"), Identifier.parse("ancientwarfarenpc:shield_tribal_2")));
		saveNpc(buildPriest(zimba)
				.equipment(Identifier.parse("minecraft:stick"), null));
		saveNpc(buildTrader(zimba)
				.equipment(Identifier.parse("minecraft:stick"), null));
		saveNpc(buildFemaleCivilian(zimba)
				.soundSet(AncientWarfare.id("human_female")));

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

	public FactionNpcData.Builder buildTrader(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("trader"))
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.experienceDropped(40)
				.equipment(Items.BOOK, null)
				.lootTable(Identifier.withDefaultNamespace("empty"));
	}

	public FactionNpcData.Builder buildSpellcaster(FactionNpcData.Builder builder, int variant) {
		return builder.copy()
				.npcType(AncientWarfare.id(variant > 0 ? "spell_caster_" + variant : "spell_caster"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(40)
				.lootTable(AncientWarfare.id("entities/soldier"))
				.spells(Set.of(Identifier.parse("ebwizardry:magic_missile")));
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
				.addAttribute(Attributes.ATTACK_DAMAGE, 0);
	}

	public FactionNpcData.Builder buildFemaleCivilian(FactionNpcData.Builder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("female_civilian"))
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0);
	}

	/**
	 * Default npc parameters for any faction
	 */
	public FactionNpcData.Builder factionBuilder(String faction) {
		return FactionNpcData.builder()
				.faction(AncientWarfare.id(faction))
				.soundSet(AncientWarfare.id("human_male"))
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0.3)
				.addAttribute(Attributes.FOLLOW_RANGE, 35)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.experienceDropped(30)
				.navigation(true, false, true)
				.burnsInSun(false)
				.undead(false)
				.lootTable(Identifier.withDefaultNamespace("empty"));
	}
}
