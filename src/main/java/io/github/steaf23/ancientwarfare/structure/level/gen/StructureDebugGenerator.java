package io.github.steaf23.ancientwarfare.structure.level.gen;

import com.mojang.serialization.MapCodec;
//~ if <=1.21.11 'BuiltInResourceKeys' -> 'BuiltInRegistryKeys' {
import com.mojang.serialization.codecs.RecordCodecBuilder;
//~}

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWStructures;
import io.github.steaf23.ancientwarfare.structure.level.structure.AWGridStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class StructureDebugGenerator extends ChunkGenerator {

	public static final MapCodec<StructureDebugGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			RegistryOps.retrieveElement(Biomes.PLAINS)).apply(i, i.stable(StructureDebugGenerator::new)));

	private static final Identifier test = AncientWarfare.id("portal_test_5");

	public StructureDebugGenerator(Holder.Reference<Biome> plains) {
		//~ if <= 1.21.11 'BuiltInResourceKeys.biomeHolderGetter()' -> 'BuiltInRegistryKeys.biomeRegistryWrapper()'
		super(new FixedBiomeSource(plains));
	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
		super.applyBiomeDecoration(level, chunk, structureManager);

		ChunkPos centerPos = chunk.getPos();
		int chunkX = centerPos.x();
		int chunkZ = centerPos.z();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int worldX = SectionPos.sectionToBlockCoord(chunkX, x);
				int worldZ = SectionPos.sectionToBlockCoord(chunkZ, z);
				level.setBlock(new BlockPos(worldX, 0, worldZ), Blocks.IRON_BLOCK.defaultBlockState(), 0);
			}
		}
	}

	@Override
	public void createStructures(RegistryAccess registryAccess, ChunkGeneratorStructureState state, StructureManager structureManager, ChunkAccess centerChunk, StructureTemplateManager structureTemplateManager, ResourceKey<Level> level) {
//		super.createStructures(registryAccess, state, structureManager, centerChunk, structureTemplateManager, level);
		SectionPos pos = SectionPos.bottomOf(centerChunk);

		StructureGrid grid = new StructureGrid();
		grid.setup(structureTemplateManager, i -> i.getNamespace().equals(AncientWarfare.MOD_ID));
		for (BlockPos startPos : grid.getStructuresAtSection(pos).keySet()) {
			Identifier templateId = grid.templates.get(startPos);
			Optional<Structure> opt = registryAccess.lookupOrThrow(Registries.STRUCTURE).getOptional(AWStructures.DEBUG);
			if (opt.isEmpty()) {
				System.out.println("Error generating structure, debug structure not registered for some reason...");
				return;
			}

			PiecesContainer pieces = new PiecesContainer(List.of(
					new AWGridStructurePiece(0, structureTemplateManager, templateId, templateId.getPath(), new StructurePlaceSettings(), startPos)));

			StructureStart start = new StructureStart(opt.get(), centerChunk.getPos(), 0, pieces);

			structureManager.setStartForStructure(
					pos,
					opt.get(),
					start,
					centerChunk);
		}
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void buildSurface(final WorldGenRegion level, final StructureManager structureManager, final RandomState randomState, final ChunkAccess protoChunk) {
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(
			final Blender blender, final RandomState randomState, final StructureManager structureManager, final ChunkAccess centerChunk
	) {
		return CompletableFuture.completedFuture(centerChunk);
	}

	@Override
	public int getBaseHeight(final int x, final int z, final Heightmap.Types type, final LevelHeightAccessor heightAccessor, final RandomState randomState) {
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(final int x, final int z, final LevelHeightAccessor heightAccessor, final RandomState randomState) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(final List<String> result, final RandomState randomState, final BlockPos feetPos) {
	}

	@Override
	public void applyCarvers(
			final WorldGenRegion region,
			final long seed,
			final RandomState randomState,
			final BiomeManager biomeManager,
			final StructureManager structureManager,
			final ChunkAccess chunk
	) {
	}

	@Override
	public void spawnOriginalMobs(final WorldGenRegion worldGenRegion) {
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getGenDepth() {
		return 384;
	}

	@Override
	public int getSeaLevel() {
		return 63;
	}
}
