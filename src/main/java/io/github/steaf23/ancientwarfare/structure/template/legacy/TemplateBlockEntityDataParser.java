package io.github.steaf23.ancientwarfare.structure.template.legacy;

import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;
import java.util.UUID;

public class TemplateBlockEntityDataParser {

	public static void convert(Identifier oldId, ValueInput input, ValueOutput output) {
		output.putString("id", oldId.toString());
		String oldName = oldId.getPath();
		if (oldName.equals("advanced_loot_chest")) {
			oldName = "chest";
		}
		switch (oldName) {
			case "skull" -> { // Convert Owner:{Properties:{textures:[{Value:<texture>}]}}
				Optional<ValueInput> owner = input.child("Owner");
				owner.ifPresent(o -> {
					ValueInput.ValueInputList textureList = o.childOrEmpty("Properties").childrenListOrEmpty("textures");
					textureList.stream().findAny().ifPresent(texture -> {
						ValueOutput profile = output.child("profile");
						ValueOutput textureProp = profile.childrenList("properties").addChild();
						profile.putIntArray("id", UUIDUtil.uuidToIntArray(UUID.fromString(o.getStringOr("Id", ""))));
						textureProp.putString("name", "textures");
						textureProp.putString("value", texture.getStringOr("Value", ""));
					});
				});
			}
			case "chest", "trapped_chest", "furnace", "brewing_stand", "dropper", "shulker_box", "hopper", "dispenser" -> {
				ValueOutput.TypedOutputList<ItemStackWithSlot> items = output.list("Items", ItemStackWithSlot.CODEC);

				for (ValueInput slot : input.childrenListOrEmpty("Items")) {
					int amount = Integer.parseInt(slot.getStringOr("Count", "0b").replace("b", ""));
					int slotNr = Integer.parseInt(slot.getStringOr("Slot", "0b").replace("b", ""));
					Identifier type = Identifier.parse(slot.getStringOr("id", ""));
					assert(!type.getPath().isEmpty());
					int meta = Integer.parseInt(slot.getStringOr("Damage", "0s").replace("s", ""));
					ItemStack stack = TemplateRuleItem.fromOldId(type, meta);
					stack.setCount(amount);

					items.add(new ItemStackWithSlot(slotNr, stack));
				}
			}
		}
	}
}
