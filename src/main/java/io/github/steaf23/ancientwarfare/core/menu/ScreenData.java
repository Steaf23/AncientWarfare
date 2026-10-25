package io.github.steaf23.ancientwarfare.core.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;

public class ScreenData extends HashMap<String, byte[]> {

	public static final StreamCodec<FriendlyByteBuf, ScreenData> STREAM_CODEC = ByteBufCodecs.map(
			i -> new ScreenData(),
			ByteBufCodecs.STRING_UTF8,
			ByteBufCodecs.BYTE_ARRAY);
}
