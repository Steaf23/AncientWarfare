package io.github.steaf23.ancientwarfare.client.core.gui.components;

import net.minecraft.client.gui.layouts.LinearLayout;

public class FixedSizeLayout extends LinearLayout {

	private final int width;
	private final int height;

	public FixedSizeLayout(int x, int y, int width, int height, Orientation orientation) {
		super(x, y, orientation);

		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
