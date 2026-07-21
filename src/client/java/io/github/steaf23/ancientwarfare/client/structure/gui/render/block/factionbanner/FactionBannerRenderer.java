package io.github.steaf23.ancientwarfare.client.structure.gui.render.block.factionbanner;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.structure.block.factionbanner.FactionBannerBlockEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class FactionBannerRenderer implements BlockEntityRenderer<FactionBannerBlockEntity, FactionBannerRenderState> {

	public FactionBannerRenderer(BlockEntityRendererProvider.Context context) {
		this.sprites = context.sprites();
		EntityModelSet modelSet = context.entityModelSet();
		this.standingModel = new BannerModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER));
		this.wallModel = new BannerModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER));
		this.standingFlagModel = new FactionBannerFlagModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
		this.wallFlagModel = new FactionBannerFlagModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER_FLAG));
	}

	public FactionBannerRenderer(SpecialModelRenderer.BakingContext context) {
		this.sprites = context.sprites();
		EntityModelSet modelSet = context.entityModelSet();
		this.standingModel = new BannerModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER));
		this.wallModel = new BannerModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER));
		this.standingFlagModel = new FactionBannerFlagModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
		this.wallFlagModel = new FactionBannerFlagModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER_FLAG));
	}

	@Override
	public FactionBannerRenderState createRenderState() {
		return new FactionBannerRenderState();
	}

	@Override
	public void extractRenderState(FactionBannerBlockEntity blockEntity, FactionBannerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		if (blockEntity.getFaction().equals(Factions.NEUTRAL)) {
			state.bannerImage = Identifier.withDefaultNamespace("banner_base");
		} else {
			state.bannerImage = blockEntity.getFaction().id();
		}

		// Vanilla banner renderer start
		BlockState blockState = blockEntity.getBlockState();
		if (blockState.getBlock() instanceof BannerBlock) {
			state.transformation = BannerRenderer.TRANSFORMATIONS.freeTransformations(blockState.getValue(BannerBlock.ROTATION));
			state.attachmentType = BannerBlock.AttachmentType.GROUND;
		} else {
			state.transformation = BannerRenderer.TRANSFORMATIONS.wallTransformation(blockState.getValue(WallBannerBlock.FACING));
			state.attachmentType = BannerBlock.AttachmentType.WALL;
		}

		long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
		BlockPos blockPos = blockEntity.getBlockPos();
		state.phase = ((float)Math.floorMod(blockPos.getX() * 7L + blockPos.getY() * 9L + blockPos.getZ() * 13L + gameTime, 100L) + partialTicks) / 100.0F;
		// Vanilla banner renderer end
	}

	@Override
	public void submit(FactionBannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.mulPose(state.transformation);
		submitFactionBanner(
				this.sprites,
				poseStack,
				submitNodeCollector,
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				this.bannerModel(state.attachmentType),
				this.flagModel(state.attachmentType),
				state.bannerImage,
				state.phase,
				state.breakProgress,
				0
		);
		poseStack.popPose();
	}

	public void submitSpecial(
			final BannerBlock.AttachmentType type,
			final PoseStack poseStack,
			final SubmitNodeCollector submitNodeCollector,
			final int lightCoords,
			final int overlayCoords,
			final @Nullable ResourceKey<Faction> faction,
			final int outlineColor
	) {
		submitFactionBanner(
				this.sprites,
				poseStack,
				submitNodeCollector,
				lightCoords,
				overlayCoords,
				this.bannerModel(type),
				this.flagModel(type),
				faction == null ? Identifier.withDefaultNamespace("banner_base") : faction.identifier(),
				0.0F,
				null,
				outlineColor
		);
	}

	// Vanilla banner renderer start
	private final SpriteGetter sprites;
	private final BannerModel standingModel;
	private final BannerModel wallModel;
	private final FactionBannerFlagModel standingFlagModel;
	private final FactionBannerFlagModel wallFlagModel;

	private static void submitFactionBanner(
			final SpriteGetter sprites,
			final PoseStack poseStack,
			final SubmitNodeCollector submitNodeCollector,
			final int lightCoords,
			final int overlayCoords,
			final BannerModel model,
			final FactionBannerFlagModel flagModel,
			final Identifier bannerImage,
			final float phase,
			final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
			final int outlineColor
	) {
		SpriteId sprite = Sheets.BANNER_MAPPER.apply(bannerImage);
		submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);
		submitNodeCollector.submitModel(flagModel, phase, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);
	}

	private BannerModel bannerModel(final BannerBlock.AttachmentType type) {
		return switch (type) {
			case WALL -> this.wallModel;
			case GROUND -> this.standingModel;
		};
	}

	private FactionBannerFlagModel flagModel(final BannerBlock.AttachmentType type) {
		return switch (type) {
			case WALL -> this.wallFlagModel;
			case GROUND -> this.standingFlagModel;
		};
	}

	public void getExtents(final Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		this.standingModel.root().getExtentsForGui(poseStack, output);
		this.standingFlagModel.setupAnim(0.0F);
		this.standingFlagModel.root().getExtentsForGui(poseStack, output);
	}

	// Vanilla banner renderer end
}
