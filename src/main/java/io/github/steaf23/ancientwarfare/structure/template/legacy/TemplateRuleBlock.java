package io.github.steaf23.ancientwarfare.structure.template.legacy;


import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;


public class TemplateRuleBlock extends TemplateRule {
	protected BlockState state = Blocks.AIR.defaultBlockState();
	private Identifier legacyId;
	private ItemStack cachedStack = null;
	private boolean placeInSurvival = false;
	private ValueInput nbt = null;

	public TemplateRuleBlock() {
	}

	@Override
	public List<ItemStack> getResources() {
		if (state.getBlock() == Blocks.AIR) {
			return Collections.emptyList();
		}

		ItemStack stack = getCachedStack();
		if (!stack.isEmpty()) {
			return Collections.singletonList(stack);
		}

		return Collections.emptyList();
	}

	private ItemStack getCachedStack() {
		cacheStack();
		return cachedStack;
	}

	private void cacheStack() {
		if (cachedStack == null) {
//			Optional<ItemStack> stack = getStack();
//			placeInSurvival = stack.isPresent();
//			cachedStack = stack.orElse(ItemStack.EMPTY);
		}
	}

	@Override
	public boolean shouldPlaceOnBuildPass(Level world, int turns, BlockPos pos, int buildPass) {
		return false;
	}

	@Override
	public boolean placeInSurvival() {
		cacheStack();
		return state.getBlock() != Blocks.AIR && placeInSurvival;
	}

	@Override
	protected String getRuleType() {
		return "rule";
	}

	@Override
	public void parseRule(ValueInput tag) {
		try {
			legacyId = Identifier.parse(tag.childOrEmpty("blockState").getStringOr("blockName", "minecraft:air"));
			state = readBlockState(tag);
			this.nbt = tag.childOrEmpty("teData");
		}
		catch (MissingResourceException e) {
//			AncientWarfareStructure.LOG.warn("Unable to find blockstate while parsing structure template thus replacing it with air - {}.", e.getMessage());
			state = Blocks.AIR.defaultBlockState();
		}
		catch (TemplateParsingException.TemplateRuleParsingException e) {
			System.out.println(e.getMessage());
			state = Blocks.AIR.defaultBlockState();
		}
	}

	public BlockState getState() {
		return state;
	}

	public void writeBlockEntityData(ValueOutput output) {
		TemplateBlockEntityDataParser.convert(legacyId, nbt, output);
	}
//
//	@Nullable
//	public TileEntity getTileEntity(int turns) {
//		return null;
//	}

	@SuppressWarnings("squid:S1172")
	public boolean isDynamicallyRendered(int turns) {
		return false;
	}


	public BlockState readBlockState(ValueInput data) throws TemplateParsingException.TemplateRuleParsingException {
		ValueInput blockStateData = data.childOrEmpty("blockState");
		Identifier originalId = Identifier.parse(blockStateData.getStringOr("blockName", "minecraft:air"));
		Identifier blockId = originalId;
		ValueInput stateProperties = blockStateData.childOrEmpty("properties");
		if (blockId.getNamespace().equals("minecraft")) {
			switch (blockId.getPath()) {
				case "planks", "log", "log2" -> {
					String variant = stateProperties.getStringOr("variant", "");
					if (variant.isEmpty()) {
						return Blocks.AIR.defaultBlockState();
					}
					blockId = Identifier.withDefaultNamespace(variant + "_" + blockId.getPath().replace("2", ""));
				}
				case "wooden_slab", "double_wooden_slab" -> {
					String variant = stateProperties.getStringOr("variant", "");
					if (variant.isEmpty()) {
						return Blocks.AIR.defaultBlockState();
					}
					blockId = Identifier.withDefaultNamespace(variant + "_slab");
				}
				case "wooden_door" -> blockId = Identifier.withDefaultNamespace("oak_door");
				case "lit_pumpkin" ->
						blockId = Identifier.withDefaultNamespace("jack_o_lantern");
				case "magma" -> blockId = Identifier.withDefaultNamespace("magma_block");
				case "fence" -> blockId = Identifier.withDefaultNamespace("oak_fence");
				case "fence_gate" ->
						blockId = Identifier.withDefaultNamespace("oak_fence_gate");
				case "noteblock" -> blockId = Identifier.withDefaultNamespace("note_block");
				case "stonebrick" -> {
					String variant = stateProperties.getStringOr("variant", "");
					blockId = switch (variant) {
						case "stonebrick" -> Identifier.withDefaultNamespace("stone_bricks");
						case "chiseled_stonebrick" -> Identifier.withDefaultNamespace("chiseled_stone_bricks");
						case "mossy_stonebrick" -> Identifier.withDefaultNamespace("mossy_stone_bricks");
						case "cracked_stonebrick" -> Identifier.withDefaultNamespace("cracked_stone_bricks");
						default -> Identifier.withDefaultNamespace(variant);
					};
				}
				case "bed" -> {
					int bedColor = data.getIntOr("bedColor", 0);
					blockId = Identifier.withDefaultNamespace(DyeColor.byId(bedColor).getName() + "_bed");
				}
				case "dirt" -> {
					String variant = stateProperties.getStringOr("variant", "");
					if (variant.isEmpty()) {
						return Blocks.AIR.defaultBlockState();
					}

					if (variant.equals("coarse_dirt")) {
						blockId = Identifier.withDefaultNamespace("coarse_dirt");
					}
				}
				case "grass_path" -> blockId = Identifier.withDefaultNamespace("dirt_path");
				case "stone_stairs" ->
						blockId = Identifier.withDefaultNamespace("cobblestone_stairs");
				case "nether_brick" ->
						blockId = Identifier.withDefaultNamespace("nether_bricks");
				case "daylight_detector_inverted" ->
						blockId = Identifier.withDefaultNamespace("daylight_detector");
				case "red_nether_brick" ->
						blockId = Identifier.withDefaultNamespace("red_nether_bricks");
				case "web" -> blockId = Identifier.withDefaultNamespace("cobweb");
				case "snow_layer" -> blockId = Identifier.withDefaultNamespace("snow");
				case "trapdoor" -> blockId = Identifier.withDefaultNamespace("oak_trapdoor");
				case "wooden_button" -> blockId = Identifier.withDefaultNamespace("oak_button");
				case "wooden_pressure_plate" ->
						blockId = Identifier.withDefaultNamespace("oak_pressure_plate");
				case "silver_glazed_terracotta" ->
						blockId = Identifier.withDefaultNamespace("light_gray_glazed_terracotta");
				case "grass" -> blockId = Identifier.withDefaultNamespace("grass_block");
				case "end_bricks" ->
						blockId = Identifier.withDefaultNamespace("end_stone_bricks");
				case "slime" -> blockId = Identifier.withDefaultNamespace("slime_block");
				case "quartz_ore" ->
						blockId = Identifier.withDefaultNamespace("nether_quartz_ore");
				case "reeds" -> blockId = Identifier.withDefaultNamespace("sugar_cane");
				case "deadbush" -> blockId = Identifier.withDefaultNamespace("dead_bush");
				case "melon_block" -> blockId = Identifier.withDefaultNamespace("melon");
				case "portal" -> blockId = Identifier.withDefaultNamespace("nether_portal");
				case "mob_spawner" -> blockId = Identifier.withDefaultNamespace("spawner");
				case "lit_furnace" -> blockId = Identifier.withDefaultNamespace("furnace");
				case "lit_redstone_lamp" ->
						blockId = Identifier.withDefaultNamespace("redstone_lamp");
				case "lit_redstone_ore" ->
						blockId = Identifier.withDefaultNamespace("redstone_ore");
				case "unlit_redstone_torch" ->
						blockId = Identifier.withDefaultNamespace("redstone_torch");
				case "powered_repeater" ->
						blockId = Identifier.withDefaultNamespace("repeater");
				case "unpowered_repeater" ->
						blockId = Identifier.withDefaultNamespace("repeater");
				case "unpowered_comparator" ->
						blockId = Identifier.withDefaultNamespace("comparator");
				case "powered_comparator" ->
						blockId = Identifier.withDefaultNamespace("comparator");
				case "hardened_clay" -> blockId = Identifier.withDefaultNamespace("terracotta");
				case "waterlily" -> blockId = Identifier.withDefaultNamespace("lily_pad");
				case "brick_block" -> blockId = Identifier.withDefaultNamespace("bricks");
				case "stained_hardened_clay" -> {
					blockId = Identifier.withDefaultNamespace(getColorString(blockStateData) + "_terracotta");
				}
				case "leaves", "leaves2" -> {
					blockId = Identifier.withDefaultNamespace(stateProperties.getStringOr("variant", blockId.getPath()) + "_leaves");
				}
				case "brown_mushroom_block", "red_mushroom_block" -> {
					if (stateProperties.getStringOr("variant", "").equals("stem")) {
						blockId = Identifier.withDefaultNamespace("mushroom_stem");
					}
				}
				case "sapling" -> {
					blockId = Identifier.withDefaultNamespace(stateProperties.getStringOr("type", blockId.getPath()) + "_sapling");
				}
				case "stained_glass", "stained_glass_pane", "wool", "carpet", "concrete", "concrete_powder",
				     "wall_banner" -> {
					blockId = Identifier.withDefaultNamespace(getColorString(blockStateData) + "_" + blockId.getPath());
				}
				case "standing_banner" -> {
					blockId = Identifier.withDefaultNamespace(getColorString(blockStateData) + "_banner");
				}
				case "sandstone", "red_sandstone" -> {
					blockId = Identifier.withDefaultNamespace(stateProperties.getStringOr("type", blockId.getPath()));
				}
				case "stone_slab", "double_stone_slab", "stone_slab2", "double_stone_slab2" -> {
					String variant = stateProperties.getStringOr("variant", "stone");
					variant = variant.equals("stone") ? "smooth_stone" : variant;
					variant = variant.equals("wood_old") ? "oak" : variant;
					blockId = Identifier.withDefaultNamespace(variant + "_slab");

					if (originalId.getPath().contains("double_stone_slab")) {
						if (stateProperties.getStringOr("seamless", "false").equals("true")) {
							String smoothVariant = stateProperties.getStringOr("variant", "stone");

							if (!smoothVariant.equals("nether_brick") && !smoothVariant.equals("stone_brick")) {
								blockId = Identifier.withDefaultNamespace("smooth_" + stateProperties.getStringOr("variant", "stone"));
							}
						}
					}
				}
				case "prismarine" -> {
					blockId = Identifier.withDefaultNamespace(stateProperties.getStringOr("variant", blockId.getPath()));
				}
				case "red_flower", "yellow_flower" -> {
					blockId = Identifier.withDefaultNamespace(getFlowerString(blockStateData));
				}
				case "flower_pot" -> {
					blockId = Identifier.withDefaultNamespace(getFlowerPotString(data.getStringOr("itemName", ""), data.getIntOr("itemMeta", 0)));
				}
				case "skull" -> {
					String skullType = data.childOrEmpty("teData").getStringOr("SkullType", "0b");
					switch (skullType) {
						case "0b" ->
								blockId = Identifier.withDefaultNamespace("skeleton_skull");
						case "1b" ->
								blockId = Identifier.withDefaultNamespace("wither_skeleton_skull");
						case "2b" -> blockId = Identifier.withDefaultNamespace("zombie_head");
						case "3b" -> blockId = Identifier.withDefaultNamespace("player_head");
						case "4b" -> blockId = Identifier.withDefaultNamespace("creeper_head");
						case "5b" -> blockId = Identifier.withDefaultNamespace("dragon_head");
					}
				}
				case "monster_egg" -> {
					String blockType = stateProperties.getStringOr("variant", "stone");
					switch (blockType) {
						case "stone" ->
								blockId = Identifier.withDefaultNamespace("infested_stone");
						case "cobblestone" ->
								blockId = Identifier.withDefaultNamespace("infested_cobblestone");
						case "stone_brick" ->
								blockId = Identifier.withDefaultNamespace("infested_stone_bricks");
						case "mossy_brick" ->
								blockId = Identifier.withDefaultNamespace("infested_mossy_stone_bricks");
						case "cracked_brick" ->
								blockId = Identifier.withDefaultNamespace("infested_cracked_stone_bricks");
						case "chiseled_brick" ->
								blockId = Identifier.withDefaultNamespace("infested_chiseled_stone_bricks");
					}
				}
				case "stone" -> {
					String blockType = stateProperties.getStringOr("variant", "stone");
					blockType = switch (blockType) {
						case "smooth_andesite" -> "polished_andesite";
						case "smooth_diorite" -> "polished_diorite";
						case "smooth_granite" -> "polished_granite";
						default -> blockType;
					};
					blockId = Identifier.withDefaultNamespace(blockType);
				}
				case "tallgrass" -> {
					String grassType = stateProperties.getStringOr("type", "tall_grass");
					if (grassType.equals("tall_grass")) {
						blockId = Identifier.withDefaultNamespace("short_grass");
					} else if (grassType.equals("fern")) {
						blockId = Identifier.withDefaultNamespace("fern");
					} else {
						blockId = Identifier.withDefaultNamespace(grassType);
					}
				}
				case "double_plant" -> {
					String grassType = stateProperties.getStringOr("variant", "double_grass");
					switch (grassType) {
						case "double_grass" ->
								blockId = Identifier.withDefaultNamespace("tall_grass");
						case "sunflower" ->
								blockId = Identifier.withDefaultNamespace("sunflower");
						case "syringa" -> blockId = Identifier.withDefaultNamespace("lilac");
						case "double_fern" ->
								blockId = Identifier.withDefaultNamespace("large_fern");
						case "double_rose" ->
								blockId = Identifier.withDefaultNamespace("rose_bush");
						case "paeonia" -> blockId = Identifier.withDefaultNamespace("peony");
						default -> blockId = Identifier.withDefaultNamespace(grassType);
					}
				}
				case "wall_sign", "standing_sign" ->
						blockId = Identifier.withDefaultNamespace("oak_sign");
				case "anvil" -> {
					String damage = stateProperties.getStringOr("damage", "0");
					switch (damage) {
						case "0" -> blockId = Identifier.withDefaultNamespace("anvil");
						case "1" -> blockId = Identifier.withDefaultNamespace("chipped_anvil");
						case "2" -> blockId = Identifier.withDefaultNamespace("damaged_anvil");
					}
				}
				case "torch" -> {
					if (stateProperties.getStringOr("facing", "up").equals("up")) {
						blockId = Identifier.withDefaultNamespace("torch");
					} else {
						blockId = Identifier.withDefaultNamespace("wall_torch");
					}
				}
				case "redstone_torch" -> {
					if (stateProperties.getStringOr("facing", "up").equals("up")) {
						blockId = Identifier.withDefaultNamespace("redstone_torch");
					} else {
						blockId = Identifier.withDefaultNamespace("redstone_wall_torch");
					}
				}
				case "cauldron" -> {
					if (stateProperties.getStringOr("level", "0").equals("0")) {
						blockId = Identifier.withDefaultNamespace("cauldron");
					} else {
						blockId = Identifier.withDefaultNamespace("water_cauldron");
					}
				}
			}
		} else if (blockId.getNamespace().equals("ancientwarfarestructure")) {
			switch (blockId.getPath()) {
				case "fire_pit" -> blockId = Identifier.withDefaultNamespace("campfire");
				case "advanced_loot_chest" -> blockId = Identifier.withDefaultNamespace("chest");
			}
		}

		var opt = BuiltInRegistries.BLOCK.get(blockId);
		if (opt.isEmpty()) {
			if (blockId.getNamespace().equals("minecraft")) {
				System.out.println("block with id " + blockId + " not registered");
			}
			return Blocks.AIR.defaultBlockState();
		}

		Map<Property<?>, Comparable<?>> propertyMap = new HashMap<>();
		Block block = opt.get().value();

		BlockState defaultState = block.defaultBlockState();
		for (Property<?> prop : defaultState.getProperties()) {
			propertyMap.put(prop, defaultState.getValue(prop));
		}

		if (originalId.getPath().contains("double_") && originalId.getPath().contains("_slab")) {
			propertyMap.put(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE);
		}
		if (originalId.getPath().equals("daylight_detector_inverted")) {
			propertyMap.put(BlockStateProperties.INVERTED, true);
		}
		if (originalId.getPath().contains("unlit_")) {
			propertyMap.put(BlockStateProperties.LIT, false);
		}
		else if (originalId.getPath().contains("lit_")) {
			propertyMap.put(BlockStateProperties.LIT, true);
		}
		if (originalId.getPath().contains("unpowered_")) {
			propertyMap.put(BlockStateProperties.POWERED, false);
		}
		else if (originalId.getPath().contains("powered_")) {
			propertyMap.put(BlockStateProperties.POWERED, true);
		}
		if (originalId.getPath().equals("skull")) {
			byte rotationData = data.getByteOr("skullRotation", (byte)0);
			propertyMap.put(SkullBlock.ROTATION, (int)rotationData);
		}

		if (blockId.getPath().equals("mushroom_stem") && !stateProperties.getStringOr("variant", "stem").equals("all_stem")) {
			propertyMap.put(HugeMushroomBlock.UP, false);
			propertyMap.put(HugeMushroomBlock.DOWN, false);
		} else if (blockId.getPath().contains("mushroom_block")) {
			propertyMap.put(HugeMushroomBlock.NORTH, false);
			propertyMap.put(HugeMushroomBlock.SOUTH, false);
			propertyMap.put(HugeMushroomBlock.EAST, false);
			propertyMap.put(HugeMushroomBlock.WEST, false);
			propertyMap.put(HugeMushroomBlock.UP, false);
			propertyMap.put(HugeMushroomBlock.DOWN, false);

			switch (stateProperties.getStringOr("variant", "all_outside")) {
				case "north" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.NORTH, true);
				}
				case "south" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.SOUTH, true);
				}
				case "west" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.WEST, true);
				}
				case "east" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.EAST, true);
				}
				case "center" ->
				{
					propertyMap.put(HugeMushroomBlock.UP, true);
				}
				case "north_west" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.NORTH, true);
					propertyMap.put(HugeMushroomBlock.WEST, true);
				}
				case "north_east" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.NORTH, true);
					propertyMap.put(HugeMushroomBlock.EAST, true);
				}
				case "south_west" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.SOUTH, true);
					propertyMap.put(HugeMushroomBlock.WEST, true);
				}
				case "south_east" -> {
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.SOUTH, true);
					propertyMap.put(HugeMushroomBlock.EAST, true);
				}
				case "all_outside" -> {
					propertyMap.put(HugeMushroomBlock.NORTH, true);
					propertyMap.put(HugeMushroomBlock.SOUTH, true);
					propertyMap.put(HugeMushroomBlock.EAST, true);
					propertyMap.put(HugeMushroomBlock.WEST, true);
					propertyMap.put(HugeMushroomBlock.UP, true);
					propertyMap.put(HugeMushroomBlock.DOWN, true);
				}
			}
		}

		for (String propName : stateProperties.keySet()) {
			// TODO: check which one of these to check for item types
			if (switch (propName) {
				case "variant",
					 "contents",
					 "damage",
					 "custom",
					 "type",
					 "wet",
					 "explode",
					 "color",
					 "seamless",
					 "nodrop",
					 "check_decay",
					 "legacy_data" -> true;
				default -> false;
			}) {
				continue;
			}

			Property<?> property = switch (propName) {
				case "snowy" -> BlockStateProperties.SNOWY;
				case "facing" -> BlockStateProperties.HORIZONTAL_FACING;
				case "shape" -> {
					if (blockId.toString().contains("stairs")) {
						yield BlockStateProperties.STAIRS_SHAPE;
					}
					else {
						yield BlockStateProperties.RAIL_SHAPE;
					}
				}
				case "half" -> {
					if (blockId.toString().contains("door")) {
						yield BlockStateProperties.DOUBLE_BLOCK_HALF;
					}
					else if (blockId.toString().contains("slab")) {
						yield BlockStateProperties.SLAB_TYPE;
					}
					else if (originalId.getPath().equals("double_plant")) {
						yield DoublePlantBlock.HALF;
					}
					else {
						yield BlockStateProperties.HALF;
					}
				}
				case "hinge" -> BlockStateProperties.DOOR_HINGE;
				case "powered" -> BlockStateProperties.POWERED;
				case "south" -> BlockStateProperties.SOUTH;
				case "north" -> BlockStateProperties.NORTH;
				case "east" -> BlockStateProperties.EAST;
				case "west" -> BlockStateProperties.WEST;
				case "up" -> BlockStateProperties.UP;
				case "down" -> BlockStateProperties.DOWN;
				case "axis" -> BlockStateProperties.AXIS;
				case "lit" -> BlockStateProperties.LIT;
				case "open" -> BlockStateProperties.OPEN;
				case "part" -> BlockStateProperties.BED_PART;
				case "moisture" -> BlockStateProperties.MOISTURE;
				case "age" -> switch (blockId.getPath()) {
					case "nether_wart" -> NetherWartBlock.AGE;
					case "beetroots" -> BeetrootBlock.AGE;
					case "cocoa" -> CocoaBlock.AGE;
					case "chorus_flower" -> ChorusFlowerBlock.AGE;
					case "pumpkin_stem", "melon_stem" -> StemBlock.AGE;
					case "wheat", "potatoes", "carrots" -> CropBlock.AGE;
					case "reeds" -> SugarCaneBlock.AGE;
					case "fire" -> FireBlock.AGE;
					case "cactus" -> CactusBlock.AGE;
					default -> BlockStateProperties.AGE_25;
				};
				case "level" -> BlockStateProperties.LEVEL_CAULDRON;
				case "in_wall" -> BlockStateProperties.IN_WALL;
				case "occupied" -> BlockStateProperties.OCCUPIED;
				case "has_bottle_0" -> BlockStateProperties.HAS_BOTTLE_0;
				case "has_bottle_1" -> BlockStateProperties.HAS_BOTTLE_1;
				case "has_bottle_2" -> BlockStateProperties.HAS_BOTTLE_2;
				case "power" -> BlockStateProperties.POWER;
				case "enabled" -> BlockStateProperties.ENABLED;
				case "triggered" -> BlockStateProperties.TRIGGERED;
				case "eye" -> BlockStateProperties.EYE;
				case "disarmed" -> BlockStateProperties.DISARMED;
				case "extended" -> BlockStateProperties.EXTENDED;
				case "bites" -> BlockStateProperties.BITES;
				case "attached" -> BlockStateProperties.ATTACHED;
				case "has_record" -> BlockStateProperties.HAS_RECORD;
				case "conditional" -> BlockStateProperties.CONDITIONAL;
				case "short" -> BlockStateProperties.SHORT;
				case "decayable" -> LeavesBlock.PERSISTENT;
				case "layers" -> BlockStateProperties.LAYERS;
				case "rotation" -> BlockStateProperties.ROTATION_16;
				case "stage" -> BlockStateProperties.STAGE;
				case "delay" -> BlockStateProperties.DELAY;
				case "mode" -> BlockStateProperties.MODE_COMPARATOR;
				case "locked" -> BlockStateProperties.LOCKED;
				default -> throw new TemplateParsingException.TemplateRuleParsingException("Unknown block state property: " + propName + " for block " + blockId);
			};
			String propertyValue = stateProperties.getStringOr(propName, "");
			if (propName.equals("decayable")) { // decayable to persistent flips the meaning of the property.
				propertyValue = propertyValue.equals("true") ? "false" : "true";
			}
			Optional<? extends Comparable<?>> value = property.getValue(propertyValue);
			value.ifPresent(v -> propertyMap.put(property, v));
		}

		BlockState state = new BlockState(block, propertyMap.keySet().toArray(Property[]::new), propertyMap.values().toArray(Comparable[]::new));
		return state;
	}

	private static String getFlowerString(ValueInput blockState) {
		return blockState.childOrEmpty("properties").getStringOr("type", "").replace("houstonia", "azure_bluet");
	}

	private static String getFlowerPotString(String itemName, int itemMeta) {
		if (itemName.isEmpty()) {
			return "flower_pot";
		}
		Identifier id = Identifier.parse(itemName);
		return "potted_" + switch (id.getPath()) {
			case "sapling" -> TemplateRuleItem.saplingNameFromMeta(itemMeta);
			case "red_flower" -> TemplateRuleItem.redFlowerNameFromMeta(itemMeta);
			case "yellow_flower" -> "dandelion";
			case "deadbush" -> "dead_bush";
			case "tallgrass" -> "short_grass";
			default -> id.getPath();
		};
	}

	private static String getColorString(ValueInput blockState) {
		return blockState.childOrEmpty("properties").getStringOr("color", "white").replace("silver", "light_gray");
	}

	public boolean hasBlockEntityData() {
		return nbt != null;
	}
}