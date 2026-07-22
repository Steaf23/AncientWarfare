package io.github.steaf23.ancientwarfare.client.core.gui;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
//? if <=1.21.11
//import net.minecraft.client.gui.render.state.GuiTextRenderState;

public class ScreenHelper {

	public static final Style INVENTORY_STYLE = Style.EMPTY.withColor(0xff404040).withShadowColor(0);

	public static final Identifier GUI_BACKGROUND = AncientWarfare.id("inventory_background");
	public static final Identifier INNER_GUI_BACKGROUND = AncientWarfare.id("inner_inventory_background");
	public static final Identifier SCROLLABLE_BACKGROUND = AncientWarfare.id("scrollable_background");


	public static void extractInventoryBackground(GuiGraphicsExtractor graphics, Layout layout) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, GUI_BACKGROUND, layout.getX() - 4, layout.getY() - 4, layout.getWidth() + 8, layout.getHeight() + 8);
	}

	public static void extractScrollAreaBackground(GuiGraphicsExtractor graphics, Layout layout) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLABLE_BACKGROUND, layout.getX(), layout.getY() - 1, layout.getWidth() + 1, layout.getHeight() + 2);
	}

	public static void extractInnerInventoryBackground(GuiGraphicsExtractor graphics, Layout layout) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, INNER_GUI_BACKGROUND, layout.getX() - 3, layout.getY() - 3, layout.getWidth() + 6, layout.getHeight() + 6);
	}

	public static void blitPartLayoutCenter(GuiGraphicsExtractor graphics, SpritePart sprite, Layout layout) {
		int layoutCenterX = layout.getX() + layout.getWidth() / 2;
		int layoutCenterY = layout.getY() + layout.getHeight() / 2;
		sprite.blit(graphics, layoutCenterX - sprite.width() / 2, layoutCenterY - sprite.height() / 2);
	}

	public static void centerLayout(Layout layout, int screenWidth, int screenHeight) {
		layout.setX(screenWidth / 2 - layout.getWidth() / 2);
		layout.setY(screenHeight / 2 - layout.getHeight() / 2);
	}

	public static void drawText(Font font, GuiGraphicsExtractor graphics, Component text, int xStart, int yStart, int color, boolean dropShadow) {
		//? if <=1.21.11 {
		/*graphics.guiRenderState.submitText(new GuiTextRenderState(
				font,
				text.getVisualOrderText(),
				new Matrix3x2f(graphics.pose()),
				xStart, yStart,
				color,
				0, dropShadow,
				false,
				graphics.scissorStack.peek()));
		*///?} else
		graphics.text(font, text, xStart, yStart, color, dropShadow);
	}

	public static Component inventoryText(String text) {
		return Component.literal(text).withStyle(INVENTORY_STYLE);
	}
}
