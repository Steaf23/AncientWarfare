package io.github.steaf23.ancientwarfare.npc.entity.playerowned.upkeep;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class NpcUpkeep {

	// Amount of time left in ticks before the npc is hungry.
	int ticksRemaining;

	final int requiredUpkeepTicks;
	final UpkeepValueProvider valueProvider;

	public NpcUpkeep(int requiredUpkeepTicks, UpkeepValueProvider valueProvider) {
		this.requiredUpkeepTicks = requiredUpkeepTicks;
		this.valueProvider = valueProvider;
	}

	public void read(ValueInput values) {
		ticksRemaining = values.getIntOr("upkeep_ticks_remaining", 0);
	}

	public void write(ValueOutput values) {
		values.putInt("upkeep_ticks_remaining", ticksRemaining);
	}

	public int getRequiredUpkeepTicks() {
		return requiredUpkeepTicks;
	}

	public void refillTest() {
		ticksRemaining += 7000;
	}

	public int getTicksRemaining() {
		return ticksRemaining;
	}

	public boolean refillFromContainer(Container upkeepSource) {
		int amount = getRequiredUpkeepTicks() - ticksRemaining;
		if (amount <= 0) {
			return true;
		}
		int val;
		int eaten = 0;
		for (int slot = 0; slot < upkeepSource.getContainerSize(); slot++) {
			ItemStack stack = upkeepSource.getItem(slot);
			val = valueProvider.getValue(stack);
			if (val <= 0) {
				continue;
			}
			while (eaten < amount && !stack.isEmpty()) {
				eaten += val;
				upkeepSource.removeItem(slot, 1);
			}
		}
		ticksRemaining += eaten;
		return ticksRemaining >= getRequiredUpkeepTicks();
	}

	public void tick() {
		if (ticksRemaining > 0) {
			ticksRemaining--;
		}
	}

	public boolean requiresUpkeep() {
		return ticksRemaining <= 0;
	}
}
