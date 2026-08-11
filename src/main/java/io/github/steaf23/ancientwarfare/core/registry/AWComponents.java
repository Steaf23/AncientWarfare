package io.github.steaf23.ancientwarfare.core.registry;

import com.mojang.serialization.Codec;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlockInfo;
import io.github.steaf23.ancientwarfare.worksite.marker.SurveyArea;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.UUID;

public class AWComponents {

	public static final DataComponentType<List<UUID>> SELECTED_ENTITIES = AWComponents.register("selected_entities", Codec.list(UUIDUtil.CODEC));

	public static final DataComponentType<CoinMetal> COIN_METAL = AWComponents.register("coin_metal", CoinMetal.CODEC);

	public static final DataComponentType<CapturedBlockInfo> BLOCK_CAPTURE = AWComponents.register("block_capture", CapturedBlockInfo.CODEC);

	public static final DataComponentType<ResourceKey<Faction>> FACTION_ITEM = AWComponents.register("faction_item", ResourceKey.codec(Factions.FACTION_REGISTRY_KEY));

	public static final DataComponentType<SurveyArea> SURVEY_STAKES = AWComponents.register("survey_stakes", SurveyArea.CODEC);

	public static <T> DataComponentType<T> register(String name, Codec<T> codec) {
		return Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				AncientWarfare.id(name),
				DataComponentType.<T>builder().persistent(codec).build()
		);
	}

	public static void initialize() {

	}
}
