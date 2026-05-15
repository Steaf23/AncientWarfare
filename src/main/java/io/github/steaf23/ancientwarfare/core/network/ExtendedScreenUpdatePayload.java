package io.github.steaf23.ancientwarfare.core.network;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.menu.ScreenUpdate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ExtendedScreenUpdatePayload(int syncId, ScreenUpdate update) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<ExtendedScreenUpdatePayload> ID = new CustomPacketPayload.Type<>(AncientWarfare.id("extended_screen_update"));
	public static final StreamCodec<FriendlyByteBuf, ExtendedScreenUpdatePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, ExtendedScreenUpdatePayload::syncId,
			ScreenUpdate.STREAM_CODEC, ExtendedScreenUpdatePayload::update,
			ExtendedScreenUpdatePayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
