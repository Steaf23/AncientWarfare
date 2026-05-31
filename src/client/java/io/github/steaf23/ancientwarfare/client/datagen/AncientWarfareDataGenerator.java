package io.github.steaf23.ancientwarfare.client.datagen;

//? > 1.21.11
import io.github.steaf23.ancientwarfare.client.datagen.structure.AWStructureConversionProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AncientWarfareDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(AWModelProvider::new);
		pack.addProvider(AWLanguageProvider::new);
		pack.addProvider(FactionProvider::new);
		pack.addProvider(FactionNpcProvider::new);

		//? >1.21.11 {
		pack.addProvider(AWStructureConversionProvider::new);
		//?}
	}
}
