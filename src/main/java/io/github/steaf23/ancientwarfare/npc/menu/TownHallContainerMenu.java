package io.github.steaf23.ancientwarfare.npc.menu;

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

public class TownHallContainerMenu extends ExtendedContainerMenu {
	private final Container townHall;

	public TownHallContainerMenu(int containerId, Inventory playerInv, BlockEntityScreenData screenData) {
		this(containerId, playerInv, new SimpleContainer(27));
		applyOpeningData(screenData.getOpeningData());

	}

	// Server ctor
	public TownHallContainerMenu(int containerId, Inventory playerInv, Container townHall) {
		super(AWContainerMenus.TOWN_HALL, containerId, playerInv);
		this.townHall = townHall;

		int invStartX = 8;
		int invStartY = 18;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlot(new Slot(townHall, y * 9 + x, invStartX + 18 * x, invStartY + 18 * y));
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
