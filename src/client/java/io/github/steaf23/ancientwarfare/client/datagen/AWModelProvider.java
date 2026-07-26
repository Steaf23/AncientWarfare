package io.github.steaf23.ancientwarfare.client.datagen;

import com.google.gson.JsonObject;
import com.mojang.math.Quadrant;
import io.github.steaf23.ancientwarfare.client.structure.gui.render.block.factionbanner.FactionBannerSpecialRenderer;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import io.github.steaf23.ancientwarfare.structure.block.CoinStackBlock;
import io.github.steaf23.ancientwarfare.structure.item.CoinItem;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
//?if >1.21.11
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AWModelProvider extends FabricModelProvider {

	public AWModelProvider(FabricPackOutput output) {
		super(output);

		ModelDatagenHelper.initialize();
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		AncientWarfareBlockModelGenerators blockModels = new AncientWarfareBlockModelGenerators(blockStateModelGenerator);
		blockModels.createAirLikeBlock(AWBlocks.INVALID_CONVERSION, new Material(AncientWarfare.id("block/invalid_conversion")));
		blockModels.registerSimpleFlatItemModel(AWBlocks.INVALID_CONVERSION);
		blockModels.createTrivialBlock(AWBlocks.ADVANCED_SPAWNER, TexturedModel.CUBE_INNER_FACES);
		blockModels.createTrivialBlock(AWBlocks.TOWN_HALL, TexturedModel.CUBE_TOP_BOTTOM);
		blockModels.createNonTemplateModelBlock(AWBlocks.WARDED_BLOCK);
		blockModels.createCoinStack();
		blockModels.createRotatableWorksite(AWBlocks.ANIMAL_FARM);
		blockModels.createFactionBanner();
		blockModels.createTrivialBlock(AWBlocks.WORKSITE_MARKER, TexturedModel.CUBE_INNER_FACES);
		blockModels.createSurveyStake(AWBlocks.SURVEY_STAKE);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		AncientWarfareItemModelGenerators itemModels = new AncientWarfareItemModelGenerators(itemModelGenerator);
		itemModels.generateFlatItem(AWItems.RESEARCH_BOOK, ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(AWItems.STEEL_INGOT, ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(AWItems.WOODEN_COMMAND_BATON, ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(AWItems.TOWN_HALL_KEY_DUMMY, ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(AWItems.WARD_SEAL, ModelTemplates.FLAT_ITEM);
		itemModels.generateCoinMetalItem(AWItems.COINS);
		itemModels.generateFlatItem(AWItems.SURVEY_KIT, ModelTemplates.FLAT_ITEM);

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

		public void createCoinStack() {
			this.blockStateOutput.accept(MultiVariantGenerator.dispatch(AWBlocks.COIN_STACK)
					.with(PropertyDispatch.initial(CoinStackBlock.METAL, CoinStackBlock.STACK_SIZE)
							.generate((metal, size) -> {
								Identifier id = generateCoinStack(metal, size);

								return variants(
										plainModel(id),
										plainModel(id).withYRot(Quadrant.R90),
										plainModel(id).withYRot(Quadrant.R180),
										plainModel(id).withYRot(Quadrant.R270)
										);
							})));
		}

		public void createSurveyStake(Block block) {
			this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(AncientWarfare.id("block/survey_stake"))));
		}

		public Identifier generateCoinStack(CoinMetal metal, CoinStackBlock.StackSize size) {
			Identifier modelId = AncientWarfare.id(
					"block/coin_stack/" +
							metal.getSerializedName() + "_" +
							size.getSerializedName());
			Identifier parent = AncientWarfare.id(
					"block/coin_stack_" + size.getSerializedName()
			);

			JsonObject json = new JsonObject();

			json.addProperty("parent", parent.toString());

			JsonObject textures = new JsonObject();
			textures.addProperty("top", "ancientwarfare:block/" + metal.getSerializedName() + "_coin_stack_top");
			textures.addProperty("side", "ancientwarfare:block/" + metal.getSerializedName() + "_coin_stack_side");
			textures.addProperty("side_offset", "ancientwarfare:block/" + metal.getSerializedName() + "_coin_stack_side_offset");
			textures.addProperty("bottom", "ancientwarfare:block/" + metal.getSerializedName() + "_coin_stack_bottom");

			json.add("textures", textures);

			modelOutput.accept(modelId, () -> json);
			return modelId;
		}

		public void createFactionBanner() {
			MultiVariant model = plainVariant(ModelLocationUtils.decorateBlockModelLocation("banner"));
			blockStateOutput.accept(createSimpleBlock(AWBlocks.FACTION_BANNER, model));
			blockStateOutput.accept(createSimpleBlock(AWBlocks.FACTION_WALL_BANNER, model));
			itemModelOutput.accept(AWItems.FACTION_BANNER, ItemModelUtils.specialModel(
					ModelLocationUtils.decorateItemModelLocation("template_banner"),
					BannerRenderer.TRANSFORMATIONS.freeTransformations(0),
					new FactionBannerSpecialRenderer.Unbaked(BannerBlock.AttachmentType.GROUND)));
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

		public void generateCoinMetalItem(CoinItem item) {
			List<SelectItemModel.SwitchCase<CoinMetal>> cases = new ArrayList<>();
			for (CoinMetal metal : CoinMetal.ALL) {
				ItemModel.Unbaked model = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_" + metal.getSerializedName(), ModelTemplates.FLAT_ITEM));
				cases.add(new SelectItemModel.SwitchCase<>(List.of(metal), model));
			}
			this.itemModelOutput.accept(item, ItemModelUtils.select(new CoinMetalSelectProperty(), cases.getFirst().model(), cases));
		}
	}
}
