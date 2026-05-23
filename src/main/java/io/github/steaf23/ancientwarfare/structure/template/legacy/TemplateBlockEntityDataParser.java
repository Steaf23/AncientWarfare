package io.github.steaf23.ancientwarfare.structure.template.legacy;

import io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock.WardInfo;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlockInfo;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;
import java.util.UUID;

public class TemplateBlockEntityDataParser {

	public record TileEntityConversionContext(Identifier oldId, BlockState newState, ValueInput input, HolderLookup.Provider registries) {}

	public static void convert(TileEntityConversionContext context, ValueOutput output) {
		Identifier oldId = context.oldId;

		ValueInput input = context.input;

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
			case "advanced_loot_chest", "loot_basket" -> {
				output.putString("id", "ancientwarfare:warded_block");

				TagValueOutput chestOutput = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
				chestOutput.putString("id", "minecraft:chest");

				input.child("lootSettings").ifPresentOrElse(settings -> {
					String entity = settings.getStringOr("entity", "");
					EntityType<?> entityType = null;
					if (!entity.isEmpty() && !entity.equals("minecraft:")) {
						Identifier entityId = TemplateEntityDataParser.updateId(Identifier.parse(entity));
						entityType = BuiltInRegistries.ENTITY_TYPE.getValue(entityId);
					}
					MobEffectInstance effectInstance = null;
					var effectList = settings.childrenListOrEmpty("effects");
					var maybeEffect = effectList.stream().findAny();
					if (maybeEffect.isPresent()) {
						ValueInput effect = maybeEffect.orElseThrow();
						Identifier effectId = Identifier.parse(effect.getStringOr("RegistryName", ""));
						int durationTicks = effect.getIntOr("Duration", 10);
						int amplifier = Integer.parseInt(effect.getStringOr("Amplifier", "1b").replace("b", ""));
						effectInstance = new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(BuiltInRegistries.MOB_EFFECT.getValue(effectId)), durationTicks, amplifier);
					}

					WardInfo info = new WardInfo(entityType, effectInstance);
					output.store("ward_info", WardInfo.CODEC, info);

					if (settings.getStringOr("hasLoot", "0b").equals("1b")) {
						chestOutput.putString("LootTable", settings.getStringOr("lootTableName", ""));
					} else {
						writeItemsToTileEntity(input.childrenListOrEmpty("Items"), chestOutput);
					}
				}, () -> {
					writeItemsToTileEntity(input.childrenListOrEmpty("Items"), chestOutput);
				});

				//TODO: FIX FACING
				String facing = context.newState.getValue(BlockStateProperties.FACING).getName();
				output.store("block_capture", CapturedBlockInfo.CODEC, new CapturedBlockInfo(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.byName(facing)), chestOutput.buildResult()));
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
