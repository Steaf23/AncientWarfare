package io.github.steaf23.ancientwarfare.worksite.marker;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public record SurveyArea(List<BlockPos> stakes) {

	public static final Codec<SurveyArea> CODEC = BlockPos.CODEC.listOf().xmap(SurveyArea::new, SurveyArea::stakes);
	public static final SurveyArea EMPTY = new SurveyArea();

	private SurveyArea() {
		this(List.of());
	}

	public boolean isValid() {
		// At least 4 stakes are needed to create an area.
		if (stakes.size() <= 3) {
			return false;
		}

		// Before doing anything fancy, make sure all stakes are at least lined up with each other.
		for (int i = 0; i < stakes.size(); i++) {
			BlockPos current = stakes.get(i);
			BlockPos next = i == stakes.size() - 1 ? stakes.getFirst() : stakes.get(i + 1);

			if (!SurveyArea.canStakesConnect(current, next)) {
				return false;
			}
		}

		return true;
	}

	public static boolean canStakesConnect(BlockPos stake1, BlockPos stake2) {
		if (stake1.getY() != stake2.getY()) {
			return false;
		}

		int sameAxes = 0;
		if (stake1.getX() == stake2.getX()) {
			sameAxes++;
		}
		if (stake1.getZ() == stake2.getZ()) {
			sameAxes++;
		}

		// Either the X or Z needs to be the same to be in line with the next stake.
		return sameAxes == 1;
	}

	boolean containsPosition(BlockPos position) {
		// Use simple odd/even algo to test whether at least the X and Z are within the staked out area.

		int x = position.getX();
		int z = position.getZ();

		boolean inside = false;
		for (var pair : adjacentPairs()) {
			BlockPos first = pair.getFirst();
			BlockPos second = pair.getSecond();
			if (first.getX() == second.getX()) {
				continue;
			}

			if (x <= Math.min(first.getX(), second.getX()) ||
					x >= (Math.max(first.getX(), second.getX()))) {
				continue;
			}

			if (z < first.getZ()) {
				inside = !inside;
			}
		}

		return inside;
	}

	/**
	 * @return pairs of adjacent stakes that share the same X or Z coordinate.
	 */
	public Iterable<Pair<BlockPos, BlockPos>> adjacentPairs() {
		return () -> new Iterator<>() {
			private int current = 0;

			@Override
			public boolean hasNext() {
				return current < stakes.size();
			}

			@Override
			public Pair<BlockPos, BlockPos> next() {
				BlockPos pos1 = stakes.get(current);
				BlockPos pos2 = stakes.get((current + 1) % stakes.size());
				current++;
				return Pair.of(pos1, pos2);
			}
		};
	}

	Iterable<ChunkPos> chunks() {
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxZ = Integer.MIN_VALUE;

		for (BlockPos stake : stakes) {
			minX = Math.min(minX, stake.getX());
			maxX = Math.max(maxX, stake.getX());
			minZ = Math.min(minZ, stake.getZ());
			maxZ = Math.max(maxZ, stake.getZ());
		}

		int minChunkX = SectionPos.blockToSectionCoord(minX);
		int maxChunkX = SectionPos.blockToSectionCoord(maxX);
		int minChunkZ = SectionPos.blockToSectionCoord(minZ);
		int maxChunkZ = SectionPos.blockToSectionCoord(maxZ);

		return () -> new Iterator<>() {

			private int chunkX;
			private int chunkZ;
			private ChunkPos next = findNext();

			private ChunkPos findNext() {
				while (chunkX <= maxChunkX) {
					while (chunkZ <= maxChunkZ) {
						ChunkPos pos = new ChunkPos(chunkX, chunkZ++);

						//TODO: maybe reimplement if the current check is too slow?
//
//						int minX = pos.getMinBlockX();
//						int maxX = pos.getMaxBlockX() + 15;
//						int minZ = pos.getMinBlockZ();
//						int maxZ = pos.getMaxBlockZ() + 15;
//						if (containsRectangle(minX, minZ, maxX, maxZ)) {
//							return new AreaChunk(pos, true);
//						} else if (intersectsRectangle(minX, minZ, maxX, maxZ)) {
//							return new AreaChunk(pos, false);
//						}
						return pos;
					}

					chunkZ = minChunkZ;
					chunkX++;
				}

				return null;
			}

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public ChunkPos next() {
				if (next == null) {
					throw new NoSuchElementException();
				}

				ChunkPos result = next;
				next = findNext();
				return result;
			}
		};
	}

	public record CellRange(int z, int minX, int maxX) {}

	Iterable<BlockPos> cells(int atY) {
		// This function creates an iterator to iterate over a range cells.
		// This approach uses scan lines to iterate over every cell between the intersecting lines, by maintaining a stack of the ranges.
		return () -> new Iterator<>() {
			private final int minZ = stakes.stream()
					.mapToInt(BlockPos::getZ)
					.min()
					.orElseThrow();

			private final int maxZ = stakes.stream()
					.mapToInt(BlockPos::getZ)
					.max()
					.orElseThrow();

			private int z = minZ;
			private int x = 0;
			private int endX = 0;
			private BlockPos next;

			private final List<Integer> intersections = new ArrayList<>();
			private int intersection = 0;

			{
				advance();
			}

			private void advance() {
				next = null;

				while (true) {
					if (x < endX) {
						next = new BlockPos(x++, atY, z);
						return;
					}

					if (intersection < intersections.size()) {
						int left = intersections.get(intersection++);
						int right = intersections.get(intersection++);

						// Cells need to be inside the area, so we skip the line's cell itself.
						x = left + 1;
						endX = right;

						continue;
					}

					if (++z > maxZ) {
						return;
					}

					intersections.clear();
					for (var pair : adjacentPairs()) {
						BlockPos a = pair.getFirst();
						BlockPos b = pair.getSecond();

						// get all pairs that would be intersecting with the horizontal scan lines. These pairs share the same X coordinate.
						if (a.getX() != b.getX()) {
							continue;
						}

						// We only care about the pair if the current z is somewhere in between (but not on the corner cell of) the line.
						int minEdgeZ = Math.min(a.getZ(), b.getZ());
						int maxEdgeZ = Math.max(a.getZ(), b.getZ());

						// this means that at the current z, the new range starts at intersecting line's X coordinate (on the inside).
						if (z >= minEdgeZ && z < maxEdgeZ) {
							intersections.add(a.getX());
						}
					}

					intersections.sort(Integer::compare);
					intersection = 0;
				}
			}

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public BlockPos next() {
				if (next == null) {
					throw new NoSuchElementException();
				}

				BlockPos pos = next;
				advance();
				System.out.println(pos);
				return pos;
			}
		};
	}
}
