package io.github.steaf23.ancientwarfare.client.datagen;

import io.github.steaf23.ancientwarfare.structure.template.StructureEntry;
import io.github.steaf23.ancientwarfare.structure.template.legacy.MinifyStructuresTask;
import io.github.steaf23.ancientwarfare.structure.template.legacy.ParsedStructure;
import io.github.steaf23.ancientwarfare.structure.template.legacy.TemplateParser;
import io.github.steaf23.ancientwarfare.structure.template.legacy.TemplateParsingException;
import io.github.steaf23.ancientwarfare.structure.template.legacy.fixer.FixResult;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AWStructureConversionProvider implements DataProvider {

	private static final Logger log = LogManager.getLogger(AWStructureConversionProvider.class);
	private final FabricPackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;

	AWStructureConversionProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.output = output;
		this.registries = registries;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		HolderLookup.Provider lookup = this.registries.join();

		Path structureList = Paths.get("../../aw2_data/assets/ancientwarfare/template/all_structures.txt");
		Path base = structureList.getParent();

		try {
			List<String> paths;
			paths = Files.readAllLines(structureList);
			String path = "portal_test_5.aws";
			Path input = base.resolve(path.trim()).normalize();

			new MinifyStructuresTask().minifyTemplate(input);
			convert(lookup, base, path);
//			int i = 0;
//			for (String path : paths) {
//				Path inputPath = base.resolve(path.trim()).normalize();
//				System.out.println("Converting structure #" + i + ": " + path);
//				convert(lookup, base, path);
////				if (i == 10) {
////					break;
////				}
//
//				i++;
//			}
		} catch (IOException exc) {
			log.error("e: ", exc);
		} catch (TemplateParsingException templateExc) {
			log.error("parsing exception: ", templateExc);
		}

		return CompletableFuture.completedFuture(null);
	}

	@Override
	public String getName() {
		return "Ancient Warfare Structure (.aws) Conversions";
	}

	public void convert(HolderLookup.Provider lookup, Path baseDir, String path) throws TemplateParsingException, IOException {
		Path input = baseDir.resolve(path.trim()).normalize();

		TemplateParser parser = new TemplateParser(lookup);

		Optional<FixResult<ParsedStructure>> structure = parser.parseTemplateLines(input);
		if (structure.isEmpty()) {
			return;
		}

		String outputPathStr = "ancientwarfare/structure/" + path.replace(" ", "_").replace(".", "_").replace("&", "and");
		outputPathStr = outputPathStr.toLowerCase().substring(0, outputPathStr.length() - 4);
		Path outputPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(outputPathStr);

		StructureEntry entry = new LegacyConverter().convertToTemplate(lookup, BuiltInRegistries.BLOCK, structure.get().getData(), outputPath);

	}
}
