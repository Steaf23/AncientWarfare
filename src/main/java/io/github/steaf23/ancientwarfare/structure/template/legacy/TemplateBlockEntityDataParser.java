package io.github.steaf23.ancientwarfare.structure.template.legacy;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;
import java.util.UUID;

public class TemplateBlockEntityDataParser {

	public static void convert(Identifier oldId, ValueInput input, ValueOutput output) {
		output.putString("id", oldId.toString());
		String oldName = oldId.getPath();
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
				writeItemsToTileEntity(input.childrenListOrEmpty("Items"), output);
			}
			// TODO: update to be warded blocks
			case "advanced_loot_chest" -> {
				ValueOutput.TypedOutputList<ItemStackWithSlot> items = output.list("Items", ItemStackWithSlot.CODEC);

				input.child("lootSettings").ifPresentOrElse(settings -> {
					if (settings.getByteOr("hasLoot", (byte)0) != 0) {
						output.putString("LootTable", settings.getStringOr("lootTableName", ""));
					} else {
						writeItemsToTileEntity(input.childrenListOrEmpty("Items"), output);
					}
				}, () -> {
					writeItemsToTileEntity(input.childrenListOrEmpty("Items"), output);
				});

			}
		}
	}

	public static void writeItemsToTileEntity(ValueInput.ValueInputList items, ValueOutput output) {
		ValueOutput.TypedOutputList<ItemStackWithSlot> itemsOut = output.list("Items", ItemStackWithSlot.CODEC);

		for (ValueInput slot : items) {
			int amount = Integer.parseInt(slot.getStringOr("Count", "0b").replace("b", ""));
			int slotNr = Integer.parseInt(slot.getStringOr("Slot", "0b").replace("b", ""));
			Identifier type = Identifier.parse(slot.getStringOr("id", ""));
			assert(!type.getPath().isEmpty());
			int meta = Integer.parseInt(slot.getStringOr("Damage", "0s").replace("s", ""));
			CompoundTag tag = slot.read("tag", CompoundTag.CODEC).orElse(new CompoundTag());
			ItemStackTemplate stack = TemplateRuleItem.fromOldId(type, meta, amount, tag);

//			itemsOut.add(new ItemStackWithSlot(slotNr, stack));
		}
	}
}
