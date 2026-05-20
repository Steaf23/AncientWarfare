package io.github.steaf23.ancientwarfare.npc.item;

import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CoinItem extends Item {

	public CoinItem(Properties properties) {
		super(properties);
	}

	@Override
	public Component getName(ItemStack itemStack) {
		CoinMetal metal = itemStack.get(AWComponents.COIN_METAL);
		if (metal == null) {
			return super.getName(itemStack);
		}
		return Component.translatable(getDescriptionId(), Component.translatable("metal.ancientwarfare." + metal.name()));
	}
}
