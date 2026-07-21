package io.github.steaf23.ancientwarfare.structure.menu;

import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.ExtendedContainerMenu;
import io.github.steaf23.ancientwarfare.core.menu.sync.DataApplier;
import io.github.steaf23.ancientwarfare.core.registry.AWContainerMenus;
import io.github.steaf23.ancientwarfare.structure.block.advancedspawner.AdvancedSpawnerBlockEntity;
import io.github.steaf23.ancientwarfare.structure.block.advancedspawner.AdvancedSpawnerSettings;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedSpawnerContainerMenu extends ExtendedContainerMenu {

	public AdvancedSpawnerSettings.Builder settingsBuilder;

	AdvancedSpawnerBlockEntity spawnerBlockEntity;

	private static final DataApplier<AdvancedSpawnerContainerMenu, AdvancedSpawnerSettings> SETTINGS = new DataApplier<>((ctx, settings) -> {
		AdvancedSpawnerBlockEntity be = ctx.receiver().spawnerBlockEntity;
		AdvancedSpawnerBlockEntity.updateSettings(ctx.player().level(), be.getBlockPos(), be.getBlockState(), be, settings);
	}, (clientCtx, data) -> {
		clientCtx.receiver().settingsBuilder = AdvancedSpawnerSettings.builderOf(data);
	}, AdvancedSpawnerSettings.STREAM_CODEC, AdvancedSpawnerSettings.class);

	// Server constructor
	public AdvancedSpawnerContainerMenu(int syncId, Inventory playerInventory, AdvancedSpawnerBlockEntity be) {
		super(AWContainerMenus.ADVANCED_SPAWNER, syncId, playerInventory);
		this.settingsBuilder = AdvancedSpawnerSettings.builderOf(be.logic().settings());
		this.spawnerBlockEntity = be;
	}

	public AdvancedSpawnerContainerMenu(int syncId, Inventory playerInventory, BlockEntityScreenData screenData) {
		super(AWContainerMenus.ADVANCED_SPAWNER, syncId, playerInventory);

		applyOpeningData(screenData.getOpeningData());

		this.spawnerBlockEntity = (AdvancedSpawnerBlockEntity)getPlayer().level().getBlockEntity(screenData.getPos());
	}

	public void sendSettingsToServer(AdvancedSpawnerSettings newSettings) {
		sendDataToServer("settings", newSettings);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int slot) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public @Nullable DataApplier<?, ?> getDataApplier(String key) {
		if (!key.equals("settings")) {
			return null;
		}
		return SETTINGS;
	}
}
