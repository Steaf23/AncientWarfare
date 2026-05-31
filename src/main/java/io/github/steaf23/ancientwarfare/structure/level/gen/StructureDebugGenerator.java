package io.github.steaf23.ancientwarfare.structure.level.gen;

import com.mojang.serialization.MapCodec;
//~ if <=1.21.11 'BuiltInResourceKeys' -> 'BuiltInRegistryKeys'
import net.fabricmc.fabric.impl.biome.modification.BuiltInResourceKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StructureDebugGenerator extends ChunkGenerator {

	public static final MapCodec<StructureDebugGenerator> CODEC = MapCodec.unit(new StructureDebugGenerator());

	public StructureDebugGenerator() {
		//~ if <= 1.21.11 'BuiltInResourceKeys.biomeHolderGetter()' -> 'BuiltInRegistryKeys.biomeRegistryWrapper()'
		super(new FixedBiomeSource(BuiltInResourceKeys.biomeHolderGetter().getOrThrow(Biomes.PLAINS)));
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {

	}

	@Override
	public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {

	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {

	}

	@Override
	public int getGenDepth() {
		return 0;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess centerChunk) {
		centerChunk.fillBiomesFromNoise(
				(x, y, z, sampler) -> this.biomeSource.getNoiseBiome(x, y, z, randomState.sampler()), randomState.sampler()
		);
		return CompletableFuture.completedFuture(centerChunk);
	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {

	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) {
		return null;
	}

	@Override
	public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {

	}
}
