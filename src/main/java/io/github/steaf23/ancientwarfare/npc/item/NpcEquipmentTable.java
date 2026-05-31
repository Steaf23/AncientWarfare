package io.github.steaf23.ancientwarfare.npc.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.EquipmentTable;

public record NpcEquipmentTable(EquipmentTable equipment) implements NpcEquipment {

	public static final MapCodec<NpcEquipmentTable> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EquipmentTable.CODEC.fieldOf("table").forGetter(NpcEquipmentTable::equipment)
	).apply(i, NpcEquipmentTable::new));

	@Override
	public MapCodec<? extends NpcEquipment> codec() {
		return CODEC;
	}

	@Override
	public String type() {
		return "table";
	}
}
