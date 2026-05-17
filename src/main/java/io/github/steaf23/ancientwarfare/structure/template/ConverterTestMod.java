package io.github.steaf23.ancientwarfare.structure.template;

import io.github.steaf23.ancientwarfare.structure.template.legacy.ParsedStructure;
import io.github.steaf23.ancientwarfare.structure.template.legacy.TemplateParser;
import io.github.steaf23.ancientwarfare.structure.template.legacy.TemplateParsingException;
import io.github.steaf23.ancientwarfare.structure.template.legacy.fixer.FixResult;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.BuiltInRegistries;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConverterTestMod implements ModInitializer {

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			try {
//				List<String> lines = readLines("/assets/ancientwarfare/template/block_test.aws");
//				Optional<FixResult<ParsedStructure>> structure = parser.parseTemplateLines("block_test", lines);
//				if (structure.isEmpty()) {
//					return;
//				}
//
//				StructureEntry entry = new LegacyConverter().convertToTemplate(BuiltInRegistries.BLOCK, structure.get().getData());
				int i = 0;
				List<String> allPaths = readLines("/assets/ancientwarfare/template/all_structures.txt");
				for (String path : allPaths) {
					path = path.replace(" ", "_").replace(".", "_").replace("&", "and");
					TemplateParser parser = new TemplateParser(server.registryAccess());
					System.out.println("Structure #" + i + ": " + path);

					List<String> lines = readLines("/assets/ancientwarfare/template/" + path);
					Optional<FixResult<ParsedStructure>> structure = parser.parseTemplateLines(path, lines);
					if (structure.isEmpty()) {
						return;
					}

					StructureEntry entry = new LegacyConverter().convertToTemplate(BuiltInRegistries.BLOCK, structure.get().getData(), path.toLowerCase().substring(0, path.length() - 4));
					i++;

					if (i == 10) {
						return;
					}
				}
			} catch (TemplateParsingException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public static List<String> readLines(String filePath) {
		List<String> lines = new ArrayList<>();

		try (InputStream stream = ConverterTestMod.class.getResourceAsStream(filePath)) {
			if (stream == null) {
				return List.of();
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

				String line;
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lines;
	}
}
