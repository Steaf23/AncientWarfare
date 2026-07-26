package io.github.steaf23.ancientwarfare.worksite.marker;

import io.github.steaf23.ancientwarfare.core.menu.BlockEntityMenuProvider;
import io.github.steaf23.ancientwarfare.core.menu.BlockEntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.ScreenData;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.core.registry.AWBlocks;
import io.github.steaf23.ancientwarfare.worksite.requirement.WorksiteRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WorksiteMarkerBlockEntity extends BlockEntity implements BlockEntityMenuProvider {

	private final List<WorksiteRequirement> requirements = new ArrayList<>();
	private int searchRadius = 8;

	public WorksiteMarkerBlockEntity(BlockPos worldPosition, BlockState blockState) {
 		super(AWBlockEntities.WORKSITE_MARKER, worldPosition, blockState);
	}

	public List<WorksiteRequirement> checkAndGetIncompleteRequirements() {
		SearchContext context = new SearchContext(getBlockPos(), searchRadius, (ServerLevel)getLevel());
		return requirements.stream()
				.filter(Predicate.not(r -> r.isCompleted(context)))
				.toList();
	}

	public void setRequirements(List<WorksiteRequirement> requirements) {
		this.requirements.clear();
		this.requirements.addAll(requirements);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		searchRadius = input.getInt("search_radius").orElse(8);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("search_radius", searchRadius);
	}

	@Override
	public BlockEntityScreenData getScreenOpeningData(ServerPlayer player) {
		return new BlockEntityScreenData(this, new ScreenData());
	}

	@Override
	public Component getDisplayName() {
		return AWBlocks.WORKSITE_MARKER.getName();
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		return null;
	}
}
