package io.github.steaf23.ancientwarfare.core.versioned;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
//? if <= 1.21.11 {
/*import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
*///?} else {
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
//?}
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CreativeTabManager {

	public static final ResourceKey<CreativeModeTab> ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), AncientWarfare.id("ancient_warfare"));
	public static final CreativeModeTab ITEM_GROUP = FabricCreativeModeTab.builder()
			.icon(AWBlocks.TOWN_HALL.asItem()::getDefaultInstance)
			.title(Component.translatable("itemGroup.ancient_warfare"))
			.build();

	public static class Tab {
		CreativeModeTab.ItemDisplayParameters context;
		List<ItemStack> stacks = new ArrayList<>();

		public void addItem(Item item) {
			addItem(item.getDefaultInstance());
		}

		public void addItem(ItemStack stack) {
			stacks.add(stack);
		}

		public CreativeModeTab.ItemDisplayParameters context() {
			return context;
		}
	}

	//~if <=1.21.11 'CreativeModeTabEvents.ModifyOutput' -> 'ItemGroupEvents.ModifyEntries'
	public static void addItemsToModTab(Consumer<Tab> itemAdder) {
		// Defer adding items to the event using the consumer, to allow the caller to create ItemStacks for example.

		//? if <= 1.21.11 {
		/*ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(group -> {
		*///?} else {
		CreativeModeTabEvents.modifyOutputEvent(ITEM_GROUP_KEY).register(group -> {
		//?}
			Tab tab = new Tab();
			tab.context = group.getContext();
			itemAdder.accept(tab);
			group.acceptAll(tab.stacks);
		});
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP_KEY, ITEM_GROUP);
	}
}
