package io.github.steaf23.ancientwarfare.client.automation.gui;

import io.github.steaf23.ancientwarfare.worksite.menu.AnimalFarmContainerMenu;
import io.github.steaf23.ancientwarfare.client.core.gui.ExtendedScreen;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AnimalFarmScreen extends ExtendedScreen<AnimalFarmContainerMenu> {

	public static final Identifier MENU = AncientWarfare.id("textures/gui/town_hall.png");

	public AnimalFarmScreen(AnimalFarmContainerMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	public void updateFromHandler() {

	}

	@Override
	public void drawBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, MENU, leftPos, topPos, 0, 0, 256, 256, 256, 256);
	}
}
