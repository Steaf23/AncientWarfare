package io.github.steaf23.ancientwarfare.client.core;

import io.github.steaf23.ancientwarfare.client.core.registry.AWBlockEntityRenderers;
import io.github.steaf23.ancientwarfare.client.core.registry.AWRenderer;
import io.github.steaf23.ancientwarfare.client.core.registry.AWScreens;
import io.github.steaf23.ancientwarfare.client.datagen.CoinMetalTintSource;
import io.github.steaf23.ancientwarfare.client.npc.gui.SelectedUnitsElement;
import io.github.steaf23.ancientwarfare.client.npc.render.item.CommandBatonOverlay;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//? if <=1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
//?}
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class AncientWarfareClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ItemTintSources.ID_MAPPER.put(AncientWarfare.id("coin_metal"), CoinMetalTintSource.CODEC);

		AWScreens.initialize();
		AWRenderer.initialize();
		AWBlockEntityRenderers.initialize();

		//?if <=1.21.11 {
		/*BlockRenderLayerMap.putBlock(AWBlocks.ADVANCED_SPAWNER, ChunkSectionLayer.CUTOUT);

		WorldRenderEvents.AFTER_ENTITIES.register(CommandBatonOverlay::renderOutlineBoxes);
		*///?} else {
		LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(CommandBatonOverlay::renderOutlineBoxes);
		//?}

		SelectedUnitsElement element = new SelectedUnitsElement();
		HudElementRegistry.addLast(AncientWarfare.id("selected_units"), element);
	}
}
