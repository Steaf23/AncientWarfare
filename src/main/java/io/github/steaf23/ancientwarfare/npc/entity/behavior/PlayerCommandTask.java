package io.github.steaf23.ancientwarfare.npc.entity.behavior;

import io.github.steaf23.ancientwarfare.npc.command.NpcCommand;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.equine.AbstractHorse;

import java.util.Map;

public class PlayerCommandTask extends NpcTask<PlayerOwnedNpc> {

	BlockPos moveTargetPos = null;

	public PlayerCommandTask(Map<MemoryModuleType<?>, MemoryStatus> map) {
		super(map);
	}

	@Override
	public Behavior.Status getStatus() {
		return null;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel serverLevel, PlayerOwnedNpc npc) {
		NpcCommand.Command command = npc.getCurrentCommand();
		return command != NpcCommand.Command.NONE&& (!(command.type == NpcCommand.CommandType.GUARD || command.type == NpcCommand.CommandType.ATTACK_AREA) || npc.getTarget() == null);
	}

	@Override
	protected void tick(ServerLevel serverLevel, PlayerOwnedNpc npc, long l) {
		NpcCommand.Command command = npc.getCurrentCommand();
		handleCommand(command, npc);
		if (!command.type.isPersistent()) {
			npc.setPlayerCommand(command);
		}
	}

	public void handleCommand(NpcCommand.Command command, PlayerOwnedNpc npc) {
		switch (command.type)//handle instant type commands
		{
			case CLEAR_HOME: {
				npc.getHome().clear();
				break;
			}
			case CLEAR_UPKEEP: {
				npc.setAutoUpkeepBlock(null);
				break;
			}
			case SET_HOME: {
				npc.getHome().setHomePosAndRange(command.pos, npc.getHome().getHomeRange());
				break;
			}
			case SET_UPKEEP: {
				npc.setAutoUpkeepBlock(command.pos);
				break;
			}
			case CLEAR_COMMAND:
			case ATTACK: {
				//should already be handled by npc 'handle command' functionality when command first received
				npc.setPlayerCommand(NpcCommand.Command.NONE);
				break;
			}
			case ATTACK_AREA: {
				//TODO this likely needs an implementaion that will prioritize attacking targets over moving
				handleMoveCommand(command, npc);
				break;
			}
			case GUARD: {
				handleGuardCommand(command, npc);
				break;
			}
			case MOVE: {
				handleMoveCommand(command, npc);
				break;
			}
			default:
		}
	}

	@Override
	protected void stop(ServerLevel serverLevel, PlayerOwnedNpc npc, long l) {
		NpcCommand.Command cmd = npc.getCurrentCommand();
		if (cmd != NpcCommand.Command.NONE && (npc.getTarget() == null || !cmd.type.isPersistent())) {
			npc.handlePlayerCommand(NpcCommand.Command.NONE);
		}
	}

	public void handleMoveCommand(NpcCommand.Command command, PlayerOwnedNpc npc) {
		if (moveTargetPos == null || moveTargetPos != command.pos) {
			moveTargetPos = command.pos;
		}
		double sqDist = npc.distanceToSqr(moveTargetPos.getCenter());
		if (sqDist > BaseNpc.MIN_MOVE_RANGE_SQUARED) {
			//FIXME: IMPLEMENT
//			moveToPosition(moveTargetPos, sqDist);//not finished moving...move along path (or at least try)
		} else {
			npc.setPlayerCommand(NpcCommand.Command.NONE);//finished moving..clear the command...
		}
	}

	public void handleGuardCommand(NpcCommand.Command command, PlayerOwnedNpc npc) {
		Entity e = command.getEntityTarget(npc.level());
		if (e == null) {
			npc.setPlayerCommand(NpcCommand.Command.NONE);//clear the command if the target entity cannot be found
			return;
		}
		double sqDist = npc.distanceToSqr(e);
		if (sqDist > BaseNpc.MIN_MOVE_RANGE_SQUARED) {
			//FIXME: IMPLEMENT
//			moveToEntity(e, sqDist);//move to entity...
		} else {
			//FIXME: IMPLEMENT
//			npc.getNavigation().clearPath();//clear path to stop moving

			// TODO: support all mountable mobs instead of only horses
			if (e instanceof AbstractHorse horse) {
				if (!npc.isPassenger() && horse.getPassengers().isEmpty()) {
					npc.startRiding(horse);
					//FIXME: IMPLEMENT
//					horse.prevRotationYaw = horse.rotationYaw = npc.rotationYaw % 360F;
				} else if (npc.getVehicle() == horse) {
					npc.removeVehicle();
				}
				npc.setPlayerCommand(NpcCommand.Command.NONE);
			}
			//do not clear command, guard command is persistent
		}
	}
}
