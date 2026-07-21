package io.github.steaf23.ancientwarfare.client.structure.gui.render.block.factionbanner;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class FactionBannerFlagModel extends Model<Float> {

	private final ModelPart flag;

	public FactionBannerFlagModel(ModelPart root) {
		super(root, RenderTypes::entityCutoutCull);
		this.flag = root.getChild("flag");
	}

	@Override
	public void setupAnim(final Float phase) {
		super.setupAnim(phase);
		this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
	}
}
