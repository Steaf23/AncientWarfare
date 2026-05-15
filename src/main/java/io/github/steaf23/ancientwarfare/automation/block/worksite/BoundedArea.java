package io.github.steaf23.ancientwarfare.automation.block.worksite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.AABB;

import java.util.function.Consumer;

public class BoundedArea {

	public static final Codec<BoundedArea> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockPos.CODEC.fieldOf("minPos").forGetter(BoundedArea::minPos),
			BlockPos.CODEC.fieldOf("maxPos").forGetter(BoundedArea::maxPos),
			Codec.INT.fieldOf("maxHeight").forGetter(BoundedArea::maxHeight),
			Codec.INT.fieldOf("maxArea").forGetter(BoundedArea::maxArea)
	).apply(i, BoundedArea::new));

	public static final StreamCodec<FriendlyByteBuf, BoundedArea> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, BoundedArea::minPos,
			BlockPos.STREAM_CODEC, BoundedArea::maxPos,
			ByteBufCodecs.INT, BoundedArea::maxHeight,
			ByteBufCodecs.INT, BoundedArea::maxArea,
			BoundedArea::new
			);

	BlockPos minPos = BlockPos.ZERO;
	BlockPos maxPos = BlockPos.ZERO;

	int maxHeight;
	int maxArea;

	private BlockPos minPos() {
		return minPos;
	}

	private BlockPos maxPos() {
		return maxPos;
	}

	private int maxArea() {
		return maxArea;
	}

	private int maxHeight() {
		return maxHeight;
	}

	private BoundedArea(BlockPos minPos, BlockPos maxPos, int maxHeight, int maxArea) {
		this.minPos = minPos;
		this.maxPos = maxPos;
		this.maxHeight = maxHeight;
		this.maxArea = maxArea;
	}

	public BoundedArea maximizeBoundsOnCenter(BlockPos center) {
		BlockPos areaQuart = new BlockPos((int) Math.sqrt(maxArea) / 2, 0,(int) Math.sqrt(maxArea) / 2);
		BlockPos bottomLeft = center.subtract(areaQuart);
		BlockPos topRight = center.offset(areaQuart);
		setBounds(bottomLeft, topRight);
		return this;
	}

	public BoundedArea(int maxHeight, int maxArea) {
		this.maxHeight = maxHeight;
		this.maxArea = maxArea;
	}

	public boolean setBounds(BlockPos pos1, BlockPos pos2) {
		BlockPos newMin = BlockPos.min(pos1, pos2);
		BlockPos newMax = BlockPos.max(pos1, pos2);

		int xSize = newMax.getX() - newMin.getX() + 1;
		int ySize = newMax.getY() - newMin.getY() + 1;
		int zSize = newMax.getZ() - newMin.getZ() + 1;
		int calculatedArea = xSize * zSize;

		if (calculatedArea > maxArea || ySize > maxHeight) {
			return false;
		}

		this.minPos = newMin;
		this.maxPos = newMax;
		return true;
	}

	public void setMaxHeight(int height) {
		this.maxHeight = height;
	}

	public void setMaxArea(int areaSize) {
		this.maxArea = areaSize;
	}

	public void forAllPositionsInBoundedPlane(int relativePlaneHeight, Consumer<BlockPos> action) {
		int y = minPos.getY() + relativePlaneHeight;
		for (int x = minPos.getX(); x < maxPos.getX(); x++) {
			for (int z = minPos.getZ(); z < maxPos.getZ(); z++) {
				action.accept(new BlockPos(x, y, z));
			}
		}
	}

	//TODO: Add Y check??
	public boolean isWithinBounds(BlockPos pos) {
		return pos.getX() >= minPos.getX() && pos.getX() <= maxPos.getX() &&
				pos.getZ() >= minPos.getZ() && pos.getZ() <= maxPos.getZ();
	}

	public AABB asBox() {
		return new AABB(minPos.getCenter(), maxPos.getCenter()).inflate(0.5);
	}
}
