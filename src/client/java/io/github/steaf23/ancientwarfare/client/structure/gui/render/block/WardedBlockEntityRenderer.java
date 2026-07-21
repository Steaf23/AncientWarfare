package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.steaf23.ancientwarfare.client.core.render.SubmitHelper;
import io.github.steaf23.ancientwarfare.structure.block.wardedblock.WardedBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
//? >1.21.11
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state./*?if >1.21.11 {*/level./*?}*/CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WardedBlockEntityRenderer implements BlockEntityRenderer<WardedBlockEntity, WardedBlockRenderState> {

	//? >1.21.11
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
		state.capturedBlockInfo = blockEntity.getBlockToRestore();
		state.wardInfo = blockEntity.getWard();
		state.showDebugLabel = Minecraft.getInstance().player.isCreative();

		//?if <=1.21.11 {
		/*if (blockEntity.getBlockToRestore().state().isAir()) {
			state.blockModel = Optional.empty();
		} else {
			state.blockModel = Optional.of(blockEntity.getBlockToRestore().state());
		}
		*///?} else {
		context.blockModelResolver().update(state.blockModel, blockEntity.getBlockToRestore().state(), BLOCK_DISPLAY_CONTEXT);
		//?}
	}

	@Override
	public void submit(WardedBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		ClientLevel level = Minecraft.getInstance().level;

		if (state.showDebugLabel) {
			List<Component> text = new ArrayList<>();
			text.add(Component.literal("[Warded Block]").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA));
			text.add(Component.literal("Captured Block: ").append(state.capturedBlockInfo.state().getBlock().getName().withStyle(ChatFormatting.GOLD)));

			if (state.wardInfo.entityToSpawn() != null) {
				text.add(Component.literal("Guarded by entity: ").append(Component.empty().append(state.wardInfo.entityToSpawn().getDescription()).withStyle(ChatFormatting.GOLD)));
			}

			if (state.wardInfo.effect() != null) {
				text.add(Component.literal("Guarded by effect: ").append(Component.translatable(state.wardInfo.effect().getDescriptionId()).withStyle(ChatFormatting.GOLD)));
			}

			SubmitHelper.submitLabelAboveBlock(text, state.blockPos, poseStack, submitNodeCollector, camera, state.lightCoords);
		}

		// render block
		//?if <=1.21.11 {
		/*state.blockModel.ifPresent(blockState ->
				submitNodeCollector.submitBlock(poseStack, blockState, state.lightCoords, OverlayTexture.NO_OVERLAY, 0));

		*///?} else {
		state.blockModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		//?}
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
