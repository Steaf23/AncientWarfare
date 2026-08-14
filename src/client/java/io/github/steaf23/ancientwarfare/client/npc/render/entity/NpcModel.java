package io.github.steaf23.ancientwarfare.client.npc.render.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.steaf23.ancientwarfare.core.AncientWarfare;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public record NpcModel(ModelType modelType, List<Identifier> textures) {

	NpcModel(List<Identifier> textures) {
		this(ModelType.HUMANOID, textures);
	}

	public static NpcModel usingSkinPack(List<String> skinPaths) {
		return new NpcModel(ModelType.HUMANOID, skinPaths.stream()
				.map(name -> AncientWarfare.id("textures/entity/npc/skin_pack/" + name + ".png"))
				.toList());
	}

	public static NpcModel usingSkinPack(String skinPath) {
		return NpcModel.usingSkinPack(List.of(skinPath));
	}

	public static final Codec<NpcModel> CODEC = RecordCodecBuilder.create(i -> i.group(
			StringRepresentable.fromEnum(ModelType::values).fieldOf("model_type").forGetter(NpcModel::modelType),
			Identifier.CODEC.listOf().fieldOf("textures").forGetter(NpcModel::textures)
	).apply(i, NpcModel::new));

	public enum ModelType implements StringRepresentable {
		HUMANOID("ancientwarfare:humanoid"),
		;

		private final String name;

		ModelType(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
