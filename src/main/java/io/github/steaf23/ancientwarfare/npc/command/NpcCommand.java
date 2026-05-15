package io.github.steaf23.ancientwarfare.npc.command;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.util.EntityHelper;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import io.github.steaf23.ancientwarfare.npc.item.CommandBaton;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NpcCommand {
	//TODO refactor to proper individual instances of commands that will not only hold the type, but their logic as well (move that from NPCAIPlayerOwnedFollowCommand)

	public enum CommandType {
		NONE(0),
		MOVE(1), ATTACK(2), //attack click on entity
		ATTACK_AREA(3), //attack click on block
		GUARD(4), //attack click on friendly player or npc
		SET_HOME(5), SET_UPKEEP(6), CLEAR_HOME(7), CLEAR_UPKEEP(8), CLEAR_COMMAND(9);

		private final int id;

		CommandType(int id) {
			this.id = id;
		}

		// Experimental: move commands are also persistent; NPCs will return to that task after performing others
		public boolean isPersistent() {
			return (this == MOVE || this == ATTACK || this == GUARD || this == ATTACK_AREA);
		}
	}

	/*
	 * client-side handle command. called from command baton key handler
	 */
	public static void handleCommandClient(CommandType type, @Nullable HitResult hit) {
//		if (hit != null && hit.getType() != HitResult.Type.MISS) {
//			if (hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult entityHit) {
//				NetworkHandler.sendToServer(new PacketNpcCommand(type, hit.entityHit));
//			} else if (hit.typeOfHit == RayTraceResult.Type.BLOCK) {
//				NetworkHandler.sendToServer(new PacketNpcCommand(type, hit.getBlockPos()));
//			}
//		}
	}

	/*
	 * server side handle command. called from packet triggered from client key input while baton is equipped
	 */
	public static void handleServerCommand(ServerPlayer player, CommandType type, boolean block, BlockPos pos, UUID entityID) {
		Command cmd;
		if (block) {
			cmd = new Command(type, pos);
		} else {
			cmd = new Command(type, entityID);
		}
		List<PlayerOwnedNpc> targets = CommandBaton.getCommandedNpcs(player.level(), EntityHelper.getItemFromEitherHand(player, AWItems.COMMAND_BATONS));
		for (PlayerOwnedNpc t : targets) {
			t.handlePlayerCommand(cmd);
		}
	}

	public static class Command {
		public static Codec<Command> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("type").forGetter(Command::commandTypeId),
				BlockPos.CODEC.fieldOf("block_pos").forGetter(Command::getPos),
				UUIDUtil.CODEC.optionalFieldOf("entity_uuid").forGetter(Command::getEntityUuid),
				Codec.INT.fieldOf("entity_id").forGetter(Command::getEntityId)

		).apply(instance, (type, blockPos, entityUuid, entityId) -> new Command(type, blockPos, entityUuid.orElse(null), entityId)));

		public static final Command NONE = new Command(CommandType.NONE, new BlockPos(0, 0, 0));

		public CommandType type;
		public BlockPos pos = BlockPos.ZERO;
		private boolean blockTarget;
		private UUID entityUUID;
		private Entity entity;
		private int entityID;

		public Command() {
		}

		public Command(CommandType type, BlockPos pos) {
			this.type = type;
			this.pos = pos;
			blockTarget = true;
		}

		public Command(CommandType type, UUID entityID) {
			this.type = type;
			blockTarget = false;
		}

		public Command(String type, BlockPos blockPos, UUID entityUuid, int entityId) {
			this.type = CommandType.valueOf(type);
			if (blockPos != null) {
				blockTarget = true;
				this.pos = blockPos;
			} else {
				this.entityUUID = null;
				this.entityID = entityId;
				blockTarget = false;
			}
		}

		/*
		 * should be called by packet prior to passing command into npc processing
		 */
		private void findEntity(Level world) {
			if (blockTarget) {
				return;
			}
			if (entity != null) {
				return;
			}
			if (entityUUID == null) {
				entity = world.getEntity(entityID);
				if (entity != null) {
					entityUUID = entity.getUUID();
				}
			} else {
				entity = world.getEntity(entityUUID);
			}
		}

		@Nullable
		public Entity getEntityTarget(Level world) {
			if (blockTarget) {
				return null;
			}
			if (entity != null) {
				return entity;
			} else {
				findEntity(world);
			}
			return entity;
		}

		public BlockPos getPos() {
			return pos;
		}

		public String commandTypeId() {
			return type.toString();
		}

		public Optional<UUID> getEntityUuid() {
			return Optional.ofNullable(entityUUID);
		}

		public int getEntityId() {
			return entityID;
		}
	}
}
