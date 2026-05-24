package io.github.steaf23.ancientwarfare.structure.template.legacy;

import net.minecraft.resources.Identifier;

public class TemplateEntityDataParser {

	public static Identifier updateId(Identifier oldId) {
		if (oldId.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
			return switch (oldId.getPath()) {
				case "evocation_illager" -> Identifier.withDefaultNamespace("evoker");
				case "illusion_illager" -> Identifier.withDefaultNamespace("illusioner");
				default -> oldId;
			};
		}

		return oldId;
	}
}
