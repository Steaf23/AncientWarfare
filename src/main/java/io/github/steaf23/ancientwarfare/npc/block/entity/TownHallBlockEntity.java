package io.github.steaf23.ancientwarfare.npc.block.entity;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityMenuProvider;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import io.github.steaf23.ancientwarfare.npc.menu.TownHallContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class TownHallBlockEntity extends BaseContainerBlockEntity implements BlockEntityMenuProvider {

	private NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);

	public float keyRotationDegrees = 0;

	int counter = 0;

	public TownHallBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AWBlockEntities.TOWN_HALL, blockPos, blockState);

		if (level != null && !level.isClientSide()) {
			broadcastUpkeep();
		}
	}

	public static void serverTick(Level level, BlockPos pos, BlockState blockState, TownHallBlockEntity townHall) {
		townHall.counter++;
		if (townHall.counter >= AncientWarfare.ONE_SECOND * 3) {
			townHall.counter = 0;

			townHall.broadcastUpkeep();
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState blockState, TownHallBlockEntity townHall) {
		townHall.keyRotationDegrees += 1.0f % 360f;
	}

	public void broadcastUpkeep() {
		for (PlayerOwnedNpc npc : level.getEntitiesOfClass(PlayerOwnedNpc.class, AABB.ofSize(getBlockPos().getCenter(), 30.0, 30.0, 30.0))) {
			npc.setAutoUpkeepBlock(getBlockPos());
		}
	}

	@Override
	public @NonNull BlockEntityScreenData getScreenOpeningData(@NonNull ServerPlayer player) {
		return new BlockEntityScreenData(this, null);
	}

	@Override
	protected @NotNull AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory) {
		return new TownHallContainerMenu(containerId, inventory, this);
	}

	@Override
	public @NotNull NonNullList<ItemStack> getItems() {
		return stacks;
	}

	@Override
	protected void setItems(@NonNull NonNullList<ItemStack> nonNullList) {
		stacks = nonNullList;
	}

	@Override
	protected void saveAdditional(@NonNull ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);

		ContainerHelper.saveAllItems(valueOutput, stacks);
	}

	@Override
	protected @NotNull Component getDefaultName() {
		return Component.translatable(getBlockState().getBlock().getDescriptionId());
	}

	@Override
	protected void loadAdditional(@NonNull ValueInput valueInput) {
		super.loadAdditional(valueInput);

		ContainerHelper.loadAllItems(valueInput, stacks);
	}

	@Override
	public int getContainerSize() {
		return 27;
	}
}
