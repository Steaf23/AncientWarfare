package io.github.steaf23.ancientwarfare.structure.block;

import io.github.steaf23.ancientwarfare.structure.block.entity.factionbanner.FactionBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FactionBannerBlock extends BannerBlock {

	public FactionBannerBlock(Properties properties) {
		super(DyeColor.WHITE, properties);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
		return new FactionBannerBlockEntity(worldPosition, blockState);
	}
}
