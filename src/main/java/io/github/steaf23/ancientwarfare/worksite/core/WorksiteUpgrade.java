package io.github.steaf23.ancientwarfare.worksite.core;

public interface WorksiteUpgrade {

	void setup(UpgradableWorksite workSite);

	void removed(UpgradableWorksite worksite);
}
