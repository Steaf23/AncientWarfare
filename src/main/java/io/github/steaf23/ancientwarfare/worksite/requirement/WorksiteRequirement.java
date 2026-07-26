package io.github.steaf23.ancientwarfare.worksite.requirement;

import io.github.steaf23.ancientwarfare.worksite.marker.SearchContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.function.Predicate;

public interface WorksiteRequirement {

	boolean isCompleted(SearchContext context);

	static <T extends BlockEntity> int countBlockEntities(SearchContext context, Class<T> type, Predicate<T> predicate) {
		return WorksiteRequirement.countBlockEntities(context, be -> type.isInstance(be) && predicate.test(type.cast(be)));
	}

	static int countBlockEntities(SearchContext context, Predicate<BlockEntity> predicate) {
		BlockPos center = context.workSitePos();
		int radius = context.searchRadius();
		ServerLevel level = context.level();

		int minX = center.getX() - radius;
		int minY = center.getY() - radius;
		int minZ = center.getZ() - radius;

		int maxX = center.getX() + radius;
		int maxY = center.getY() + radius;
		int maxZ = center.getZ() + radius;

		int minChunkX = minX >> 4;
		int maxChunkX = maxX >> 4;
		int minChunkZ = minZ >> 4;
		int maxChunkZ = maxZ >> 4;

		int count = 0;

		for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
			for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
				LevelChunk chunk = level.getChunk(chunkX, chunkZ);

				for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
					BlockPos pos = blockEntity.getBlockPos();

					if (pos.getX() < minX || pos.getX() > maxX
							|| pos.getY() < minY || pos.getY() > maxY
							|| pos.getZ() < minZ || pos.getZ() > maxZ) {
						continue;
					}

					if (predicate.test(blockEntity)) {
						count++;
					}
				}
			}
		}

		return count;
	}
}
