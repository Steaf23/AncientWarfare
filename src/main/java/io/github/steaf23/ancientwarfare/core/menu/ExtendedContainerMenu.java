package io.github.steaf23.ancientwarfare.core.menu;

import io.github.steaf23.ancientwarfare.core.menu.sync.DataApplier;
import io.github.steaf23.ancientwarfare.core.network.ExtendedScreenUpdatePayload;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ExtendedContainerMenu extends AbstractContainerMenu {

	private final Player player;
	private Runnable updateNotifier = () -> {};
	private ClientDataSender dataSender = (payload) -> {};

	protected ExtendedContainerMenu(@Nullable MenuType<?> type, int containerId, Inventory playerInv) {
		super(type, containerId);
		this.player = playerInv.player;
	}

	/**
	 * Should only be called from the Client side.
	 * @param data what data to apply on the client.
	 */
	public void applyOpeningData(ScreenData data) {
		if (!getPlayer().level().isClientSide()) {
			System.out.println("ExtendedContainerMenu.applyOpeningData should only be called from client!");
			return;
		}
		if (data != null) {
			for (String k : data.keySet()) {
				applyServerUpdate(player, new ScreenUpdate(k, data.get(k)));
			}
		}
	}

	public final void setClientSender(@NotNull ClientDataSender dataSender) {
		if (!getPlayer().level().isClientSide()) {
			System.out.println("ExtendedContainerMenu.setClientSender should only be called on the client!");
			return;
		}
		this.dataSender = dataSender;
	}

	public <Data> void sendDataToServer(String key, Data data) {
		if (!getPlayer().level().isClientSide()) {
			System.out.println("ExtendedContainerMenu.applyDataToServer should only be called on the client!");
			return;
		}

		DataApplier<?, ?> applier = getDataApplier(key);
		if (applier == null) {
			System.out.println("NO data applier found for key " + key);
			return;
		}

		if (!applier.dataClass().isInstance(data)) {
			throw new IllegalArgumentException("Apply data to server: Field of type " + data.getClass() + " cannot be applied with data applier \"" + key + "\"");
		}

		if (updateNotifier != null) {
			updateNotifier.run();
		}

		@SuppressWarnings("unchecked")
		DataApplier<?, Data> typed = (DataApplier<?, Data>) applier;
		FriendlyByteBuf buf = FriendlyByteBufs.create();
		typed.codec().encode(buf, data);
		dataSender.send(new ExtendedScreenUpdatePayload(containerId, new ScreenUpdate(key, buf.array())));
	}

	public <Data> void applyClientUpdate(ServerPlayer player, ScreenUpdate update) {
		if (player.level().isClientSide()) {
			System.out.println("ExtendedContainerMenu.applyClientData should only be called on the Server!");
			return;
		}
		String key = update.key();

		DataApplier<?, ?> applier = getDataApplier(key);
		if (applier == null) {
			System.out.println("NO data applier found for key " + key);
			return;
		}

		@SuppressWarnings("unchecked")
		DataApplier<ExtendedContainerMenu, Data> typed = (DataApplier<ExtendedContainerMenu, Data>) applier;

		try {
			Data value = typed.codec().decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(update.data())));
			typed.applyToServer(new DataApplier.ServerApplicationContext<>(player, this), value);
		} catch (Exception e) {
			System.out.println("Failed to decode GUI update for key '" + key + "' on the server: " + e.getMessage());
		}

		if (updateNotifier != null) {
			updateNotifier.run();
		}
	}

	public <Data> void applyServerUpdate(Player player, ScreenUpdate update) {
		if (!getPlayer().level().isClientSide()) {
			System.out.println("ExtendedContainerMenu.applyDataToServer should only be called on the client!");
			return;
		}
		String key = update.key();

		DataApplier<?, ?> applier = getDataApplier(key);
		if (applier == null) {
			System.out.println("NO data applier found for key " + key);
			return;
		}

		@SuppressWarnings("unchecked")
		DataApplier<ExtendedContainerMenu, Data> typed = (DataApplier<ExtendedContainerMenu, Data>) applier;

		try {
			Data value = typed.codec().decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(update.data())));
			typed.applyToClient(new DataApplier.ClientApplicationContext<>(player, this), value);
		} catch (Exception e) {
			System.out.println("Failed to decode GUI update for key '" + key + "' on the client: " + e.getMessage());
		}

		if (updateNotifier != null) {
			updateNotifier.run();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setUpdateNotifier(Runnable notifier) {
		this.updateNotifier = notifier;
	}

	public abstract @Nullable DataApplier<?, ?> getDataApplier(String key);

	@FunctionalInterface
	public interface ClientDataSender {
		void send(ExtendedScreenUpdatePayload payload);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int i) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(i);
		if (slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (i < 27 ? !this.moveItemStackTo(itemStack2, 27, this.slots.size(), true) : !this.moveItemStackTo(itemStack2, 0, 27, false)) {
				return ItemStack.EMPTY;
			}
			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemStack;
	}
}
