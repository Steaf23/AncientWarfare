package io.github.steaf23.ancientwarfare.npc.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//TODO: Actually finish this class?
public class NpcHome {

	private final BaseNpc npc;

	private @Nullable BlockPos homePos = null;
	private int homeRange = 0;

	public NpcHome(BaseNpc npc) {
		this.npc = npc;
	}

	public BlockPos getOrCreateHomePosition(int range) {
		return npc.blockPosition();
	}

	public void read(ValueInput view) {
		Optional<BlockPos> optPos = view.read("homePos", BlockPos.CODEC);
		optPos.ifPresent(blockPos -> homePos = blockPos);

		homeRange = view.getIntOr("homeRange", 0);
	}

	public void write(ValueOutput view) {
		view.store("homePos", BlockPos.CODEC, getOrCreateHomePosition(getHomeRange()));
		view.putInt("homeRange", getHomeRange());
	}

	public BlockPos getHomePosition() {
		return homePos;
	}

	public int getHomeRange() {
		return homeRange;
	}

	public void clear() {
		homePos = null;
	}

	public void setHomePosAndRange(BlockPos pos, int range) {
		homePos = pos;
		homeRange = range;
	}
}
