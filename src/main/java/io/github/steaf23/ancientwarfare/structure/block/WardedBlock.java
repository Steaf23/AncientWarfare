package io.github.steaf23.ancientwarfare.structure.block;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock.WardedBlockEntity;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class WardedBlock extends BaseEntityBlock {

	public WardedBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(AdvancedSpawnerBlock::new);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
		return new WardedBlockEntity(worldPosition, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		WardedBlockEntity be = getBlockEntity(level, pos);
		if (be == null) {
			return super.getShape(state, level, pos, context);
		}

		CapturedBlock captureInfo = be.getBlockToRestore();
		if (captureInfo == null || captureInfo.state() == null || captureInfo.state().isAir()) {
			return super.getShape(state, level, pos, context);
		}
		return captureInfo.state().getShape(level, pos, context);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (player.isCreative()) {
			// TODO: open menu to edit
			return InteractionResult.SUCCESS;
		}

		if (!world.isClientSide()) {
			activate(state, (ServerLevel)world, pos, player, hit);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide()) {

			BlockEntity be = level.getBlockEntity(pos);

			if (be instanceof WardedBlockEntity warded) {

				warded.restore();

				return warded.getBlockToRestore().state();
			}
		}

		return super.playerWillDestroy(level, pos, state, player);
	}


	public void activate(BlockState state, ServerLevel world, BlockPos pos, Player player, BlockHitResult hit) {
		// TODO: spawn stuff
		if (world.getBlockEntity(pos) instanceof WardedBlockEntity wardedBe) {
			wardedBe.activate();
		}
	}

	public static void place(ServerLevel level, BlockPos pos) {
		BlockState currentState = level.getBlockState(pos);
		if (currentState.getBlock() == AWBlocks.WARDED_BLOCK) {
			return; // cannot place ward if one is already placed.
		}

		CompoundTag tag = new CompoundTag();
		BlockEntity be = level.getBlockEntity(pos);
		if (be != null) {
			tag = be.saveWithFullMetadata(level.registryAccess());
		}
		level.removeBlockEntity(pos);

		level.setBlock(pos, AWBlocks.WARDED_BLOCK.defaultBlockState(), WardedBlock.UPDATE_ALL);

		WardedBlockEntity wardedBe = (WardedBlockEntity) level.getBlockEntity(pos);
		if (wardedBe != null) {
			wardedBe.setBlockToRestore(currentState, tag);
		}
	}

	public WardedBlockEntity getBlockEntity(BlockGetter level, BlockPos pos) {
		return (WardedBlockEntity) level.getBlockEntity(pos);
	}
}
