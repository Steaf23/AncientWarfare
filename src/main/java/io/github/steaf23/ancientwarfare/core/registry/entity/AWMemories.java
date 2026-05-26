package io.github.steaf23.ancientwarfare.core.registry.entity;

import com.mojang.serialization.Codec;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class AWMemories {

	public static final MemoryModuleType<Unit> UPKEEP = register("upkeep", Unit.CODEC);

	public static <U> MemoryModuleType<U> register(String string, Codec<U> codec) {
		return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, AncientWarfare.id(string), new MemoryModuleType<U>(Optional.of(codec)));
	}

	public static void initialize() {

	}
}
