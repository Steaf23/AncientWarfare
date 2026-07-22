package io.github.steaf23.ancientwarfare.client.core.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DefinedSprite {

	private final Identifier textureId;
	private final int width;
	private final int height;
	private final Map<String, SpritePart> parts = new HashMap<>();

	public DefinedSprite(Identifier textureId, int width, int height) {
		this.textureId = textureId;
		this.width = width;
		this.height = height;
	}

	public DefinedSprite addPart(String name, int startX, int startY, int endX, int endY) {
		parts.put(name, new SpritePart(this, startX, startY, endX, endY));
		return this;
	}

	public SpritePart part(String name) {
		return parts.getOrDefault(name, new SpritePart(this, 0, 0, 0, 0));
	}

	public void blitPart(GuiGraphicsExtractor graphics, String partName, int startX, int startY) {
		part(partName).blit(graphics, startX, startY);
	}

	public Identifier sprite() {
		return textureId;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}
}
