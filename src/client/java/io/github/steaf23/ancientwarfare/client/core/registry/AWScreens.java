package io.github.steaf23.ancientwarfare.client.core.registry;

import io.github.steaf23.ancientwarfare.client.worksite.animalfarm.AnimalFarmScreen;
import io.github.steaf23.ancientwarfare.client.npc.gui.PlayerOwnedNpcScreen;
import io.github.steaf23.ancientwarfare.client.npc.gui.TownHallScreen;
import io.github.steaf23.ancientwarfare.client.structure.gui.AdvancedSpawnerScreen;
import io.github.steaf23.ancientwarfare.core.registry.AWContainerMenus;
import io.github.steaf23.ancientwarfare.structure.menu.AdvancedSpawnerContainerMenu;
import net.minecraft.client.gui.screens.MenuScreens;

public class AWScreens {

	public static void initialize() {
		MenuScreens.<AdvancedSpawnerContainerMenu, AdvancedSpawnerScreen>register(AWContainerMenus.ADVANCED_SPAWNER,  (container, inv, title) -> new AdvancedSpawnerScreen(container, title));
		MenuScreens.register(AWContainerMenus.TOWN_HALL, TownHallScreen::new);
		MenuScreens.register(AWContainerMenus.ANIMAL_FARM, AnimalFarmScreen::new);

		MenuScreens.register(AWContainerMenus.NPC, PlayerOwnedNpcScreen::new);
	}

}
