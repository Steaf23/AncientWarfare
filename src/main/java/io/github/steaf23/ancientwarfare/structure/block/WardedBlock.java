package io.github.steaf23.ancientwarfare.structure.block;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock.WardedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
}
