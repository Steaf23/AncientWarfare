package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.structure.level.structure.AWDebugStructure;
import io.github.steaf23.ancientwarfare.structure.level.structure.AWGridStructurePiece;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class AWStructures {

	public static final StructurePieceType.StructureTemplateType DEBUG_PIECE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, AncientWarfare.id("debug_piece"), AWGridStructurePiece::new);

	public static final ResourceKey<Structure> DEBUG = ResourceKey.create(Registries.STRUCTURE, AncientWarfare.id("debug"));
	public static final StructureType<AWDebugStructure> DEBUG_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, AncientWarfare.id("debug"), () -> AWDebugStructure.CODEC);

	public static void initialize() {}

	public static void bootstrap(BootstrapContext<Structure> context) {
		var biomes = context.lookup(Registries.BIOME);
		var plains = HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS));
		context.register(DEBUG, new AWDebugStructure(new Structure.StructureSettings(plains)));
	}

}
