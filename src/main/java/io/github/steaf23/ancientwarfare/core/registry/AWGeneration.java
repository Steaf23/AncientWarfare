package io.github.steaf23.ancientwarfare.core.registry;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.structure.level.gen.StructureDebugGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class AWGeneration {
	public static final ResourceKey<MapCodec<? extends ChunkGenerator>> STRUCTURE_DEBUG_GENERATOR_KEY =
			ResourceKey.create(Registries.CHUNK_GENERATOR, AncientWarfare.id("structure_debug_generator"));

	public static void initialize() {
		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, STRUCTURE_DEBUG_GENERATOR_KEY, StructureDebugGenerator.CODEC);
	}
}
