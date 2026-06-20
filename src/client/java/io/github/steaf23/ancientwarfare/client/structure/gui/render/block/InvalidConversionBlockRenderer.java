package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.structure.block.entity.invalidconversionblock.InvalidConversionBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class InvalidConversionBlockRenderer implements BlockEntityRenderer<InvalidConversionBlockEntity, InvalidConversionBlockRenderState> {

	private final BlockEntityRendererProvider.Context context;

	public InvalidConversionBlockRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public InvalidConversionBlockRenderState createRenderState() {
		return new InvalidConversionBlockRenderState();
	}

	@Override
	public void extractRenderState(InvalidConversionBlockEntity blockEntity, InvalidConversionBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.infoStackState = new ItemStackRenderState();

		context.itemModelResolver().updateForTopItem(state.infoStackState, new ItemStack(AWBlocks.INVALID_CONVERSION.asItem()), ItemDisplayContext.FIXED, blockEntity.getLevel(), new ItemOwner() {
			@Override
			public Level level() {
				return blockEntity.getLevel();
			}

			@Override
			public Vec3 position() {
				return blockEntity.getBlockPos().getCenter();
			}

			@Override
			public float getVisualRotationYInDegrees() {
				return 0.0f;
			}
		}, 0);
	}

	@Override
	public void submit(InvalidConversionBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.5, 0.5, 0.5);
		poseStack.mulPose(camera.orientation);

		state.infoStackState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		poseStack.popPose();
	}
}
