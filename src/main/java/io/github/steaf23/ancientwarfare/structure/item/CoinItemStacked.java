package io.github.steaf23.ancientwarfare.structure.item;

import io.github.steaf23.ancientwarfare.structure.block.CoinStackBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class CoinItemStacked extends BlockItem {

	public CoinItemStacked(Block block, Properties properties, CoinStackBlock.StackSize size) {
		super(block, properties);
	}
}
