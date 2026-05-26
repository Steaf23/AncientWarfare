package io.github.steaf23.ancientwarfare.client.npc.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.steaf23.ancientwarfare.client.core.render.RenderHelper;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.util.EntityHelper;
import io.github.steaf23.ancientwarfare.npc.item.CommandBaton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;

import java.util.List;
import java.util.UUID;

//? if <= 1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
//?}

public class CommandBatonOverlay {

	//~ if <=1.21.11 'LevelRenderContext' -> 'WorldRenderContext'
	public static void renderOutlineBoxes(LevelRenderContext context) {
		if (Minecraft.getInstance() == null || Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) {
			return;
		}

		ClientLevel world = Minecraft.getInstance().level;
		Player player = Minecraft.getInstance().player;

		ItemStack baton = EntityHelper.getItemFromEitherHand(player, AWItems.COMMAND_BATONS);
		if (baton.isEmpty()) {
			return;
		}

		//? if <=1.21.11 {
		/*PoseStack poses = context.matrices();
		Vec3 camPos = context.worldState().cameraRenderState.pos;

		VertexConsumer buffer = context.consumers().getBuffer(RenderTypes.debugQuads());
		*///?} else {
		PoseStack poses = context.poseStack();
		Vec3 camPos = context.levelState().cameraRenderState.pos;

		MultiBufferSource.BufferSource immediate = context.bufferSource();
		VertexConsumer buffer = immediate.getBuffer(RenderTypes.debugQuads());
		//?}



		HitResult hoveringHit = CommandBaton.playerRaycast(player, 50, 1.0f, 0.5f, false);
		switch (hoveringHit.getType()) {
			case MISS:
				break;
			case BLOCK:
				BlockHitResult blockHit = (BlockHitResult) hoveringHit;
				BlockPos blockPos = blockHit.getBlockPos();
				renderBlockBoxOutline(blockPos, poses, buffer, camPos, 1f, 1f, 1f, .5f);
				break;
			case ENTITY:
				renderEntityBoxOutline(((EntityHitResult) hoveringHit).getEntity(), poses, buffer, camPos, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true), 1f, 1f, 1f, .5f);
		}

		List<UUID> entities = CommandBaton.getSelectedEntities(baton);
		if (entities.isEmpty()) {
			return;
		}

		for (UUID id : entities) {
			Entity entity = world.getEntity(id);
			if (entity == null || entity.isRemoved()) {
				continue;
			}
			renderEntityBoxOutline(entity, poses, buffer, camPos, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true), 0f, 1f, 0f, .5f);
		}

		//?if >1.21.11 {
		immediate.endBatch();
		//?}
	}

	private static void renderBlockBoxOutline(BlockPos pos, PoseStack poses, VertexConsumer buffer,
											  Vec3 camPos, float r, float g, float b, float a) {
		Vec3 cameraDir = camPos.subtract(pos.getCenter()).normalize();

		BlockState blockState = Minecraft.getInstance().level.getBlockState(pos);

		VoxelShape shape = blockState.getShape(Minecraft.getInstance().level, pos);
		AABB box = shape.bounds().move(pos.getX(), pos.getY(), pos.getZ()).move(-camPos.x, -camPos.y, -camPos.z).inflate(0.1f);

		Matrix4f matrix = poses.last().pose();
		RenderHelper.renderBoxOutline(matrix, buffer, box, r, g, b, a, cameraDir);
	}

	private static void renderEntityBoxOutline(Entity entity, PoseStack poses, VertexConsumer buffer,
											   Vec3 camPos, float tickDelta, float r, float g, float b, float a) {
		Vec3 cameraDir = camPos.subtract(getInterpolatedPos(entity, tickDelta)).normalize();
		AABB box = getInterpolatedBox(entity, tickDelta);
		box = box.move(0.0, box.getYsize() / 2.0, 0.0).move(-camPos.x, -camPos.y, -camPos.z).inflate(0.1f);
		Matrix4f matrix = poses.last().pose();

		// Vanilla style line box
		RenderHelper.renderBoxOutline(matrix, buffer, box, r, g, b, a, cameraDir);
	}

	private static AABB getInterpolatedBox(Entity e, float tickDelta) {
		Vec3 interpolated = getInterpolatedPos(e, tickDelta);
		AABB bb = e.getBoundingBox();
		double w = (bb.maxX - bb.minX) / 2.0;
		double h = (bb.maxY - bb.minY) / 2.0;
		double d = (bb.maxZ - bb.minZ) / 2.0;
		return new AABB(interpolated.x - w, interpolated.y - h, interpolated.z - d,
				interpolated.x + w, interpolated.y + h, interpolated.z + d);
	}

	private static Vec3 getInterpolatedPos(Entity e, float tickDelta) {
		double x = e.xo + (e.getX() - e.xo) * tickDelta;
		double y = e.yo + (e.getY() - e.yo) * tickDelta;
		double z = e.zo + (e.getZ() - e.zo) * tickDelta;
		return new Vec3(x, y, z);
	}
}
