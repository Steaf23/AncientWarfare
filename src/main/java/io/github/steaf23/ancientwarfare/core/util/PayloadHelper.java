package io.github.steaf23.ancientwarfare.core.util;

import io.netty.buffer.ByteBuf;

public class PayloadHelper {

	public static String readString(ByteBuf buf) {
		short strSize = buf.readShort();
		byte[] bytes = new byte[strSize];
		for (int j = 0; j < strSize; j++) {
			bytes[j] = buf.readByte();
		}
		return new String(bytes);
	}

	public static void writeString(String text, ByteBuf stream) {
		byte[] bytes = text.getBytes();
		stream.writeShort(bytes.length);
		stream.writeBytes(bytes);
	}
}
