package io.github.steaf23.ancientwarfare.core.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

public class ScreenData extends HashMap<String, byte[]> {
	public static final StreamCodec<FriendlyByteBuf, ScreenData> STREAM_CODEC = StreamCodec.of(
			(buf, data) -> {
				buf.writeMap(data == null ? Map.of() : data, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.BYTE_ARRAY);
			}, (buf) -> {
				ScreenData data = new ScreenData();
				data.putAll(buf.readMap(ByteBufCodecs.STRING_UTF8, ByteBufCodecs.BYTE_ARRAY));
				return data;
			});
}
