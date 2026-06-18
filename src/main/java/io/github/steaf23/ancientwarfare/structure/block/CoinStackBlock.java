package io.github.steaf23.ancientwarfare.structure.block;

import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CoinStackBlock extends Block {

	public enum StackSize implements StringRepresentable {
		SIZE_8(0, 8),
		SIZE_16(1, 16),
		SIZE_24(2, 24),
		SIZE_32(3, 32),
		SIZE_40(4, 40),
		SIZE_48(5, 48),
		SIZE_56(6, 56),
		SIZE_64(7, 64),
		SIZE_72(8, 72),
		SIZE_80(9, 80),
		SIZE_88(10, 88),
		SIZE_96(11, 96),
		SIZE_104(12, 104),
		SIZE_112(13, 112),
		SIZE_120(14, 120),
		SIZE_128(15, 128),
		;

		private final int amount;
		private final int id;

		StackSize(int id, int amount) {
			this.amount = amount;
			this.id = id;
		}

		@Override
		public String getSerializedName() {
			return "" + amount;
		}

		public boolean canAdd() {
			return this != SIZE_128;
		}

		public StackSize add() {
			return switch (this) {
				case SIZE_8 -> SIZE_16;
				case SIZE_16 -> SIZE_24;
				case SIZE_24 -> SIZE_32;
				case SIZE_32 -> SIZE_40;
				case SIZE_40 -> SIZE_48;
				case SIZE_48 -> SIZE_56;
				case SIZE_56 -> SIZE_64;
				case SIZE_64 -> SIZE_72;
				case SIZE_72 -> SIZE_80;
				case SIZE_80 -> SIZE_88;
				case SIZE_88 -> SIZE_96;
				case SIZE_96 -> SIZE_104;
				case SIZE_104 -> SIZE_112;
				case SIZE_112 -> SIZE_120;
				case SIZE_120 -> SIZE_128;
				case SIZE_128 -> SIZE_128;
			};
		}

		public boolean canTake() {
			return this != SIZE_8;
		}

		public StackSize take(int amount) {
			if (amount <= 0) {
				return this;
			}
			StackSize newSize = take();
			return newSize.take(amount - 1);
		}

		public StackSize take() {
			return switch (this) {
				case SIZE_8 -> SIZE_8;
				case SIZE_16 -> SIZE_8;
				case SIZE_24 -> SIZE_16;
				case SIZE_32 -> SIZE_24;
				case SIZE_40 -> SIZE_32;
				case SIZE_48 -> SIZE_40;
				case SIZE_56 -> SIZE_48;
				case SIZE_64 -> SIZE_56;
				case SIZE_72 -> SIZE_64;
				case SIZE_80 -> SIZE_72;
				case SIZE_88 -> SIZE_80;
				case SIZE_96 -> SIZE_88;
				case SIZE_104 -> SIZE_96;
				case SIZE_112 -> SIZE_104;
				case SIZE_120 -> SIZE_112;
				case SIZE_128 -> SIZE_120;
			};
		}

		public int id() {
			return id;
		}

		public int amount() {
			return amount;
		}
	}

	private static final VoxelShape[] SHAPES = Block.boxes(16, height -> Block.column(16.0, 0.0, height));

	public static final EnumProperty<StackSize> STACK_SIZE = EnumProperty.create("stack_size", StackSize.class);
	public static final EnumProperty<CoinMetal> METAL = EnumProperty.create("metal", CoinMetal.class);

	public CoinStackBlock(Properties properties) {
		super(properties);

		registerDefaultState(defaultBlockState()
				.setValue(STACK_SIZE, StackSize.SIZE_8)
				.setValue(METAL, CoinMetal.GOLD));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STACK_SIZE).add(METAL);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(STACK_SIZE).id + 1];
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		CoinMetal metalInHand = context.getItemInHand().get(AWComponents.COIN_METAL);
		if (metalInHand == null) {
			return defaultBlockState();
		}

		if (state.is(this) && state.getValue(METAL) == metalInHand) {
			StackSize size = state.getValue(STACK_SIZE);
			return state.setValue(STACK_SIZE, size.add());
		}
		else {
			return defaultBlockState().setValue(METAL, metalInHand);
		}
	}

	@Override
	protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!itemStack.is(AWItems.COINS)) {
			StackSize currentSize = state.getValue(STACK_SIZE);
			if (level instanceof ServerLevel && !player.isCreative()) {
				player.addItem(stackWithCount(state, 8));
			}

			if (currentSize.canTake()) {
				level.setBlockAndUpdate(pos, state.setValue(STACK_SIZE, currentSize.take()));
				return InteractionResult.SUCCESS;
			}
			else {
				level.removeBlock(pos, false);
				return InteractionResult.SUCCESS;
			}
		}

		return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
	}

	@Override
	protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		StackSize amount = state.getValue(STACK_SIZE);
		if (amount.canAdd() && context.getItemInHand().is(AWItems.COINS)) {
			CoinMetal metalInHand = context.getItemInHand().get(AWComponents.COIN_METAL);
			if (metalInHand != state.getValue(METAL)) {
				return false;
			}

			int coinsInHand = context.getItemInHand().count();
			if (coinsInHand > 8 && context.replacingClickedOnBlock()) {
				return context.getClickedFace() == Direction.UP;
			}
			return true;
		}
		return false;
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		ItemStack coins = super.getCloneItemStack(level, pos, state, includeData);
		if (coins.has(AWComponents.COIN_METAL)) {
			coins.set(AWComponents.COIN_METAL, state.getValue(METAL));
		}

		return coins;
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		int amount = state.getValue(STACK_SIZE).amount;

		if (amount > 64) {
			return List.of(stackWithCount(state, 64), stackWithCount(state, amount % 64));
		}

		return List.of(stackWithCount(state, amount));
	}

	public ItemStack stackWithCount(BlockState state, int count) {
		ItemStack stack = AWItems.COINS.getDefaultInstance();
		stack.setCount(count);
		stack.set(AWComponents.COIN_METAL, state.getValue(METAL));
		return stack;
	}
}
