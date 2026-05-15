package io.github.steaf23.ancientwarfare.npc.menu;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.menu.EntityScreenData;
import io.github.steaf23.ancientwarfare.core.menu.ExtendedContainerMenu;
import io.github.steaf23.ancientwarfare.core.menu.MobEquipmentSlot;
import io.github.steaf23.ancientwarfare.core.menu.sync.DataApplier;
import io.github.steaf23.ancientwarfare.core.registry.AWContainerMenus;
import io.github.steaf23.ancientwarfare.core.util.PayloadHelper;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class NpcContainerMenu extends ExtendedContainerMenu {

	public static final Identifier EMPTY_SWORD_BACKGROUND_TEXTURE = Identifier.withDefaultNamespace("container/slot/sword");
	public static final Identifier EMPTY_UPKEEP_BACKGROUND_TEXTURE = AncientWarfare.id("upkeep_background");
	public static final Identifier EMPTY_ORDER_BACKGROUND_TEXTURE = AncientWarfare.id("order_background");
	public static final int UPKEEP_SLOT_INDEX = 0;
	public static final int ORDER_SLOT_INDEX = 1;


	private static final DataApplier<NpcContainerMenu, String> NPC_NAME = new DataApplier<>(
			(context, name) -> context.receiver().npc.setNpcName(name),
			(clientCtx, data) -> clientCtx.receiver().npc.setNpcName(data),
			ByteBufCodecs.STRING_UTF8, String.class);

	private static final DataApplier<NpcContainerMenu, Boolean> FOLLOWING_PLAYER = new DataApplier<>(
			(context, following) -> {
				if (context.receiver().npc instanceof PlayerOwnedNpc playerNpc) {
					playerNpc.followPlayer(context.player(), following);
				}
			}, (clientCtx, data) -> {},
			ByteBufCodecs.BOOL, Boolean.class);

	private static final DataApplier<NpcContainerMenu, MenuAction> ACTION = new DataApplier<>(
			(context, action) -> {
				BaseNpc npc = context.receiver().getNpc();
				switch (action) {
					case SetHome -> npc.getHome().setHomePosAndRange(npc.blockPosition(), npc.getHome().getHomeRange());
					case ClearHome -> npc.getHome().clear();
					case Recall -> npc.recall(context.player());
				}
			}, (clientCtx, data) -> {},
			MenuAction.STREAM_CODEC, MenuAction.class);

	private final BaseNpc npc;

	// Client constructor
	public NpcContainerMenu(int containerId, Inventory playerInventory, EntityScreenData screenData) {
		this(containerId, playerInventory, (BaseNpc) playerInventory.player.level().getEntity(screenData.getUniqueId()));

		applyOpeningData(screenData.getOpeningData());
	}

	public NpcContainerMenu(int containerId, Inventory playerInventory, BaseNpc npc) {
		super(AWContainerMenus.NPC, containerId, playerInventory);
		this.npc = npc;

		addEquipmentSlot(npc, EquipmentSlot.MAINHAND, 20, 34, EMPTY_SWORD_BACKGROUND_TEXTURE);
		addEquipmentSlot(npc, EquipmentSlot.OFFHAND, 20, 53, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
		addEquipmentSlot(npc, EquipmentSlot.HEAD, 50, 31, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET);
		addEquipmentSlot(npc, EquipmentSlot.CHEST, 50, 58, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE);
		addEquipmentSlot(npc, EquipmentSlot.LEGS, 104, 31, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS);
		addEquipmentSlot(npc, EquipmentSlot.FEET, 104, 58, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);

		addSlot(new Slot(npc.getItems(), UPKEEP_SLOT_INDEX, 137, 42)
		{
			@Override
			public Identifier getNoItemIcon() {
				return EMPTY_UPKEEP_BACKGROUND_TEXTURE;
			}
		});
		addSlot(new Slot(npc.getItems(), ORDER_SLOT_INDEX, 137, 61)
		{
			@Override
			public Identifier getNoItemIcon() {
				return EMPTY_ORDER_BACKGROUND_TEXTURE;
			}
		});

		addStandardInventorySlots(playerInventory, 12, 134);
	}

	public void setNpcName(String name) {
		npc.setNpcName(name);
		sendDataToServer("name", name);
	}

	public void setFollowingPlayer(Boolean follow) {
		sendDataToServer("follow_player", follow);
	}

	public void setHome() {
		sendDataToServer("action", MenuAction.SetHome);
	}

	public void clearHome() {
		sendDataToServer("action", MenuAction.ClearHome);
	}

	public void recall() {
		sendDataToServer("action", MenuAction.Recall);
	}

	public String getNpcName() {
		return npc.getNpcName();
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	private void addEquipmentSlot(BaseNpc npc, EquipmentSlot slot, int x, int y, Identifier background) {
		addSlot(new MobEquipmentSlot(npc, slot.getId(), x, y, slot, background) {
			@Override
			public @Nullable Identifier getNoItemIcon() {
				return background;
			}
		});
	}

	public BaseNpc getNpc() {
		return npc;
	}

	@Override
	public @Nullable DataApplier<? extends ExtendedContainerMenu, ?> getDataApplier(String name) {
		return switch (name) {
			case "action" -> NpcContainerMenu.ACTION;
			case "name" -> NpcContainerMenu.NPC_NAME;
			case "follow_player" -> NpcContainerMenu.FOLLOWING_PLAYER;
			default -> throw new IllegalStateException("Unexpected value, data applier with name {" + name + "} does not exist for " + NpcContainerMenu.class);
		};
	}

	public enum MenuAction {
		SetHome,
		ClearHome,
		Recall,
		;

		private static final StreamCodec<ByteBuf, MenuAction> STREAM_CODEC = StreamCodec.of(
				(buf, cmd) -> {
					PayloadHelper.writeString(cmd.toString(), buf);
				},
				(buf) -> MenuAction.valueOf(PayloadHelper.readString(buf)));
	}
}
