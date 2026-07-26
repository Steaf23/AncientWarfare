package io.github.steaf23.ancientwarfare.worksite.menu;

import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.ExtendedContainerMenu;
import io.github.steaf23.ancientwarfare.core.menu.sync.DataApplier;
import io.github.steaf23.ancientwarfare.core.registry.AWContainerMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class AnimalFarmContainerMenu extends ExtendedContainerMenu {
	private final Container animalFarm;

	// Client constructor
	public AnimalFarmContainerMenu(int containerId, Inventory playerInv, BlockEntityScreenData beScreenData) {
		this(containerId, playerInv, new SimpleContainer(27));
		applyOpeningData(beScreenData.getOpeningData());
	}

	public AnimalFarmContainerMenu(int containerId, Inventory playerInv, Container animalFarm) {
		super(AWContainerMenus.ANIMAL_FARM, containerId, playerInv);
		this.animalFarm = animalFarm;

		int invStartX = 8;
		int invStartY = 18;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlot(new Slot(animalFarm, y * 9 + x, invStartX + 18 * x, invStartY + 18 * y));
			}
		}

		addStandardInventorySlots(playerInv, 8, 84);
	}

	@Override
	public @Nullable DataApplier<?, ?> getDataApplier(String key) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
