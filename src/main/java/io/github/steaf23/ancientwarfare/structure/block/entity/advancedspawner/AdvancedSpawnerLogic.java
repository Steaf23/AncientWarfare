package io.github.steaf23.ancientwarfare.structure.block.entity.advancedspawner;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdvancedSpawnerLogic {

	public int ticksUntilNextAttempt;
	public String factionName= "";

	public boolean powered = false;

	private AdvancedSpawnerSettings settings = null;
	private List<SpawnerSpawnGroup> groupsToSpawn = new ArrayList<>();

	public AdvancedSpawnerLogic() {
		setSettings(AdvancedSpawnerSettings.builder().build());

		ticksUntilNextAttempt = settings.maxDelayTicks();
	}

	public void setSettings(@NotNull AdvancedSpawnerSettings settings) {
		this.settings = settings;
		// clear groups
		groupsToSpawn = new ArrayList<>();

		for (AdvancedSpawnerSettings.SpawnGroup group : settings.groups()) {
			groupsToSpawn.add(SpawnerSpawnGroup.fromSettings(group));
		}

		ticksUntilNextAttempt = settings.maxDelayTicks();
	}

	public AdvancedSpawnerSettings settings() {
		return settings;
	}

	void update(ServerLevel world, BlockPos pos, BlockState state) {
		if (!settings.redstoneSensitive()) {
			updateNormally(world, pos, state);
		} else {
			updateRedstoneAware(world, pos, state);
		}
		if (groupsToSpawn.isEmpty()) {
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	void updateNormally(ServerLevel level, BlockPos pos, BlockState state) {
		if (ticksUntilNextAttempt > 0) {
			ticksUntilNextAttempt--;
		}
		if (ticksUntilNextAttempt <= 0) {
			ticksUntilNextAttempt = getNewSpawnDelay(level);
			trySpawnEntities(level, pos);
		}
	}

	int getNewSpawnDelay(ServerLevel level) {
		int delayRange = settings.maxDelayTicks() - settings.minDelayTicks();
		return settings.minDelayTicks() + (delayRange <= 0 ? 0 : level.getRandom().nextInt(delayRange));
	}

	void updateRedstoneAware(ServerLevel world, BlockPos pos, BlockState state) {
		boolean wasPowered = powered;
		powered = world.hasNeighborSignal(pos);

		boolean shouldTrigger = !wasPowered && powered;
		if (settings.redstoneSensitive() && !shouldTrigger) {
			return;
		}

		updateNormally(world, pos, state);
	}

	void trySpawnEntities(ServerLevel world, BlockPos pos) {
		if (!canSpawnEntities(world, pos)) {
			return;
		}

		int totalWeight = 0;
		for (SpawnerSpawnGroup group : this.groupsToSpawn)//count total weights
		{
			totalWeight += group.getWeight();
		}
		int rand = totalWeight == 0 ? 0 : world.getRandom().nextInt(totalWeight);//select an object
		int check = 0;
		SpawnerSpawnGroup toSpawn = null;
		int index = 0;
		for (SpawnerSpawnGroup group : this.groupsToSpawn)//iterate to find selected object
		{
			check += group.getWeight();
			if (rand < check)//object found, break
			{
				toSpawn = group;
				break;
			}
			index++;
		}

		if (toSpawn != null) {
			toSpawn.spawnEntities(world, pos, index, settings.spawnYOffset(), settings.spawnRange());
			if (toSpawn.shouldRemove()) {
				groupsToSpawn.remove(toSpawn);
			}
		}
	}

	private boolean canSpawnEntities(ServerLevel world, BlockPos spawnerPos) {
		if (settings.lightSensitive() && world.getMaxLocalRawBrightness(spawnerPos) <= 0) {
			return false;
		}

		if (!checkPlayerConditions(world, spawnerPos)) {
			return false;
		}

		if (!checkNearbyMobs(world, spawnerPos)) {
			return false;
		}

		return true;
	}

	/**
	 * @return true when any survival player in the world is within range of the given position
	 */
	private boolean checkPlayerConditions(ServerLevel world, BlockPos pos) {
		Collection<Player> nearbyPlayers = getPlayersWithinRange(world, pos);
		if (nearbyPlayers.isEmpty()) {
			return false;
		}

		// TODO: re-add faction check!
		for (Player player : nearbyPlayers) {
			if ((settings.debugMode() || (!player.isCreative() && !player.isSpectator()))) {
				return true;
			}
		}
		return false;
	}

	private Collection<Player> getPlayersWithinRange(ServerLevel world, BlockPos pos) {
		Set<Player> players = new HashSet<>();
		for (Player player : world.players()) {
			if (pos.closerThan(player.blockPosition(), settings.playerRange())) {
				players.add(player);
			}
		}

		return players;
	}

	private boolean checkNearbyMobs(ServerLevel world, BlockPos pos) {
		if (settings.maximumAllowedNearbyEntities() >= 0 && settings.mobRange() >= 0) {
			int count = world.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(settings.mobRange()), (e) -> true).size();
			// AncientWarfareStructure.LOG.debug("skipping spawning because of too many nearby entities");
			return count <= settings.maximumAllowedNearbyEntities();
		}
		return false;
	}

	public void readData(ValueInput reader) {
		setSettings(reader.read("settings", AdvancedSpawnerSettings.CODEC).orElse(AdvancedSpawnerSettings.builder().build()));
		ticksUntilNextAttempt = reader.getIntOr("ticksUntilNextAttempt", ticksUntilNextAttempt);
	}

	public void writeData(ValueOutput writer) {
		writer.store("settings", AdvancedSpawnerSettings.CODEC, settings);
		writer.putInt("ticksUntilNextAttempt", ticksUntilNextAttempt);
	}

}
