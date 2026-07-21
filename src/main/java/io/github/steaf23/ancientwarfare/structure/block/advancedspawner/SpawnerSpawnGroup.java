package io.github.steaf23.ancientwarfare.structure.block.advancedspawner;

import io.github.steaf23.ancientwarfare.core.registry.AWResources;
import io.github.steaf23.ancientwarfare.core.registry.entity.AWEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpawnerSpawnGroup {

	private int groupWeight = 1;
	private final List<Entry> entitiesToSpawn = new ArrayList<>();

	public SpawnerSpawnGroup() {
	}

	public static SpawnerSpawnGroup fromSettings(AdvancedSpawnerSettings.SpawnGroup spawnGroup) {
		SpawnerSpawnGroup group = new SpawnerSpawnGroup();
		group.setWeight(spawnGroup.weight());

		for (AdvancedSpawnerSettings.SpawnEntry e : spawnGroup.entries()) {
			Entry entry = new Entry();
			entry.entityId = e.entity();
			entry.maxToSpawn = e.max();
			entry.minToSpawn = e.min();
			entry.remainingSpawnCount = e.total() == 0 ? -1 : e.total();
			entry.entityData = e.entityData();

			group.addEntry(entry);
		}
		return group;
	}

	public AdvancedSpawnerSettings.SpawnGroup save() {
		List<AdvancedSpawnerSettings.SpawnEntry> entries = new ArrayList<>();
		for (Entry e : entitiesToSpawn) {
			AdvancedSpawnerSettings.SpawnEntry entry = new AdvancedSpawnerSettings.SpawnEntry(e.minToSpawn, e.maxToSpawn, e.remainingSpawnCount, e.entityId, e.entityData);
			entries.add(entry);
		}
		return new AdvancedSpawnerSettings.SpawnGroup(groupWeight, entries);
	}

	public void setWeight(int weight) {
		this.groupWeight = weight <= 0 ? 1 : weight;
	}

	public void addEntry(Entry setting) {
		entitiesToSpawn.add(setting);
	}

	public void eraseEntry(int atIndex) {
		entitiesToSpawn.remove(atIndex);
	}

	public void spawnEntities(ServerLevel world, BlockPos spawnPos, int grpIndex, int yOffset, int range) {
		spawnPos = spawnPos.offset(0, yOffset, 0);
		Iterator<Entry> it = entitiesToSpawn.iterator();
		int index = 0;
		Entry entitySpawnSettings;
		while (it.hasNext() && (entitySpawnSettings = it.next()) != null) {
			entitySpawnSettings.spawnEntities(world, spawnPos, range);
			if (entitySpawnSettings.remainingSpawnCount == 0) {
				it.remove();
			}

			int a1 = 0;
			int b2 = entitySpawnSettings.remainingSpawnCount;
			int a = (a1 << 16) | (grpIndex & 0x0000ffff);
			int b = (index << 16) | (b2 & 0x0000ffff);
//			world.addBlockEvent(spawnPos, AWStructureBlocks.ADVANCED_SPAWNER, a, b);
			index++;
		}
	}

	public boolean shouldRemove() {
		return entitiesToSpawn.isEmpty();
	}

	public List<Entry> getEntitiesToSpawn() {
		return entitiesToSpawn;
	}

	public int getWeight() {
		return groupWeight;
	}

//	public void writeData(WriteView writer) {
//		writer.putInt("groupWeight", groupWeight);
//		NBTTagList settingsList = new NBTTagList();
//
//		NBTTagCompound settingTag;
//		for (Entry setting : this.entitiesToSpawn) {
//			settingTag = new NBTTagCompound();
//			setting.writeToNBT(settingTag);
//			settingsList.appendTag(settingTag);
//		}
//		tag.setTag("settingsList", settingsList);
//	}
//
//	public void readData(ReadView reader) {
//		groupWeight = reader.getInt("groupWeight", 1);
//		NBTTagList settingsList = tag.getTagList("settingsList", Constants.NBT.TAG_COMPOUND);
//		Entry setting;
//		for (int i = 0; i < settingsList.tagCount(); i++) {
//			setting = new Entry(this);
//			setting.readFromNBT(settingsList.getCompoundTagAt(i));
//			if (!setting.shouldRemove()) {
//				this.entitiesToSpawn.add(setting);
//			}
//		}
//	}

	public static class Entry {

		public int minToSpawn = 1;
		public int maxToSpawn = 1;
		public int remainingSpawnCount = 1;
		public Identifier entityId = Identifier.withDefaultNamespace("pig");
		public CompoundTag entityData = new CompoundTag();

		public void spawnEntities(ServerLevel world, BlockPos spawnPos, int range) {
			int toSpawn = getAmountToSpawn(world.getRandom());

			for (int i = 0; i < toSpawn; i++) {
				EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(entityId);

				if (type == AWEntities.FACTION_NPC) {
					if (AWResources.npc(Identifier.parse(entityData.getStringOr("npc_data", ""))) == null) {
						if (remainingSpawnCount > 0) {
							remainingSpawnCount--;
						}
						continue;
					}
				}

				ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), entityData);
				Entity e = EntityType.create(type, input, world, EntitySpawnReason.SPAWNER).orElse(null);
				boolean doSpawn = e != null && findAndSetSpawnLocation(world, spawnPos, range, e);
				if (doSpawn) {
					spawnEntityAt(e, world);
					if (remainingSpawnCount > 0) {
						remainingSpawnCount--;
					}
				}
			}
		}

		private void spawnEntityAt(Entity e, ServerLevel world) {
			world.addFreshEntity(e);
		}

		private int getAmountToSpawn(RandomSource random) {
			int randRange = maxToSpawn - minToSpawn;
			int toSpawn;
			if (randRange <= 0) {
				toSpawn = minToSpawn;
			} else {
				toSpawn = minToSpawn + random.nextInt(randRange);
			}
			if (remainingSpawnCount >= 0 && toSpawn > remainingSpawnCount) {
				toSpawn = remainingSpawnCount;
			}
			return toSpawn;
		}

		private boolean findAndSetSpawnLocation(ServerLevel world, BlockPos spawnPos, int range, Entity e) {
			BlockPos.MutableBlockPos mutable = spawnPos.mutable();
			if (range == 0) {
				mutable.set(spawnPos.offset(0, 1, 0));
				if (world.isEmptyBlock(mutable) && world.noCollision(e.getType().getDimensions().makeBoundingBox(mutable.getX(), mutable.getY(), mutable.getZ()))) {
					e.snapTo(mutable.getX() + 0.5, mutable.getY(), mutable.getZ() + 0.5, world.getRandom().nextFloat() * 360.0F, 0.0F);
					return true;
				}
				return false;
			}

			for (int tries = 0; tries < 10; tries++) {
				int x = spawnPos.getX() + world.getRandom().nextInt(range * 2) - range;
				int y = spawnPos.getY() + world.getRandom().nextInt(4) - 2;
				int z = spawnPos.getZ() + world.getRandom().nextInt(range * 2) - range;

				mutable.set(x, y, z);
				if (world.isEmptyBlock(mutable) && world.noCollision(e.getType().getDimensions().makeBoundingBox(x, y, z))) {
					e.snapTo(x + 0.5, y, z + 0.5, world.getRandom().nextFloat() * 360.0F, 0.0F);
					return true;
				}
			}
			return false;
		}
	}

}
