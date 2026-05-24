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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
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
			case "wall_banner", "standing_banner" -> {
				output.putString("id", Identifier.withDefaultNamespace("banner").toString());
				ValueOutput.ValueOutputList patternsOut = output.childrenList("patterns");
				ValueInput.ValueInputList patterns = input.childrenListOrEmpty("Patterns");
				for (ValueInput pattern : patterns) {
					String patternName = pattern.getStringOr("Pattern", "");
					patternName = switch(patternName) {
						case "bs" -> "stripe_bottom";
						case "ts" -> "stripe_top";
						case "ls" -> "stripe_left";
						case "rs" -> "stripe_right";
						case "cs" -> "stripe_center";
						case "ms" -> "stripe_middle";
						case "drs" -> "stripe_downright";
						case "dls" -> "stripe_downleft";
						case "ss" -> "small_stripes";
						case "cr" -> "cross";
						case "sc" -> "straight_cross";
						case "bt" -> "triangle_bottom";
						case "tt" -> "triangle_top";
						case "bts" -> "triangles_bottom";
						case "tts" -> "triangles_top";
						case "ld" -> "diagonal_up_right";
						case "rd" -> "diagonal_up_left";
						case "lud" -> "diagonal_right";
						case "rud" -> "diagonal_left";
						case "mc" -> "circle";
						case "mr" -> "rhombus";
						case "vh" -> "half_vertical";
						case "hh" -> "half_horizontal";
						case "vhr" -> "half_vertical_right";
						case "hhb" -> "half_horizontal_bottom";
						case "bo" -> "border";
						case "cbo" -> "curly_border";
						case "gra" -> "gradient";
						case "gru" -> "gradient_up";
						case "bri" -> "bricks";
						case "cre" -> "creeper";
						case "sku" -> "skull";
						case "flo" -> "flower";
						case "moj" -> "mojang";
						case "glb" -> "globe";
						case "pig" -> "piglin";
						case "flw" -> "flow";
						case "gus" -> "guster";
						default -> {
							System.out.println("Cannot load banner pattern with name: " + patternName);
							yield "";
						}
					};

					DyeColor color = DyeColor.byId(15 - pattern.getIntOr("Color", 0));
					ValueOutput out = patternsOut.addChild();
					out.putString("pattern", patternName);
					out.putString("color", color.getSerializedName());
				}
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
						if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
							System.out.println("Cannot load entity with id: " + entityId + ", converting to pig!");
						}
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
						if (BuiltInRegistries.MOB_EFFECT.containsKey(effectId)) {
							MobEffect mobEffect = BuiltInRegistries.MOB_EFFECT.getValue(effectId);
							effectInstance = new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(mobEffect), durationTicks, amplifier);
						} else {
							System.out.println("Cannot load mob effect with id: " + effectId);
						}
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
