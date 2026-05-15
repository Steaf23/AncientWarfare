package io.github.steaf23.ancientwarfare.core.item;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;

public class UnobtainableItem extends Item {

	public UnobtainableItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isEnabled(FeatureFlagSet featureFlagSet) {
		return false;
	}
}
