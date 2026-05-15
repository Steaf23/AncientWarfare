package io.github.steaf23.ancientwarfare.core.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityScreenData {

	public static final StreamCodec<FriendlyByteBuf, BlockEntityScreenData> PACKET_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, BlockEntityScreenData::getPos,
			ScreenData.STREAM_CODEC, BlockEntityScreenData::getOpeningData,
			BlockEntityScreenData::new);

	private final BlockPos pos;
	private final ScreenData openingData;

	public BlockEntityScreenData(BlockEntity be, ScreenData openingData) {
		this(be.getBlockPos(), openingData);
	}

	private BlockEntityScreenData(BlockPos pos, ScreenData openingData) {
		this.pos = pos;
		this.openingData = openingData;
	}

	public BlockPos getPos() {
		return pos;
	}

	public ScreenData getOpeningData() {
		return openingData;
	}
}
