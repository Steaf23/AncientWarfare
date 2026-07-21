package io.github.steaf23.ancientwarfare.structure.block.invalidconversionblock;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class InvalidConversionBlock extends Block implements EntityBlock {

	public InvalidConversionBlock(Properties properties) {
		super(properties);

	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
		return new InvalidConversionBlockEntity(worldPosition, blockState);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level instanceof ServerLevel) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof InvalidConversionBlockEntity conversionBe) {
				player.sendSystemMessage(Component.literal("Reason for failure of block conversion: ").withStyle(ChatFormatting.RED));
				for (String line : conversionBe.getComment()) {
					player.sendSystemMessage(Component.literal(line));
				}
				player.sendSystemMessage(Component.empty());
			}
		}
		return InteractionResult.SUCCESS;
	}
}
