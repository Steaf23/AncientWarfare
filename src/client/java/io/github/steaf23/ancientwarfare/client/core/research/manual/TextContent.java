package io.github.steaf23.ancientwarfare.client.core.research.manual;

import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public record TextContent(Component message) implements ManualContent {

	public TextContent(Component message) {
		this.message = message.copy().withStyle(Manual.DEFAULT_STYLE);
	}

	@Override
	public LayoutElement getElement(Screen screen) {
		return new MultiLineTextWidget(message, screen.getFont())
				.setMaxWidth(168);
	}

	@Override
	public boolean split(Screen screen, Layout first, Layout second) {
		return false;
	}

	@Override
	public int minHeight(Screen screen) {
		return screen.getFont().lineHeight;
	}
}
