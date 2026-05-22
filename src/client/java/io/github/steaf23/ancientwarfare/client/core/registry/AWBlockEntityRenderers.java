package io.github.steaf23.ancientwarfare.client.core.registry;

import io.github.steaf23.ancientwarfare.client.automation.render.block.WorksiteBlockEntityRenderer;
import io.github.steaf23.ancientwarfare.client.npc.render.block.TownHallBlockEntityRenderer;
import io.github.steaf23.ancientwarfare.client.structure.gui.render.block.WardedBlockEntityRenderer;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class AWBlockEntityRenderers {

	public static void initialize() {
		BlockEntityRenderers.register(AWBlockEntities.TOWN_HALL, TownHallBlockEntityRenderer::new);
		BlockEntityRenderers.register(AWBlockEntities.ANIMAL_FARM, WorksiteBlockEntityRenderer::new);
		BlockEntityRenderers.register(AWBlockEntities.WARDED_BLOCK, WardedBlockEntityRenderer::new);
	}
}
