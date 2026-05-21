package io.github.steaf23.ancientwarfare.structure.template.legacy;

import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.util.CoinMetal;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;

public class TemplateRuleItem {

	public static ItemStackTemplate fromOldId(Identifier oldId, int meta, int count, CompoundTag tag) {
		Identifier newId = oldId;
		if (oldId.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
			switch (oldId.getPath()) {
				case "speckled_melon" -> newId = Identifier.withDefaultNamespace("glistering_melon_slice");
				case "dye" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "ink_sac";
					case 1 -> "red_dye";
					case 2 -> "green_dye";
					case 3 -> "cocoa_beans";
					case 4 -> "lapis_lazuli";
					case 5 -> "purple_dye";
					case 6 -> "cyan_dye";
					case 7 -> "light_gray_dye";
					case 8 -> "gray_dye";
					case 9 -> "pink_dye";
					case 10 -> "lime_dye";
					case 11 -> "yellow_dye";
					case 12 -> "light_blue_dye";
					case 13 -> "magenta_dye";
					case 14 -> "orange_dye";
					case 15 -> "bone_meal";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "fish" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "cod";
					case 1 -> "salmon";
					case 2 -> "tropical_fish";
					case 3 -> "pufferfish";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "cooked_fish" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "cooked_cod";
					case 1 -> "cooked_salmon";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "planks" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "oak";
					case 1 -> "spruce";
					case 2 -> "birch";
					case 3 -> "jungle";
					case 4 -> "acacia";
					case 5 -> "dark_oak";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				} + "_planks");
				case "wooden_slab" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "oak";
					case 1 -> "spruce";
					case 2 -> "birch";
					case 3 -> "jungle";
					case 4 -> "acacia";
					case 5 -> "dark_oak";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				} + "_slab");
				case "log" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "oak";
					case 1 -> "spruce";
					case 2 -> "birch";
					case 3 -> "jungle";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				} + "_log");
				case "log2" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "acacia_log";
					case 1 -> "dark_oak_log";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "stonebrick" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "stone_bricks";
					case 1 -> "mossy_stone_bricks";
					case 2 -> "cracked_stone_bricks";
					case 3 -> "chiseled_stone_bricks";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "double_plant" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "sunflower";
					case 1 -> "lilac";
					case 2 -> "tall_grass";
					case 3 -> "large_fern";
					case 4 -> "rose_bush";
					case 5 -> "peony";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "skull" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "skeleton_skull";
					case 1 -> "wither_skeleton_skull";
					case 2 -> "zombie_head";
					case 3 -> "player_head";
					case 4 -> "creeper_head";
					case 5 -> "dragon_head";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "tallgrass" -> newId = Identifier.withDefaultNamespace(switch (meta) {
					case 0 -> "shrub"; // non-existent in modern MC 26.
					case 1 -> "short_grass";
					case 2 -> "fern";
					default -> {
						System.out.println("Converted item to air! (" + oldId.toString() + "), " + meta);
						yield "";
					}
				});
				case "wool" -> newId = Identifier.withDefaultNamespace(colorNameFromMeta(meta) + "_wool");
				case "red_flower" -> newId = Identifier.withDefaultNamespace(redFlowerNameFromMeta(meta));
				case "sapling" -> newId = Identifier.withDefaultNamespace(saplingNameFromMeta(meta));
				case "wooden_pressure_plate" -> newId = Identifier.withDefaultNamespace("oak_pressure_plate");
				case "wooden_door" -> newId = Identifier.withDefaultNamespace("oak_door");
				case "golden_rail" -> newId = Identifier.withDefaultNamespace("powered_rail");
				case "yellow_flower" -> newId = Identifier.withDefaultNamespace("dandelion");
				case "reeds" -> newId = Identifier.withDefaultNamespace("sugar_cane");
				case "web" -> newId = Identifier.withDefaultNamespace("cobweb");
				case "fireworks" -> newId = Identifier.withDefaultNamespace("firework_rocket");
				case "firework_charge" -> newId = Identifier.withDefaultNamespace("firework_star");
				case "fence" -> newId = Identifier.withDefaultNamespace("oak_fence");
				case "sign" -> newId = Identifier.withDefaultNamespace("oak_sign");
			}
			if (oldId.getPath().contains("record_")) {
				newId = Identifier.withDefaultNamespace(oldId.getPath().replace("record_", "music_disc_"));
			}
			if (!BuiltInRegistries.ITEM.containsKey(newId)) {
				System.out.println("Unknown item with id: " + newId + " (meta: " + meta + ")");
			}
			return new ItemStackTemplate(BuiltInRegistries.ITEM.getValue(newId), count);
		} else {
			DataComponentPatch.Builder components = DataComponentPatch.builder();
			switch (oldId.toString()) {
				case "ancientwarfarenpc:coin" -> {
					components.set(AWComponents.COIN_METAL, CoinMetal.fromName(tag.getStringOr("metal", "gold")));
					newId = BuiltInRegistries.ITEM.getKey(AWItems.COINS);
				}
				default -> {
					if (!BuiltInRegistries.ITEM.containsKey(oldId)) {
						System.out.println("cannot load item with id: " + oldId);
						return null;
					}
				}
			}

			return new ItemStackTemplate(BuiltInRegistries.ITEM.getValue(newId), count);
		}
	}

	public static String redFlowerNameFromMeta(int meta) {
		return switch (meta) {
			case 0 -> "poppy";
			case 1 -> "blue_orchid";
			case 2 -> "allium";
			case 3 -> "azure_bluet";
			case 4 -> "red_tulip";
			case 5 -> "orange_tulip";
			case 6 -> "white_tulip";
			case 7 -> "pink_tulip";
			case 8 -> "oxeye_daisy";
			default -> meta  + "";
		};
	}

	public static String saplingNameFromMeta(int meta) {
		return switch (meta) {
			case 0 -> "oak";
			case 1 -> "spruce";
			case 2 -> "birch";
			case 3 -> "jungle";
			case 4 -> "acacia";
			case 5 -> "dark_oak";
			default -> meta + "";
		} + "_sapling";
	}

	public static String colorNameFromMeta(int meta) {
		return switch (meta) {
			case 0 -> "white";
			case 1 -> "orange";
			case 2 -> "magenta";
			case 3 -> "light_blue";
			case 4 -> "yellow";
			case 5 -> "lime";
			case 6 -> "pink";
			case 7 -> "gray";
			case 8 -> "light_gray";
			case 9 -> "cyan";
			case 10 -> "purple";
			case 11 -> "blue";
			case 12 -> "brown";
			case 13 -> "green";
			case 14 -> "red";
			case 15 -> "black";
			default -> meta + "";
		};
	}
}
