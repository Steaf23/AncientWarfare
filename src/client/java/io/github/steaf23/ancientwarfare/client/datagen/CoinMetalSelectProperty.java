package io.github.steaf23.ancientwarfare.client.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class CoinMetalSelectProperty implements SelectItemModelProperty<CoinMetal> {

	public static final SelectItemModelProperty.Type<CoinMetalSelectProperty, CoinMetal> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit(new CoinMetalSelectProperty()), CoinMetal.CODEC);


	@Override
	public @Nullable CoinMetal get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
		CoinMetal metal = itemStack.get(AWComponents.COIN_METAL);
		if (metal == null) {
			return CoinMetal.GOLD;
		}
		return metal;
	}

	@Override
	public Codec<CoinMetal> valueCodec() {
		return CoinMetal.CODEC;
	}

	@Override
	public Type<? extends SelectItemModelProperty<CoinMetal>, CoinMetal> type() {
		return TYPE;
	}
}
