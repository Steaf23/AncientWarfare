package io.github.steaf23.ancientwarfare.core.registry;

import io.github.steaf23.ancientwarfare.automation.block.worksite.entity.AnimalFarmBlockEntity;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.npc.block.entity.TownHallBlockEntity;
import io.github.steaf23.ancientwarfare.structure.block.entity.advancedspawner.AdvancedSpawnerBlockEntity;
import io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock.WardedBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AWBlockEntities {

	public static final BlockEntityType<AdvancedSpawnerBlockEntity> ADVANCED_SPAWNER = AWBlockEntities.register("advanced_spawner", AdvancedSpawnerBlockEntity::new, AWBlocks.ADVANCED_SPAWNER);

	public static final BlockEntityType<TownHallBlockEntity> TOWN_HALL = AWBlockEntities.register("town_hall", TownHallBlockEntity::new, AWBlocks.TOWN_HALL);

	public static final BlockEntityType<AnimalFarmBlockEntity> ANIMAL_FARM = AWBlockEntities.register("animal_farm", AnimalFarmBlockEntity::new, AWBlocks.ANIMAL_FARM);

	public static final BlockEntityType<WardedBlockEntity> WARDED_BLOCK = AWBlockEntities.register("warded_block", WardedBlockEntity::new, AWBlocks.WARDED_BLOCK);

	public static <T extends BlockEntity> BlockEntityType<T> register(
			String name,
			FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
			Block... blocks
	) {
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, AncientWarfare.id(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}

	public static void initialize() {
	}
}
