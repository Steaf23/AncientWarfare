package io.github.steaf23.ancientwarfare.automation.block.worksite.entity;

import io.github.steaf23.ancientwarfare.automation.block.worksite.WorksiteUpgrade;
import io.github.steaf23.ancientwarfare.automation.menu.AnimalFarmContainerMenu;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class AnimalFarmBlockEntity extends AbstractWorksiteBlockEntity {

	NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);

	public AnimalFarmBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AWBlockEntities.ANIMAL_FARM, blockPos, blockState);
	}

	@Override
	protected @NonNull Component getDefaultName() {
		return Component.translatable(AWBlocks.ANIMAL_FARM.getDescriptionId());
	}

	@Override
	protected @NonNull NonNullList<ItemStack> getItems() {
		return stacks;
	}

	@Override
	protected void setItems(@NonNull NonNullList<ItemStack> nonNullList) {
		stacks = nonNullList;
	}

	@Override
	protected @NonNull AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory) {
		return new AnimalFarmContainerMenu(containerId, inventory, this);
	}

	@Override
	public int getContainerSize() {
		return stacks.size();
	}

	@Override
	public BlockEntityScreenData getScreenOpeningData(@NonNull ServerPlayer serverPlayer) {
		return new BlockEntityScreenData(this, null);
	}

	@Override
	public Set<WorksiteUpgrade> possibleUpgrades() {
		return Set.of();
	}
}
