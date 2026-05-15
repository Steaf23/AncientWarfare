package io.github.steaf23.ancientwarfare.npc.entity.playerowned;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWMemories;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.behavior.NpcTaskUpkeep;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.StrollToPoi;
import net.minecraft.world.entity.ai.behavior.ValidateNearbyPoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PlayerOwnedNpcAi {

	public static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
			MemoryModuleType.PATH,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.INTERACTION_TARGET,
			MemoryModuleType.DOORS_TO_CLOSE,
			MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
			MemoryModuleType.NEAREST_LIVING_ENTITIES,
			MemoryModuleType.HOME,
			MemoryModuleType.JOB_SITE,
			MemoryModuleType.HEARD_BELL_TIME,
			AWMemories.UPKEEP
	);

	public static final ImmutableList<SensorType<? extends Sensor<? super BaseNpc>>> SENSORS = ImmutableList.of(
	);

	public static List<ActivityData<BaseNpc>> getActivities() {
		ActivityData<BaseNpc> core = ActivityData.create(Activity.CORE, 0,
				ImmutableList.of(
						new MoveToTargetSink(),
						InteractWithDoor.create(),
						NpcTaskUpkeep.create(),
						SetEntityLookTarget.create(EntityType.PLAYER, 10),
						ValidateNearbyPoi.create(p -> p.is(PoiTypes.ARMORER), MemoryModuleType.JOB_SITE),
						AcquirePoi.create(p -> p.is(PoiTypes.ARMORER), MemoryModuleType.JOB_SITE, true, Optional.empty())));

		ActivityData<BaseNpc> work = ActivityData.create(Activity.WORK, 12,
				ImmutableList.of(
						ValidateNearbyPoi.create(p -> p.is(PoiTypes.ARMORER), MemoryModuleType.JOB_SITE),
						StrollToPoi.create(MemoryModuleType.JOB_SITE, 3.0f, 2, 30)),
				Set.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));

		ActivityData<BaseNpc> idle = ActivityData.create(Activity.IDLE, 20,
				ImmutableList.of(
						new RunOne<>(List.of(
								Pair.of(RandomStroll.stroll(1.5f, false), 0),
								Pair.of(new DoNothing(50, 100), 0)
						))
				));
		return List.of(core, work, idle);

//		brain.setCoreActivities(Set.of(Activity.CORE));
//		brain.setDefaultActivity(Activity.IDLE);
//		brain.useDefaultActivity();
//		return brain;
	}

	public static void followCommand(BaseNpc npc) {
		npc.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(((PlayerOwnedNpc)npc).getOwner(), 3.0F, 0));
		npc.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(((PlayerOwnedNpc) npc).getOwner(), true));
	}

	public static void stopFollowCommand(BaseNpc npc) {
		npc.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
	}

}
