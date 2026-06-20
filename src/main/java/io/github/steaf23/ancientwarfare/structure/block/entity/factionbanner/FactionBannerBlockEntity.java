package io.github.steaf23.ancientwarfare.structure.block.entity.factionbanner;

import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.util.FactionOwned;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FactionBannerBlockEntity extends BlockEntity implements FactionOwned {

	public FactionBannerBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(AWBlockEntities.FACTION_BANNER, worldPosition, blockState);
	}

	@Override
	public @Nullable ResourceKey<Faction> getFactionKey() {
		return components().get(AWComponents.FACTION_ITEM);
	}
}
