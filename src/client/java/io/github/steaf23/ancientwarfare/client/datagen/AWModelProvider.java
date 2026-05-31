package io.github.steaf23.ancientwarfare.client.datagen;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.Constant;import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
//?if >1.21.11
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class AWModelProvider extends FabricModelProvider {

	public AWModelProvider(FabricPackOutput output) {
		super(output);

		ItemTintSources.ID_MAPPER.put(AncientWarfare.id("coin_metal"), CoinMetalTintSource.CODEC);
		ItemTintSources.ID_MAPPER.put(AncientWarfare.id("faction"), FactionTintSource.CODEC);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		AncientWarfareBlockModelGenerators blockModels = new AncientWarfareBlockModelGenerators(blockStateModelGenerator);
		blockModels.createTrivialBlock(AWBlocks.ADVANCED_SPAWNER, TexturedModel.CUBE_INNER_FACES);
		blockModels.createTrivialBlock(AWBlocks.TOWN_HALL, TexturedModel.CUBE_TOP_BOTTOM);
		blockModels.createNonTemplateModelBlock(AWBlocks.WARDED_BLOCK);

		blockModels.createRotatableWorksite(AWBlocks.ANIMAL_FARM);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		AncientWarfareItemModelGenerators itemModels = new AncientWarfareItemModelGenerators(itemModelGenerator);
		itemModels.generateFlatItem(AWItems.STEEL_INGOT, ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(AWItems.WOODEN_COMMAND_BATON, ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(AWItems.TOWN_HALL_KEY_DUMMY, ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(AWItems.WARD_SEAL, ModelTemplates.FLAT_ITEM);
		itemModels.generateTintedItem(AWItems.COINS, new CoinMetalTintSource());

		itemModels.generateNpcSpawnerItem(true, "miner");
		itemModels.generateNpcSpawnerItem(false, "soldier");
	}

	private static class AncientWarfareBlockModelGenerators extends BlockModelGenerators {

		private static TextureMapping workSiteTextureMapping(Block workSite, boolean active) {
			return new TextureMapping()
					.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(workSite, "_front"))
					.put(TextureSlot.UP, TextureMapping.getBlockTexture(workSite, "_top"))
					.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(workSite, "_bottom"))
					.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(workSite, "_front"))
					.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(workSite, "_rear"))
					.put(TextureSlot.WEST, TextureMapping.getBlockTexture(workSite, "_left"))
					.put(TextureSlot.EAST, TextureMapping.getBlockTexture(workSite, active ? "_right" : "_right_inactive"));
		}

		public AncientWarfareBlockModelGenerators(BlockModelGenerators generators) {
			super(generators.blockStateOutput, generators.itemModelOutput, generators.modelOutput);
		}

		public void createRotatableWorksite(Block workSite) {
			createHorizontallyRotatedBlock(workSite, TexturedModel.createDefault(b -> workSiteTextureMapping(b, true), ModelTemplates.CUBE));
		}
	}

	private static class AncientWarfareItemModelGenerators extends ItemModelGenerators {

		private static final TextureSlot LAYER3 = TextureSlot.create("layer3");
		private static final ModelTemplate FOUR_LAYERED_ITEMS = createTemplate("generated", TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2, LAYER3);

		private static ModelTemplate createTemplate(String name, TextureSlot... textureSlots) {
			return new ModelTemplate(Optional.of(Identifier.withDefaultNamespace((String)("item/" + name))), Optional.empty(), textureSlots);
		}

		public AncientWarfareItemModelGenerators(ItemModelGenerators generators) {
			super(generators.itemModelOutput, generators.modelOutput);
		}

		//? if <=1.21.11 {
		/*public void generateNpcSpawnerItem(boolean playerOwned, String npcTypePrefix) {
			Item item = playerOwned ? AWItems.NPC_SPAWNER : AWItems.FACTION_NPC_SPAWNER;
			Identifier loc = FOUR_LAYERED_ITEMS.create(item,
					new TextureMapping()
							.put(TextureSlot.LAYER0, AncientWarfare.id("item/npc/spawner_base"))
							.put(TextureSlot.LAYER1, AncientWarfare.id("item/npc/spawner_overlay_bottom"))
							.put(TextureSlot.LAYER2, AncientWarfare.id("item/npc/spawner_overlay_top"))
							.put(LAYER3, AncientWarfare.id("item/npc/spawner_" + npcTypePrefix)),
					this.modelOutput);

			itemModelOutput.accept(item, ItemModelUtils.plainModel(loc));
		}
		*///?} else {
		public void generateNpcSpawnerItem(boolean playerOwned, String npcTypePrefix) {
			Item item = playerOwned ? AWItems.NPC_SPAWNER : AWItems.FACTION_NPC_SPAWNER;
			Material base = new Material(AncientWarfare.id("item/npc/spawner_base"));
			Material overlayBottom = new Material(AncientWarfare.id("item/npc/spawner_overlay_bottom"));
			Material overlayTop = new Material(AncientWarfare.id("item/npc/spawner_overlay_top"));
			Material npcType = new Material(AncientWarfare.id("item/npc/spawner_" + npcTypePrefix));

			int topColor = playerOwned ? 0xffffffff : 0xffed3832;

			Identifier loc = FOUR_LAYERED_ITEMS.create(item,
					new TextureMapping()
							.put(TextureSlot.LAYER0, base)
							.put(TextureSlot.LAYER1, overlayBottom)
							.put(TextureSlot.LAYER2, overlayTop)
							.put(LAYER3, npcType),
					this.modelOutput);

			itemModelOutput.accept(item, ItemModelUtils.tintedModel(loc, new Constant(0xffffffff), new FactionTintSource(), new Constant(topColor)));
		}
		//?}

		public void generateTintedItem(Item item, ItemTintSource source) {
			//~ if <= 1.21.11 'Material' -> 'Identifier'
			Material layer = TextureMapping.getItemTexture(item);
			Identifier model = ModelLocationUtils.getModelLocation(item);
			ModelTemplates.FLAT_ITEM.create(model, TextureMapping.layer0(layer), this.modelOutput);
			this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, source));
		}
	}
}
