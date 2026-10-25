package io.github.steaf23.ancientwarfare.core.registry.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.schedule.Activity;

public class AWActivities {

	public static final Activity FOLLOW_COMMAND = register("follow_command");
	public static final Activity UPKEEP = register("upkeep");

	public static Activity register(String id) {
		return Registry.register(BuiltInRegistries.ACTIVITY, id, new Activity(id));
	}

	public static void initialize() {

	}

}
