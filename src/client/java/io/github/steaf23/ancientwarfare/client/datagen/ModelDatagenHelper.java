package io.github.steaf23.ancientwarfare.client.datagen;

import io.github.steaf23.ancientwarfare.client.datagen.faction.FactionTintSource;
import io.github.steaf23.ancientwarfare.client.structure.gui.render.block.factionbanner.FactionBannerSpecialRenderer;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class ModelDatagenHelper {

	public static void initialize() {
		ItemTintSources.ID_MAPPER.put(AncientWarfare.id("coin_metal"), CoinMetalTintSource.CODEC);
		SelectItemModelProperties.ID_MAPPER.put(AncientWarfare.id("coin_metal"), CoinMetalSelectProperty.TYPE);
		ItemTintSources.ID_MAPPER.put(AncientWarfare.id("faction"), FactionTintSource.CODEC);
		SpecialModelRenderers.ID_MAPPER.put(AncientWarfare.id("faction_banner"), FactionBannerSpecialRenderer.Unbaked.MAP_CODEC);
	}

}
