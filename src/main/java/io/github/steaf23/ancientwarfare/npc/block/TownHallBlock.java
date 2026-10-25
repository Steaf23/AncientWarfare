package io.github.steaf23.ancientwarfare.npc.block;

import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.core.util.SimpleEntityBlock;
import io.github.steaf23.ancientwarfare.npc.block.entity.TownHallBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class TownHallBlock extends SimpleEntityBlock {

	public TownHallBlock(Properties settings) {
		super(settings, TownHallBlockEntity::new);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState blockState, BlockEntityType<T> type) {
		return createTickerHelper(type, AWBlockEntities.TOWN_HALL, level.isClientSide() ? TownHallBlockEntity::clientTick : TownHallBlockEntity::serverTick);
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(@NonNull BlockState blockState, Level level, @NonNull BlockPos blockPos, @NonNull Player player, @NonNull BlockHitResult blockHitResult) {
		if (!level.isClientSide()) {
			MenuProvider provider = blockState.getMenuProvider(level, blockPos);
			if (provider != null) {
				player.openMenu(provider);
			}
		}

		return InteractionResult.SUCCESS;
	}
}
