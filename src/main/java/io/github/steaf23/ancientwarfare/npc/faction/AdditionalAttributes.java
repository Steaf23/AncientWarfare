package io.github.steaf23.ancientwarfare.npc.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AdditionalAttributes(boolean burnsInSun, double healPerTry) {
	public static final Codec<AdditionalAttributes> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.BOOL.fieldOf("burns_in_sun").forGetter(AdditionalAttributes::burnsInSun),
			Codec.DOUBLE.fieldOf("heal_per_try").forGetter(AdditionalAttributes::healPerTry)
	).apply(i, AdditionalAttributes::new));

}
