package io.github.steaf23.ancientwarfare.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

	@WrapOperation(
			method="tryPickItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Inventory;findSlotMatchingItem(Lnet/minecraft/world/item/ItemStack;)I"
			)
		)
	private int ancientwarfare$tryPickItem(Inventory inventory, ItemStack itemStack, Operation<Integer> original) {
		if (itemStack.is(AWItems.SURVEY_KIT)) {
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				if (inventory.getItem(i).is(AWItems.SURVEY_KIT)) {
					return i;
				}
			}
		}

		return original.call(inventory, itemStack);
	}
}
