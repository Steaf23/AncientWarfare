package io.github.steaf23.ancientwarfare.npc.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record AdditionalAttributes(boolean burnsInSun, boolean undead, double healPerTry, Identifier soundSet) {
	public static final Codec<AdditionalAttributes> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.BOOL.fieldOf("burns_in_sun").forGetter(AdditionalAttributes::burnsInSun),
			Codec.BOOL.fieldOf("undead").forGetter(AdditionalAttributes::undead),
			Codec.DOUBLE.fieldOf("heal_per_try").forGetter(AdditionalAttributes::healPerTry),
			Identifier.CODEC.fieldOf("sound_set").forGetter(AdditionalAttributes::soundSet)
	).apply(i, AdditionalAttributes::new));

}
