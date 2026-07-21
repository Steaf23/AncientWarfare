package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.automation.block.worksite.WorksiteBlock;
import io.github.steaf23.ancientwarfare.automation.block.worksite.entity.AnimalFarmBlockEntity;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.versioned.CreativeTabManager;
import io.github.steaf23.ancientwarfare.npc.block.TownHallBlock;
import io.github.steaf23.ancientwarfare.structure.block.advancedspawner.AdvancedSpawnerBlock;
import io.github.steaf23.ancientwarfare.structure.block.CoinStackBlock;
import io.github.steaf23.ancientwarfare.structure.block.factionbanner.FactionBannerBlock;
import io.github.steaf23.ancientwarfare.structure.block.factionbanner.FactionWallBannerBlock;
import io.github.steaf23.ancientwarfare.structure.block.invalidconversionblock.InvalidConversionBlock;
import io.github.steaf23.ancientwarfare.structure.block.wardedblock.WardedBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class AWBlocks {
	public static final Block INVALID_CONVERSION = AWBlocks.register("invalid_conversion", InvalidConversionBlock::new, BlockBehaviour.Properties.of()
			.noOcclusion()
			.noCollision(), true);

	public static final Block ADVANCED_SPAWNER = AWBlocks.register("advanced_spawner", AdvancedSpawnerBlock::new, BlockBehaviour.Properties.of()
			.noOcclusion(), true);

	public static final Block TOWN_HALL = register("town_hall", TownHallBlock::new, BlockBehaviour.Properties.of(), true);

	public static final Block COIN_STACK = register("coin_stack", CoinStackBlock::new, BlockBehaviour.Properties.of()
			.noOcclusion()
			.sound(AWSounds.COIN_STACK_BLOCK), false);

	public static final Block ANIMAL_FARM = register("animal_farm", properties -> new WorksiteBlock(properties, AnimalFarmBlockEntity::new), BlockBehaviour.Properties.of(), true);

	public static final Block WARDED_BLOCK = register("warded_block", WardedBlock::new, BlockBehaviour.Properties.of()
			.noOcclusion()
			.strength(-1.0f, 3600000.0f)
			.noLootTable()
			.isValidSpawn(Blocks::never)
			.noTerrainParticles(), false);

	public static final Block FACTION_BANNER = register("faction_banner", FactionBannerBlock::new, BlockBehaviour.Properties.of(), false);
	public static final Block FACTION_WALL_BANNER = register("faction_wall_banner", FactionWallBannerBlock::new, BlockBehaviour.Properties.of(), false);

	public static Block register(String name, BlockBehaviour.Properties settings, boolean withItem) {
		return register(name, Block::new, settings, withItem);
	}

	public static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings, boolean withItem) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, AncientWarfare.id(name));
		settings = settings.setId(key);
		Block block = factory.apply(settings);

		if (withItem) {
			ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, AncientWarfare.id(name));
			BlockItem item = new BlockItem(block, new Item.Properties().overrideDescription(block.getDescriptionId()).setId(itemKey));
			Registry.register(BuiltInRegistries.ITEM, itemKey, item);
			CreativeTabManager.addItemsToModTab(group -> group.addItem(item));
		}
		return Registry.register(BuiltInRegistries.BLOCK, key, block);
	}

	public static void initialize() {

	}
}
