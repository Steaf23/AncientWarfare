package io.github.steaf23.ancientwarfare.structure.level.structure;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.registry.AWStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class AWDebugStructure extends Structure {

	public static final MapCodec<AWDebugStructure> CODEC = simpleCodec(AWDebugStructure::new);

	public AWDebugStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		return Optional.empty();
	}

	@Override
	public StructureType<?> type() {
		return AWStructures.DEBUG_TYPE;
	}
}
