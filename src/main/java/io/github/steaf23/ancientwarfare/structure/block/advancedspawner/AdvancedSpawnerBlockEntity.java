package io.github.steaf23.ancientwarfare.structure.block.advancedspawner;

import io.github.steaf23.ancientwarfare.core.menu.BlockEntityMenuProvider;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.ScreenData;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import io.github.steaf23.ancientwarfare.npc.entity.faction.FactionNpc;
import io.github.steaf23.ancientwarfare.structure.menu.AdvancedSpawnerContainerMenu;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
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

import java.util.List;

public class AdvancedSpawnerBlockEntity extends BlockEntity implements BlockEntityMenuProvider {

	private final AdvancedSpawnerLogic logic;

	private @Nullable Component entityDisplayName = null;

	public AdvancedSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(AWBlockEntities.ADVANCED_SPAWNER, pos, state);

		logic = new AdvancedSpawnerLogic();
		computeEntityDisplayName();
	}

	public static void serverTick(Level level, BlockPos pos, BlockState blockState, AdvancedSpawnerBlockEntity spawner) {
		spawner.logic.update((ServerLevel) level, pos, blockState);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState blockState, AdvancedSpawnerBlockEntity spawner) {

	}

	public static void updateSettings(Level level, BlockPos pos, BlockState blockState, AdvancedSpawnerBlockEntity spawner, AdvancedSpawnerSettings settings) {
		spawner.logic.setSettings(settings);
		spawner.updated();
		//TODO: add?

		if (!level.isClientSide()) {
			level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_ALL);
		}
	}

	@Override
	protected void loadAdditional(ValueInput view) {
		logic.readData(view);
		updated();
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
	public @org.jspecify.annotations.Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable(getBlockState().getBlock().getDescriptionId());
	}

	public void computeEntityDisplayName() {
		entityDisplayName = null;

		if (logic == null || logic.settings().groups().isEmpty()) {
			return;
		}

		List<AdvancedSpawnerSettings.SpawnEntry> entries = logic.settings().groups().getFirst().entries();
		if (entries.isEmpty()) {
			return;
		}

		AdvancedSpawnerSettings.SpawnEntry entry = entries.getFirst();
		EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(entry.entity());
		if (type.equals(AWEntities.FACTION_NPC)) {
			entityDisplayName = Component.translatable(FactionNpc.getNpcDescriptionIdFromData(entry.entityData()));
		}
		else {
			entityDisplayName = Component.translatable(type.getDescriptionId());
		}
	}

	public Component displayEntityName() {
		return entityDisplayName;
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

	public void updated() {
		computeEntityDisplayName();
		if (level != null) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(AdvancedSpawnerBlock.TRANSPARENT, logic.settings().transparent()));
		}
	}
}
