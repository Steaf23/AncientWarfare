package io.github.steaf23.ancientwarfare.client.core.gui.components;

import io.github.steaf23.ancientwarfare.client.core.gui.ScreenHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OptionList extends SubScreen {

	LinearLayout mainLayout;
	ScrollableLayout scrollable;
	LinearLayout optionsLayout;
	EditBox filterBox;
	Consumer<Identifier> clickResponder;

	final List<Option> allOptions;
	final List<Option> filtered;

	boolean isInitialized = false;

	String filter = "";

	public OptionList(List<Option> options, @NotNull Consumer<Identifier> clickResponder) {
		super(Component.empty());
		this.allOptions = options;
		this.filtered = new ArrayList<>(options);
		this.clickResponder = clickResponder;
	}

	public void applyFilter(String filter) {
		this.filter = filter;
		filtered.clear();
		for (Option o : allOptions) {
			if (o.id.getPath().replace("_", " ").contains(this.filter.toLowerCase())) {
				filtered.add(o);
			}
		}

		if (!isInitialized) {
			return;
		}
		init();
	}

	@Override
	protected void init() {
		super.init();
		clearWidgets();
		isInitialized = true;
		setup();
		mainLayout.visitWidgets(this::addRenderableWidget);
	}

	private void setup() {
		mainLayout = LinearLayout.vertical();
		mainLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Select Creature"), font), LayoutSettings.defaults().padding(5));

		LinearLayout filterLayout = LinearLayout.horizontal();
		filterLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Filter: "), font), LayoutSettings.defaults().alignVerticallyMiddle());
		filterBox = new EditBox(font, 0, 0, 100, 12, Component.empty());
		filterBox.setBordered(true);
		filterBox.setVisible(true);
		filterBox.setValue(filter);
		filterBox.setResponder(this::applyFilter);
		filterLayout.addChild(filterBox);
		mainLayout.addChild(filterLayout, LayoutSettings.defaults().alignHorizontallyCenter().paddingHorizontal(5));
		optionsLayout = LinearLayout.vertical();
		optionsLayout.spacing(1);

		int idx = 0;
		for (Option o : filtered) {
			LayoutSettings padding = LayoutSettings.defaults();
			if (filtered.size() - 1 == idx) {
				padding.paddingBottom(1);
			}

			if (idx == 0) {
				padding.paddingTop(1);
			}

			optionsLayout.addChild(Button.builder(o.text, (btn) -> clickResponder.accept(o.id))
					.tooltip(o.tooltip)
					.size(110, 12)
					.build(), padding);
			idx++;
		}

		if (filtered.isEmpty()) {
			optionsLayout.addChild(new StringWidget(Component.literal("No creature like: \"" + filter + "\""), font), LayoutSettings.defaults().padding(5));
		}

		scrollable = new ScrollableLayout(Minecraft.getInstance(), optionsLayout, 200);
		mainLayout.addChild(scrollable, LayoutSettings.defaults().alignHorizontallyCenter().padding(5));
		scrollable.setMaxHeight(175);
		//? >1.21.11
		scrollable.setMinHeight(175);
		scrollable.setMinWidth(0);
		mainLayout.arrangeElements();
		ScreenHelper.centerLayout(mainLayout, width, height);
	}

	@Override
	public void drawBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
		ScreenHelper.extractInventoryBackground(graphics, mainLayout);
		ScreenHelper.extractScrollAreaBackground(graphics, scrollable);
	}

	public record Option(Identifier id, Component text, Tooltip tooltip) {
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean isInGameUi() {
		return true;
	}
}
