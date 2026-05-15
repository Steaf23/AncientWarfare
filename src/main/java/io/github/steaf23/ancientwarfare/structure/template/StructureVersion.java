package io.github.steaf23.ancientwarfare.structure.template;

public class StructureVersion {
	private final int major;
	private final int minor;

	public StructureVersion(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}

	public StructureVersion(String version) {
		this(Integer.parseInt(version.substring(0, version.indexOf('.'))), Integer.parseInt(version.substring(version.indexOf('.') + 1)));
	}

	public boolean isGreaterThan(StructureVersion otherVersion) {
		return getMajor() > otherVersion.getMajor() || getMinor() > otherVersion.getMinor();
	}

	public static final StructureVersion NONE = new StructureVersion(0, 0);

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}
}
