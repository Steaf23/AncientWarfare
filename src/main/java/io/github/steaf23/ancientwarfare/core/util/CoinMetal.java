package io.github.steaf23.ancientwarfare.core.util;


import com.mojang.serialization.Codec;

import java.util.List;

// TODO: make registry?
public record CoinMetal(String name, int color) {
	public static final CoinMetal ANCIENT = new CoinMetal("ancient", 0xff567d63);
	public static final CoinMetal GOLD = new CoinMetal("gold", 0xfff6ea69);
	public static final CoinMetal SILVER = new CoinMetal("silver", 0xffffffff);
	public static final CoinMetal COPPER = new CoinMetal("copper", 0xffff864f);

	public static final List<CoinMetal> ALL = List.of(
			ANCIENT,
			GOLD,
			SILVER,
			COPPER
	);

	public static final Codec<CoinMetal> CODEC = Codec.STRING.xmap(CoinMetal::fromName, CoinMetal::name);

	public int color() {
		return color;
	}

	public static CoinMetal fromName(String name) {
		for (CoinMetal metal : ALL) {
			if (metal.name.equals(name)) {
				return metal;
			}
		}
		throw new IllegalArgumentException("Unknown coin color: " + name);
	}

	public String name() {
		return name;
	}
}
