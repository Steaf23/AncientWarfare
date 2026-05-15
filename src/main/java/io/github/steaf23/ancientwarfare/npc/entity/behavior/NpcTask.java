package io.github.steaf23.ancientwarfare.npc.entity.behavior;

import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public abstract class NpcTask<T extends BaseNpc> extends Behavior<T> {

	public NpcTask(Map<MemoryModuleType<?>, MemoryStatus> map) {
		super(map);
	}
}
