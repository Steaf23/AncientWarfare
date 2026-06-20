package io.github.steaf23.ancientwarfare.structure.block.entity.advancedspawner;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import io.github.steaf23.ancientwarfare.npc.entity.faction.FactionNpc;
import io.github.steaf23.ancientwarfare.npc.faction.FactionNpcData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.TypedEntityData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record AdvancedSpawnerSettings(
		boolean debugMode,
		boolean transparent,
		boolean redstoneSensitive,
		boolean lightSensitive,
		int playerRange,
		int mobRange,
		int spawnRange,
		int minDelayTicks,
		int maxDelayTicks,
		int maximumAllowedNearbyEntities,
		int xpToDrop,
		int spawnYOffset,
		List<SpawnGroup> groups
) {

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builderOf(AdvancedSpawnerSettings settings) {
		return new Builder()
				.debugMode(settings.debugMode)
				.transparent(settings.transparent)
				.redstoneSensitive(settings.redstoneSensitive)
				.lightSensitive(settings.lightSensitive)
				.playerRange(settings.playerRange)
				.mobRange(settings.mobRange)
				.spawnRange(settings.spawnRange)
				.minDelayTicks(settings.minDelayTicks)
				.maxDelayTicks(settings.maxDelayTicks)
				.maximumAllowedNearbyEntities(settings.maximumAllowedNearbyEntities)
				.xpToDrop(settings.xpToDrop)
				.spawnYOffset(settings.spawnYOffset)
				.groups(settings.groups);
	}

	public static class Builder {

		boolean debugMode = false;
		boolean transparent = false;
		boolean redstoneSensitive = false;
		boolean lightSensitive = false;
		int playerRange = 8;
		int mobRange = 1;
		int spawnRange = 0;
		int minDelayTicks = AncientWarfare.ONE_SECOND * 10;
		int maxDelayTicks = AncientWarfare.ONE_SECOND * 20;
		int maximumAllowedNearbyEntities = 0;
		int xpToDrop = 0;
		int spawnYOffset = 0;

		List<SpawnGroup> groups = List.of(new SpawnGroup(1, List.of(SpawnEntry.standard())));

		public AdvancedSpawnerSettings build() {
			return new AdvancedSpawnerSettings(
					debugMode,
					transparent,
					redstoneSensitive,
					lightSensitive,
					playerRange,
					mobRange,
					spawnRange,
					minDelayTicks,
					maxDelayTicks,
					maximumAllowedNearbyEntities,
					xpToDrop,
					spawnYOffset,
					groups);
		}

		public Builder debugMode(boolean debugMode) {
			this.debugMode = debugMode;
			return this;
		}

		public Builder transparent(boolean transparent) {
			this.transparent = transparent;
			return this;
		}

		public Builder redstoneSensitive(boolean redstoneSensitive) {
			this.redstoneSensitive = redstoneSensitive;
			return this;
		}

		public Builder lightSensitive(boolean lightSensitive) {
			this.lightSensitive = lightSensitive;
			return this;
		}

		public Builder playerRange(int playerRange) {
			this.playerRange = playerRange;
			return this;
		}

		public Builder mobRange(int mobRange) {
			this.mobRange = mobRange;
			return this;
		}

		public Builder spawnRange(int spawnRange) {
			this.spawnRange = spawnRange;
			return this;
		}

		public Builder minDelayTicks(int minDelayTicks) {
			this.minDelayTicks = minDelayTicks;
			return this;
		}

		public Builder maxDelayTicks(int maxDelayTicks) {
			this.maxDelayTicks = maxDelayTicks;
			return this;
		}

		public Builder maximumAllowedNearbyEntities(int maximumAllowedNearbyEntities) {
			this.maximumAllowedNearbyEntities = maximumAllowedNearbyEntities;
			return this;
		}

		public Builder xpToDrop(int xpToDrop) {
			this.xpToDrop = xpToDrop;
			return this;
		}

		public Builder spawnYOffset(int spawnYOffset) {
			this.spawnYOffset = spawnYOffset;
			return this;
		}

		public Builder groups(List<SpawnGroup> groups) {
			this.groups = groups;
			return this;
		}

		public Builder addGroup(SpawnGroup group) {
			List<SpawnGroup> groupsCopy = new ArrayList<>(groups);
			groupsCopy.add(group);
			groups(groupsCopy);
			return this;
		}

		public Builder removeGroup(int groupIndex) {
			List<SpawnGroup> groupsCopy = new ArrayList<>(groups);
			groupsCopy.remove(groupIndex);
			groups(groupsCopy);
			return this;
		}

		public Builder groupWeight(int groupIndex, int weight) {
			List<SpawnGroup> groupsCopy = new ArrayList<>(groups);
			SpawnGroup group = groupsCopy.remove(groupIndex);
			groupsCopy.add(groupIndex, new SpawnGroup(weight, group.entries));
			groups(groupsCopy);
			return this;
		}

		public Builder groupEntries(int groupIndex, List<SpawnEntry> entries) {
			List<SpawnGroup> groupsCopy = new ArrayList<>(groups);
			SpawnGroup group = groupsCopy.remove(groupIndex);
			groupsCopy.add(groupIndex, new SpawnGroup(group.weight, entries));
			groups(groupsCopy);
			return this;
		}

		public Builder addGroupEntry(int groupIndex, SpawnEntry entry) {
			SpawnGroup group = groups.get(groupIndex);
			List<SpawnEntry> entriesCopy = new ArrayList<>(group.entries);
			entriesCopy.add(entry);
			groupEntries(groupIndex, entriesCopy);
			return this;
		}

		public Builder groupEntryEntity(int groupIndex, int entryIndex, Identifier entity, CompoundTag entityData) {
			SpawnGroup group = groups.get(groupIndex);
			List<SpawnEntry> entriesCopy = new ArrayList<>(group.entries);
			SpawnEntry entry = entriesCopy.remove(entryIndex);
			entriesCopy.add(new SpawnEntry(entry.min, entry.max, entry.total, entity, entityData));
			groupEntries(groupIndex, entriesCopy);
			return this;
		}

		public Builder groupEntryMin(int groupIndex, int entryIndex, int min) {
			SpawnGroup group = groups.get(groupIndex);
			List<SpawnEntry> entriesCopy = new ArrayList<>(group.entries);
			SpawnEntry entry = entriesCopy.remove(entryIndex);
			entriesCopy.add(new SpawnEntry(min, entry.max, entry.total, entry.entity, entry.entityData));
			groupEntries(groupIndex, entriesCopy);
			return this;
		}

		public Builder groupEntryMax(int groupIndex, int entryIndex, int max) {
			SpawnGroup group = groups.get(groupIndex);
			List<SpawnEntry> entriesCopy = new ArrayList<>(group.entries);
			SpawnEntry entry = entriesCopy.remove(entryIndex);
			entriesCopy.add(new SpawnEntry(entry.min, max, entry.total, entry.entity, entry.entityData));
			groupEntries(groupIndex, entriesCopy);
			return this;
		}

		public Builder groupEntryTotal(int groupIndex, int entryIndex, int total) {
			SpawnGroup group = groups.get(groupIndex);
			List<SpawnEntry> entriesCopy = new ArrayList<>(group.entries);
			SpawnEntry entry = entriesCopy.remove(entryIndex);
			entriesCopy.add(new SpawnEntry(entry.min, entry.max, total, entry.entity, entry.entityData));
			groupEntries(groupIndex, entriesCopy);
			return this;
		}

		public void removeGroupEntry(int groupIndex, int entryIndex) {
			SpawnGroup group = groups.get(groupIndex);
			List<SpawnEntry> entriesCopy = new ArrayList<>(group.entries);
			entriesCopy.remove(entryIndex);

			if (entriesCopy.isEmpty()) {
				removeGroup(groupIndex);
			} else {
				groupEntries(groupIndex, entriesCopy);
			}
		}

		public ImmutableList<SpawnGroup> groups() {
			return ImmutableList.copyOf(groups);
		}
	}

	public static final Codec<AdvancedSpawnerSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.BOOL.fieldOf("debug_mode").forGetter(AdvancedSpawnerSettings::debugMode),
			Codec.BOOL.fieldOf("transparent").forGetter(AdvancedSpawnerSettings::transparent),
			Codec.BOOL.fieldOf("redstone_sensitive").forGetter(AdvancedSpawnerSettings::redstoneSensitive),
			Codec.BOOL.fieldOf("light_sensitive").forGetter(AdvancedSpawnerSettings::lightSensitive),

			Codec.INT.fieldOf("player_range").forGetter(AdvancedSpawnerSettings::playerRange),
			Codec.INT.fieldOf("mob_range").forGetter(AdvancedSpawnerSettings::mobRange),
			Codec.INT.fieldOf("spawn_range").forGetter(AdvancedSpawnerSettings::spawnRange),
			Codec.INT.fieldOf("minDelay_ticks").forGetter(AdvancedSpawnerSettings::minDelayTicks),
			Codec.INT.fieldOf("maxDelay_ticks").forGetter(AdvancedSpawnerSettings::maxDelayTicks),
			Codec.INT.fieldOf("maximum_allowed_nearby_entities").forGetter(AdvancedSpawnerSettings::maximumAllowedNearbyEntities),
			Codec.INT.fieldOf("xp_to_drop").forGetter(AdvancedSpawnerSettings::xpToDrop),
			Codec.INT.fieldOf("spawn_y_offset").forGetter(AdvancedSpawnerSettings::spawnYOffset),
			SpawnGroup.CODEC.listOf().fieldOf("groups").forGetter(AdvancedSpawnerSettings::groups)
	).apply(i, AdvancedSpawnerSettings::new));

	public static final StreamCodec<ByteBuf, AdvancedSpawnerSettings> STREAM_CODEC = StreamCodec.of(
			(buf, s) -> {
				FriendlyByteBuf friendly = new FriendlyByteBuf(buf);
				friendly.writeBoolean(s.debugMode());
				friendly.writeBoolean(s.transparent());
				friendly.writeBoolean(s.redstoneSensitive());
				friendly.writeBoolean(s.lightSensitive());

				friendly.writeInt(s.playerRange());
				friendly.writeInt(s.mobRange());
				friendly.writeInt(s.spawnRange());
				friendly.writeInt(s.minDelayTicks());
				friendly.writeInt(s.maxDelayTicks());
				friendly.writeInt(s.maximumAllowedNearbyEntities());
				friendly.writeInt(s.xpToDrop());
				friendly.writeInt(s.spawnYOffset());
				friendly.writeCollection(s.groups(), SpawnGroup.STREAM_CODEC);
			},
			buf -> {
				FriendlyByteBuf friendly = new FriendlyByteBuf(buf);
				return new AdvancedSpawnerSettings(
						friendly.readBoolean(),
						friendly.readBoolean(),
						friendly.readBoolean(),
						friendly.readBoolean(),

						friendly.readInt(),
						friendly.readInt(),
						friendly.readInt(),
						friendly.readInt(),
						friendly.readInt(),
						friendly.readInt(),
						friendly.readInt(),
						friendly.readInt(),
						friendly.readCollection(ArrayList::new, SpawnGroup.STREAM_CODEC));
			});

	public record SpawnGroup(int weight, List<SpawnEntry> entries) {

		public static final Codec<SpawnGroup> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("weight").forGetter(SpawnGroup::weight),
				SpawnEntry.CODEC.listOf().fieldOf("entries").forGetter(SpawnGroup::entries)
		).apply(i, SpawnGroup::new));

		public static final StreamCodec<ByteBuf, SpawnGroup> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, SpawnGroup::weight,
				ByteBufCodecs.collection(ArrayList::new, SpawnEntry.STREAM_CODEC), SpawnGroup::entries,
				SpawnGroup::new
		);
	}

	public record SpawnEntry(int min, int max, int total, Identifier entity, CompoundTag entityData) {

		public SpawnEntry(int min, int max, int total, Identifier entity, Optional<CompoundTag> entityData) {
			this(min, max, total, entity, entityData.orElse(new CompoundTag()));
		}

		public static SpawnEntry standard() {
			return new SpawnEntry(1, 4, 0, Identifier.withDefaultNamespace("pig"), new CompoundTag());
		}

		public static final Codec<SpawnEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("min").forGetter(SpawnEntry::min),
				Codec.INT.fieldOf("max").forGetter(SpawnEntry::max),
				Codec.INT.fieldOf("total").forGetter(SpawnEntry::total),
				Identifier.CODEC.fieldOf("entity").forGetter(SpawnEntry::entity),
				CompoundTag.CODEC.optionalFieldOf("entityData").forGetter(entry -> Optional.of(entry.entityData))
		).apply(i, SpawnEntry::new));

		public static final StreamCodec<ByteBuf, SpawnEntry> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, SpawnEntry::min,
				ByteBufCodecs.INT, SpawnEntry::max,
				ByteBufCodecs.INT, SpawnEntry::total,
				Identifier.STREAM_CODEC, SpawnEntry::entity,
				ByteBufCodecs.COMPOUND_TAG, SpawnEntry::entityData,
				SpawnEntry::new
		);
	}
}
