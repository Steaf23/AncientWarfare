package io.github.steaf23.ancientwarfare.client.npc.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;

public class NpcEntityModel extends HumanoidModel<NpcRenderState> {

	protected static final String LEFT_SLEEVE = "left_sleeve";
	protected static final String RIGHT_SLEEVE = "right_sleeve";
	protected static final String LEFT_PANTS = "left_pants";
	protected static final String RIGHT_PANTS = "right_pants";
	private final List<ModelPart> parts;
	public final ModelPart leftSleeve;
	public final ModelPart rightSleeve;
	public final ModelPart leftPants;
	public final ModelPart rightPants;
	public final ModelPart jacket;
	private final boolean thinArms;

	public NpcEntityModel(ModelPart root, boolean thinArms) {
		super(root, RenderTypes::entityTranslucent);
		this.thinArms = thinArms;
		this.leftSleeve = this.leftArm.getChild(LEFT_SLEEVE);
		this.rightSleeve = this.rightArm.getChild(RIGHT_SLEEVE);
		this.leftPants = this.leftLeg.getChild(LEFT_PANTS);
		this.rightPants = this.rightLeg.getChild(RIGHT_PANTS);
		this.jacket = this.body.getChild(PartNames.JACKET);
		this.parts = List.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}

	public static MeshDefinition getTexturedModelData(CubeDeformation dilation, boolean slim) {
		PartDefinition modelPartData3;
		PartDefinition modelPartData2;
		MeshDefinition modelData = HumanoidModel.createMesh(dilation, 0.0f);
		PartDefinition modelPartData = modelData.getRoot();
		float f = 0.25f;
		if (slim) {
			modelPartData2 = modelPartData.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create().texOffs(32, 48).addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, dilation), PartPose.offset(5.0f, 2.0f, 0.0f));
			modelPartData3 = modelPartData.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create().texOffs(40, 16).addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, dilation), PartPose.offset(-5.0f, 2.0f, 0.0f));
			modelPartData2.addOrReplaceChild(LEFT_SLEEVE, CubeListBuilder.create().texOffs(48, 48).addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, dilation.extend(0.25f)), PartPose.ZERO);
			modelPartData3.addOrReplaceChild(RIGHT_SLEEVE, CubeListBuilder.create().texOffs(40, 32).addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, dilation.extend(0.25f)), PartPose.ZERO);
		} else {
			modelPartData2 = modelPartData.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create().texOffs(32, 48).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation), PartPose.offset(5.0f, 2.0f, 0.0f));
			modelPartData3 = modelPartData.getChild(PartNames.RIGHT_ARM);
			modelPartData2.addOrReplaceChild(LEFT_SLEEVE, CubeListBuilder.create().texOffs(48, 48).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.extend(0.25f)), PartPose.ZERO);
			modelPartData3.addOrReplaceChild(RIGHT_SLEEVE, CubeListBuilder.create().texOffs(40, 32).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.extend(0.25f)), PartPose.ZERO);
		}
		modelPartData2 = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create().texOffs(16, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation), PartPose.offset(1.9f, 12.0f, 0.0f));
		modelPartData3 = modelPartData.getChild(PartNames.RIGHT_LEG);
		modelPartData2.addOrReplaceChild(LEFT_PANTS, CubeListBuilder.create().texOffs(0, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.extend(0.25f)), PartPose.ZERO);
		modelPartData3.addOrReplaceChild(RIGHT_PANTS, CubeListBuilder.create().texOffs(0, 32).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.extend(0.25f)), PartPose.ZERO);
		PartDefinition modelPartData4 = modelPartData.getChild(PartNames.BODY);
		modelPartData4.addOrReplaceChild(PartNames.JACKET, CubeListBuilder.create().texOffs(16, 32).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, dilation.extend(0.25f)), PartPose.ZERO);
		return modelData;
	}

	public static ArmorModelSet<MeshDefinition> createEquipmentModelData(CubeDeformation hatDilation, CubeDeformation armorDilation) {
		return HumanoidModel.createArmorMeshSet(hatDilation, armorDilation).map(modelData -> {
			PartDefinition modelPartData = modelData.getRoot();
			PartDefinition modelPartData2 = modelPartData.getChild(PartNames.LEFT_ARM);
			PartDefinition modelPartData3 = modelPartData.getChild(PartNames.RIGHT_ARM);
			modelPartData2.addOrReplaceChild(LEFT_SLEEVE, CubeListBuilder.create(), PartPose.ZERO);
			modelPartData3.addOrReplaceChild(RIGHT_SLEEVE, CubeListBuilder.create(), PartPose.ZERO);
			PartDefinition modelPartData4 = modelPartData.getChild(PartNames.LEFT_LEG);
			PartDefinition modelPartData5 = modelPartData.getChild(PartNames.RIGHT_LEG);
			modelPartData4.addOrReplaceChild(LEFT_PANTS, CubeListBuilder.create(), PartPose.ZERO);
			modelPartData5.addOrReplaceChild(RIGHT_PANTS, CubeListBuilder.create(), PartPose.ZERO);
			PartDefinition modelPartData6 = modelPartData.getChild(PartNames.BODY);
			modelPartData6.addOrReplaceChild(PartNames.JACKET, CubeListBuilder.create(), PartPose.ZERO);
			return modelData;
		});
	}

	@Override
	public void translateToHand(NpcRenderState npcRenderState, HumanoidArm arm, PoseStack pose) {
		this.root.translateAndRotate(pose);
		ModelPart modelPart = this.getArm(arm);
		if (this.thinArms) {
			float f = 0.5f * (float)(arm == HumanoidArm.RIGHT ? 1 : -1);
			modelPart.x += f;
			modelPart.translateAndRotate(pose);
			modelPart.x -= f;
		} else {
			modelPart.translateAndRotate(pose);
		}
	}

	public ModelPart getRandomPart(RandomSource random) {
		return Util.getRandom(this.parts, random);
	}

}
