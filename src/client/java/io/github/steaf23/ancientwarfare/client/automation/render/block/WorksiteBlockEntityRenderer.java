package io.github.steaf23.ancientwarfare.client.automation.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.steaf23.ancientwarfare.automation.block.worksite.entity.AbstractWorksiteBlockEntity;
import io.github.steaf23.ancientwarfare.client.core.render.RenderHelper;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class WorksiteBlockEntityRenderer implements BlockEntityRenderer<AbstractWorksiteBlockEntity, WorksiteRenderState> {

	private final BlockEntityRendererProvider.Context context;

	public WorksiteBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public WorksiteRenderState createRenderState() {
		return new WorksiteRenderState();
	}

	@Override
	public void extractRenderState(AbstractWorksiteBlockEntity blockEntity, WorksiteRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);
		blockEntityRenderState.bounds = blockEntity.bounds().orElse(null);
	}

	@Override
	public void submit(WorksiteRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		if (blockEntityRenderState.bounds == null) {
			return;
		}
		AABB box = blockEntityRenderState.bounds.asBox().move(-blockEntityRenderState.blockPos.getX(), -blockEntityRenderState.blockPos.getY(), -blockEntityRenderState.blockPos.getZ());

		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> {
			Vec3 camDir = cameraRenderState.pos.subtract(blockEntityRenderState.blockPos.getCenter()).normalize();
			RenderHelper.renderBoxOutline(pose.pose(), buffer, box, 1.0f, 1.0f, 1.0f, 1.0f, camDir);
		});
	}
}
