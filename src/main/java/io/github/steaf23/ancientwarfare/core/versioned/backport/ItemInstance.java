package io.github.steaf23.ancientwarfare.core.versioned.backport;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;

public interface ItemInstance extends TypedInstance<Item>, DataComponentGetter {
	String FIELD_ID = "id";
	String FIELD_COUNT = "count";
	String FIELD_COMPONENTS = "components";

	int count();

	default int getMaxStackSize() {
		return this.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
	}
}
