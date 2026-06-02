package io.github.steaf23.ancientwarfare.npc.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public sealed interface NpcEquipment permits NpcEquipmentEmpty, NpcEquipmentFixed, NpcEquipmentTable {
	Codec<NpcEquipment> CODEC = Codec.STRING.dispatchStable(NpcEquipment::type, type -> switch (type) {
		case "table" -> NpcEquipmentTable.CODEC;
		case "fixed" -> NpcEquipmentFixed.CODEC;
		default -> NpcEquipmentEmpty.CODEC;
	});

	MapCodec<? extends NpcEquipment> codec();

	String type();
}
