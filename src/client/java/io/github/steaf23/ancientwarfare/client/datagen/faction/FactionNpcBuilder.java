package io.github.steaf23.ancientwarfare.client.datagen.faction;

import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.registry.Factions;
import io.github.steaf23.ancientwarfare.npc.faction.AdditionalAttributes;
import io.github.steaf23.ancientwarfare.npc.faction.Faction;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipment;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentEmpty;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentFixed;
import io.github.steaf23.ancientwarfare.npc.item.NpcEquipmentTable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentTable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TypedEntityData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FactionNpcBuilder {

	private ResourceKey<Faction> faction;
	private Identifier npcType;
	private String displayName;
	private Map<Identifier, Double> defaultAttributes;
	private int experienceDropped;
	private boolean canSwim;
	private boolean canBreakDoors;
	private boolean canOpenDoors;
	private @Nullable NpcEquipment equipment;
	private @Nullable Identifier lootTable;
	private boolean enabled;
	private Set<Identifier> spells = new HashSet<>();
	private @Nullable TypedEntityData<EntityType<?>> mount;
	private boolean burnsInSun;
	private boolean undead;
	private double healPerTry;
	private Identifier soundSet = AncientWarfare.id("player");

	public static FactionNpcBuilder builder() {
		FactionNpcBuilder builder = new FactionNpcBuilder();
		builder.setFromData(FactionNpcData.INVALID_DEFAULT);
		return builder;
	}

	public FactionNpcBuilder copy() {
		FactionNpcBuilder copy = new FactionNpcBuilder();
		copy.faction = this.faction;
		copy.npcType = this.npcType;
		copy.defaultAttributes = new HashMap<>(this.defaultAttributes);
		copy.experienceDropped = this.experienceDropped;
		copy.canSwim = this.canSwim;
		copy.canBreakDoors = this.canBreakDoors;
		copy.canOpenDoors = this.canOpenDoors;
		copy.equipment = this.equipment;
		copy.lootTable = this.lootTable;
		copy.enabled = this.enabled;
		copy.spells = this.spells;
		// Additional
		copy.burnsInSun = this.burnsInSun;
		copy.undead = this.undead;
		copy.healPerTry = this.healPerTry;
		copy.soundSet = this.soundSet;
		return copy;
	}

	public FactionNpcBuilder setFromData(FactionNpcData data) {
		this.faction = data.faction();
		this.npcType = data.npcType();
		this.displayName = "";
		this.defaultAttributes = data.defaultAttributes();
		this.experienceDropped = data.experienceDropped();
		this.canSwim = data.canSwim();
		this.canBreakDoors = data.canBreakDoors();
		this.canOpenDoors = data.canOpenDoors();
		this.equipment = data.equipment();
		this.lootTable = data.lootTable();
		this.enabled = data.enabled();
		this.spells = data.spells();
		// Additional
		this.burnsInSun = data.additional().burnsInSun();
		this.undead = data.additional().undead();
		this.healPerTry = data.additional().healPerTry();
		this.soundSet = data.additional().soundSet();
		return this;
	}

	private FactionNpcBuilder() {
	}

	public FactionNpcBuilder npcType(Identifier type) {
		this.npcType = type;
		return this;
	}

	public FactionNpcBuilder faction(Identifier faction) {
		this.faction = ResourceKey.create(Factions.FACTION_REGISTRY_KEY, faction);
		return this;
	}

	public FactionNpcBuilder name(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getName() {
		if (displayName == null) {
			throw new IllegalStateException("Did not define a name for " + autoId());
		}
		return displayName;
	}

	public FactionNpcBuilder spells(Set<Identifier> spells) {
		this.spells = spells;
		return this;
	}

	public FactionNpcBuilder experienceDropped(int expPoints) {
		this.experienceDropped = expPoints;
		return this;
	}

	public FactionNpcBuilder navigation(boolean canSwim, boolean canBreakDoors, boolean canOpenDoors) {
		this.canSwim = canSwim;
		this.canBreakDoors = canBreakDoors;
		this.canOpenDoors = canOpenDoors;
		return this;
	}

	public FactionNpcBuilder canSwim(boolean canSwim) {
		this.canSwim = canSwim;
		return this;
	}

	public FactionNpcBuilder lootTable(Identifier lootTable) {
		this.lootTable = lootTable;
		return this;
	}

	public FactionNpcBuilder burnsInSun(boolean burnsInSun) {
		this.burnsInSun = burnsInSun;
		return this;
	}

	public FactionNpcBuilder undead(boolean undead) {
		this.undead = undead;
		return this;
	}

	public FactionNpcBuilder healPerTry(double healPerTry) {
		this.healPerTry = healPerTry;
		return this;
	}

	public FactionNpcBuilder soundSet(Identifier soundSet) {
		this.soundSet = soundSet;
		return this;
	}

	public FactionNpcBuilder equipmentTable(EquipmentTable table) {
		this.equipment = new NpcEquipmentTable(table);
		return this;
	}

	public FactionNpcBuilder addAttribute(Holder<Attribute> attribute, double defaultValue) {
		this.defaultAttributes.put(attribute.unwrapKey().map(ResourceKey::identifier).orElseThrow(), defaultValue);
		return this;
	}

	public FactionNpcBuilder equipment(Identifier mainHand, Identifier offHand) {
		this.equipment = new NpcEquipmentFixed(mainHand, offHand);
		return this;
	}

	public FactionNpcBuilder equipment(Item mainHand, Item offHand) {
		this.equipment = new NpcEquipmentFixed(mainHand == null ? null : BuiltInRegistries.ITEM.getKey(mainHand), offHand == null ? null : BuiltInRegistries.ITEM.getKey(offHand));
		return this;
	}

	public FactionNpcBuilder noEquipment() {
		this.equipment = new NpcEquipmentEmpty();
		return this;
	}

	public FactionNpcBuilder simpleMount(EntityType<?> entity) {
		this.mount = TypedEntityData.of(entity, new CompoundTag());
		return this;
	}

	public FactionNpcBuilder horseMount() {
		CompoundTag tag = new CompoundTag();
		CompoundTag saddleSlot = new CompoundTag();
		CompoundTag saddle = new CompoundTag();
		saddle.putInt("count", 1);
		saddle.putString("id", BuiltInRegistries.ITEM.getKey(Items.SADDLE).toString());

		tag.put("equipment", saddleSlot);
		saddleSlot.put("saddle", saddle);

		this.mount = TypedEntityData.of(EntityType.HORSE, tag);
		return this;
	}

	public FactionNpcData build(Identifier id) {
		return new FactionNpcData(
				id,
				faction,
				npcType,
				defaultAttributes,
				experienceDropped,
				canSwim,
				canBreakDoors,
				canOpenDoors,
				equipment,
				lootTable,
				enabled,
				spells,
				Optional.ofNullable(mount),
				new AdditionalAttributes(burnsInSun, undead, healPerTry, soundSet)
		);
	}

	public Identifier autoId() {
		return AncientWarfare.id((faction == null ? "" : faction.identifier().getPath() + "/") + npcType.getPath());
	}

	public String getTranslationKey() {
		Identifier id = autoId();
		return "npc." + id.getNamespace() + "." + id.getPath().replace("/", ".");
	}

	public FactionNpcData buildAutoId() {
		return build(autoId());
	}

	public static FactionNpcBuilder buildCavalry(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("cavalry"))
				.horseMount()
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MAX_HEALTH, 2)
				.experienceDropped(40)
				.equipment(Items.IRON_SWORD, null)
				.lootTable(AncientWarfare.id("entities/cavalry"));
	}

	public static FactionNpcBuilder buildSoldier(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("soldier"))
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.experienceDropped(20)
				.equipment(Items.IRON_SWORD, null)
				.lootTable(AncientWarfare.id("entities/soldier"));
	}

	public static FactionNpcBuilder buildEliteSoldier(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("elite_soldier"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0.325)
				.addAttribute(Attributes.ATTACK_DAMAGE, 2)
				.experienceDropped(30)
				.equipment(Items.IRON_SWORD, null)
				.lootTable(AncientWarfare.id("entities/elite_soldier"));
	}

	public static FactionNpcBuilder buildArcher(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("archer"))
				.addAttribute(Attributes.MAX_HEALTH, 20)
				.addAttribute(Attributes.FOLLOW_RANGE, 60)
				.experienceDropped(20)
				.equipment(Items.BOW, null)
				.lootTable(AncientWarfare.id("entities/archer"));
	}

	public static FactionNpcBuilder buildMountedArcher(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("mounted_archer"))
				.horseMount()
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(30)
				.equipment(Items.BOW, null)
				.lootTable(AncientWarfare.id("entities/mounted_archer"));
	}

	public static FactionNpcBuilder buildEliteArcher(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("elite_archer"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.FOLLOW_RANGE, 80)
				.experienceDropped(30)
				.equipment(Items.BOW, null)
				.lootTable(AncientWarfare.id("entities/elite_archer"));
	}

	public static FactionNpcBuilder buildLeader(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("leader"))
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.ATTACK_DAMAGE, 3)
				.experienceDropped(50)
				.equipment(Items.DIAMOND_SWORD, null)
				.lootTable(AncientWarfare.id("entities/leader"));
	}

	public static FactionNpcBuilder buildEliteLeader(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("elite_leader"))
				.addAttribute(Attributes.MAX_HEALTH, 60)
				.addAttribute(Attributes.ATTACK_DAMAGE, 4)
				.experienceDropped(60)
				.equipment(Items.DIAMOND_SWORD, null)
				.lootTable(AncientWarfare.id("entities/elite_leader"));
	}

	public static FactionNpcBuilder buildSiegeEngineer(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("siege_engineer"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.addAttribute(Attributes.FOLLOW_RANGE, 120)
				.experienceDropped(30)
				.lootTable(AncientWarfare.id("entities/siege_engineer"));
	}

	public static FactionNpcBuilder buildPriest(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("priest"))
				.addAttribute(Attributes.MAX_HEALTH, 40)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0.375)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.experienceDropped(40)
				.healPerTry(0.5)
				.equipment(Items.BOOK, null)
				.lootTable(AncientWarfare.id("entities/priest"));
	}

	public static FactionNpcBuilder buildTrader(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("trader"))
				.addAttribute(Attributes.MOVEMENT_SPEED, 0)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.experienceDropped(40)
				.equipment(Items.BOOK, null)
				.lootTable(Identifier.withDefaultNamespace("empty"));
	}

	public static FactionNpcBuilder buildSpellcaster(FactionNpcBuilder builder, int variant) {
		return builder.copy()
				.npcType(AncientWarfare.id(variant > 0 ? "spell_caster_" + variant : "spell_caster"))
				.addAttribute(Attributes.MAX_HEALTH, 30)
				.experienceDropped(40)
				.lootTable(AncientWarfare.id("entities/soldier"))
				.spells(Set.of(Identifier.parse("ebwizardry:magic_missile")));
	}

	public static FactionNpcBuilder buildBard(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("bard"))
				.addAttribute(Attributes.ATTACK_DAMAGE, 0)
				.experienceDropped(40)
				.equipment(AWItems.STEEL_INGOT, null);
	}

	public static FactionNpcBuilder buildMaleCivilian(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("male_civilian"))
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0);
	}

	public static FactionNpcBuilder buildFemaleCivilian(FactionNpcBuilder builder) {
		return builder.copy()
				.npcType(AncientWarfare.id("female_civilian"))
				.addAttribute(Attributes.MAX_HEALTH, 10)
				.addAttribute(Attributes.ATTACK_DAMAGE, 0);
	}

	/**
	 * Default npc parameters for any faction
	 */
	public static FactionNpcBuilder factionBuilder(String faction) {
		return FactionNpcBuilder.builder()
				.faction(AncientWarfare.id(faction))
				.soundSet(AncientWarfare.id("human_male"))
				.addAttribute(Attributes.MAX_HEALTH, 50)
				.addAttribute(Attributes.MOVEMENT_SPEED, 0.3)
				.addAttribute(Attributes.FOLLOW_RANGE, 35)
				.addAttribute(Attributes.ATTACK_DAMAGE, 1)
				.experienceDropped(30)
				.navigation(true, false, true)
				.burnsInSun(false)
				.undead(false)
				.lootTable(Identifier.withDefaultNamespace("empty"));
	}
}
