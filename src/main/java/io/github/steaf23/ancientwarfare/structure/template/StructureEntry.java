package io.github.steaf23.ancientwarfare.structure.template;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Set;

public record StructureEntry(Header header, Validation validation, StructureTemplate template) {

	public record Header(String name, StructureVersion version, Set<String> modDependencies, Vec3i size, Vec3i offset) {}
	public record Validation() {}

}
