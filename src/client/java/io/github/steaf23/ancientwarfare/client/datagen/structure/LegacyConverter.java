package io.github.steaf23.ancientwarfare.client.datagen.structure;

import com.mojang.serialization.Codec;
import io.github.steaf23.ancientwarfare.structure.template.StructureEntry;
import io.github.steaf23.ancientwarfare.structure.template.legacy.ParsedStructure;
import io.github.steaf23.ancientwarfare.structure.template.legacy.TemplateRuleBlock;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class LegacyConverter {
	public StructureEntry convertToTemplate(HolderLookup.Provider registries, HolderGetter<Block> blockLookup, ParsedStructure structure, Path filePath) {

		TagValueOutput writer = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);

		var sizeList = writer.list("size", Codec.INT);
		sizeList.add(structure.size.getX());
		sizeList.add(structure.size.getY());
		sizeList.add(structure.size.getZ());
		var list = writer.list("palette", BlockState.CODEC);
		for (TemplateRuleBlock rule : structure.getBlockRules().values()) {
			list.add(rule.getState());
		}

		ValueOutput.ValueOutputList blocks = writer.childrenList("blocks");
		for (int y = 0; y < structure.size.getY(); y++) {
			for (int z = 0; z < structure.size.getZ(); z++) {
				for (int x = 0; x < structure.size.getX(); x++) {
					var blockNr = structure.getTemplateData()[ParsedStructure.getIndex(new Vec3i(x, y, z), structure.size)];
					if (blockNr == 0) {
						continue;
					}
					ValueOutput blockEntry = blocks.addChild();
					blockEntry.putInt("state", blockNr);
					var posList = blockEntry.list("pos", Codec.INT);
					posList.add(x);
					posList.add(y);
					posList.add(z);

					TemplateRuleBlock rule = structure.getBlockRules().get((int)blockNr);
					if (rule == null) {
						continue;
					}

					if (!rule.hasBlockEntityData()) {
						continue;
					}

					rule.writeBlockEntityData(registries, blockEntry.child("nbt"));
				}
			}
		}

		try {
			File folder = filePath.getParent().toFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			NbtIo.writeCompressed(writer.buildResult(), folder.toPath().resolve(filePath.getFileName() + ".nbt"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		StructureEntry.Header structureHeader = new StructureEntry.Header(structure.name, structure.getVersion(), structure.modDependencies, structure.size, structure.offset);
		StructureTemplate template = new StructureTemplate();
		template.load(blockLookup, writer.buildResult());
		return new StructureEntry(structureHeader, new StructureEntry.Validation(), template);
	}
}
