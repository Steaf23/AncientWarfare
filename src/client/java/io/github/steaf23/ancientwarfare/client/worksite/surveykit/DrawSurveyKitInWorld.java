package io.github.steaf23.ancientwarfare.client.worksite.surveykit;

import io.github.steaf23.ancientwarfare.client.core.render.RenderHelper;
import io.github.steaf23.ancientwarfare.core.registry.AWComponents;
import io.github.steaf23.ancientwarfare.core.registry.AWItems;
import io.github.steaf23.ancientwarfare.core.util.EntityHelper;
import io.github.steaf23.ancientwarfare.worksite.marker.SurveyArea;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DrawSurveyKitInWorld {

	public static void drawSurveyKit(LevelRenderContext context) {
		List<Vec3> stakes = getStakePositions();
		if (stakes.isEmpty()) {
			return;
		}

		RenderHelper.customGeometry(context, RenderTypes.debugQuads(), (consumer, poses, cameraPos) -> {
			for (int i = 0; i < stakes.size(); i++) {
				Vec3 stake = stakes.get(i);
				// If this is the last position, don't draw a line.
				Vec3 cameraDir = cameraPos.subtract(stake).normalize();
				if (i < stakes.size() - 1) {
					Vec3 other = stakes.get(i + 1);
					stake = stake.add(cameraPos.multiply(new Vec3(-1, -1, -1)));
					other = other.add(cameraPos.multiply(new Vec3(-1, -1, -1)));
					RenderHelper.drawThickLine(
							consumer, poses.last().pose(),
							stake.x, stake.y, stake.z,
							other.x, other.y, other.z,
							0.15f,
							cameraDir,
							1.0f, 1.0f, 1.0f, 1.0f);
				}
			}
		});

	}

	private static List<Vec3> getStakePositions() {
		if (Minecraft.getInstance() == null || Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) {
			return List.of();
		}

		Player player = Minecraft.getInstance().player;

		ItemStack kit = EntityHelper.getItemFromEitherHand(player, AWItems.SURVEY_KIT);
		if (kit.isEmpty()) {
			return List.of();
		}

		List<BlockPos> stakes = kit.getOrDefault(AWComponents.SURVEY_STAKES, SurveyArea.EMPTY).stakes();
		return stakes.stream()
				.map(Vec3::atCenterOf)
				.toList();
	}

}
