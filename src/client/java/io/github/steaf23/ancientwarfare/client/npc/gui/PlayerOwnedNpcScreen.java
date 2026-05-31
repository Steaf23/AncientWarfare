package io.github.steaf23.ancientwarfare.client.npc.gui;

import io.github.steaf23.ancientwarfare.client.core.gui.ExtendedScreen;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import io.github.steaf23.ancientwarfare.npc.menu.NpcContainerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerOwnedNpcScreen extends ExtendedScreen<NpcContainerMenu> {

	public static final Identifier MENU = AncientWarfare.id("textures/gui/player_npc.png");
	public static final Identifier SKIN_BUTTON = AncientWarfare.id("skin_button");
	public static final Identifier TOGGLE_FOLLOW_BUTTON = AncientWarfare.id("toggle_follow_button");
	public static final Identifier SET_HOME_BUTTON = AncientWarfare.id("set_home_button");
	public static final Identifier CLEAR_HOME_BUTTON = AncientWarfare.id("clear_home_button");
	public static final Identifier RECALL_BUTTON = AncientWarfare.id("recall_button");
	public static final Identifier BUTTON_PRESSED = AncientWarfare.id("button_pressed");

	EditBox nameField;
	List<Button> commandButtons = new ArrayList<>();

	public PlayerOwnedNpcScreen(NpcContainerMenu handler, Inventory inventory, Component titleUnused) {
		super(handler, inventory, Component.empty(), 184, 216);
		this.inventoryLabelY = imageHeight - 93;
		this.inventoryLabelX = 12;
	}

	@Override
	public void updateFromHandler() {
		if (!nameField.getValue().equals(menu.getNpcName()))
		{
			nameField.setValue(menu.getNpcName());
		}
	}

	@Override
	protected void init() {
		super.init();

		nameField = new EditBox(Minecraft.getInstance().font, leftPos + 76, topPos + 4, 88, 14, Component.empty());
		nameField.setBordered(true);
		nameField.setVisible(true);
		nameField.setResponder(menu::setNpcName);
		nameField.setValue(getMenu().getNpcName());
		addRenderableWidget(nameField);

		commandButtons.clear();

		addCommandButton(TOGGLE_FOLLOW_BUTTON,
				Component.literal("Toggle Follow - Can also be done by shift right-clicking outside of the inventory"),
				(btn) -> toggleFollowPlayer());

		addCommandButton(SET_HOME_BUTTON,
				Component.literal("Set Home"),
				(btn) -> setHome());

		addCommandButton(CLEAR_HOME_BUTTON,
				Component.literal("Clear Home"),
				(btn) -> clearHome());

		addCommandButton(RECALL_BUTTON,
				Component.literal("Recall NPC to inventory"),
				(btn) -> recall());

		SpriteIconButton skinBtn = SpriteIconButton.builder(Component.literal("Change Skin"), (btn) -> {}, true)
				.size(10, 10)
				.sprite(new WidgetSprites(SKIN_BUTTON), 10, 10)
				.withTootip()
				.build();
		skinBtn.setPosition(leftPos + 90, topPos + 63);
		addRenderableWidget(skinBtn);
	}

	protected void addCommandButton(Identifier texture, Component tooltip, Button.OnPress action) {

		SpriteIconButton button = SpriteIconButton.builder(tooltip, action, true)
				.size(16, 16)
				.sprite(new WidgetSprites(texture), 16, 16)
				.withTootip()
				.build();
		button.setPosition(leftPos + 14 + (commandButtons.size() * 19), topPos + 98);
		commandButtons.add(button);
		addRenderableWidget(button);
	}

	@Override
	public void drawBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, MENU, leftPos, topPos, 0, 0, 256, 256, 256, 256);

		//?if <=1.21.11 {
		/*InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, leftPos + 69, topPos + 27, leftPos + 100, topPos + 73, 20, 0.0625f, mouseX, mouseY, menu.getNpc());
		*///?} else
		InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, leftPos + 69, topPos + 27, leftPos + 100, topPos + 73, 20, 0.0625f, mouseX, mouseY, menu.getNpc());
	}

	public void toggleFollowPlayer() {
		System.out.println("boop!");
		menu.setFollowingPlayer(!((PlayerOwnedNpc) menu.getNpc()).isFollowingPlayer());
	}

	public void setHome() {
		System.out.println("Setting home");
		menu.setHome();
	}

	public void clearHome() {
		System.out.println("Clearing home");
		menu.clearHome();
	}

	public void recall() {
		menu.recall();
	}
}
