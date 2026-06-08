package io.github.steaf23.ancientwarfare.structure.level.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class StructureGrid {

	Map<BlockPos, Identifier> templates = new HashMap<>();

	public void setup(StructureTemplateManager templateManager, Predicate<Identifier> namePredicate) {
		Map<Identifier, Vec3i> allStructureSizes = new HashMap<>();
		templateManager.listTemplates().forEach(id -> {
			if (namePredicate.test(id)) {
				allStructureSizes.put(id, templateManager.get(id).orElseThrow().getSize());
			}
		});

		if (allStructureSizes.size() == this.templates.size()) {
			return;
		}

		List<Identifier> sortedById = allStructureSizes.keySet().stream()
//				.sorted(Comparator.
//						<Identifier>comparingInt(a -> allStructureSizes.get(a).getX())
//						.thenComparingInt(a -> allStructureSizes.get(a).getZ()))
				.toList();

		// Pack all structures based on their size to fit in a nice rectangle.

		// ...But for now just put them all next to each other.
		BlockPos current = BlockPos.ZERO;
		int idx = 0;
		int rowMaxZ = 0;
		int padding = 1;
		for (Identifier id : sortedById) {
			if (idx % 10 == 0) {
				current = new BlockPos(0, 0, current.getZ() + rowMaxZ + padding);
				rowMaxZ = 0;
			}
			templates.put(current, id);
			int currentZ = allStructureSizes.get(id).getZ();
			if (currentZ > rowMaxZ) {
				rowMaxZ = currentZ;
			}

			current = current.relative(Direction.Axis.X, allStructureSizes.get(id).getX() + padding);
			idx++;
		}
	}

	public Map<BlockPos, Identifier> getStructuresAtSection(SectionPos section) {
		Map<BlockPos, Identifier> subList = new HashMap<>();

		for (BlockPos pos : this.templates.keySet()) {
			if (section.chunk().contains(pos)) {
				subList.put(pos, this.templates.get(pos));
			}
		}

		return subList;
	}
}
