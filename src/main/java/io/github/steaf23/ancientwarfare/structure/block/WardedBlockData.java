package io.github.steaf23.ancientwarfare.structure.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record WardedBlockData(@Nullable EntityType<?> entityToSpawn, @Nullable MobEffectInstance effect) {
	public static final Codec<WardedBlockData> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityType.CODEC.optionalFieldOf("entity_to_spawn").forGetter(WardedBlockData::maybeEntity),
			MobEffectInstance.CODEC.optionalFieldOf("effect").forGetter(WardedBlockData::maybeEffect)
	).apply(i, (entity, effect) -> new WardedBlockData(entity.orElse(null), effect.orElse(null))));

	Optional<EntityType<?>> maybeEntity() {
		return Optional.ofNullable(entityToSpawn);
	}

	Optional<MobEffectInstance> maybeEffect() {
		return Optional.ofNullable(effect);
	}
}
