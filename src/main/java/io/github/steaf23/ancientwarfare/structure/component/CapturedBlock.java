package io.github.steaf23.ancientwarfare.structure.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record CapturedBlock(BlockState state, CompoundTag capturedBlockEntityData) {

	public static final CapturedBlock EMPTY = new CapturedBlock(Blocks.AIR.defaultBlockState(), new CompoundTag());

	public static final Codec<CapturedBlock> CODEC = RecordCodecBuilder.create(g -> g.group(
		BlockState.CODEC.fieldOf("state").forGetter(CapturedBlock::state),
		CompoundTag.CODEC.fieldOf("nbt").forGetter(CapturedBlock::capturedBlockEntityData)
	).apply(g, CapturedBlock::new));

	public boolean isBlockEntity() {
		return !capturedBlockEntityData.isEmpty();
	}
}
