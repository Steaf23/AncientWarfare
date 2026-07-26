package io.github.steaf23.ancientwarfare.worksite.marker;

import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.core.util.SimpleEntityBlock;
import io.github.steaf23.ancientwarfare.worksite.requirement.StorageRequirement;
import io.github.steaf23.ancientwarfare.worksite.requirement.WorksiteRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class WorksiteMarkerBlock extends SimpleEntityBlock {

	public WorksiteMarkerBlock(Properties properties) {
		super(properties, WorksiteMarkerBlockEntity::new);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		WorksiteMarkerBlockEntity be = level.getBlockEntity(pos, AWBlockEntities.WORKSITE_MARKER).orElseThrow();
		List<WorksiteRequirement>  incompleteRequirements = be.checkAndGetIncompleteRequirements();
		System.out.println("Incomplete worksite!");
		for (var requirement : incompleteRequirements) {
			System.out.println(requirement);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		WorksiteMarkerBlockEntity be = level.getBlockEntity(pos, AWBlockEntities.WORKSITE_MARKER).orElseThrow();

		if (itemStack.is(ItemTags.AXES)) {
			if (!level.isClientSide()) {
				be.setRequirements(List.of(new StorageRequirement(2)));
			}
		} else {
			return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
		}

		return InteractionResult.SUCCESS;
	}
}
