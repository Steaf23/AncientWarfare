package io.github.steaf23.ancientwarfare.client.core.research.manual;

import io.github.steaf23.ancientwarfare.client.core.gui.SpritePart;
import io.github.steaf23.ancientwarfare.client.core.gui.components.SpritePartWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public record HeaderContent(Component text) implements ManualContent {

	public HeaderContent(String text) {
		this(Component.literal(text).setStyle(Manual.DEFAULT_STYLE));
	}

	@Override
	public LayoutElement getElement(Screen screen) {
		LinearLayout layout = LinearLayout.vertical();
		StringWidget textWidget = new StringWidget(text, screen.getFont());
		SpritePart headerSprite = Manual.SPRITE.part("header");
		SpritePartWidget banner = new SpritePartWidget(headerSprite);

		layout.addChild(textWidget);
		layout.addChild(banner, LayoutSettings.defaults().paddingTop(3).paddingBottom(5));
		return layout;
	}

	@Override
	public boolean split(Screen screen, Layout first, Layout second) {
		return false;
	}

	@Override
	public int minHeight(Screen screen) {
		return 0;
	}
}
