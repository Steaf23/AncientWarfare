package io.github.steaf23.ancientwarfare.client.core.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SubmitHelper {

	public static void submitLabelAboveBlock(List<Component> text, BlockPos pos, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int light) {
		submitLabelAboveBlock(text, pos, poseStack, submitNodeCollector, camera, light, true);
	}

	public static void submitLabelAboveBlock(List<Component> text, BlockPos pos, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int light, boolean needsToLook) {
		if (needsToLook) {
			HitResult hit = Minecraft.getInstance().hitResult;

			if (!(hit instanceof BlockHitResult block)) {
				return;
			}

			if (!block.getBlockPos().equals(pos)) {
				return;
			}
		}

		poseStack.pushPose();
		Vec3 tagAttachment = new Vec3(0.5, 1.0, 0.5);

		for (Component c : text.reversed()) {
			submitNodeCollector.submitNameTag(
					poseStack,
					tagAttachment,
					0,
					c,
					true,
					light,
					camera.pos.distanceToSqr(pos.getCenter()),
					camera
			);
			tagAttachment = tagAttachment.add(0.0, 0.2, 0.0);
		}

		poseStack.popPose();
	}
}
