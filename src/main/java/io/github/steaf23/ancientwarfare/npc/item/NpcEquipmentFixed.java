package io.github.steaf23.ancientwarfare.npc.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record NpcEquipmentFixed(@Nullable Identifier mainHandItemId, @Nullable Identifier offHandItemId) implements NpcEquipment {

	public static final MapCodec<NpcEquipmentFixed> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Identifier.CODEC.optionalFieldOf("mainhand").forGetter(data -> Optional.ofNullable(data.mainHandItemId())),
			Identifier.CODEC.optionalFieldOf("offhand").forGetter(data -> Optional.ofNullable(data.offHandItemId()))
	).apply(i, (mainHand, offHand) -> new NpcEquipmentFixed(mainHand.orElse(null), offHand.orElse(null))));

	@Override
	public MapCodec<? extends NpcEquipment> codec() {
		return CODEC;
	}

	@Override
	public String type() {
		return "fixed";
	}
}
