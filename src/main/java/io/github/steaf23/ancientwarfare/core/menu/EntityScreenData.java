package io.github.steaf23.ancientwarfare.core.menu;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class EntityScreenData {

	public static final StreamCodec<FriendlyByteBuf, EntityScreenData> PACKET_CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, EntityScreenData::getUniqueId,
			ScreenData.STREAM_CODEC, EntityScreenData::getOpeningData,
			EntityScreenData::new);

	private final UUID uuid;
	private final ScreenData openingData;

	public EntityScreenData(Entity entity, ScreenData openingData) {
		this(entity.getUUID(), openingData);
	}

	private EntityScreenData(UUID uuid, ScreenData openingData) {
		this.uuid = uuid;
		this.openingData = openingData;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public ScreenData getOpeningData() {
		return openingData;
	}
}
