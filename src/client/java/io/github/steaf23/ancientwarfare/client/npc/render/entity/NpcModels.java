package io.github.steaf23.ancientwarfare.client.npc.render.entity;

import io.github.steaf23.ancientwarfare.npc.entity.faction.FactionNpc;
import net.minecraft.resources.Identifier;

import java.util.Map;

public class NpcModels {

	private static Map<Identifier, NpcModel> models = Map.of();

	private NpcModels() {}

	public static void setModels(Map<Identifier, NpcModel> models) {
		NpcModels.models = models;
	}
	
	public static Identifier getSkinForFactionNpc(FactionNpc npc) {
		Identifier id = npc.getFullNpcId();
		NpcModel model = models.get(id);
		if (model == null) {
			return NpcEntityRenderer.FALLBACK;
		} else {
			int index = Math.abs(npc.skinVariant()) % model.textures().size();
			return model.textures().get(index);
		}
	}
}
