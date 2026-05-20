package io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock;

import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class WardedBlockEntity extends BlockEntity {

	public record CapturedBlock(BlockState state, CompoundTag capturedBlockEntityData) {}

	public record WardedBlockData(@Nullable EntityType<?> entityToSpawn, @Nullable MobEffectInstance effect) {}

	private CapturedBlock blockToRestore = null;
	private WardedBlockData wardingData;

	public WardedBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(AWBlockEntities.WARDED_BLOCK, worldPosition, blockState);
	}

	public void setup(BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		BlockEntity be = level.getBlockEntity(pos);

		CompoundTag nbt = be == null ? null : be.saveWithFullMetadata(level.registryAccess());

		setBlockToRestore(new CapturedBlock(state, nbt));
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

	public void activate() {
//		if (wardingData.entityToSpawn()) {
//
//		}
		System.out.println("Spawning Entity: " + wardingData.entityToSpawn().getDescriptionId());

		if (level == null || blockToRestore == null) return;

		level.setBlock(getBlockPos(), blockToRestore.state(), Block.UPDATE_ALL);
		HolderLookup.Provider provider = level.registryAccess();

		if (blockToRestore.capturedBlockEntityData() != null) {
			BlockEntity be = BlockEntity.loadStatic(getBlockPos(), blockToRestore.state(), blockToRestore.capturedBlockEntityData(), provider);
			level.setBlockEntity(be);
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);

		ValueInput capture = input.childOrEmpty("block_capture");
		BlockState state = capture.read("state", BlockState.CODEC).orElse(null);
		CompoundTag tag = capture.read("tag", CompoundTag.CODEC).orElse(null);
		blockToRestore = new CapturedBlock(state, tag);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);

		ValueOutput capture = output.child("block_capture");
		capture.store("state", BlockState.CODEC, blockToRestore.state());
		capture.store("nbt", CompoundTag.CODEC, blockToRestore.capturedBlockEntityData());
	}
}
