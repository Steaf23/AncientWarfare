package io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record WardInfo(@Nullable EntityType<?> entityToSpawn, @Nullable MobEffectInstance effect) {
	public static final Codec<WardInfo> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityType.CODEC.optionalFieldOf("entity_to_spawn").forGetter(WardInfo::maybeEntity),
			MobEffectInstance.CODEC.optionalFieldOf("effect").forGetter(WardInfo::maybeEffect)
	).apply(i, (entity, effect) -> new WardInfo(entity.orElse(null), effect.orElse(null))));

	private Optional<EntityType<?>> maybeEntity() {
		return Optional.ofNullable(entityToSpawn);
	}

	private Optional<MobEffectInstance> maybeEffect() {
		return Optional.ofNullable(effect);
	}
}
