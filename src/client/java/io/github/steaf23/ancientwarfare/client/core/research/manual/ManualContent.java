package io.github.steaf23.ancientwarfare.client.core.research.manual;

import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;

public interface ManualContent {

	 LayoutElement getElement(Screen screen);

	 boolean split(Screen screen, Layout first, Layout second);

	 int minHeight(Screen screen);
}
