package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.steaf23.ancientwarfare.client.core.render.SubmitHelper;
import io.github.steaf23.ancientwarfare.structure.block.entity.advancedspawner.AdvancedSpawnerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class AdvancedSpawnerBlockEntityRenderer implements BlockEntityRenderer<AdvancedSpawnerBlockEntity, AdvancedSpawnerRenderState> {

	private final BlockEntityRendererProvider.Context context;

	public AdvancedSpawnerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public AdvancedSpawnerRenderState createRenderState() {
		return new AdvancedSpawnerRenderState();
	}

	@Override
	public void extractRenderState(AdvancedSpawnerBlockEntity blockEntity, AdvancedSpawnerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.displayEntityName = blockEntity.displayEntityName();
	}

	@Override
	public void submit(AdvancedSpawnerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!Minecraft.getInstance().player.isCreative()) {
			return;
		}

		if (state.displayEntityName != null) {
			SubmitHelper.submitLabelAboveBlock(List.of(state.displayEntityName), state.blockPos, poseStack, submitNodeCollector, camera, state.lightCoords, false);
		}
	}
}
