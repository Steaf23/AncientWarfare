package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.steaf23.ancientwarfare.automation.block.worksite.BoundedArea;
import io.github.steaf23.ancientwarfare.client.core.render.RenderHelper;
import io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock.WardedBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.CopperGolemRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class WardedBlockEntityRenderer implements BlockEntityRenderer<WardedBlockEntity, WardedBlockRenderState> {

	public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
	private final BlockEntityRendererProvider.Context context;

	public WardedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public WardedBlockRenderState createRenderState() {
		return new WardedBlockRenderState();
	}

	@Override
	public void extractRenderState(WardedBlockEntity blockEntity, WardedBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		context.blockModelResolver().update(state.blockModel, blockEntity.getBlockToRestore().state(), BLOCK_DISPLAY_CONTEXT);
	}

	@Override
	public void submit(WardedBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		ClientLevel level = Minecraft.getInstance().level;

		// render block

		AABB box = new BoundedArea(1, 25).asBox().move(-state.blockPos.getX(), -state.blockPos.getY(), -state.blockPos.getZ());

		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> {
			Vec3 camDir = camera.pos.subtract(state.blockPos.getCenter()).normalize();
			RenderHelper.renderBoxOutline(pose.pose(), buffer, box, 1.0f, 1.0f, 1.0f, 1.0f, camDir);
		});

		state.blockModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
//
//		// render BE
//		BlockEntity fake = BlockEntity.loadStatic(state.blockPos, state.capturedBlock.state(), state.capturedBlock.capturedBlockEntityData(), level.registryAccess());
//		BlockEntityRenderer<BlockEntity, BlockEntityRenderState> renderer = context.blockEntityRenderDispatcher().getRenderer(fake);
//		if (renderer == null || fake instanceof WardedBlockEntity) {
//			return; // no null pointers or render-ception please!
//		}
//
//		BlockEntityRenderState capturedRenderState = renderer.createRenderState();
//		renderer.extractRenderState(fake, capturedRenderState, 0.0f, camera.pos, new ModelFeatureRenderer.CrumblingOverlay(-1, poseStack.last()));
//		renderer.submit(capturedRenderState, poseStack, submitNodeCollector, camera);
	}
}
