package io.github.steaf23.ancientwarfare.worksite.requirement;

import com.mojang.serialization.Codec;
import io.github.steaf23.ancientwarfare.worksite.marker.SearchContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;

public interface WorksiteRequirement {

	Codec<WorksiteRequirement> CODEC = WorksiteRequirementType.REGISTRY.byNameCodec()
			.dispatch("type", WorksiteRequirement::type, WorksiteRequirementType::codec);

	boolean isCompleted(SearchContext context);

	WorksiteRequirementType<?> type();

	static <T extends BlockEntity> int countBlockEntitiesAtWorksiteY(SearchContext context, Class<T> type, Predicate<T> predicate) {
		return context.findAllBlockEntities(context.workSitePos().getY(), be -> type.isInstance(be) && predicate.test(type.cast(be))).size();
	}

	/**
	 * Faster method compared to countBlockEntitiesAtWorksiteY because it stops iteration early if all entities are found.
	 */
	static <T extends BlockEntity> int countUpToAmountOfBEAtWorksite(int amount, SearchContext context, Class<T> type, Predicate<T> predicate) {
		return context.findUpToAmountOfBlockEntities(context.workSitePos().getY(), amount, be -> type.isInstance(be) && predicate.test(type.cast(be))).size();
	}
}
