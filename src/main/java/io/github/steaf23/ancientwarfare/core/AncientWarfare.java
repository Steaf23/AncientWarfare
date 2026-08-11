package io.github.steaf23.ancientwarfare.core;

import io.github.steaf23.ancientwarfare.core.menu.ExtendedContainerMenu;
import io.github.steaf23.ancientwarfare.core.network.ExtendedScreenUpdatePayload;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.registry.AWContainerMenus;
import io.github.steaf23.ancientwarfare.core.registry.AWGeneration;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.registry.AWSounds;
import io.github.steaf23.ancientwarfare.core.registry.AWStructures;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.core.registry.WorksiteRequirements;
import io.github.steaf23.ancientwarfare.core.registry.WorksiteUpgrades;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWActivities;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWMemories;
import io.github.steaf23.ancientwarfare.structure.block.wardedblock.WardedBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AncientWarfare implements ModInitializer {

	public static final String MOD_ID = "ancientwarfare";

	public static final int ONE_SECOND = 20;

	@Override
	public void onInitialize() {
		WorksiteRequirements.initialize();
		WorksiteUpgrades.initialize();
		AWSounds.initialize();
		AWActivities.initialize();
		AWMemories.initialize();
		AWComponents.initialize();
		AWItems.initialize();
		AWBlocks.initialize();
		AWBlockEntities.initialize();
		AWContainerMenus.initialize();
		Factions.initialize();
		AWEntities.initialize();
		AWGeneration.initialize();
		AWStructures.initialize();

		//~ if <=1.21.11 'serverboundPlay' -> 'playC2S' {
		PayloadTypeRegistry.serverboundPlay().register(ExtendedScreenUpdatePayload.ID, ExtendedScreenUpdatePayload.CODEC);
		//~}
		ServerPlayNetworking.registerGlobalReceiver(ExtendedScreenUpdatePayload.ID, (payload, context) -> {
			context.server().execute(() -> {
				AbstractContainerMenu handler = context.player().containerMenu;
				if (handler.containerId == payload.syncId() && handler instanceof ExtendedContainerMenu extendedHandler) {
					extendedHandler.applyClientUpdate(context.player(), payload.update());
				}
			});
		});

		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, be) -> {
			if (!level.isClientSide() && player.isCreative()) {
				if (be instanceof WardedBlockEntity wardedBlockEntity) {
					wardedBlockEntity.restore();
					return false;
				}
			}
			return true;
		});
	}

	public static Identifier id(String name) {
		return Identifier.fromNamespaceAndPath(MOD_ID, name);
	}
}
