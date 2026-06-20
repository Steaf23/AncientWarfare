package io.github.steaf23.ancientwarfare.client.structure.gui;

import io.github.steaf23.ancientwarfare.client.core.gui.ScreenHelper;
import io.github.steaf23.ancientwarfare.client.core.gui.components.OptionList;
import io.github.steaf23.ancientwarfare.client.core.gui.components.SpinBoxWidget;
import io.github.steaf23.ancientwarfare.client.core.gui.components.SubScreen;
import io.github.steaf23.ancientwarfare.structure.block.entity.advancedspawner.AdvancedSpawnerSettings;
import io.github.steaf23.ancientwarfare.structure.menu.AdvancedSpawnerContainerMenu;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdvancedSpawnerScreen extends SubScreen implements MenuAccess<AdvancedSpawnerContainerMenu> {

	final AdvancedSpawnerContainerMenu container;
	AdvancedSpawnerSettings settings = null;
	AdvancedSpawnerSettings.Builder newSettingsBuilder;
	LinearLayout mainLayout;
	ScrollableLayout scrollableSettingsLayout;
	ScrollableLayout scrollableLayout;
	List<Layout> groupLayouts = new ArrayList<>();

	public AdvancedSpawnerScreen(AdvancedSpawnerContainerMenu container, Component title) {
		super(title);
		this.container = container;
		this.newSettingsBuilder = container.settingsBuilder;

		container.setClientSender(ClientPlayNetworking::send);
//		container.setUpdateNotifier(this::updateFromHandler);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean isInGameUi() {
		return true;
	}

	@Override
	protected void init() {
		super.init();
		clearWidgets();

		settings = newSettingsBuilder.build();

		mainLayout = LinearLayout.vertical();
		mainLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Advanced Spawner"), font), LayoutSettings.defaults().padding(4));

		mainLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Settings: "), font), LayoutSettings.defaults().padding(4));

		LinearLayout settingsLayout = LinearLayout.vertical().spacing(2);
		LinearLayout booleanInputs = LinearLayout.vertical().spacing(2);
		addCheckbox(booleanInputs,
				Checkbox.builder(Component.literal("Light Sensitive").withColor(CommonColors.WHITE), font)
						.tooltip(Tooltip.create(Component.literal("Does not spawn Monsters if it's bright around the spawner."))),
				settings.lightSensitive(), newSettingsBuilder::lightSensitive);

		addCheckbox(booleanInputs,
				Checkbox.builder(Component.literal("React to Redstone").withColor(CommonColors.WHITE), font)
						.tooltip(Tooltip.create(Component.literal("Only spawns creatures when powered by redstone."))),
				settings.redstoneSensitive(), newSettingsBuilder::redstoneSensitive);

		addCheckbox(booleanInputs,
				Checkbox.builder(Component.literal("Transparent").withColor(CommonColors.WHITE), font)
						.tooltip(Tooltip.create(Component.literal("Makes the spawner invisible."))),
				settings.transparent(), newSettingsBuilder::transparent);

		addCheckbox(booleanInputs,
				Checkbox.builder(Component.literal("Debug").withColor(CommonColors.WHITE), font)
						.tooltip(Tooltip.create(Component.literal("Spawn creatures even when the player is in creative mode."))),
				settings.debugMode(), newSettingsBuilder::debugMode);

		settingsLayout.addChild(booleanInputs, LayoutSettings.defaults().paddingTop(2));
		GridLayout numberInputs = new GridLayout().rowSpacing(2);
		addInput(0, numberInputs,
				Component.literal("Req. Player Range"),
				Component.literal("Players have to be within range to trigger the spawner."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::playerRange).minValue(1).maxValue(128).startValue(settings.playerRange()));

		addInput(1, numberInputs,
				Component.literal("Max Nearby Creatures"),
				Component.literal("Number of allowed creatures nearby to still trigger the spawner."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::maximumAllowedNearbyEntities).maxValue(128).startValue(settings.maximumAllowedNearbyEntities()));

		addInput(2, numberInputs,
				Component.literal("Range of Nearby Creatures"),
				Component.literal("Distance between a creature and the spawner to be considered nearby."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::mobRange).minValue(1).maxValue(128).startValue(settings.mobRange()));

		addInput(3, numberInputs,
				Component.literal("Spawn Range"),
				Component.literal("Total range around the spawner that determines where creatures can spawn."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::spawnRange).maxValue(128).startValue(settings.spawnRange()));

		addInput(4, numberInputs,
				Component.literal("Minimum Delay"),
				Component.literal("Minimum delay that it can take for the spawner to trigger again (in game ticks)."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::minDelayTicks).minValue(1).startValue(settings.minDelayTicks()));

		addInput(5, numberInputs,
				Component.literal("Maximum Delay"),
				Component.literal("Maximum delay that it can take for the spawner to trigger again (in game ticks)."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::maxDelayTicks).minValue(1).startValue(settings.maxDelayTicks()));

		addInput(6, numberInputs,
				Component.literal("Xp to drop"),
				Component.literal("Amount of Experience Points to drop when the spawner is broken."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::xpToDrop).minValue(0).startValue(settings.xpToDrop()));

		addInput(7, numberInputs,
				Component.literal("Vertical Spawn Offset"),
				Component.literal("Used to spawn creatures above or below from the spawner."),
				SpinBoxWidget.defaultIntegerBox(font, newSettingsBuilder::spawnYOffset).minValue(-128).maxValue(128).startValue(settings.spawnYOffset()));

//		addInput(2, numberInputs,
//				Component.literal("Block hardness"),
//				Component.literal("Determines the time it takes to break the spawner."),
//				SpinBoxWidget.defaultDoubleBox(font, value -> container.settings(). = value).minValue(1).maxValue(128).startValue(container.settings().mobRange));

		settingsLayout.addChild(numberInputs, LayoutSettings.defaults().paddingVertical(2));
		scrollableSettingsLayout = new ScrollableLayout(minecraft, settingsLayout, 200);
		scrollableSettingsLayout.setMaxHeight(80);
		//? if > 1.21.11 {
		scrollableSettingsLayout.setMinHeight(80);
		//?}
		mainLayout.addChild(scrollableSettingsLayout, LayoutSettings.defaults().padding(2).alignHorizontallyCenter());
		mainLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Spawn groups: "), font), LayoutSettings.defaults().padding(4));

		LinearLayout scrollContents = LinearLayout.vertical();

		groupLayouts.clear();
		LinearLayout groupsLayout = LinearLayout.vertical().spacing(6);

		int groupIdx = 0;
		for (AdvancedSpawnerSettings.SpawnGroup group : newSettingsBuilder.groups()) {
			Layout groupLayout = groupLayout(groupIdx, group);
			groupsLayout.addChild(groupLayout);
			groupLayouts.add(groupLayout);
			groupIdx++;
		}

		scrollContents.addChild(groupsLayout, LayoutSettings.defaults().padding(5));
		scrollContents.addChild(new Button.Builder(Component.literal("Add group"), (btn) -> {
			AdvancedSpawnerSettings.SpawnGroup group = new AdvancedSpawnerSettings.SpawnGroup(1,
					List.of(AdvancedSpawnerSettings.SpawnEntry.standard()));

			newSettingsBuilder.addGroup(group);
			minecraft.setScreen(this);
		})
				.size(100, 12)
				.build(), LayoutSettings.defaults().paddingHorizontal(5).paddingBottom(5));

		scrollableLayout = new ScrollableLayout(minecraft, scrollContents, 200);
		mainLayout.addChild(scrollableLayout, LayoutSettings.defaults().padding(2));
		scrollableLayout.setMaxHeight(80);
		//? if > 1.21.11 {
		scrollableLayout.setMinHeight(80);
		//?}
		mainLayout.arrangeElements();
		ScreenHelper.centerLayout(mainLayout, width, height);

		mainLayout.visitWidgets(this::addRenderableWidget);
	}

	private Layout groupLayout(int groupIndex, AdvancedSpawnerSettings.SpawnGroup spawnGroup) {
		LinearLayout main = LinearLayout.vertical().spacing(1);
		LinearLayout groupInfo = LinearLayout.horizontal().spacing(3);
		groupInfo.addChild(new StringWidget(ScreenHelper.inventoryText("Group weight"), font), LayoutSettings.defaults().alignVerticallyMiddle());
		groupInfo.addChild(SpinBoxWidget.defaultIntegerBox(font, val -> newSettingsBuilder.groupWeight(groupIndex, val)).minValue(1).startValue(spawnGroup.weight()));
		main.addChild(groupInfo);

		GridLayout listLayout = new GridLayout();
		listLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Type"), font), 0, 0);
		listLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Min"), font), 0, 1, LayoutSettings.defaults().alignHorizontallyCenter());
		listLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Max"), font), 0, 2, LayoutSettings.defaults().alignHorizontallyCenter());
		listLayout.addChild(new StringWidget(ScreenHelper.inventoryText("Total"), font), 0, 3, LayoutSettings.defaults().alignHorizontallyCenter());


		int entryIndex = 0;
		for (AdvancedSpawnerSettings.SpawnEntry entry : spawnGroup.entries()) {
			int indexToEdit = entryIndex;
			listLayout.addChild(entityButton(entry.entity(), (btn, newEntity) -> {
				newSettingsBuilder.groupEntryEntity(groupIndex, indexToEdit, newEntity, new CompoundTag());
			}), entryIndex + 1, 0);

			listLayout.addChild(SpinBoxWidget.defaultIntegerBox(font, val -> {
				newSettingsBuilder.groupEntryMin(groupIndex, indexToEdit, val);
			}).minValue(1).maxValue(10).startValue(entry.min()), entryIndex + 1, 1, LayoutSettings.defaults().paddingHorizontal(2));

			listLayout.addChild(SpinBoxWidget.defaultIntegerBox(font, val -> {
				newSettingsBuilder.groupEntryMax(groupIndex, indexToEdit, val);
			}).minValue(1).maxValue(10).startValue(entry.max()), entryIndex + 1, 2, LayoutSettings.defaults().paddingHorizontal(2));

			listLayout.addChild(SpinBoxWidget.defaultIntegerBox(font, val -> {
				newSettingsBuilder.groupEntryTotal(groupIndex, indexToEdit, val);
			}).minValue(0).maxValue(10).startValue(entry.total()), entryIndex + 1, 3, LayoutSettings.defaults().paddingHorizontal(2));

			listLayout.addChild(Button.builder(Component.literal("-"), (btn) -> {
								newSettingsBuilder.removeGroupEntry(groupIndex, indexToEdit);
								minecraft.setScreen(this);
							})
							.size(12, 12)
							.build()
					, entryIndex + 1, 4, LayoutSettings.defaults().paddingHorizontal(2));
			entryIndex++;
		}
		listLayout.addChild(Button.builder(Component.literal("+"), (btn) -> {
					newSettingsBuilder.addGroupEntry(groupIndex, AdvancedSpawnerSettings.SpawnEntry.standard());
					minecraft.setScreen(this);
				})
				.size(50, 12)
				.build(), entryIndex + 1, 0);

		main.addChild(listLayout);
		return main;
	}

	private void addInput(int row, GridLayout toLayout, Component text, Component tooltip, LayoutElement widget) {
		StringWidget label = new StringWidget(text, font);
		label.setTooltip(Tooltip.create(tooltip));
		toLayout.addChild(label, row, 0);
		toLayout.addChild(widget, row, 1, LayoutSettings.defaults().paddingHorizontal(4));
	}

	private void addCheckbox(LinearLayout toLayout, Checkbox.Builder boxBuilder, boolean startValue, Consumer<Boolean> valueCallback) {
		toLayout.addChild(boxBuilder.selected(startValue).onValueChange((box, v) -> valueCallback.accept(v)).build(),
				LayoutSettings.defaults().paddingHorizontal(4));
	}

	@Override
	public void drawBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
		ScreenHelper.extractInventoryBackground(graphics, mainLayout);
		ScreenHelper.extractScrollAreaBackground(graphics, scrollableSettingsLayout);
		ScreenHelper.extractScrollAreaBackground(graphics, scrollableLayout);
		graphics.enableScissor(scrollableLayout.getX(), scrollableLayout.getY(), scrollableLayout.getX() + scrollableLayout.getWidth(), scrollableLayout.getY() + scrollableLayout.getHeight());
		for (Layout g : groupLayouts) {
			ScreenHelper.extractInnerInventoryBackground(graphics, g);
		}
		graphics.disableScissor();
	}

	@Override
	public void onClose() {
		sendSettingsToServer();
		super.onClose();
	}

	@Override
	public boolean keyPressed(KeyEvent keyEvent) {
		if (super.keyPressed(keyEvent)) {
			return true;
		}
		if (this.minecraft.options.keyInventory.matches(keyEvent)) {
			this.onClose();
			return true;
		}

		return true;
	}

	public void sendSettingsToServer() {
		container.sendSettingsToServer(newSettingsBuilder.build());
//		ClientPlayNetworking.send(new ExtendedScreenUpdatePayload<>(handler.settings().posForClientSync, handler.settings(), AWStructureScreenHandlers.ADVANCED_SPAWNER_PAYLOAD));
	}

	@Override
	public @NotNull AdvancedSpawnerContainerMenu getMenu() {
		return container;
	}

	private static List<OptionList.Option> allEntityOptions() {
		return BuiltInRegistries.ENTITY_TYPE.stream()
				.filter(e -> e.getCategory() != MobCategory.MISC)
				.map(e -> {
					Identifier loc = BuiltInRegistries.ENTITY_TYPE.getKey(e);
					return new OptionList.Option(loc, e.getDescription(), Tooltip.create(Component.literal(loc.toString())));
				})
				.toList();
	}

	private Button entityButton(Identifier selected, BiConsumer<Button, Identifier> onSelected) {
		Button selectButton = new Button.Builder(BuiltInRegistries.ENTITY_TYPE.getValue(selected).getDescription(), (btn) -> {
			Screen current = this;

			OptionList newScreen = new OptionList(allEntityOptions(), (clickedId) -> {
				onSelected.accept(btn, clickedId);
				minecraft.setScreen(current);
			});
			minecraft.setScreen(newScreen);
		})
				.size(50, 12)
				.build();

		selectButton.setTooltip(Tooltip.create(Component.literal("Click to change")));
		return selectButton;
	}
}
