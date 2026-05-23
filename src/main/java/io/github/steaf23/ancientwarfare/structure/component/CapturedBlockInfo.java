package io.github.steaf23.ancientwarfare.structure.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record CapturedBlockInfo(BlockState state, CompoundTag capturedBlockEntityData) {

	public static final CapturedBlockInfo EMPTY = new CapturedBlockInfo(Blocks.AIR.defaultBlockState(), new CompoundTag());

	public static final Codec<CapturedBlockInfo> CODEC = RecordCodecBuilder.create(g -> g.group(
		BlockState.CODEC.fieldOf("state").forGetter(CapturedBlockInfo::state),
		CompoundTag.CODEC.fieldOf("nbt").forGetter(CapturedBlockInfo::capturedBlockEntityData)
	).apply(g, CapturedBlockInfo::new));

	public boolean isBlockEntity() {
		return !capturedBlockEntityData.isEmpty();
	}
}
