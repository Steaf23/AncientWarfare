package io.github.steaf23.ancientwarfare.structure.block;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.structure.block.entity.advancedspawner.AdvancedSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedSpawnerBlock extends BaseEntityBlock {

	public AdvancedSpawnerBlock(Properties settings) {
		super(settings);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, AWBlockEntities.ADVANCED_SPAWNER, world.isClientSide() ? AdvancedSpawnerBlockEntity::clientTick : AdvancedSpawnerBlockEntity::serverTick);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(AdvancedSpawnerBlock::new);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AdvancedSpawnerBlockEntity(pos, state);
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!world.isClientSide()) {
			MenuProvider factory = state.getMenuProvider(world, pos);
			if (factory != null) {
				player.openMenu(factory);
			}
		}

		return InteractionResult.SUCCESS;
	}
}
