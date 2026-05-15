package io.github.steaf23.ancientwarfare.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AncientWarfareDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(AWModelProvider::new);
		pack.addProvider(AWLanguageProvider::new);
		pack.addProvider(AWStructureProvider::new);
	}
}
