package io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock;

import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.structure.block.WardedBlockData;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class WardedBlockEntity extends BlockEntity {

	private CapturedBlock blockToRestore = CapturedBlock.EMPTY;
	private @NotNull WardedBlockData wardingData = new WardedBlockData(null, null);

	public WardedBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(AWBlockEntities.WARDED_BLOCK, worldPosition, blockState);
	}

	public void setBlockToRestore(BlockState state, CompoundTag tag) {
		this.blockToRestore = new CapturedBlock(state, tag);
		if (level != null) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	public void setBlockToRestore(CapturedBlock block) {
		this.blockToRestore = block;
	}

	public boolean isCapturedBlockLootable() {
		return blockToRestore.capturedBlockEntityData().contains("LootTable");
	}

	public void setLootTableForCapturedBlock(Identifier lootTable, long seed) {
		// While it probably doesn't hurt to set a loot table always, it may cause weird effects with other mods etc...
		if (!isCapturedBlockLootable()) {
			return;
		}

		CompoundTag tag = blockToRestore.capturedBlockEntityData();
		tag.putString("LootTable", lootTable.toString());
		tag.putLong("LootTableSeed", seed);

		blockToRestore = new CapturedBlock(blockToRestore.state(), tag);
	}

	public CapturedBlock getBlockToRestore() {
		return blockToRestore;
	}

	public WardedBlockData getWard() {
		return wardingData;
	}

	public void activate() {
		if (wardingData.entityToSpawn() != null) {
			System.out.println("Spawning Entity: " + wardingData.entityToSpawn().getDescriptionId());
		}

		restore();
	}

	public void restore() {
		if (level == null || blockToRestore == null) return;

		level.setBlock(getBlockPos(), blockToRestore.state(), Block.UPDATE_ALL);
		HolderLookup.Provider provider = level.registryAccess();

		if (!blockToRestore.capturedBlockEntityData().isEmpty()) {
			BlockEntity be = BlockEntity.loadStatic(getBlockPos(), blockToRestore.state(), blockToRestore.capturedBlockEntityData(), provider);
			level.setBlockEntity(be);
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);

		blockToRestore = input.read("block_capture", CapturedBlock.CODEC).orElse(CapturedBlock.EMPTY);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);

		output.store("block_capture", CapturedBlock.CODEC, blockToRestore);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveWithoutMetadata(registries);
	}

	@Override
	public @org.jspecify.annotations.Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
