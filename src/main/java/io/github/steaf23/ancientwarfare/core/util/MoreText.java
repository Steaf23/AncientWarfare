package io.github.steaf23.ancientwarfare.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class MoreText {

	public static Component blockPos(BlockPos pos, boolean onlyXyz) {
		return Component.literal("X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ());
	}

	public static Component blockPos(BlockPos pos) {
		return blockPos(pos, true);
	}
}
