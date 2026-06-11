package io.github.steaf23.ancientwarfare.structure.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CoinStackBlock extends Block {

	public enum StackSize implements StringRepresentable {
		SIZE_8(8),
		SIZE_16(16),
		SIZE_24(24),
		SIZE_32(32),
		SIZE_40(40),
		SIZE_48(48),
		SIZE_56(56),
		SIZE_64(64),
		;

		private final int amount;

		StackSize(int amount) {
			this.amount = amount;
		}

		@Override
		public String getSerializedName() {
			return "" + amount;
		}
	}

	public static final EnumProperty<StackSize> STACK_SIZE = EnumProperty.create("stack_size", StackSize.class);

	public CoinStackBlock(Properties properties) {
		super(properties);

		registerDefaultState(defaultBlockState().setValue(STACK_SIZE, StackSize.SIZE_8));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STACK_SIZE);
	}
}
