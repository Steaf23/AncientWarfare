package io.github.steaf23.ancientwarfare.structure.block.entity.advancedspawner;

import io.github.steaf23.ancientwarfare.core.menu.BlockEntityMenuProvider;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.ScreenData;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.structure.menu.AdvancedSpawnerContainerMenu;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedSpawnerBlockEntity extends BlockEntity implements BlockEntityMenuProvider {

	private final AdvancedSpawnerLogic logic;

	public AdvancedSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(AWBlockEntities.ADVANCED_SPAWNER, pos, state);

		logic = new AdvancedSpawnerLogic();
	}

	public static void serverTick(Level level, BlockPos pos, BlockState blockState, AdvancedSpawnerBlockEntity spawner) {
		spawner.logic.update((ServerLevel) level, pos, blockState);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState blockState, AdvancedSpawnerBlockEntity spawner) {

	}

	public static void updateSettings(Level level, BlockPos pos, BlockState blockState, AdvancedSpawnerBlockEntity spawner, AdvancedSpawnerSettings settings) {
		spawner.logic.setSettings(settings);
		//TODO: add?
		level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_ALL);
	}

	@Override
	protected void loadAdditional(ValueInput view) {
		logic.readData(view);
	}

	@Override
	protected void saveAdditional(ValueOutput view) {
		logic.writeData(view);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		return saveWithoutMetadata(registryLookup);
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable(getBlockState().getBlock().getDescriptionId());
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new AdvancedSpawnerContainerMenu(containerId, playerInventory, this);
	}

	@Override
	public BlockEntityScreenData getScreenOpeningData(ServerPlayer player) {
		TagValueOutput view = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
		logic.writeData(view);

		ByteBuf buf = Unpooled.buffer();
		ScreenData data = new ScreenData();
		AdvancedSpawnerSettings.STREAM_CODEC.encode(buf, logic.settings());
		data.put("settings", buf.array());
		return new BlockEntityScreenData(this, data);
	}

	public AdvancedSpawnerLogic logic() {
		return logic;
	}
}
