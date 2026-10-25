package io.github.steaf23.ancientwarfare.client.npc.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class NpcPortrait {

	public static final float BODY_ANGLE = (float)Math.PI * 0.85f;

	public static void extract(GuiGraphicsExtractor graphics, int bottomLeftX, int bottomLeftY, int width, float scale, float extraYOffset, LivingEntity entity) {
		EntityRenderState renderState = extractRenderState(entity);
		if (renderState instanceof LivingEntityRenderState livingState) {
			livingState.bodyRot = 0.0f;
			livingState.yRot = 0.0f;
			livingState.xRot = 0.0f;
			livingState.pose = Pose.STANDING;
			livingState.walkAnimationSpeed = 0;
			livingState.nameTag = null;
		}

		Quaternionf rotation = new Quaternionf()
				.rotateZ((float) Math.PI)
				.rotateX((float) Math.toRadians(-15));
		Quaternionf yRot = new Quaternionf().rotateY(BODY_ANGLE);
		rotation.mul(yRot);
		Vector3f translation = new Vector3f(0.0f, renderState.boundingBoxHeight / 2.0f + extraYOffset, 0.0f);
		graphics.entity(renderState, scale, translation, rotation, yRot, bottomLeftX, bottomLeftY - width * 2, bottomLeftX + width, bottomLeftY);
	}

	private static EntityRenderState extractRenderState(final LivingEntity entity) {
		EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		EntityRenderer<? super LivingEntity, ?> renderer = entityRenderDispatcher.getRenderer(entity);
		EntityRenderState renderState = renderer.createRenderState(entity, 1.0F);
		renderState.shadowPieces.clear();
		renderState.outlineColor = 0;
		return renderState;
	}

}
