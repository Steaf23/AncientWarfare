package io.github.steaf23.ancientwarfare.client.core.research;

import io.github.steaf23.ancientwarfare.client.core.gui.ScreenHelper;
import io.github.steaf23.ancientwarfare.client.core.gui.SpritePart;
import io.github.steaf23.ancientwarfare.client.core.research.manual.HeaderContent;
import io.github.steaf23.ancientwarfare.client.core.research.manual.Manual;
import io.github.steaf23.ancientwarfare.client.core.research.manual.ManualContent;
import io.github.steaf23.ancientwarfare.client.core.research.manual.TextContent;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResearchScreen extends Screen {

	private final Manual manual;
	private final List<Component> pages = new ArrayList<>();
	private final int pageCount;

	private LinearLayout layout;
	private int currentPage;

	private MultiLineTextWidget leftPageText;
	private MultiLineTextWidget rightPageText;
	private StringWidget pageCountWidget;

	private PageButton previousPageBtn;
	private PageButton nextPageBtn;

	public ResearchScreen() {
		super(Component.translatable(AWItems.RESEARCH_BOOK.getDescriptionId()));

		this.manual = new Manual(List.of(
				new HeaderContent("Test Header"),
				new TextContent(Component.literal("This is some text\n\n double enter to spice it up. This text can grow outside of the page size, which will need to be fixed in the future, otherwise we cannot really properly add multiple chapters to the manual. Some chapters will be unlockable so we have to rebuild the manual after every unlock..."))));

		Component contents = Component.literal("1 \n2 \n3 \n4 \n5 \n6 \n7 \n8 \n9")
				.withStyle(Manual.DEFAULT_STYLE);
		List<FormattedText> splitContents = font.splitIgnoringLanguage(contents, 10 * 168);
		for (FormattedText pageText : splitContents) {
			MutableComponent page = Component.empty();
			pageText.visit((style, text) -> {
				page.append(Component.literal(text).setStyle(style));
				return Optional.empty();
			}, Style.EMPTY);
			pages.add(page);
		}

		pageCount = (int)Math.ceil(pages.size() / 2.0D);
		currentPage = 0;
	}


	@Override
	protected void init() {
		super.init();
		clearWidgets();

		layout = LinearLayout.horizontal();
		LinearLayout leftPage = LinearLayout.vertical();
		FrameLayout leftFrame = new FrameLayout(168, 200);
		leftPageText = new MultiLineTextWidget(Component.empty(), font)
				.setMaxWidth(168);

		LinearLayout pageLayout = LinearLayout.vertical();
		for (ManualContent content : manual.allContent()) {
			 pageLayout.addChild(content.getElement(this));
		}
//		leftFrame.addChild(leftPageText, LayoutSettings.defaults().alignVerticallyTop());
		leftFrame.addChild(pageLayout, LayoutSettings.defaults().alignVerticallyTop());
		leftPage.addChild(leftFrame);

		LinearLayout rightPage = LinearLayout.vertical();
		FrameLayout rightFrame = new FrameLayout(168, 200);
		rightPageText = new MultiLineTextWidget(Component.empty(), font)
				.setMaxWidth(168);

		rightFrame.addChild(rightPageText, LayoutSettings.defaults().alignVerticallyTop());
		rightPage.addChild(rightFrame);

		previousPageBtn = new PageButton(0, 0, false, btn -> {
			setPage(currentPage - 1);
		}, true);
		leftPage.addChild(previousPageBtn);

		nextPageBtn = new PageButton(0, 0, true, btn -> {
			setPage(currentPage + 1);
		}, true);
		rightPage.addChild(nextPageBtn, LayoutSettings.defaults().alignHorizontallyRight());

		layout.addChild(leftPage, LayoutSettings.defaults().paddingHorizontal(15));
		layout.addChild(rightPage, LayoutSettings.defaults().paddingHorizontal(15));

		layout.arrangeElements();
		ScreenHelper.centerLayout(layout, width, height);
		layout.visitWidgets(this::addRenderableWidget);

		StringWidget title = new StringWidget(getTitle(), font);
		SpritePart bookPart = Manual.SPRITE.part("book");
		int bookStartY = (height / 2) - bookPart.height() / 2;
		title.setWidth(bookPart.width());
		title.setX(width / 2 - (font.width(getTitle()) / 2));
		title.setY(bookStartY - 18);
		addRenderableWidget(title);

		pageCountWidget = new StringWidget(Component.literal("0/0"), font);
		int bookEndY = (height / 2) + bookPart.height() / 2;
		pageCountWidget.setWidth(bookPart.width());
		pageCountWidget.setY(bookEndY + 8);
		addRenderableWidget(pageCountWidget);

		setPage(currentPage);
	}

	void setPage(int page) {
		currentPage = Math.clamp(page, 0, pageCount - 1);
		leftPageText.setMessage(pages.isEmpty() ? Component.empty() : pages.get(currentPage * 2));
		rightPageText.setMessage(pages.size() < pageCount * 2 && currentPage == pageCount - 1 ? Component.empty() : pages.get(currentPage * 2 + 1));

		Component pageCounter = Component.literal((currentPage + 1) + "/" + (pageCount));
		pageCountWidget.setMessage(pageCounter);
		pageCountWidget.setX(width / 2 - (font.width(pageCounter) / 2));

		previousPageBtn.visible = currentPage != 0;
		nextPageBtn.visible = currentPage < pageCount - 1;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);

		ScreenHelper.blitPartLayoutCenter(graphics, Manual.SPRITE.part("book"), layout);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (super.keyPressed(event)) {
			return true;
		}

		if (this.minecraft.options.keyInventory.matches(event)) {
			this.onClose();
			return true;
		}
		return false;
	}
}
