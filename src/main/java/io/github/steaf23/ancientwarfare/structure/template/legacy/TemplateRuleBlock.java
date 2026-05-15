package io.github.steaf23.ancientwarfare.structure.template.legacy;


import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.fixes.BedItemColorFix;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.ValueInput;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;


public class TemplateRuleBlock extends TemplateRule {
	protected BlockState state = Blocks.AIR.defaultBlockState();
	private ItemStack cachedStack = null;
	private boolean placeInSurvival = false;

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
			state = readBlockState(tag);
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
		switch (blockId.getPath()) {
			case "planks", "log", "log2" -> {
				String variant = stateProperties.getStringOr("variant", "");
				if (variant.isEmpty()) {
					return Blocks.AIR.defaultBlockState();
				}
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), variant + "_" + blockId.getPath().replace("2", ""));
			}
			case "wooden_slab", "double_wooden_slab" -> {
				String variant = stateProperties.getStringOr("variant", "");
				if (variant.isEmpty()) {
					return Blocks.AIR.defaultBlockState();
				}
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), variant + "_slab");
			}
			case "wooden_door" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "oak_door");
			case "lit_pumpkin" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "jack_o_lantern");
			case "magma" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "magma_block");
			case "fence" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "oak_fence");
			case "fence_gate" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "oak_fence_gate");
			case "noteblock" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "note_block");
			case "stonebrick" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "stone_bricks");
			case "bed" -> {
				int bedColor = data.getIntOr("bedColor", 0);
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), DyeColor.byId(bedColor).getName() + "_bed");
			}
			case "dirt" -> {
				String variant = stateProperties.getStringOr("variant", "");
				if (variant.isEmpty()) {
					return Blocks.AIR.defaultBlockState();
				}

				if (variant.equals("coarse_dirt")) {
					blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "coarse_dirt");
				}
			}
			case "grass_path" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "dirt_path");
			case "stone_stairs" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "cobblestone_stairs");
			case "nether_brick" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "nether_bricks");
			case "daylight_detector_inverted" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "daylight_detector");
			case "red_nether_brick" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "red_nether_bricks");
			case "web" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "cobweb");
			case "snow_layer" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "snow");
			case "trapdoor" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "oak_trapdoor");
			case "wooden_button" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "oak_button");
			case "wooden_pressure_plate" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "oak_pressure_plate");
			case "silver_glazed_terracotta" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "light_gray_glazed_terracotta");
			case "grass" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "grass_block");
			case "end_bricks" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "end_stone_bricks");
			case "slime" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "slime_block");
			case "quartz_ore" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "nether_quartz_ore");
			case "reeds" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "sugar_cane");
			case "deadbush" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "dead_bush");
			case "melon_block" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "melon");
			case "portal" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "nether_portal");
			case "mob_spawner" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "spawner");
			case "lit_furnace" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "furnace");
			case "lit_redstone_lamp" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "redstone_lamp");
			case "lit_redstone_ore" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "redstone_ore");
			case "unlit_redstone_torch" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "redstone_torch");
			case "powered_repeater" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "repeater");
			case "unpowered_repeater" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "repeater");
			case "unpowered_comparator" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "comparator");
			case "powered_comparator" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "comparator");
			case "hardened_clay" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "terracotta");
			case "waterlily" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "lily_pad");
			case "brick_block" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "bricks");
			case "stained_hardened_clay" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), getColorString(blockStateData) + "_terracotta");
			}
			case "leaves", "leaves2" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), stateProperties.getStringOr("variant", blockId.getPath()) + "_leaves");
			}
			case "brown_mushroom_block", "red_mushroom_block" -> {
				if (stateProperties.getStringOr("variant", "").equals("stem")) {
					blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "mushroom_stem");
				}
			}
			case "sapling" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), stateProperties.getStringOr("type", blockId.getPath()) + "_sapling");
			}
			case "stained_glass", "stained_glass_pane", "wool", "carpet", "concrete", "concrete_powder", "wall_banner" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), getColorString(blockStateData) + "_" + blockId.getPath());
			}
			case "standing_banner" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), getColorString(blockStateData) + "_banner");
			}
			case "sandstone", "red_sandstone" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), stateProperties.getStringOr("type", blockId.getPath()));
			}
			case "stone_slab", "double_stone_slab", "stone_slab2", "double_stone_slab2" -> {
				String variant = stateProperties.getStringOr("variant", "stone");
				variant = variant.equals("stone") ? "smooth_stone" : variant;
				variant = variant.equals("wood_old") ? "oak" : variant;
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), variant + "_slab");

				if (originalId.getPath().contains("double_stone_slab")) {
					if (stateProperties.getStringOr("seamless", "false").equals("true")) {
						blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "smooth_" + stateProperties.getStringOr("variant", "stone"));
					}
				}
			}
			case "prismarine" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), stateProperties.getStringOr("variant", blockId.getPath()));
			}
			case "red_flower", "yellow_flower" -> {
				blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), getFlowerString(blockStateData));
			}
			case "skull" -> {
				String skullType = data.childOrEmpty("teData").getStringOr("SkullType", "0b");
				switch (skullType) {
					case "0b" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "skeleton_skull");
					case "1b" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "wither_skeleton_skull");
					case "2b" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "zombie_head");
					case "3b" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "player_head");
					case "4b" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "creeper_head");
					case "5b" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "dragon_head");
				}
			}
			case "monster_egg" -> {
				String blockType = stateProperties.getStringOr("variant", "stone");
				switch (blockType) {
					case "stone" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "infested_stone");
					case "cobblestone" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "infested_cobblestone");
					case "stone_brick" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "infested_stone_bricks");
					case "mossy_brick" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "infested_mossy_stone_bricks");
					case "cracked_brick" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "infested_cracked_stone_bricks");
					case "chiseled_brick" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "infested_chiseled_stone_bricks");
				}
			}
			case "tallgrass" -> {
				String grassType = stateProperties.getStringOr("type", "tall_grass");
				if (grassType.equals("tall_grass")) {
					blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "short_grass");
				} else if (grassType.equals("fern")) {
					blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "fern");
				}
				else {
					blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), grassType);
				}
			}
			case "double_plant" -> {
				String grassType = stateProperties.getStringOr("variant", "double_grass");
				switch (grassType) {
					case "double_grass" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "tall_grass");
					case "sunflower" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "sunflower");
					case "syringa" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "lilac");
					case "double_fern" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "large_fern");
					case "double_rose" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "rose_bush");
					case "paeonia" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "peony");
					default -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), grassType);
				}
			}
			case "wall_sign", "standing_sign" -> blockId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "oak_sign");
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
				case "age" -> BlockStateProperties.AGE_25;
				case "level" -> BlockStateProperties.LEVEL;
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
				case "decayable" -> BlockStateProperties.PERSISTENT;
				case "layers" -> BlockStateProperties.LAYERS;
				case "rotation" -> BlockStateProperties.ROTATION_16;
				case "stage" -> BlockStateProperties.STAGE;
				case "delay" -> BlockStateProperties.DELAY;
				case "mode" -> BlockStateProperties.MODE_COMPARATOR;
				case "locked" -> BlockStateProperties.LOCKED;
				default -> throw new TemplateParsingException.TemplateRuleParsingException("Unknown block state property: " + propName + " for block " + blockId);
			};
			Optional<? extends Comparable<?>> value = property.getValue(stateProperties.getStringOr(propName, ""));
			value.ifPresent(v -> propertyMap.put(property, v));
		}

		BlockState state = new BlockState(block, propertyMap.keySet().toArray(Property[]::new), propertyMap.values().toArray(Comparable[]::new));
		return state;
	}

	private static String getFlowerString(ValueInput blockState) {
		return blockState.childOrEmpty("properties").getStringOr("color", "poppy").replace("houstonia", "azure_bluet");
	}

	private static String getColorString(ValueInput blockState) {
		return blockState.childOrEmpty("properties").getStringOr("color", "white").replace("silver", "light_gray");
	}
}