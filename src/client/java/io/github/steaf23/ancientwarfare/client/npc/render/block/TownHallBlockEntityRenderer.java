package io.github.steaf23.ancientwarfare.client.npc.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.npc.block.entity.TownHallBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TownHallBlockEntityRenderer implements BlockEntityRenderer<TownHallBlockEntity, TownHallRenderState> {

	private final BlockEntityRendererProvider.Context context;

	public TownHallBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public @NotNull TownHallRenderState createRenderState() {
		return new TownHallRenderState();
	}

	@Override
	public void extractRenderState(TownHallBlockEntity blockEntity, TownHallRenderState state, float delta, Vec3 vec3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, delta, vec3, crumblingOverlay);
		state.keyRotation = (blockEntity.keyRotationDegrees + delta) * 5.0f % 360.0f;
		state.lightCoords = blockEntity.getLevel() != null ? LevelRenderer.getLightCoords(blockEntity.getLevel(), blockEntity.getBlockPos().above()) : 0xF000F0;
		state.keyState = new ItemStackRenderState();

		context.itemModelResolver().updateForTopItem(state.keyState, new ItemStack(AWItems.TOWN_HALL_KEY_DUMMY), ItemDisplayContext.FIXED, blockEntity.getLevel(), new ItemOwner() {
			@Override
			public @NotNull Level level() {
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
	public void submit(TownHallRenderState state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		poses.pushPose();
		poses.translate(0.5, 1.001, 0.5);
		poses.mulPose(Axis.YP.rotationDegrees(state.keyRotation));

		poses.pushPose();
		poses.translate(0.0, 0.5, 0.0);


		state.keyState.submit(poses, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		poses.popPose();
		poses.popPose();
	}
}
