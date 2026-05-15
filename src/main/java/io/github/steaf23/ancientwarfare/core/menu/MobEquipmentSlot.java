package io.github.steaf23.ancientwarfare.core.menu;

import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MobEquipmentSlot extends Slot {

	private final Mob entity;
	private final EquipmentSlot slot;
	private final Identifier background;

	public MobEquipmentSlot(Mob entity, int index, int x, int y, EquipmentSlot slot, Identifier background) {
		super(new SimpleContainer(0), index, x, y);
		this.entity = entity;
		this.slot = slot;
		this.background = background;
	}

	@Override
	public ItemStack getItem() {
		return entity.getItemBySlot(slot);
	}

	@Override
	public void setByPlayer(ItemStack stack) {
		entity.setItemSlot(slot, stack);
		this.setChanged();
	}

	@Override
	public void set(ItemStack stack) {
		setByPlayer(stack);
	}

	@Override
	public void setByPlayer(ItemStack stack, ItemStack previousStack) {
		setByPlayer(stack);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return entity.isEquippableInSlot(stack, slot);
	}

	@Override
	public void setChanged() {
		//Unused, setting equipment already syncs data across
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		entity.setItemSlot(slot, ItemStack.EMPTY);
		super.onTake(player, stack);
	}

	@Override
	public ItemStack remove(int amount) {
		ItemStack stack = entity.getItemBySlot(slot);
		if (stack.isEmpty()) return ItemStack.EMPTY;

		ItemStack result = stack.split(amount);
		entity.setItemSlot(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
		return result;
	}

	@Override
	public boolean mayPickup(Player playerEntity) {
		return true;
	}

	@Override
	public @Nullable Identifier getNoItemIcon() {
		return background;
	}
}
