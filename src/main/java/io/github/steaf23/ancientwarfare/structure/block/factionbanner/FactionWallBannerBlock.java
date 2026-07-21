package io.github.steaf23.ancientwarfare.structure.block.factionbanner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FactionWallBannerBlock extends WallBannerBlock {

	public FactionWallBannerBlock(Properties properties) {
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

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof FactionBannerBlockEntity factionBanner) {
			return factionBanner.getItem();
		}

		return super.getCloneItemStack(level, pos, state, includeData);
	}
}
