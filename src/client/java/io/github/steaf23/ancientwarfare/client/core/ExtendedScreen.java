package io.github.steaf23.ancientwarfare.client.core;

import io.github.steaf23.ancientwarfare.core.menu.ExtendedContainerMenu;
import io.github.steaf23.ancientwarfare.core.menu.ScreenUpdate;
import io.github.steaf23.ancientwarfare.core.network.ExtendedScreenUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public abstract class ExtendedScreen<Menu extends ExtendedContainerMenu> extends AbstractContainerScreen<Menu> {

	public ExtendedScreen(Menu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);

		// client packets can only be sent from... the client. So use the screen to register a way to send packets from the handler for easier usability
		menu.setClientSender(ClientPlayNetworking::send);
		menu.setUpdateNotifier(this::updateFromHandler);
	}

	public ExtendedScreen(Menu menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
		super(menu, inventory, title, imageWidth, imageHeight);

		// client packets can only be sent from... the client. So use the screen to register a way to send packets from the handler for easier usability
		menu.setClientSender(ClientPlayNetworking::send);
		menu.setUpdateNotifier(this::updateFromHandler);
	}

	public void sendUpdateToServer(ScreenUpdate update) {
		ClientPlayNetworking.send(new ExtendedScreenUpdatePayload(menu.containerId, update));
	}

	public abstract void updateFromHandler();

	@Override
	public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
		super.extractRenderState(graphics, mouseX, mouseY, deltaTicks);

		this.extractTooltip(graphics, mouseX, mouseY);
	}
}
