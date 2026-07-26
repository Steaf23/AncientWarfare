package io.github.steaf23.ancientwarfare.worksite.requirement;

import io.github.steaf23.ancientwarfare.worksite.marker.SearchContext;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.jspecify.annotations.NonNull;

public record StorageRequirement(int requiredAmount) implements WorksiteRequirement {

	@Override
	public boolean isCompleted(SearchContext context) {
		int count = WorksiteRequirement.countBlockEntities(context, BaseContainerBlockEntity.class, be -> true);
		return count >= requiredAmount;
	}

	@Override
	public @NonNull String toString() {
		return "Storage for this worksite requires " + requiredAmount + " to be present.";
	}
}
