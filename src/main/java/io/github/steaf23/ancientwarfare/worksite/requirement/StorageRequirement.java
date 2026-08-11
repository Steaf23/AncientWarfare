package io.github.steaf23.ancientwarfare.worksite.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.core.registry.WorksiteRequirements;
import io.github.steaf23.ancientwarfare.worksite.marker.SearchContext;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.jspecify.annotations.NonNull;

public record StorageRequirement(int requiredAmount) implements WorksiteRequirement {

	public static final MapCodec<StorageRequirement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("required_amount").forGetter(StorageRequirement::requiredAmount)
	).apply(i, StorageRequirement::new));

	@Override
	public boolean isCompleted(SearchContext context) {
		int count = WorksiteRequirement.countUpToAmountOfBEAtWorksite(requiredAmount, context, BaseContainerBlockEntity.class, be -> true);
		return count >= requiredAmount;
	}

	@Override
	public WorksiteRequirementType<?> type() {
		return WorksiteRequirements.STORAGE;
	}

	@Override
	public @NonNull String toString() {
		return "Storage for this worksite requires " + requiredAmount + " to be present.";
	}
}
