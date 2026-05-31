package io.github.steaf23.ancientwarfare.client.core.registry;

import io.github.steaf23.ancientwarfare.client.npc.render.entity.NpcEntityModel;
import io.github.steaf23.ancientwarfare.client.npc.render.entity.NpcEntityRenderer;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class AWRenderer {

	public final static ModelLayerLocation NPC = createMainModelLayer("npc", () -> LayerDefinition.create(NpcEntityModel.getTexturedModelData(CubeDeformation.NONE, false), 64, 64));
	public static final ArmorModelSet<ModelLayerLocation> NPC_EQUIPMENT = ModelLayers.PLAYER_ARMOR;

	//~ if <=1.21.11 'TexturedLayerDefinitionProvider' -> 'TexturedModelDataProvider'
	private static ModelLayerLocation createMainModelLayer(String name, ModelLayerRegistry.TexturedLayerDefinitionProvider modelDataProvider) {
		ModelLayerLocation layer = new ModelLayerLocation(AncientWarfare.id(name), "main");
		ModelLayerRegistry.registerModelLayer(layer, modelDataProvider);
		return layer;
	}

	public static void initialize() {
		EntityRenderers.register(AWEntities.PLAYER_NPC, NpcEntityRenderer::new);
		EntityRenderers.register(AWEntities.FACTION_NPC, NpcEntityRenderer::new);
	}
}
