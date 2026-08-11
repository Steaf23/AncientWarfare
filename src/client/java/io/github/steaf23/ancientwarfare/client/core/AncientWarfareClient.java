package io.github.steaf23.ancientwarfare.client.core;

import io.github.steaf23.ancientwarfare.client.core.registry.AWBlockEntityRenderers;
import io.github.steaf23.ancientwarfare.client.core.registry.AWRenderer;
import io.github.steaf23.ancientwarfare.client.core.registry.AWScreens;
import io.github.steaf23.ancientwarfare.client.core.research.ResearchScreen;
import io.github.steaf23.ancientwarfare.client.datagen.ModelDatagenHelper;
import io.github.steaf23.ancientwarfare.client.npc.gui.SelectedUnitsElement;
import io.github.steaf23.ancientwarfare.client.npc.render.item.CommandBatonOverlay;
import io.github.steaf23.ancientwarfare.client.worksite.surveykit.DrawSurveyKitInWorld;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.research.ResearchBookPayload;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//? if <=1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
//?}
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class AncientWarfareClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModelDatagenHelper.initialize();

		AWScreens.initialize();
		AWRenderer.initialize();
		AWBlockEntityRenderers.initialize();

		//?if <=1.21.11 {
		/*BlockRenderLayerMap.putBlock(AWBlocks.ADVANCED_SPAWNER, ChunkSectionLayer.CUTOUT);

		WorldRenderEvents.AFTER_ENTITIES.register(CommandBatonOverlay::renderOutlineBoxes);
		*///?} else {
		LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(CommandBatonOverlay::renderOutlineBoxes);
		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(DrawSurveyKitInWorld::drawSurveyKit);
		//?}

		PayloadTypeRegistry.clientboundPlay().register(ResearchBookPayload.ID, ResearchBookPayload.CODEC);

		ClientPlayNetworking.registerGlobalReceiver(ResearchBookPayload.ID, (payload, context) -> {
			ResearchScreen screen = new ResearchScreen();
			context.client().setScreenAndShow(screen);
		});

		SelectedUnitsElement element = new SelectedUnitsElement();
		HudElementRegistry.addLast(AncientWarfare.id("selected_units"), element);
	}
}
