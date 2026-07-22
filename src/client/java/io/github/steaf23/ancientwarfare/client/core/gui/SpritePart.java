package io.github.steaf23.ancientwarfare.client.core.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public record SpritePart(DefinedSprite sprite, int startX, int startY, int endX, int endY) {

	public void blit(GuiGraphicsExtractor graphics, int x1, int y1) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite.sprite(),
				sprite.width(), sprite.height(),
				startX, startY,
				x1, y1,
				width(), height());
	}

	public int width() {
		return endX - startX;
	}

	public int height() {
		return endY - startY;
	}

	public int centerX() {
		return startX + width() / 2;
	}

	public int centerY() {
		return startY + height() / 2;
	}
}
