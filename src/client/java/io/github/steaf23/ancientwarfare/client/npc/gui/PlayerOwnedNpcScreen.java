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
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
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

	private float entityYRot = 0.0f;

	public PlayerOwnedNpcScreen(NpcContainerMenu handler, Inventory inventory, Component titleUnused) {
		super(handler, inventory, Component.empty(), 181, 235);
		this.inventoryLabelY = 141;
		this.inventoryLabelX = 8;
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

		nameField = new EditBox(Minecraft.getInstance().font, leftPos + 93, topPos + 110, 88, 14, Component.empty());
		nameField.setBordered(false);
		nameField.setVisible(true);
		nameField.setResponder(menu::setNpcName);
		nameField.setValue(getMenu().getNpcName());
		nameField.setCanLoseFocus(true);
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
		skinBtn.setPosition(leftPos + 78, topPos + 91);
		addRenderableWidget(skinBtn);
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
		IO.println("slotId=" + slotId + ", button=" + buttonNum);
		super.slotClicked(slot, slotId, buttonNum, containerInput);
	}

	protected void addCommandButton(Identifier texture, Component tooltip, Button.OnPress action) {

		SpriteIconButton button = SpriteIconButton.builder(tooltip, action, true)
				.size(16, 16)
				.sprite(new WidgetSprites(texture), 16, 16)
				.withTootip()
				.build();
		button.setPosition(leftPos + 8 + (commandButtons.size() * 19), topPos + 117);
		commandButtons.add(button);
		addRenderableWidget(button);
	}

	@Override
	public void drawBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, MENU, leftPos, topPos, 0, 0, 256, 256, 256, 256);

		entityYRot += deltaTicks / 20.0f;

		NpcPortrait.extract(graphics, leftPos + 80, topPos + 103, 100, 64.0f, 1.1f, menu.getNpc());
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

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (!nameField.isFocused()) {
			return super.keyPressed(event);
		}

		if (Minecraft.getInstance().options.keyInventory.matches(event)) {
			return false;
		}

		return super.keyPressed(event);
	}

	public void recall() {
		menu.recall();
	}
}
