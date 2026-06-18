package io.github.steaf23.ancientwarfare.core.util;


import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.List;

// TODO: make registry?
public enum CoinMetal implements StringRepresentable {
	ANCIENT("ancient", 0xff567d63),
	GOLD("gold", 0xfff8d854),
	SILVER("silver", 0xffd0f4ff),
	COPPER("copper", 0xffff7a4d),
	;

	public static final List<CoinMetal> ALL = List.of(
			ANCIENT,
			GOLD,
			SILVER,
			COPPER
	);

	public static final Codec<CoinMetal> CODEC = Codec.STRING.xmap(CoinMetal::fromName, CoinMetal::getSerializedName);

	private final String name;
	private final int color;

	CoinMetal(String name, int color) {
		this.name = name;
		this.color = color;
	}

	public int color() {
		return color;
	}

	public static CoinMetal fromName(String name) {
		for (CoinMetal metal : ALL) {
			if (metal.getSerializedName().equals(name)) {
				return metal;
			}
		}
		throw new IllegalArgumentException("Unknown coin color: " + name);
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
