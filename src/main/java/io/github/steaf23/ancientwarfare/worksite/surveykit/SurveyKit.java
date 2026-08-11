package io.github.steaf23.ancientwarfare.worksite.surveykit;

import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.worksite.marker.SurveyArea;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.List;

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

		ItemStack stack = context.getItemInHand();
		List<BlockPos> placedStakes = stack.getOrDefault(AWComponents.SURVEY_STAKES, SurveyArea.EMPTY).stakes();

		if (!blockToReplace.canBeReplaced()) {
			if (blockToReplace.is(AWBlocks.SURVEY_STAKE)) {
				if (!placedStakes.isEmpty() && placedStakes.getFirst().equals(pos) && tryCloseMarkedArea(level, placedStakes)) {
					// Always break the stack upon closing the perimeter
					stack.hurtAndBreak(stack.getMaxDamage() - stack.getDamageValue(), context.getPlayer(), context.getHand());
				}
				return InteractionResult.SUCCESS;
			}

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

		if (!placedStakes.isEmpty() && !SurveyArea.canStakesConnect(placedStakes.getLast(), pos)) {
			return InteractionResult.FAIL;
		}

		playSound(level, pos);
		level.setBlockAndUpdate(pos, stateToPlace);
		level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, pos);
		stack.hurtAndBreak(1, context.getPlayer(), context.getHand());

		// Add new stake and validate existing stakes.
		List<BlockPos> newPoses = new ArrayList<>(placedStakes);
		newPoses.add(pos);
		stack.set(AWComponents.SURVEY_STAKES, new SurveyArea(newPoses.stream()
				.filter(p -> level.getBlockState(p).is(AWBlocks.SURVEY_STAKE))
				.toList()));

		// If all stakes have been placed, try to close the marked area.
		if (stack.isBroken()) {
			tryCloseMarkedArea(level, placedStakes);
		}

		return InteractionResult.SUCCESS;
	}

	public void playSound(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();
		level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
	}

	public boolean canPlace(Level level, BlockState blockState, BlockPos pos) {
		return blockState.canSurvive(level, pos);
	}

	public boolean tryCloseMarkedArea(Level level, List<BlockPos> stakes) {
		return new SurveyArea(stakes).isValid();
	}

}
