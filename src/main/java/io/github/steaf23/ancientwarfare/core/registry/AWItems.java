package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.item.UnobtainableItem;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.core.research.ResearchBook;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import io.github.steaf23.ancientwarfare.core.versioned.CreativeTabManager;
import io.github.steaf23.ancientwarfare.npc.entity.faction.FactionNpc;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import io.github.steaf23.ancientwarfare.structure.item.CoinItem;
import io.github.steaf23.ancientwarfare.npc.item.CommandBaton;
import io.github.steaf23.ancientwarfare.structure.item.WardSealItem;
import io.github.steaf23.ancientwarfare.worksite.marker.SurveyArea;
import io.github.steaf23.ancientwarfare.worksite.surveykit.SurveyKit;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.StandingAndWallBlockItem;

import java.util.List;
import java.util.function.Function;

public class AWItems {

	public static final ResearchBook RESEARCH_BOOK = registerItem("research_book", ResearchBook::new,
			new Item.Properties()
					.stacksTo(1));

	public static final Item STEEL_INGOT = registerItem("steel_ingot",
			new Item.Properties());

	public static final CoinItem COINS = registerItemNoCreativeTab("coins", CoinItem::new,
			new Item.Properties()
					.component(AWComponents.COIN_METAL, CoinMetal.GOLD));

//	public static final CoinItemStacked COIN_STACK = registerItem("coin_stack", p -> new CoinItemStacked(AWBlocks.COIN_STACK, p, CoinStackBlock.StackSize.SIZE_128), new Item.Properties());
//	public static final CoinItemStacked COIN_STACK_SLAB = registerItem("coin_stack_slab", p -> new CoinItemStacked(AWBlocks.COIN_STACK, p, CoinStackBlock.StackSize.SIZE_64), new Item.Properties());

	public static final CommandBaton WOODEN_COMMAND_BATON = AWItems.registerItem("wooden_command_baton", CommandBaton::new,
			new Item.Properties()
					.component(AWComponents.SELECTED_ENTITIES, List.of()));

	public static final SpawnEggItem NPC_SPAWNER = AWItems.registerItem("npc_spawner", SpawnEggItem::new,
			new Item.Properties()
					.spawnEgg(AWEntities.PLAYER_NPC));

	public static final SpawnEggItem FACTION_NPC_SPAWNER = AWItems.registerItemNoCreativeTab("faction_npc_spawner", SpawnEggItem::new,
			new Item.Properties()
					.spawnEgg(AWEntities.FACTION_NPC));

	public static final WardSealItem WARD_SEAL = AWItems.registerItem("ward_seal", WardSealItem::new, new Item.Properties());

	public static final Item FACTION_BANNER = AWItems.registerItemNoCreativeTab("faction_banner", properties -> new StandingAndWallBlockItem(AWBlocks.FACTION_BANNER, AWBlocks.FACTION_WALL_BANNER, Direction.DOWN, properties),
			new Item.Properties()
					.component(AWComponents.FACTION_ITEM, Factions.NEUTRAL_KEY));

	public static final SurveyKit SURVEY_KIT = AWItems.registerItem("survey_kit", SurveyKit::new,
			new Item.Properties()
					.durability(16)
					.component(AWComponents.SURVEY_STAKES, SurveyArea.EMPTY));

	public static final Item[] COMMAND_BATONS = {
			WOODEN_COMMAND_BATON,
	};

	public static final Item TOWN_HALL_KEY_DUMMY = AWItems.registerItem("town_hall_key", UnobtainableItem::new, new Item.Properties());

	public static void initialize() {
		CreativeTabManager.initialize();
		CreativeTabManager.addItemsToModTab(group -> {
			ItemStack stack = new ItemStack(COINS);
			stack.set(AWComponents.COIN_METAL, CoinMetal.GOLD);
			group.addItem(stack);
			ItemStack stack2 = new ItemStack(COINS);
			stack2.set(AWComponents.COIN_METAL, CoinMetal.SILVER);
			group.addItem(stack2);
			ItemStack stack3 = new ItemStack(COINS);
			stack3.set(AWComponents.COIN_METAL, CoinMetal.COPPER);
			group.addItem(stack3);
			ItemStack stack4 = new ItemStack(COINS);
			stack4.set(AWComponents.COIN_METAL, CoinMetal.ANCIENT);
			group.addItem(stack4);

			for (Identifier factionId : AWResources.factionIds()) {
				ItemStack banner = new ItemStack(FACTION_BANNER, 1);
				Faction faction = AWResources.faction(factionId);
				banner.set(AWComponents.FACTION_ITEM, ResourceKey.create(Factions.FACTION_REGISTRY_KEY, factionId));
				banner.set(DataComponents.ITEM_NAME, Component.translatable("item.ancientwarfare.faction_banner", faction.getDescription()));
				group.addItem(banner);
			}
		});

		CreativeTabManager.addItemsToModTab(group -> {
			var factions = AWResources.factionIds();

			for (Identifier faction : factions) {
				var npcReg = AWResources.npcsInFaction(faction);
				for (FactionNpcData npc : npcReg) {
					group.addItem(FactionNpc.itemFromNpcData(group.context().holders(), npc));
				}
			}
		}, CreativeTabManager.FACTION_NPCS_KEY);
	}

	public static Item registerItem(String name, Item.Properties settings) {
		return registerItem(name, Item::new, settings, CreativeTabManager.ITEM_GROUP_KEY);
	}

	public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory, Item.Properties settings) {
		return registerItem(name, factory, settings, CreativeTabManager.ITEM_GROUP_KEY);
	}

	public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory, Item.Properties settings, ResourceKey<CreativeModeTab> tab) {
		final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, AncientWarfare.id(name));
		T item = factory.apply(settings.setId(registryKey));
		Registry.register(BuiltInRegistries.ITEM, registryKey, item);
		CreativeTabManager.addItemsToModTab((group) -> group.addItem(item));
		return item;
	}

	public static Item registerItemNoCreativeTab(String name, Item.Properties settings) {
		return registerItemNoCreativeTab(name, Item::new, settings);
	}

	public static <T extends Item> T registerItemNoCreativeTab(String name, Function<Item.Properties, T> factory, Item.Properties settings) {
		final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, AncientWarfare.id(name));
		T item = factory.apply(settings.setId(registryKey));
		Registry.register(BuiltInRegistries.ITEM, registryKey, item);
		return item;
	}

}
