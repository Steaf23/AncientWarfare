package io.github.steaf23.ancientwarfare.core.research;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ResearchBookPayload implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ResearchBookPayload> ID = new CustomPacketPayload.Type<>(AncientWarfare.id("open_research_book"));


	public static final ResearchBookPayload INSTANCE = new ResearchBookPayload();
	public static final StreamCodec<FriendlyByteBuf, ResearchBookPayload> CODEC = StreamCodec.unit(INSTANCE);

	private ResearchBookPayload() {}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
