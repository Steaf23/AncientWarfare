package io.github.steaf23.ancientwarfare.structure.template.legacy;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public abstract class TemplateRule {

	public static final String JSON_PREFIX = "JSON:";

	public int ruleNumber = -1;

	public abstract List<ItemStack> getResources();

	public abstract boolean shouldPlaceOnBuildPass(Level world, int turns, BlockPos pos, int buildPass);

	public abstract void parseRule(ValueInput data);

	protected abstract String getRuleType();

	private void writeTag(BufferedWriter out, CompoundTag tag) throws IOException {
		String line = JSON_PREFIX + tag.toString();
		out.write(line);
		out.newLine();
	}

	@Override
	public String toString() {
		return "Template rule: " + ruleNumber + " type: " + getClass().getSimpleName();
	}

	public boolean placeInSurvival() {
		return false;
	}
}