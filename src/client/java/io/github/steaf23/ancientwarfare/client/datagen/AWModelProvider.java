package io.github.steaf23.ancientwarfare.client.datagen;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
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
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.time.temporal.TemporalUnit;
import java.util.Optional;

public class AWModelProvider extends FabricModelProvider {

	public AWModelProvider(FabricPackOutput output) {
		super(output);

		ItemTintSources.ID_MAPPER.put(AncientWarfare.id("coin_metal"), CoinMetalTintSource.CODEC);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		AncientWarfareBlockModelGenerators blockModels = new AncientWarfareBlockModelGenerators(blockStateModelGenerator);
		blockModels.createTrivialBlock(AWBlocks.ADVANCED_SPAWNER, TexturedModel.CUBE_INNER_FACES);
		blockModels.createTrivialBlock(AWBlocks.TOWN_HALL, TexturedModel.CUBE_TOP_BOTTOM);

		blockModels.createRotatableWorksite(AWBlocks.ANIMAL_FARM);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		AncientWarfareItemModelGenerators itemModels = new AncientWarfareItemModelGenerators(itemModelGenerator);
		itemModels.generateFlatItem(AWItems.STEEL_INGOT, ModelTemplates.FLAT_ITEM);
		itemModels.generateFlatItem(AWItems.WOODEN_COMMAND_BATON, ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateFlatItem(AWItems.TOWN_HALL_KEY_DUMMY, ModelTemplates.FLAT_HANDHELD_ITEM);
		itemModels.generateTintedItem(AWItems.COIN, new CoinMetalTintSource());

		itemModels.generateNpcSpawnerItem("miner");
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

		private void createTrialSpawner() {
			Block block = Blocks.TRIAL_SPAWNER;
			TextureMapping textureMapping = TextureMapping.trialSpawner(block, "_side_inactive", "_top_inactive");
			TextureMapping textureMapping2 = TextureMapping.trialSpawner(block, "_side_active", "_top_active");
			TextureMapping textureMapping3 = TextureMapping.trialSpawner(block, "_side_active", "_top_ejecting_reward");
			TextureMapping textureMapping4 = TextureMapping.trialSpawner(block, "_side_inactive_ominous", "_top_inactive_ominous");
			TextureMapping textureMapping5 = TextureMapping.trialSpawner(block, "_side_active_ominous", "_top_active_ominous");
			TextureMapping textureMapping6 = TextureMapping.trialSpawner(block, "_side_active_ominous", "_top_ejecting_reward_ominous");
			Identifier identifier = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.create(block, textureMapping, this.modelOutput);
			MultiVariant multiVariant = BlockModelGenerators.plainVariant(identifier);
			MultiVariant multiVariant2 = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active", textureMapping2, this.modelOutput));
			MultiVariant multiVariant3 = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward", textureMapping3, this.modelOutput));
			MultiVariant multiVariant4 = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_inactive_ominous", textureMapping4, this.modelOutput));
			MultiVariant multiVariant5 = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active_ominous", textureMapping5, this.modelOutput));
			MultiVariant multiVariant6 = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward_ominous", textureMapping6, this.modelOutput));
			this.registerSimpleItemModel(block, identifier);
			this.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(BlockStateProperties.TRIAL_SPAWNER_STATE, BlockStateProperties.OMINOUS).generate((trialSpawnerState, boolean_) -> switch (trialSpawnerState) {
				default -> throw new MatchException(null, null);
				case TrialSpawnerState.INACTIVE, TrialSpawnerState.COOLDOWN -> {
					if (boolean_.booleanValue()) {
						yield multiVariant4;
					}
					yield multiVariant;
				}
				case TrialSpawnerState.WAITING_FOR_PLAYERS, TrialSpawnerState.ACTIVE, TrialSpawnerState.WAITING_FOR_REWARD_EJECTION -> {
					if (boolean_.booleanValue()) {
						yield multiVariant5;
					}
					yield multiVariant2;
				}
				case TrialSpawnerState.EJECTING_REWARD -> boolean_ != false ? multiVariant6 : multiVariant3;
			})));
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

		public void generateNpcSpawnerItem(String npcTypePrefix) {
			Material base = new Material(AncientWarfare.id("item/npc/spawner_base"));
			Material overlayBottom = new Material(AncientWarfare.id("item/npc/spawner_overlay_bottom"));
			Material overlayTop = new Material(AncientWarfare.id("item/npc/spawner_overlay_top"));
			Material npcType = new Material(AncientWarfare.id("item/npc/spawner_" + npcTypePrefix));

			Identifier loc = FOUR_LAYERED_ITEMS.create(AWItems.NPC_SPAWNER,
					new TextureMapping()
							.put(TextureSlot.LAYER0, base)
							.put(TextureSlot.LAYER1, overlayBottom)
							.put(TextureSlot.LAYER2, overlayTop)
							.put(LAYER3, npcType),
					this.modelOutput);

			itemModelOutput.accept(AWItems.NPC_SPAWNER, ItemModelUtils.plainModel(loc));
		}

		public void generateTintedItem(Item item, ItemTintSource source) {
			Material layer = TextureMapping.getItemTexture(item);
			Identifier model = ModelLocationUtils.getModelLocation(item);
			ModelTemplates.FLAT_ITEM.create(model, TextureMapping.layer0(layer), this.modelOutput);
			this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, source));
		}
	}
}
