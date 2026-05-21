package io.github.steaf23.ancientwarfare.structure.item;

import io.github.steaf23.ancientwarfare.structure.block.WardedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class WardSealItem extends Item {

	public WardSealItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!context.getLevel().isClientSide()) {

			BlockPos pos = context.getClickedPos();
			WardedBlock.place(((ServerLevel)context.getLevel()), pos);
			context.getItemInHand().consume(1, context.getPlayer());
			return InteractionResult.CONSUME;
		}

		return InteractionResult.SUCCESS;
	}
}
