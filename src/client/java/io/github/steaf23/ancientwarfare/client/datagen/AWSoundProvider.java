package io.github.steaf23.ancientwarfare.client.datagen;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class AWSoundProvider extends FabricSoundsProvider {

	public AWSoundProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
		exporter.add(AWSounds.COIN_STACK_BREAK, SoundTypeBuilder.of().sound(SoundTypeBuilder.RegistrationBuilder.ofFile(AncientWarfare.id("block/coinstack_break")).volume(0.2f)));
		exporter.add(AWSounds.COIN_STACK_INTERACT, SoundTypeBuilder.of().sound(SoundTypeBuilder.RegistrationBuilder.ofFile(AncientWarfare.id("block/coinstack_interact"))));
	}

	@Override
	public String getName() {
		return "Ancient Warfare Sounds";
	}
}
