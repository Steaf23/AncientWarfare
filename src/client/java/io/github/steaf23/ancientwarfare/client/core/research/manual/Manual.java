package io.github.steaf23.ancientwarfare.client.core.research.manual;

import io.github.steaf23.ancientwarfare.client.core.gui.DefinedSprite;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;

import java.util.List;

public record Manual(List<ManualContent> allContent) {

	public static final Style DEFAULT_STYLE = Style.EMPTY
			.withoutShadow()
			.withColor(0x000000)
			.withFont(new FontDescription.Resource(Minecraft.DEFAULT_FONT));

	public static final DefinedSprite SPRITE = new DefinedSprite(AncientWarfare.id("manual"), 512, 512)
			.addPart("book", 0, 0, 412, 254)
			.addPart("header", 0, 254, 168, 262);

}
