package io.github.steaf23.ancientwarfare.client.structure.gui.render.block.factionbanner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class FactionBannerSpecialRenderer implements SpecialModelRenderer<ResourceKey<Faction>> {

	private final FactionBannerRenderer bannerRenderer;
	private final BannerBlock.AttachmentType attachment;

	public FactionBannerSpecialRenderer(FactionBannerRenderer bannerRenderer, BannerBlock.AttachmentType attachmentType) {
		this.bannerRenderer = bannerRenderer;
		this.attachment = attachmentType;
	}

	@Override
	public void submit(@Nullable ResourceKey<Faction> faction, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		bannerRenderer.submitSpecial(
				attachment,
				poseStack,
				submitNodeCollector,
				lightCoords,
				overlayCoords,
				faction,
				outlineColor);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		bannerRenderer.getExtents(output);
	}

	@Override
	public @Nullable ResourceKey<Faction> extractArgument(ItemStack stack) {
		return Faction.factionKeyFromItem(stack);
	}

	public record Unbaked(BannerBlock.AttachmentType attachment)
			implements net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked<ResourceKey<Faction>> {
		public static final MapCodec<FactionBannerSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
				i -> i.group(
								BannerBlock.AttachmentType.CODEC.optionalFieldOf("attachment", BannerBlock.AttachmentType.GROUND).forGetter(FactionBannerSpecialRenderer.Unbaked::attachment)
						)
						.apply(i, FactionBannerSpecialRenderer.Unbaked::new)
		);

		@Override
		public MapCodec<FactionBannerSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		public FactionBannerSpecialRenderer bake(final BakingContext context) {
			return new FactionBannerSpecialRenderer(new FactionBannerRenderer(context), this.attachment);
		}
	}
}
