package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock.WardedBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
		state.capturedBlock = blockEntity.getBlockToRestore();
		state.wardData = blockEntity.getWard();
		state.showDebugLabel = Minecraft.getInstance().player.isCreative();
	}

	@Override
	public void submit(WardedBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		ClientLevel level = Minecraft.getInstance().level;

		if (state.showDebugLabel) {
			List<Component> text = new ArrayList<>();
			text.add(Component.literal("[Warded Block]").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA));
			text.add(Component.literal("Captured Block: ").append(state.capturedBlock.state().getBlock().getName().withStyle(ChatFormatting.GOLD)));

			if (state.wardData.entityToSpawn() != null) {
				text.add(Component.literal("Guarded by entity: ").append(Component.empty().append(state.wardData.entityToSpawn().getDescription()).withStyle(ChatFormatting.GOLD)));
			}

			if (state.wardData.effect() != null) {
				text.add(Component.literal("Guarded by effect: ").append(Component.translatable(state.wardData.effect().getDescriptionId()).withStyle(ChatFormatting.GOLD)));
			}

			renderLabelAboveBlock(text, state.blockPos, poseStack, submitNodeCollector, camera, state.lightCoords);
		}

		// render block
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

	public void renderLabelAboveBlock(List<Component> text, BlockPos pos, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int light) {
		HitResult hit = Minecraft.getInstance().hitResult;

		if (!(hit instanceof BlockHitResult block)) {
			return;
		}

		if (!block.getBlockPos().equals(pos)) {
			return;
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
