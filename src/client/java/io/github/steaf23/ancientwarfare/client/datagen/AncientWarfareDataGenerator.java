package io.github.steaf23.ancientwarfare.client.datagen;

//? > 1.21.11
import io.github.steaf23.ancientwarfare.client.datagen.structure.StructureConversionProvider;
import io.github.steaf23.ancientwarfare.core.registry.AWStructures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class AncientWarfareDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(AWModelProvider::new);
		pack.addProvider(AWLanguageProvider::new);
		pack.addProvider(FactionProvider::new);
		pack.addProvider(FactionNpcProvider::new);
		pack.addProvider(AWSoundProvider::new);
		pack.addProvider((output, registriesFuture) ->
				new FabricDynamicRegistryProvider(output, registriesFuture) {
					@Override
					public String getName() {
						return "AW Dynamic Registries";
					}

					@Override
					protected void configure(HolderLookup.Provider registries, Entries entries) {
						entries.addAll(registries.lookupOrThrow(Registries.STRUCTURE));
					}
				});

		//? >1.21.11 {
		pack.addProvider(StructureConversionProvider::new);
		//?}
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.STRUCTURE,
				AWStructures::bootstrap);
	}
}
