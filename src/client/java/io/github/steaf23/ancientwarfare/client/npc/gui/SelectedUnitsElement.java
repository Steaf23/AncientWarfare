package io.github.steaf23.ancientwarfare.client.npc.gui;

import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.util.EntityHelper;
import io.github.steaf23.ancientwarfare.core.util.MoreText;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.item.CommandBaton;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.UUID;

public class SelectedUnitsElement implements HudElement {

	@Override
	public void extractRenderState(GuiGraphicsExtractor gui, DeltaTracker tickCounter) {
		Player player = Minecraft.getInstance().player;

		if (player == null) {
			return;
		}

		ItemStack batonStack = EntityHelper.getItemFromEitherHand(player, AWItems.COMMAND_BATONS);
		if (batonStack.isEmpty()) {
			return;
		}

		Font font = Minecraft.getInstance().font;

		extractSelectedUnits(gui, font, player, batonStack);

		extractCurrentTarget(gui, font, player);
	}

	private static void extractSelectedUnits(GuiGraphicsExtractor gui, Font font, Player player, ItemStack batonStack) {
		List<UUID> selectedUnits = CommandBaton.getSelectedEntities(batonStack);

		int xStart = 10;
		int yStart = 10;

		gui.text(font, "Commanding: ", xStart, yStart, CommonColors.SOFT_YELLOW, true);
		int idx = 0;
		for (UUID id : selectedUnits) {
			Entity e = player.level().getEntity(id);
			if (!(e instanceof BaseNpc npc)) {
				continue;
			}

			int lineSpacing = font.lineHeight + 2;

			gui.text(font,
					Component.empty()
							.append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
							.append(npc.getDisplayName()),
					xStart, yStart + lineSpacing * (idx + 1), CommonColors.WHITE, true);
			idx++;
		}
	}

	private static void extractCurrentTarget(GuiGraphicsExtractor context, Font textRenderer, Player player) {
		Component targetText = Component.empty();

		HitResult lastResult = CommandBaton.getLastHitResult();
		if (lastResult != null) {
			targetText = switch (lastResult.getType()) {
				case MISS -> Component.empty();
				case BLOCK -> MoreText.blockPos(((BlockHitResult)lastResult).getBlockPos());
				case ENTITY -> ((EntityHitResult)lastResult).getEntity().getDisplayName();
			};
		}

		Component header = Component.literal("Target:");
		int headerWidth = textRenderer.width(header);
		int targetWidth = textRenderer.width(targetText);

		context.text(textRenderer, header, context.guiWidth() - headerWidth - 10, 10, CommonColors.SOFT_YELLOW, true);
		context.text(textRenderer, targetText, context.guiWidth() - targetWidth - 10, 10 + textRenderer.lineHeight + 2, CommonColors.WHITE, true);
	}
}
