package io.github.steaf23.ancientwarfare.client.core.gui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class SubScreen extends Screen {

	protected SubScreen(Component title) {
		super(title);
	}

	public void drawBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {};

	//?if <=1.21.11 {
	/*@Override
	public void renderBackground(GuiGraphicsExtractor guiGraphics, int i, int j, float f) {
		super.renderBackground(guiGraphics, i, j, f);
		drawBackground(guiGraphics, i, j, f);
	}
	*///?} else {
	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		drawBackground(graphics, mouseX, mouseY, a);
	}
	//?}
}
