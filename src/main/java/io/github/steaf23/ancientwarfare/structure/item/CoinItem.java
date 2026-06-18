package io.github.steaf23.ancientwarfare.structure.item;

import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class CoinItem extends BlockItem {

	public CoinItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public Component getName(ItemStack itemStack) {
		CoinMetal metal = itemStack.get(AWComponents.COIN_METAL);
		if (metal == null) {
			return super.getName(itemStack);
		}
		return Component.translatable(getDescriptionId(), Component.translatable("metal.ancientwarfare." + metal.getSerializedName()));
	}

	@Override
	public InteractionResult place(BlockPlaceContext placeContext) {
		if (!(placeContext.getPlayer() != null && placeContext.getPlayer().isCreative()) && placeContext.getItemInHand().count() < 8) {
			// Cannot place the coins when you don't have enough to make a coin stack
			return InteractionResult.FAIL;
		}

		InteractionResult placed = super.place(placeContext);

		if (placed.consumesAction() && placeContext.getPlayer() != null) {
			ItemStack stack = placeContext.getItemInHand();

			// Consume 7 coins to make up for the 8 placed coins in total.
			stack.setCount(Math.max(0, stack.getCount() - 7));
		}

		return placed;
	}
}
