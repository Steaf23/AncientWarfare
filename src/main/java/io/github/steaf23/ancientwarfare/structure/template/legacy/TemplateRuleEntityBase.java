package io.github.steaf23.ancientwarfare.structure.template.legacy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;

import java.util.List;

public class TemplateRuleEntityBase extends TemplateRule {

	private BlockPos pos;

	/*
	 * Called by reflection
	 */
	public TemplateRuleEntityBase() {}

	@Override
	public List<ItemStack> getResources() {
		return List.of();
	}

	@Override
	public boolean shouldPlaceOnBuildPass(Level world, int turns, BlockPos pos, int buildPass) {
		return false;
	}

	@Override
	public void parseRule(ValueInput data) {

	}

	@Override
	protected String getRuleType() {
		return "entity";
	}

	public final void setPosition(BlockPos pos) {
		this.pos = pos;
	}

	public final BlockPos getPosition() {
		return pos;
	}
}