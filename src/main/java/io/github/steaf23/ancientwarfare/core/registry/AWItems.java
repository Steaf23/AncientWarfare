package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.item.UnobtainableItem;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import io.github.steaf23.ancientwarfare.npc.item.CoinItem;
import io.github.steaf23.ancientwarfare.npc.item.CommandBaton;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

import java.util.List;
import java.util.function.Function;

public class AWItems {

	public static final ResourceKey<CreativeModeTab> ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), AncientWarfare.id("ancient_warfare"));
	public static final CreativeModeTab ITEM_GROUP = FabricCreativeModeTab.builder()
			.icon(Items.ENCHANTING_TABLE.asItem()::getDefaultInstance)
			.title(Component.translatable("itemGroup.ancient_warfare"))
			.build();

	public static final Item STEEL_INGOT = registerItem("steel_ingot", new Item.Properties());
	public static final Item COIN = registerItemNoCreativeTab("coin", CoinItem::new, new Item.Properties()
			.component(AWComponents.COIN_METAL, CoinMetal.GOLD));

	public static final CommandBaton WOODEN_COMMAND_BATON = AWItems.registerItem("wooden_command_baton", CommandBaton::new,
			new Item.Properties()
					.component(AWComponents.SELECTED_ENTITIES, List.of()));

	public static final SpawnEggItem NPC_SPAWNER = AWItems.registerItem("npc_spawner", SpawnEggItem::new,
			new Item.Properties()
					.spawnEgg(AWEntities.BASE_NPC));

	public static Item[] COMMAND_BATONS = {
			WOODEN_COMMAND_BATON,
	};

	public static final Item TOWN_HALL_KEY_DUMMY = AWItems.registerItem("town_hall_key", UnobtainableItem::new, new Item.Properties());


	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP_KEY, ITEM_GROUP);

		CreativeModeTabEvents.modifyOutputEvent(AWItems.ITEM_GROUP_KEY).register(group -> {
			ItemStack stack = new ItemStack(COIN);
			stack.set(AWComponents.COIN_METAL, CoinMetal.GOLD);
			group.accept(stack);
			stack.set(AWComponents.COIN_METAL, CoinMetal.SILVER);
			group.accept(stack);
			stack.set(AWComponents.COIN_METAL, CoinMetal.COPPER);
			group.accept(stack);
			stack.set(AWComponents.COIN_METAL, CoinMetal.ANCIENT);
			group.accept(stack);
		});
	}

	public static Item registerItem(String name, Item.Properties settings) {
		return registerItem(name, Item::new, settings, AWItems.ITEM_GROUP_KEY);
	}

	public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory, Item.Properties settings) {
		return registerItem(name, factory, settings, AWItems.ITEM_GROUP_KEY);
	}

	public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory, Item.Properties settings, ResourceKey<CreativeModeTab> tab) {
		final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, AncientWarfare.id(name));
		T item = factory.apply(settings.setId(registryKey));
		Registry.register(BuiltInRegistries.ITEM, registryKey, item);
		CreativeModeTabEvents.modifyOutputEvent(tab)
				.register((group) -> group.accept(item));
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
