package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

public class AWSounds {

	public static final SoundEvent COIN_STACK_INTERACT = registerSound("coin_stack_interact");
	public static final SoundEvent COIN_STACK_BREAK = registerSound("coin_stack_break");

	public static final SoundType COIN_STACK_BLOCK = new SoundType(1.0f, 1.0f,
			COIN_STACK_BREAK,
			COIN_STACK_INTERACT,
			COIN_STACK_INTERACT,
			COIN_STACK_INTERACT,
			COIN_STACK_BREAK);

	private static SoundEvent registerSound(String id) {
		Identifier identifier = AncientWarfare.id(id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}

	public static void initialize() {
	}
}
