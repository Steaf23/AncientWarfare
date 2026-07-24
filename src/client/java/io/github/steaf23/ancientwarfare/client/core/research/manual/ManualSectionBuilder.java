package io.github.steaf23.ancientwarfare.client.core.research.manual;

import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;

public class ManualSectionBuilder {

	public void addHeader(String text) {
		addHeader(Component.literal(text).setStyle(Manual.DEFAULT_STYLE));
	}

	public void addHeader(Component text) {
		LinearLayout layout = LinearLayout.horizontal();
	}

	public void addTextBody(Component text) {

	}

	public void addImage(Identifier texture, int width, int height) {

	}

	public void addRecipe(Recipe<?> recipe) {

	}
}
