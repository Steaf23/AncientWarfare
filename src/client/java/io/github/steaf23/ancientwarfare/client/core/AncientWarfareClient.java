package io.github.steaf23.ancientwarfare.client.core;

import io.github.steaf23.ancientwarfare.client.core.registry.AWBlockEntityRenderers;
import io.github.steaf23.ancientwarfare.client.core.registry.AWRenderer;
import io.github.steaf23.ancientwarfare.client.core.registry.AWScreens;
import io.github.steaf23.ancientwarfare.client.datagen.CoinMetalTintSource;
import io.github.steaf23.ancientwarfare.client.npc.gui.SelectedUnitsElement;
import io.github.steaf23.ancientwarfare.client.npc.render.item.CommandBatonOverlay;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.color.item.ItemTintSources;

public class AncientWarfareClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ItemTintSources.ID_MAPPER.put(AncientWarfare.id("coin_metal"), CoinMetalTintSource.CODEC);

		AWScreens.initialize();
		AWRenderer.initialize();
		AWBlockEntityRenderers.initialize();

		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(CommandBatonOverlay::renderOutlineBoxes);

		SelectedUnitsElement element = new SelectedUnitsElement();
		HudElementRegistry.addLast(AncientWarfare.id("selected_units"), element);
	}
}
