package io.github.steaf23.ancientwarfare.npc.entity.playerowned.upkeep;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface UpkeepValueProvider {
	int getValue(ItemStack stack);

	class FoodProvider implements UpkeepValueProvider {

		@Override
		public int getValue(ItemStack stack) {
			FoodProperties food = stack.get(DataComponents.FOOD);
			return food == null ? 0 : food.nutrition() * 350;
		}
	}
}
