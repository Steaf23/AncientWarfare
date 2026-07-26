package io.github.steaf23.ancientwarfare.worksite.core;

import java.util.Set;

public interface UpgradableWorksite {

	Set<WorksiteUpgrade> installedUpgrades();

	Set<WorksiteUpgrade> possibleUpgrades();

	boolean installUpgrade(WorksiteUpgrade upgrade);

	boolean removeUpgrade(WorksiteUpgrade upgrade);

	default boolean canInstallUpgrade(WorksiteUpgrade upgrade) {
		return possibleUpgrades().contains(upgrade);
	}
}
