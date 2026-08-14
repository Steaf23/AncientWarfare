package io.github.steaf23.ancientwarfare.client.core;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.steaf23.ancientwarfare.client.core.registry.AWBlockEntityRenderers;
import io.github.steaf23.ancientwarfare.client.core.registry.AWRenderer;
import io.github.steaf23.ancientwarfare.client.core.registry.AWScreens;
import io.github.steaf23.ancientwarfare.client.core.research.ResearchScreen;
import io.github.steaf23.ancientwarfare.client.datagen.ModelDatagenHelper;
import io.github.steaf23.ancientwarfare.client.npc.gui.SelectedUnitsElement;
import io.github.steaf23.ancientwarfare.client.npc.render.entity.NpcModel;
import io.github.steaf23.ancientwarfare.client.npc.render.entity.NpcModels;
import io.github.steaf23.ancientwarfare.client.npc.render.item.CommandBatonOverlay;
import io.github.steaf23.ancientwarfare.client.worksite.surveykit.DrawSurveyKitInWorld;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.research.ResearchBookPayload;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//? if <=1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
//?}
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class AncientWarfareClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModelDatagenHelper.initialize();

		AWScreens.initialize();
		AWRenderer.initialize();
		AWBlockEntityRenderers.initialize();

		//?if <=1.21.11 {
		/*BlockRenderLayerMap.putBlock(AWBlocks.ADVANCED_SPAWNER, ChunkSectionLayer.CUTOUT);

		WorldRenderEvents.AFTER_ENTITIES.register(CommandBatonOverlay::renderOutlineBoxes);
		*///?} else {
		LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(CommandBatonOverlay::renderOutlineBoxes);
		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(DrawSurveyKitInWorld::drawSurveyKit);
		//?}

		PayloadTypeRegistry.clientboundPlay().register(ResearchBookPayload.ID, ResearchBookPayload.CODEC);

		ClientPlayNetworking.registerGlobalReceiver(ResearchBookPayload.ID, (payload, context) -> {
			ResearchScreen screen = new ResearchScreen();
			context.client().setScreenAndShow(screen);
		});

		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(AncientWarfare.id("npc_models"),
				(currentReload, taskExecutor, preparationBarrier, reloadExecutor) -> {
					ResourceManager manager = currentReload.resourceManager();
					return CompletableFuture.supplyAsync(() -> {
							Map<Identifier, NpcModel> models = new HashMap<>();
							Map<Identifier, Resource> files = manager.listResources("npcs", _ -> true);
							for (Identifier file : files.keySet()) {
								Identifier npcId = Identifier.fromNamespaceAndPath(file.getNamespace(), file.getPath().substring("npcs/".length(), file.getPath().length() - ".json".length()));
								try {
									NpcModel model = NpcModel.CODEC.decode(JsonOps.INSTANCE, GsonHelper.parse(files.get(file).openAsReader())).getOrThrow().getFirst();
									models.put(npcId, model);
								} catch (IOException e) {
									throw new RuntimeException("Could not parse ancientwarfare npc model with id " + npcId);
								}
							}
							return models;
						}, reloadExecutor)
							.thenCompose(preparationBarrier::wait)
							.thenAcceptAsync(NpcModels::setModels);
				});

		SelectedUnitsElement element = new SelectedUnitsElement();
		HudElementRegistry.addLast(AncientWarfare.id("selected_units"), element);
	}
}
