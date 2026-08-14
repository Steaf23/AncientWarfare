package io.github.steaf23.ancientwarfare.client.npc.render.entity;

import io.github.steaf23.ancientwarfare.client.core.registry.AWRenderer;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.faction.FactionNpc;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class NpcEntityRenderer<T extends BaseNpc> extends HumanoidMobRenderer<T, NpcRenderState, NpcEntityModel> {

	public static final Identifier FALLBACK = AncientWarfare.id("textures/entity/npc_default.png");

	public NpcEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new NpcEntityModel(context.bakeLayer(AWRenderer.NPC), false), 0.5f);
		addLayer(new HumanoidArmorLayer<>(
				this,
				ArmorModelSet.bake(AWRenderer.NPC_EQUIPMENT, context.getModelSet(), p -> new NpcEntityModel(p, false)),
				ArmorModelSet.bake(AWRenderer.NPC_EQUIPMENT, context.getModelSet(), p -> new NpcEntityModel(p, false)),
				context.getEquipmentRenderer()
		));
	}

	@Override
	public @NotNull Identifier getTextureLocation(NpcRenderState state) {
		return state.modelTexture;
	}

	@Override
	public @NotNull NpcRenderState createRenderState() {
		return new NpcRenderState();
	}

	@Override
	public Vec3 getRenderOffset(NpcRenderState state) {
		if (state.isPassenger) {
			return new Vec3(0.0, -.70, 0.0);
		}
		return super.getRenderOffset(state);
	}

	@Override
	public void extractRenderState(T entity, NpcRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		if (entity instanceof FactionNpc factionNpc) {
			state.modelTexture = NpcModels.getSkinForFactionNpc(factionNpc);
		}
	}
}
