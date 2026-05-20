package io.github.steaf23.ancientwarfare.client.datagen;

import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record CoinMetalTintSource() implements ItemTintSource {

	public static final MapCodec<CoinMetalTintSource> CODEC = MapCodec.unit(CoinMetalTintSource::new);

	@Override
	public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
		CoinMetal metal = itemStack.get(AWComponents.COIN_METAL);
		if (metal == null) {
			return 0xffffffff;
		}
		return metal.color();
	}

	@Override
	public MapCodec<? extends ItemTintSource> type() {
		return CODEC;
	}
}
