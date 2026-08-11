package io.github.steaf23.ancientwarfare.worksite.requirement;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record WorksiteRequirementType<T extends WorksiteRequirement>(MapCodec<T> codec) {

	public static final Registry<WorksiteRequirementType> REGISTRY = FabricRegistryBuilder.create(
			ResourceKey.<WorksiteRequirementType>createRegistryKey(AncientWarfare.id("worksite_requirement")))
			.attribute(RegistryAttribute.SYNCED)
			.buildAndRegister();
}
