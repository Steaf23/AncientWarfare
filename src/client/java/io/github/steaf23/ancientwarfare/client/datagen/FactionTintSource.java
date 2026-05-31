package io.github.steaf23.ancientwarfare.client.datagen;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class FactionTintSource implements ItemTintSource {

	public static final MapCodec<FactionTintSource> CODEC = MapCodec.unit(FactionTintSource::new);

	@Override
	public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
		Faction faction = Faction.fromItem(itemStack);

		if (faction != null) {
			return faction.color();
		}

		return 0xffffffff;
	}

	@Override
	public MapCodec<? extends ItemTintSource> type() {
		return CODEC;
	}
}
