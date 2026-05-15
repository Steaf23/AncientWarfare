package io.github.steaf23.ancientwarfare.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

public class AWStructureProvider implements DataProvider {

	private final FabricPackOutput output;
	private final CompletableFuture<HolderLookup.Provider> lookup;

	AWStructureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		this.output = output;
		this.lookup = registryLookup;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return null;
	}

	@Override
	public String getName() {
		return "Ancient Warfare Structure (.aws) Conversions";
	}
}
