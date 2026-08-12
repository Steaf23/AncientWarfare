package io.github.steaf23.ancientwarfare.client.datagen;

//? > 1.21.11
import io.github.steaf23.ancientwarfare.client.datagen.faction.FactionNpcDefinitions;
import io.github.steaf23.ancientwarfare.client.datagen.faction.FactionNpcProvider;
import io.github.steaf23.ancientwarfare.client.datagen.faction.FactionProvider;
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

		FactionNpcDefinitions npcs = new FactionNpcDefinitions();

		pack.addProvider(AWModelProvider::new);
		pack.addProvider((output, registriesFuture) -> new AWLanguageProvider(npcs, output, registriesFuture));
		pack.addProvider((output, registriesFuture) -> new FactionProvider(npcs.factions(), output, registriesFuture));
		pack.addProvider((output, registriesFuture) -> new FactionNpcProvider(npcs, output, registriesFuture));
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
