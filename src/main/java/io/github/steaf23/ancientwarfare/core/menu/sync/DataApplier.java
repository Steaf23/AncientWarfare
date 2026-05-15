package io.github.steaf23.ancientwarfare.core.menu.sync;

import io.github.steaf23.ancientwarfare.core.menu.ExtendedContainerMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public record DataApplier<Receiver extends ExtendedContainerMenu, Data>(
		BiConsumer<ServerApplicationContext<Receiver>, Data> serverUpdate,
		BiConsumer<ClientApplicationContext<Receiver>, Data> clientUpdate,
		StreamCodec<ByteBuf, Data> codec,
		Class<Data> dataClass) {

	public void applyToServer(ServerApplicationContext<Receiver> context, Data data) {
		serverUpdate.accept(context, data);
	}

	public void applyToClient(ClientApplicationContext<Receiver> context, Data data) {
		clientUpdate.accept(context, data);
	}

	public record ServerApplicationContext<Receiver> (ServerPlayer player, Receiver receiver) {};
	public record ClientApplicationContext<Receiver> (Player player, Receiver receiver) {};
}


