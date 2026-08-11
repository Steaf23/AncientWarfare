package io.github.steaf23.ancientwarfare.core.registry;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.worksite.requirement.StorageRequirement;
import io.github.steaf23.ancientwarfare.worksite.requirement.WorksiteRequirement;
import io.github.steaf23.ancientwarfare.worksite.requirement.WorksiteRequirementType;
import net.minecraft.core.Registry;

public class WorksiteRequirements {

	public static final WorksiteRequirementType<StorageRequirement> STORAGE = register("storage", StorageRequirement.CODEC);

	public static <T extends WorksiteRequirement> WorksiteRequirementType<T> register(String name, MapCodec<T> codec) {
		return Registry.register(WorksiteRequirementType.REGISTRY, AncientWarfare.id(name), new WorksiteRequirementType<>(codec));
	}

	public static void initialize() {

	}
}
