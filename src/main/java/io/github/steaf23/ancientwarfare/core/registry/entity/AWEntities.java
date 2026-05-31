package io.github.steaf23.ancientwarfare.core.registry.entity;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.npc.entity.BaseNpc;
import io.github.steaf23.ancientwarfare.npc.entity.faction.FactionNpc;
import io.github.steaf23.ancientwarfare.npc.entity.playerowned.PlayerOwnedNpc;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AWEntities {

	public static final EntityType<PlayerOwnedNpc> PLAYER_NPC = register("player_owned_npc",
			EntityType.Builder.of(PlayerOwnedNpc::new, MobCategory.CREATURE)
					.sized(0.6f, 1.8f));

	public static final EntityType<FactionNpc> FACTION_NPC = register("faction_npc",
			EntityType.Builder.of(FactionNpc::new, MobCategory.CREATURE));

	public static final Identifier NPC_TYPE_COMBAT = AncientWarfare.id("combat");
	public static final Identifier NPC_SUBTYPE_SOLDIER = AncientWarfare.id("soldier");
	public static final Identifier NPC_SUBTYPE_COMMANDER = AncientWarfare.id("commander");
	public static final Identifier NPC_SUBTYPE_ARCHER = AncientWarfare.id("archer");
	public static final Identifier NPC_TYPE_WORKER = AncientWarfare.id("worker");
	public static final Identifier NPC_SUBTYPE_MINER = AncientWarfare.id("miner");
	public static final Identifier NPC_SUBTYPE_SPELLCASTER = AncientWarfare.id("spellcaster");

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(PLAYER_NPC, BaseNpc.createNpcAttributes());
		FabricDefaultAttributeRegistry.register(FACTION_NPC, BaseNpc.createNpcAttributes());

		registerNpcs();
		registerFactions();
	}

	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
		Identifier id = AncientWarfare.id(name);
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
	}

	private static void registerNpcs() {

	}

	private static void registerFactions() {

	}

	public record NpcDeclaration(String itemModelVariant, boolean spawnBaseEntity, String entityName, String npcType) {

	}

}
