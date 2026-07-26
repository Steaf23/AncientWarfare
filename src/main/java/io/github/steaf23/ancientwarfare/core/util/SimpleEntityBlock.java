package io.github.steaf23.ancientwarfare.core.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class SimpleEntityBlock extends BaseEntityBlock {

	private final BlockEntityFactory factory;

	public SimpleEntityBlock(Properties properties, BlockEntityFactory factory) {
		super(properties);
		this.factory = factory;
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(p -> new SimpleEntityBlock(p, this.factory));
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
		return factory.create(worldPosition, blockState);
	}

	@FunctionalInterface
	public interface BlockEntityFactory {
		BlockEntity create(@NotNull BlockPos pos, @NotNull BlockState state);
	}
}
