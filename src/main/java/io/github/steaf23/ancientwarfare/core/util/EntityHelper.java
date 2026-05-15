package io.github.steaf23.ancientwarfare.core.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EntityHelper {

	public static ItemStack getItemFromEitherHand(Player player, Item... itemTypes) {
		for (Item item : itemTypes) {
			if (item.equals(player.getMainHandItem().getItem())) {
				return player.getMainHandItem();
			} else if (item.equals(player.getOffhandItem().getItem())) {
				return player.getOffhandItem();
			}
		}
		return ItemStack.EMPTY;
	}
}
