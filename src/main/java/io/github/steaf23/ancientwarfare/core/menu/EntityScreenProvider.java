package io.github.steaf23.ancientwarfare.core.menu;

//? if <= 1.21.11 {
/*import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

public interface EntityScreenProvider extends ExtendedScreenHandlerFactory<EntityScreenData> {

}
*///?} else {

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;

public interface EntityScreenProvider extends ExtendedMenuProvider<EntityScreenData> {

}
//?}