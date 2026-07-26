package io.github.steaf23.ancientwarfare.worksite.marker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public record SearchContext(BlockPos workSitePos, int searchRadius, ServerLevel level) {

}
