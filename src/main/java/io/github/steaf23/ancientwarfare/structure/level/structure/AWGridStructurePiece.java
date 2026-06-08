package io.github.steaf23.ancientwarfare.structure.level.structure;

import io.github.steaf23.ancientwarfare.core.registry.AWStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class AWGridStructurePiece extends TemplateStructurePiece {

	public AWGridStructurePiece(int genDepth, StructureTemplateManager structureTemplateManager, Identifier templateLocation, String templateName, StructurePlaceSettings placeSettings, BlockPos position) {
		super(AWStructures.DEBUG_PIECE, genDepth, structureTemplateManager, templateLocation, templateName, placeSettings, position);
	}

	public AWGridStructurePiece(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
		super(AWStructures.DEBUG_PIECE, tag, structureTemplateManager, location -> new StructurePlaceSettings());
	}

	@Override
	protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {

	}
}
