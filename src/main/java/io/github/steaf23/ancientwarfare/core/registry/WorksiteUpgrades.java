package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.automation.block.worksite.WorksiteUpgrade;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class WorksiteUpgrades {

	//~ if <=1.21.11 'create(' -> 'createSimple('
	public static final Registry<WorksiteUpgrade> UPGRADE_REGISTRY = FabricRegistryBuilder.create(ResourceKey.<WorksiteUpgrade>createRegistryKey(AncientWarfare.id("worksite_upgrade")))
			.attribute(RegistryAttribute.SYNCED)
			.buildAndRegister();

	public static void initialize() {

	}

}
