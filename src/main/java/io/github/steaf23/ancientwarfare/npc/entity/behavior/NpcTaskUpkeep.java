package io.github.steaf23.ancientwarfare.npc.entity.behavior;

import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class NpcTaskUpkeep {

	public static BehaviorControl<BaseNpc> create() {
		return BehaviorBuilder.create(instance ->
				instance.group(
						instance.present(MemoryModuleType.HOME),
						instance.absent(MemoryModuleType.WALK_TARGET)
				).apply(instance, (upkeepPosAccessor, walkTargetAccessor) ->
						(level, npc, timestamp) -> {
							GlobalPos walkPos = instance.get(upkeepPosAccessor);
							if (npcNeedsUpkeep(npc, walkPos)) {
								if (walkPos.isCloseEnough(npc.level().dimension(), npc.blockPosition(), 3)) {
									((PlayerOwnedNpc)npc).doUpkeepFromBlock(walkPos.pos());
								}
								else {
									// If we are not close enough, walk closer.
									walkTargetAccessor.set(new WalkTarget(walkPos.pos(), 3.0f, 2));
								}

								return true;
							} else {
								return false;
							}
						}));
	}

	private static boolean npcNeedsUpkeep(BaseNpc npc, GlobalPos homePos) {
		if (!(npc instanceof PlayerOwnedNpc ownedNpc)) {
			return false;
		}

		return ownedNpc.upkeep().requiresUpkeep() && homePos != null && homePos.dimension() == npc.level().dimension();
	}
}
