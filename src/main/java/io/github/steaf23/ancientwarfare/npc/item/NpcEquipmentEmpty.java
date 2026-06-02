package io.github.steaf23.ancientwarfare.npc.item;

import com.mojang.serialization.MapCodec;

public record NpcEquipmentEmpty() implements NpcEquipment {

	public static final MapCodec<NpcEquipmentEmpty> CODEC = MapCodec.unit(NpcEquipmentEmpty::new);

	@Override
	public MapCodec<? extends NpcEquipment> codec() {
		return CODEC;
	}

	@Override
	public String type() {
		return "empty";
	}
}
