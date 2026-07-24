package io.github.steaf23.ancientwarfare.client.core.gui.components;

import io.github.steaf23.ancientwarfare.client.core.gui.SpritePart;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class SpritePartWidget extends AbstractWidget {

	private final SpritePart part;

	public SpritePartWidget(SpritePart part) {
		super(0, 0, part.width(), part.height(), Component.empty());
		this.part = part;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		part.blit(graphics, this.getX(), this.getY());
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
	}

	@Override
	public boolean isActive() {
		return false;
	}
}
