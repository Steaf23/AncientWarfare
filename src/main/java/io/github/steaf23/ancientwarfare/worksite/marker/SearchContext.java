package io.github.steaf23.ancientwarfare.worksite.marker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public record SearchContext(BlockPos workSitePos, SurveyArea area, ServerLevel level) {

	public boolean isPositionInsideArea(BlockPos position) {
		return area.containsPosition(position);
	}

	//TODO: Implement chunk based block entity searching instead of sweeping all blocks (very slow at worst case).

	public @Nullable BlockEntity findFirst(int y, Predicate<BlockEntity> predicate) {
		for (ChunkPos subArea : area.chunks()) {
			LevelChunk chunk = level.getChunk(subArea.x(), subArea.z());
			var all = chunk.getBlockEntities();
			for (BlockPos pos : all.keySet()) {
				if (pos.getY() != y || !area.containsPosition(pos)) {
					continue;
				}

				BlockEntity entity = all.get(pos);
				if (predicate.test(all.get(pos))) {
					return entity;
				}
			}
		}

		return null;
	}

	public List<BlockEntity> findUpToAmountOfBlockEntities(int y, int minAmount, Predicate<BlockEntity> predicate) {
		List<BlockEntity> result = new ArrayList<>();
		for (ChunkPos subArea : area.chunks()) {
			LevelChunk chunk = level.getChunk(subArea.x(), subArea.z());
			var all = chunk.getBlockEntities();
			for (BlockPos pos : all.keySet()) {
				if (pos.getY() != y ||!area.containsPosition(pos)) {
					continue;
				}

				BlockEntity entity = all.get(pos);
				if (predicate.test(all.get(pos))) {
					result.add(entity);
					if (result.size() >= minAmount) {
						return result;
					}
				}
			}
		}

		return result;
	}

	public List<BlockEntity> findAllBlockEntities(int y, Predicate<BlockEntity> predicate) {
		List<BlockEntity> result = new ArrayList<>();
		for (ChunkPos subArea : area.chunks()) {
			LevelChunk chunk = level.getChunk(subArea.x(), subArea.z());
			var all = chunk.getBlockEntities();
			for (BlockPos pos : all.keySet()) {
				if (pos.getY() != y ||!area.containsPosition(pos)) {
					continue;
				}

				BlockEntity entity = all.get(pos);
				if (predicate.test(all.get(pos))) {
					result.add(entity);
				}
			}
		}

		return result;
	}
}
