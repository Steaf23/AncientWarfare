package io.github.steaf23.ancientwarfare.core.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ScreenUpdate(String key, byte[] data) {

	public static final StreamCodec<FriendlyByteBuf, ScreenUpdate> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, ScreenUpdate::key,
			ByteBufCodecs.BYTE_ARRAY, ScreenUpdate::data,
			ScreenUpdate::new
	);
}
