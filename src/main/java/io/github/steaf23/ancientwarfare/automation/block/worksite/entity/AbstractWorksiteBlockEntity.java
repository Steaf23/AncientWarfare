package io.github.steaf23.ancientwarfare.automation.block.worksite.entity;

import io.github.steaf23.ancientwarfare.automation.block.worksite.BoundedArea;
import io.github.steaf23.ancientwarfare.automation.block.worksite.UpgradableWorksite;
import io.github.steaf23.ancientwarfare.automation.block.worksite.WorksiteUpgrade;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityMenuProvider;
import io.github.steaf23.ancientwarfare.core.registry.AWWorksiteUpgrades;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractWorksiteBlockEntity extends BaseContainerBlockEntity implements BlockEntityMenuProvider, UpgradableWorksite {

	boolean active = false;
	Set<WorksiteUpgrade> upgrades;
	BoundedArea bounds;

	protected AbstractWorksiteBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		upgrades = new HashSet<>();
		bounds = new BoundedArea(1, 25).maximizeBoundsOnCenter(blockPos);
	}

	public Optional<BoundedArea> bounds() {
		return Optional.ofNullable(bounds);
	}

	@Override
	public Set<WorksiteUpgrade> installedUpgrades() {
		return upgrades;
	}

	@Override
	public boolean installUpgrade(WorksiteUpgrade upgrade) {
		if (!canInstallUpgrade(upgrade)) {
			return false;
		}

		upgrades.add(upgrade);
		upgrade.setup(this);

		level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		return true;
	}

	@Override
	public boolean removeUpgrade(WorksiteUpgrade upgrade) {
		if (!upgrades.contains(upgrade)) {
			return false;
		}

		upgrades.remove(upgrade);
		upgrade.removed(this);

		level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		return true;
	}

	@Override
	public void setRemoved() {
		for (WorksiteUpgrade upgrade : upgrades) {
			upgrade.removed(this);
		}

		upgrades.clear();
		active = false;

		super.setRemoved();
	}

	@Override
	protected void loadAdditional(@NonNull ValueInput input) {
		super.loadAdditional(input);

		active = input.getBooleanOr("active", false);
		bounds = input.read("bounds", BoundedArea.CODEC).orElse(new BoundedArea(1, 25).maximizeBoundsOnCenter(getBlockPos()));
		upgrades = new HashSet<>(input.read("upgrades", AWWorksiteUpgrades.UPGRADE_REGISTRY.byNameCodec().listOf()).orElse(List.of()));
	}

	@Override
	protected void saveAdditional(@NonNull ValueOutput output) {
		super.saveAdditional(output);

		output.putBoolean("active", active);
		output.store("bounds", BoundedArea.CODEC, bounds);
		output.store("upgrades", AWWorksiteUpgrades.UPGRADE_REGISTRY.byNameCodec().listOf(), new ArrayList<>(upgrades));
	}
}
