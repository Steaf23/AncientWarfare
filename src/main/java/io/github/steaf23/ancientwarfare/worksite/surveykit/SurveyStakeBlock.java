package io.github.steaf23.ancientwarfare.worksite.surveykit;

import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SurveyStakeBlock extends Block {

	private static final VoxelShape SHAPE = Shapes.create(6.0 / 16.0, 0.0, 6.0 / 16.0, 10.0 / 16.0, 12.0 / 16.0, 10.0 / 16.0);

	public SurveyStakeBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public Item asItem() {
		return AWItems.SURVEY_KIT;
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(AWItems.SURVEY_KIT);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return Block.canSupportCenter(level, pos.below(), Direction.UP);
	}
}
