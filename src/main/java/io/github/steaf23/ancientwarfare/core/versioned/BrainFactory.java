package io.github.steaf23.ancientwarfare.core.versioned;

//~ if <= 1.21.11 'Brain.ActivitySupplier<E>' -> 'Function<E, Collection<ActivityData<E>>>' {
import com.mojang.serialization.Dynamic;
//? if <= 1.21.11 {
/*import io.github.steaf23.ancientwarfare.core.versioned.backport.ActivityData;
*///?}
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public final class BrainFactory<E extends LivingEntity> {

	private final Collection<? extends MemoryModuleType<?>> memoryModules;
	private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensors;
	private final Brain.ActivitySupplier<E> activityCollector;

	private final Brain.Provider<E> provider;

	public BrainFactory(Collection<? extends MemoryModuleType<?>> memoryModules,
	                    Collection<? extends SensorType<? extends Sensor<? super E>>> sensors,
	                    Brain.ActivitySupplier<E> activityCollector) {
		this.memoryModules = memoryModules;
		this.sensors = sensors;
		this.activityCollector = activityCollector;

		//?if <= 1.21.11 {
		/*this.provider = Brain.provider(memoryModules, sensors);
		*///?} else {
		this.provider = Brain.provider(sensors, activityCollector);
		//?}
	}

	public Brain.Provider<E> provider() {
		return provider;
	}

	//?if <=1.21.11 {
	/*public Brain<E> create(E entity, Dynamic<?> packed) {
		Brain<E> brain = provider.makeBrain(packed);
		for (ActivityData<E> activity : activityCollector.apply(entity)) {
			brain.addActivityAndRemoveMemoriesWhenStopped(activity.activityType(), activity.behaviorPriorityPairs(), activity.conditions(), activity.memoriesToEraseWhenStopped());
		}

		brain.setCoreActivities(Set.of(Activity.CORE));
//		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();

		return brain;
	}

	*///?} else {
	public Brain<?> create(E entity, Brain.Packed packed) {
		return provider.makeBrain(entity, packed);
	}
	//?}
}
//~}