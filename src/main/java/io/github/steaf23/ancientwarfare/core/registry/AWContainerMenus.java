package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.automation.menu.AnimalFarmContainerMenu;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.EntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.ExtendedContainerMenu;
import io.github.steaf23.ancientwarfare.npc.menu.NpcContainerMenu;
import io.github.steaf23.ancientwarfare.npc.menu.TownHallContainerMenu;
import io.github.steaf23.ancientwarfare.structure.menu.AdvancedSpawnerContainerMenu;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class AWContainerMenus {

	public static MenuType<AdvancedSpawnerContainerMenu> ADVANCED_SPAWNER = registerBlockEntityHandlerType("advanced_spawner", AdvancedSpawnerContainerMenu::new);
	public static MenuType<NpcContainerMenu> NPC = registerEntityHandlerType("npc", NpcContainerMenu::new);
	public static MenuType<TownHallContainerMenu> TOWN_HALL = registerBlockEntityHandlerType("town_hall", TownHallContainerMenu::new);
	public static MenuType<AnimalFarmContainerMenu> ANIMAL_FARM = registerBlockEntityHandlerType("animal_farm", AnimalFarmContainerMenu::new);

	public static <T extends ExtendedContainerMenu> MenuType<T> registerBlockEntityHandlerType(String name, BlockEntityHandlerTypeFactory<T> factory) {

		ExtendedMenuType<T, BlockEntityScreenData> type = new ExtendedMenuType<>(factory::create, BlockEntityScreenData.PACKET_CODEC);
		return Registry.register(BuiltInRegistries.MENU, AncientWarfare.id(name), type);
	}

	public static <T extends ExtendedContainerMenu> MenuType<T> registerEntityHandlerType(String name, EntityHandlerTypeFactory<T> factory) {

		ExtendedMenuType<T, EntityScreenData> type = new ExtendedMenuType<>(factory::create, EntityScreenData.PACKET_CODEC);
		return Registry.register(BuiltInRegistries.MENU, AncientWarfare.id(name), type);
	}

	@FunctionalInterface
	public interface BlockEntityHandlerTypeFactory<T> {

		T create(int syncId, Inventory playerInventory, BlockEntityScreenData screenData);
	}

	@FunctionalInterface
	public interface EntityHandlerTypeFactory<T> {

		T create(int syncId, Inventory playerInventory, EntityScreenData screenData);
	}

	public static void initialize() {

	}

}
