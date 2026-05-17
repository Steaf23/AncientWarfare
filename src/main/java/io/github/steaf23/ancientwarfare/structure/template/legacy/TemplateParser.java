package io.github.steaf23.ancientwarfare.structure.template.legacy;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import io.github.steaf23.ancientwarfare.structure.template.StructureVersion;
import io.github.steaf23.ancientwarfare.structure.template.legacy.TemplateParsingException.TemplateRuleParsingException;
import io.github.steaf23.ancientwarfare.structure.template.legacy.fixer.FixResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemplateParser {

	public static final StructureVersion LATEST_LEGACY = new StructureVersion(2, 11);
	public static final String JSON_PREFIX = "JSON:";

	private final HolderLookup.Provider lookupProvider;

	public TemplateParser(HolderLookup.Provider lookupProvider) {
		this.lookupProvider = lookupProvider;
	}

	public Optional<FixResult<ParsedStructure>> parseTemplateLines(String fileName, List<String> lines) throws TemplateParsingException {

		Iterator<String> it = lines.iterator();
		String line;

		StructureValidator validation = null;
		List<String> groupedLines = new ArrayList<>();

		String name = "";
		StructureVersion version = StructureVersion.NONE;
		Vec3i size = new Vec3i(0, 0, 0);
		Vec3i offset = new Vec3i(0, 0, 0);
		short[] templateData = null;
		boolean[] initData = new boolean[4];
		Map<Integer, TemplateRuleBlock> parsedRules = new HashMap<>();
		Map<Integer, TemplateRuleEntityBase> parsedEntities = new HashMap<>();
		FixResult.Builder<ParsedStructure> resultBuilder = new FixResult.Builder<>();
		String[] modDependencies = new String[0];
		while (it.hasNext()) {
			line = it.next();
			if (line.startsWith("#") || line.equals("")) {
				continue;
			}
			if (line.startsWith("header:")) {
				while (it.hasNext()) {
					line = it.next();
					if (line.startsWith(":endheader")) {
						break;
					}
					if (line.startsWith("version=")) {
						initData[0] = true;
						version = new StructureVersion(TemplateParser.safeParseString("=", line));
					}
					if (line.startsWith("name=")) {
						name = TemplateParser.safeParseString("=", line);
						initData[1] = true;
					}
//					if (line.startsWith("mods=")) {
//						modDependencies = TemplateParser.safeParseString("=", line).split(",");
//						if (!CompatUtils.areModsLoaded(modDependencies)) {
//							AncientWarfareStructure.LOG.info("Template {} not loaded because it depends on mod that isn't loaded.", fileName);
//							return Optional.empty();
//						}
//					}
					if (line.startsWith("size=")) {
						int[] sizes = TemplateParser.safeParseIntArray("=", line);
						size = new Vec3i(sizes[0], sizes[1], sizes[2]);
						initData[2] = true;
					}
					if (line.startsWith("offset=")) {
						int[] offsets = TemplateParser.safeParseIntArray("=", line);
						offset = new Vec3i(offsets[0], offsets[1], offsets[2]);
						initData[3] = true;
					}
				}
				for (int i = 0; i < 4; i++) {
					if (!initData[i]) {
						throw new TemplateParsingException("Could not parse template for " + fileName + " -- template was missing header or header data.");
					}
				}
				templateData = new short[size.getX() * size.getY() * size.getZ()];
			}

			/*
			 * parse out validation data
			 */
			if (line.startsWith("validation:")) {
				while (it.hasNext()) {
					line = it.next();
					if (line.startsWith(":endvalidation")) {
						break;
					}
					groupedLines.add(line);
				}
//				validation = StructureValidator.parseValidator(groupedLines);
//				validation.setModlist(modDependencies);
				groupedLines.clear();
			}

			/*
			 * parse out rule data
			 */
			if (line.startsWith("rule:")) {
				groupedLines.add(line);
				while (it.hasNext()) {
					line = it.next();
					groupedLines.add(line);
					if (line.startsWith(":endrule")) {
						break;
					}
				}
				try {
					TemplateRuleBlock parsedRule = resultBuilder.updateAndGetData(getRule(version, groupedLines, "rule", new TemplateRuleBlock()));
					parsedRules.put(parsedRule.ruleNumber, parsedRule);
				}
				catch (TemplateRuleParsingException e) {
					StringBuilder data = new StringBuilder(e.getMessage() + "\n");
					for (String line1 : groupedLines) {
						data.append(line1).append("\n");
					}
					TemplateRuleParsingException e1 = new TemplateRuleParsingException(data.toString(), e);
					System.out.println(e1.getMessage());
//					AncientWarfareStructure.LOG.error("Caught exception parsing template rule for structure: " + name, e1.getMessage());
				}
				groupedLines.clear();
			}

			/*
			 * parse out entity data
			 */
			if (line.startsWith("entity:")) {
				groupedLines.add(line);
				while (it.hasNext()) {
					line = it.next();
					groupedLines.add(line);
					if (line.startsWith(":endentity")) {
						break;
					}
				}
				try {
					TemplateRuleEntityBase entityRule = resultBuilder.updateAndGetData(getRule(version, groupedLines, "entity", new TemplateRuleEntityBase()));
					parsedEntities.put(entityRule.ruleNumber, entityRule);
				}
				catch (TemplateRuleParsingException e) {
					StringBuilder data = new StringBuilder(e.getMessage() + "\n");
					for (String line1 : groupedLines) {
						data.append(line1).append("\n");
					}
					TemplateRuleParsingException e1 = new TemplateRuleParsingException(data.toString(), e);
//					AncientWarfareStructure.LOG.error("Caught exception parsing template rule for structure: " + name, e1.getMessage());
				}
				groupedLines.clear();
			}

			/*
			 * parse out layer data
			 */
			if (line.startsWith("layer:")) {
				groupedLines.add(line);
				while (it.hasNext()) {
					line = it.next();
					groupedLines.add(line);
					if (line.startsWith(":endlayer")) {
						break;
					}
				}
				parseLayer(groupedLines, size, templateData);
				groupedLines.clear();
			}
		}

		return Optional.of(resultBuilder.build(constructTemplate(name, modDependencies, version, size, offset, templateData, parsedRules, parsedEntities, validation)));
	}

	private ParsedStructure constructTemplate(String name, String[] modDependencies, StructureVersion version, Vec3i size, Vec3i offset, short[] templateData, Map<Integer, TemplateRuleBlock> rules, Map<Integer, TemplateRuleEntityBase> entityRules, StructureValidator validation) {
		ParsedStructure template = new ParsedStructure(name, Arrays.stream(modDependencies).collect(Collectors.toSet()), version, size, offset);

		rules.put(0, new TemplateRuleBlock());
		template.setBlockRules(rules);
		template.setEntityRules(entityRules);
		template.setTemplateData(templateData);
		template.setValidationSettings(validation);
		return template;
	}

	/*
	 * should parse layer and insert directly into templateData
	 */
	private void parseLayer(List<String> templateLines, Vec3i size, short[] templateData) {
		int minLayer = 0;
		int maxLayer = 0;
		List<String> rowLines = new ArrayList<>();
		for (String st : templateLines) {
			if (!st.startsWith(":endlayer")) {
				if (st.startsWith("layer:")) {
					String[] layerIds = st.split(":")[1].split("-");
					minLayer = Integer.parseInt(layerIds[0].trim());
					maxLayer = layerIds.length > 1 ? Integer.parseInt(layerIds[1].trim()) : minLayer;
				} else {
					rowLines.add(st);
				}
			}
		}
		parseLayer(size, templateData, minLayer, maxLayer, rowLines);
	}

	private void parseLayer(Vec3i size, short[] templateData, int minLayer, int maxLayer, List<String> rowLines) {
		List<short[]> rows = parseLayerRows(rowLines);
		for (int layerId = minLayer; layerId <= maxLayer; layerId++) {
			int z = 0;
			for (short[] data : rows) {
				for (int x = 0; x < size.getX() && x < data.length; x++) {
					templateData[ParsedStructure.getIndex(new Vec3i(x, layerId, z), size)] = data[x];
				}
				z++;
			}
		}
	}

	private List<short[]> parseLayerRows(List<String> rowLines) {
		List<short[]> rows = new ArrayList<>();
		for (String rowLine : rowLines) {
			String[] rowParts = rowLine.split("x");
			int repeat = 1;
			short[] blocks;
			if (rowParts.length > 1) {
				repeat = Integer.parseInt(rowParts[0]);
				blocks = parseBlocks(rowParts[1]);
			} else {
				blocks = parseBlocks(rowParts[0]);
			}
			for (int i = 0; i < repeat; i++) {
				rows.add(blocks);
			}
		}

		return rows;
	}

	private short[] parseBlocks(String row) {
		List<Short> blocks = new ArrayList<>();

		String[] blockParts = row.split(",");

		for (String blockPart : blockParts) {
			String[] blockDef = blockPart.split("\\|");
			int repeat = 1;
			if (blockDef.length > 1) {
				repeat = Integer.parseInt(blockDef[1]);
			}
			short id = Short.parseShort(blockDef[0]);
			for (int i = 0; i < repeat; i++) {
				blocks.add(id);
			}
		}

		short[] ret = new short[blocks.size()];
		int i = 0;
		for (short block : blocks) {
			ret[i++] = block;
		}
		return ret;
	}

	private static String safeParseString(String regex, String test) {
		String[] split = test.split(regex);
		if (split.length > 1) {
			return split[1];
		}
		return "";
	}

	private static int[] safeParseIntArray(String regex, String test) {
		String[] splits = test.split(regex);
		if (splits.length > 1) {
			return parseIntArray(splits[1]);
		}
		return new int[0];
	}

	private static int[] parseIntArray(String csv) {
		if (csv.trim().isEmpty()) {
			return new int[0];
		}
		String[] splits = csv.split(",");
		int[] array = new int[splits.length];
		for (int i = 0; i < splits.length; i++) {
			array[i] = Integer.parseInt(splits[i].trim());
		}
		return array;
	}

	private static int safeParseInt(String regex, String test) {
		String[] split = test.split(regex);
		if (split.length > 1) {
			return Integer.parseInt(split[1].trim());
		}
		return 0;
	}

	private <T extends TemplateRule> FixResult<T> getRule(StructureVersion version, List<String> ruleData, String ruleType, TemplateRule rule) throws TemplateRuleParsingException {
		Iterator<String> it = ruleData.iterator();
		String name = null;
		int ruleNumber = -1;
		List<String> ruleDataPackage = new ArrayList<>();
		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith(ruleType + ":")) {
				continue;
			}
			if (line.startsWith(":end" + ruleType)) {
				break;
			}
			if (line.startsWith("plugin=")) {
				name = TemplateParser.safeParseString("=", line);
			}
			if (line.startsWith("number=")) {
				ruleNumber = TemplateParser.safeParseInt("=", line);
			}
			if (line.startsWith("data:")) {
				String line2;
				while (it.hasNext()) {
					line2 = it.next();
					if (line2.startsWith(":enddata")) {
						break;
					}
					ruleDataPackage.add(line2);
				}
			}
		}

		if (name == null || ruleNumber < 0 || ruleDataPackage.isEmpty()) {
			throw new TemplateRuleParsingException("Not enough data to create template rule.\n" + "name: " + name + "\n" + "number:" + ruleNumber + "\n" + "ruleDataPackage.size:" + ruleDataPackage.size() + "\n");
		}

		FixResult.Builder<T> resultBuilder = new FixResult.Builder<>();

//		if (LATEST_LEGACY.isGreaterThan(version)) {
//			// Cannot parse a rule from an older version of AW2.
//			throw new TemplateRuleParsingException("Cannot convert template from version " + version + ". Version too old.");
//		}

//		Optional<TemplateRule> parser = Optional.of(rule);
//		if (!parser.isPresent()) {
//			throw new TemplateRuleParsingException("Not enough data to create template rule.\n" + "Missing plugin for name: " + name + "\n" + "name: " + name + "\n" + "number:" + ruleNumber + "\n" + "ruleDataPackage.size:" + ruleDataPackage.size() + "\n");
//		}

		rule.parseRule(readTag(ruleDataPackage));
		rule.ruleNumber = ruleNumber;

		T actualRule;
		try {
			//noinspection unchecked
			actualRule = (T) rule;
		}
		catch (ClassCastException e) {
			throw new TemplateRuleParsingException("Incorrect rule type is being returned\n");
		}

		return resultBuilder.build(actualRule);
	}

	private ValueInput readTag(List<String> ruleData) throws TemplateRuleParsingException {
		for (String line : ruleData) {
			if (line.startsWith(JSON_PREFIX)) {
				if (line.contains("structure_scanner_block")) {
					line = JSON_PREFIX + "{blockState:{blockName:\"ancientwarfarestructure:structure_scanner_block\",properties:{}}";
				}
				CompoundTag compound = jsonStringToNbt(line.substring(JSON_PREFIX.length()));
				return TagValueInput.create(ProblemReporter.DISCARDING, lookupProvider, compound);
			}
		}
		return TagValueInput.create(ProblemReporter.DISCARDING, lookupProvider, new CompoundTag());
	}

	private static CompoundTag jsonStringToNbt(String input) {
		JsonElement jsonElement;
		try {
			JsonReader reader = new JsonReader(new StringReader(input));
			reader.setStrictness(Strictness.LENIENT);

			jsonElement = JsonParser.parseReader(reader);
		} catch (JsonSyntaxException e) {
			System.out.println("not valid json: " + input);
			return new CompoundTag();
		}

		Dynamic<JsonElement> d = new Dynamic<>(JsonOps.INSTANCE, jsonElement);
		try {
			Dynamic<Tag> nbtD = d.convert(NbtOps.INSTANCE);
			return nbtD.cast(NbtOps.INSTANCE).asCompound().orElse(new CompoundTag());
		} catch (NullPointerException exception) {
			System.out.println("not valid json: " + input);
		}

		return new CompoundTag();
	}
}