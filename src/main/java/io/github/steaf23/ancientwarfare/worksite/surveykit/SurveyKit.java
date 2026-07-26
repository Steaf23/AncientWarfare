package io.github.steaf23.ancientwarfare.worksite.surveykit;

import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class SurveyKit extends Item {

	public SurveyKit(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		// Used Fire charge item as a reference.
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockToReplace = level.getBlockState(pos);
		if (!blockToReplace.canBeReplaced()) {
			pos = pos.relative(context.getClickedFace());
			blockToReplace = level.getBlockState(pos);
			if (!blockToReplace.canBeReplaced()) {
				return InteractionResult.FAIL;
			}
		}

		BlockState stateToPlace = AWBlocks.SURVEY_STAKE.defaultBlockState();

		if (!canPlace(level, stateToPlace, pos)) {
			return InteractionResult.FAIL;
		}

		playSound(level, pos);
		level.setBlockAndUpdate(pos, stateToPlace);
		level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, pos);
		context.getItemInHand().hurtAndBreak(1, context.getPlayer(), context.getHand());

		return InteractionResult.SUCCESS;
	}

	public void playSound(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();
		level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
	}

	public boolean canPlace(Level level, BlockState blockState, BlockPos pos) {
		return blockState.canSurvive(level, pos);
	}

}
