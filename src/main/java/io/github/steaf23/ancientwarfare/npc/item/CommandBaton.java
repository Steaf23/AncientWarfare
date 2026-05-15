package io.github.steaf23.ancientwarfare.npc.item;

import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class CommandBaton extends Item {

	//FIXME: maybe refactor this?
	private static HitResult LAST_HIT_RESULT = null;

	public CommandBaton(Properties settings) {
		super(settings);
	}

	public static List<PlayerOwnedNpc> getCommandedNpcs(ServerLevel world, @Nullable ItemStack batonStack) {
		if (batonStack == null) {
			return List.of();
		}

		List<UUID> selected = getSelectedEntities(batonStack);

		List<PlayerOwnedNpc> result = new ArrayList<>();
		for (UUID id : selected) {
			Entity e = world.getEntity(id);
			if (e == null) continue;
			if (!(e instanceof PlayerOwnedNpc playerNpc)) {
				continue;
			}
			result.add(playerNpc);
		}

		return result;
	}

	@Override
	public InteractionResult use(Level world, Player user, InteractionHand hand) {
		HitResult result = playerRaycast(user, 50, 1.0f, 0.5f, false);

		if (world.isClientSide()) {
			return super.use(world, user, hand);
		}

		if (result instanceof EntityHitResult entityHit) {
			if (entityHit.getEntity() instanceof BaseNpc npc) {
				clickNpc(npc, user.getItemInHand(hand));
				return InteractionResult.SUCCESS;
			}
		}

		return super.use(world, user, hand);
	}

	public static HitResult getLastHitResult() {
		return LAST_HIT_RESULT;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		if (user.level().isClientSide()) {
			return super.interactLivingEntity(stack, user, entity, hand);
		}

		if (entity instanceof BaseNpc npc) {
			clickNpc(npc, user.getItemInHand(hand));
			return InteractionResult.SUCCESS;
		}

		return super.interactLivingEntity(stack, user, entity, hand);
	}

	public static HitResult playerRaycast(@NotNull Player player, double maxDistance, float tickProgress, float entityMargin, boolean includeFluids) {
		HitResult blockHit = player.pick(maxDistance, tickProgress, includeFluids);
		EntityHitResult entityHit = raycastEntities(player, maxDistance, entityMargin);

		if (entityHit != null) {
			if (blockHit.getType() != HitResult.Type.MISS) {
				// Entity got hit, but there are blocks in front of it...
				if (player.distanceToSqr(blockHit.getLocation()) < player.distanceToSqr(entityHit.getEntity())) {
					LAST_HIT_RESULT = blockHit;
					return blockHit;
				}
			}
			LAST_HIT_RESULT = entityHit;
			return entityHit;
		}

		LAST_HIT_RESULT = blockHit;
		return blockHit;
	}

	@Nullable
	public static EntityHitResult raycastEntities(Player player, double maxDistance, float margin) {
		Vec3 start = player.getEyePosition(1.0F);
		Vec3 look = player.getViewVector(1.0F);
		Vec3 end = start.add(look.scale(maxDistance));

		return ProjectileUtil.getEntityHitResult(
				player.level(),
				player,
				start,
				end,
				player.getBoundingBox().expandTowards(look.scale(maxDistance)).inflate(1.0D),
				entity -> !entity.isSpectator() && entity.isAlive() && entity.isPickable() && entity != player,
				margin
		);
	}

	public void clickNpc(BaseNpc npc, ItemStack baton) {
		if (!(npc.level() instanceof ServerLevel world)) {
			return;
		}

		List<UUID> entities = new ArrayList<>(getSelectedEntities(baton));

		UUID npcId = npc.getUUID();

		if (entities.contains(npcId)) {
			entities.remove(npcId);
		} else {
			entities.add(npcId);
		}

		validateEntities(world, entities);

		setSelectedEntities(baton, entities);
	}

	public static List<UUID> getSelectedEntities(ItemStack baton) {
		return baton.getOrDefault(AWComponents.SELECTED_ENTITIES, List.of());
	}

	public static void setSelectedEntities(ItemStack baton, @NotNull List<UUID> entities) {
		baton.set(AWComponents.SELECTED_ENTITIES, entities);
	}

	public static void validateEntities(ServerLevel world, List<UUID> entities) {
		Iterator<UUID> it = entities.iterator();
		UUID id;
		while (it.hasNext()) {
			id = it.next();
			if (id == null || world.getEntity(id) == null) {
				it.remove();
			}
		}
	}
}
