package io.github.steaf23.ancientwarfare.core.registry;

import com.mojang.serialization.Codec;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlockInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;
import java.util.UUID;

public class AWComponents {

	public static final DataComponentType<List<UUID>> SELECTED_ENTITIES = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			AncientWarfare.id("selected_entities"),
			DataComponentType.<List<UUID>>builder().persistent(Codec.list(UUIDUtil.CODEC)).build()
	);

	public static final DataComponentType<CoinMetal> COIN_METAL = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			AncientWarfare.id("coin_metal"),
			DataComponentType.<CoinMetal>builder().persistent(CoinMetal.CODEC).build()
	);

	public static final DataComponentType<CapturedBlockInfo> BLOCK_CAPTURE = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			AncientWarfare.id("block_capture"),
			DataComponentType.<CapturedBlockInfo>builder().persistent(CapturedBlockInfo.CODEC).build()
	);

	public static void initialize() {

	}
}
