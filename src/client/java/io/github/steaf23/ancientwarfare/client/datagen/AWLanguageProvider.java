package io.github.steaf23.ancientwarfare.client.datagen;

import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import io.github.steaf23.ancientwarfare.core.versioned.CreativeTabManager;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.tag.FabricTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AWLanguageProvider extends FabricLanguageProvider {

	protected AWLanguageProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(packOutput, registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
		AWTranslationBuilder builder = new AWTranslationBuilder(translationBuilder);
		builder.forBlocks().auto(
				AWBlocks.ADVANCED_SPAWNER, AWBlocks.TOWN_HALL, AWBlocks.ANIMAL_FARM, AWBlocks.INVALID_CONVERSION, AWBlocks.WORKSITE_MARKER
		).forItems().auto(
				AWItems.STEEL_INGOT, AWItems.WOODEN_COMMAND_BATON, AWItems.NPC_SPAWNER, AWItems.FACTION_NPC_SPAWNER, AWItems.WARD_SEAL, AWItems.SURVEY_KIT
		).forEntityTypes().auto(
				AWEntities.PLAYER_NPC, AWEntities.FACTION_NPC
		).forCreativeTabs().auto(
				CreativeTabManager.ITEM_GROUP_KEY, CreativeTabManager.FACTION_NPCS_KEY
		)
				.custom(AWItems.COINS.getDescriptionId(), "%s Coins")
				.custom("metal.ancientwarfare." + CoinMetal.SILVER.getSerializedName(), "Silver")
				.custom("metal.ancientwarfare." + CoinMetal.COPPER.getSerializedName(), "Copper")
				.custom("metal.ancientwarfare." + CoinMetal.GOLD.getSerializedName(), "Gold")
				.custom("metal.ancientwarfare." + CoinMetal.ANCIENT.getSerializedName(), "Ancient")
				.custom("npc.ancientwarfare.empire.soldier", "Empire Spearman")
				.custom("generator.ancientwarfare.structure_debug", "AW Structure Debug")
				.custom("item.ancientwarfare.faction_banner", "%s Banner")
				.custom("item.ancientwarfare.research_book", "Treatise on the Principles of Ruling")
		;
	}

	static class AWTranslationBuilder {

		private final TranslationBuilder translationBuilder;
		private Function<Object, String> keyFunction;

		private AWTranslationBuilder(TranslationBuilder translationBuilder) {
			this.translationBuilder = translationBuilder;
		}

		static AWTranslationBuilder start(TranslationBuilder translationBuilder) {
			return new AWTranslationBuilder(translationBuilder);
		}

		public AWTranslationBuilder forBlocks() {
			this.keyFunction = o -> ((Block) o).getDescriptionId();
			return this;
		}

		public AWTranslationBuilder forItems() {
			this.keyFunction = o -> ((Item) o).getDescriptionId();
			return this;
		}

		public AWTranslationBuilder forEntityTypes() {
			this.keyFunction = o -> ((EntityType<?>) o).getDescriptionId();
			return this;
		}

		public AWTranslationBuilder forTags() {
			this.keyFunction = o -> ((FabricTagKey) o).getTranslationKey();
			return this;
		}

		public AWTranslationBuilder forCreativeTabs() {
			this.keyFunction = o -> "itemGroup." + ((ResourceKey<?>) o).identifier().getPath();
			return this;
		}

		public AWTranslationBuilder custom(String key, String value) {
			translationBuilder.add(key, value);
			return this;
		}

		public AWTranslationBuilder customPair(Tuple<MutableComponent, MutableComponent> pair, String left, String right) {
			translationBuilder.add(((TranslatableContents) pair.getA().getContents()).getKey(), left);
			translationBuilder.add(((TranslatableContents) pair.getB().getContents()).getKey(), right);
			return this;
		}

		private String lastSegment(String key) {
			int idx = key.lastIndexOf('.');
			return (idx == -1) ? key : key.substring(idx + 1);
		}

		private void addAuto(Object obj, Function<String, String> transform) {
			String key = keyFunction.apply(obj);
			String base = lastSegment(key);
			translationBuilder.add(key, transform.apply(ConventionText.snakeCaseToTitleCase(base)));
		}

		@SafeVarargs
		public final <T> AWTranslationBuilder auto(T... objects) {
			return auto(s -> s, objects);
		}

		@SafeVarargs
		public final <T> AWTranslationBuilder auto(Function<String, String> transform, T... objects) {
			for (var o : objects) {
				addAuto(o, transform);
			}
			return this;
		}

		@SafeVarargs
		public final <T> AWTranslationBuilder autoWith(Function<String, String> transform, T... objects) {
			return auto(transform, objects);
		}
	}
}
