package io.github.steaf23.ancientwarfare.npc.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record NpcData(UUID uuid, String name) {
	public static StreamCodec<RegistryFriendlyByteBuf, NpcData> CODEC = StreamCodec.ofMember(
			(data, buf) -> {
				buf.writeUUID(data.uuid);
				buf.writeUtf(data.name);
			}, (buf) -> {
				UUID uuid = buf.readUUID();
				String name = buf.readUtf();
				return new NpcData(uuid, name);
			});
}
