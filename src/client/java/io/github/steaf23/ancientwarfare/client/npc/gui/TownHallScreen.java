package io.github.steaf23.ancientwarfare.client.npc.gui;

import io.github.steaf23.ancientwarfare.client.core.gui.ExtendedScreen;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.npc.menu.TownHallContainerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class TownHallScreen extends ExtendedScreen<TownHallContainerMenu> {

	public static final Identifier MENU = AncientWarfare.id("textures/gui/town_hall.png");

	public TownHallScreen(TownHallContainerMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);

		this.inventoryLabelY = imageHeight - 93;
	}

	@Override
	public void drawBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, MENU, leftPos, topPos, 0, 0, 256, 256, 256, 256);
	}

	@Override
	public void updateFromHandler() {

	}
}
