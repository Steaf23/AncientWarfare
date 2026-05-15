package io.github.steaf23.ancientwarfare.client.core.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class RenderHelper {

	public static void renderBoxOutline(Matrix4f matrix, VertexConsumer buffer, AABB box, float r, float g, float b, float a, Vec3 camDir) {
		// Draw all 12 edges of the box
		float width = 0.1f;
		drawThickLine(buffer, matrix, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, width, camDir, r, g, b, a);
		drawThickLine(buffer, matrix, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, width, camDir, r, g, b, a);
		drawThickLine(buffer, matrix, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ, width, camDir, r, g, b, a);

		drawThickLine(buffer, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, width, camDir, r, g, b, a);
		drawThickLine(buffer, matrix, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, width, camDir, r, g, b, a);

		drawThickLine(buffer, matrix, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, width, camDir, r, g, b, a);
		drawThickLine(buffer, matrix, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ, width, camDir, r, g, b, a);

		drawThickLine(buffer, matrix, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ, width, camDir, r, g, b, a);
		drawThickLine(buffer, matrix, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ, width, camDir, r, g, b, a);

		drawThickLine(buffer, matrix, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, width, camDir, r, g, b, a);
		drawThickLine(buffer, matrix, box.maxX, box.maxY, box.maxZ, box.maxX, box.minY, box.maxZ, width, camDir, r, g, b, a);
		drawThickLine(buffer, matrix, box.maxX, box.maxY, box.maxZ, box.maxX, box.maxY, box.minZ, width, camDir, r, g, b, a);
	}

	public static void drawThickLine(VertexConsumer buffer, Matrix4f matrix,
									  double x1, double y1, double z1,
									  double x2, double y2, double z2,
									  float width, Vec3 cameraDir,
									  float r, float g, float b, float a) {
		Vec3 start = new Vec3(x1, y1, z1);
		Vec3 end = new Vec3(x2, y2, z2);
		Vec3 dir = end.subtract(start).normalize();

		// Vector perpendicular to both line direction and camera direction
		Vec3 right = dir.cross(cameraDir).normalize();

		// If degenerate (line pointing at camera), pick a fallback axis
		if (right.lengthSqr() < 1.0e-6) {
			right = dir.cross(new Vec3(0, 1, 0)).normalize();
		}

		// Offset for half-width
		Vec3 offset = right.scale(width / 2.0);

		// Build a camera-facing quad
		Vec3 p1 = start.add(offset);
		Vec3 p2 = start.subtract(offset);
		Vec3 p3 = end.subtract(offset);
		Vec3 p4 = end.add(offset);

		emitQuad(buffer, matrix, p1, p2, p3, p4, r, g, b, a);
	}

	public static void emitQuad(VertexConsumer buffer, Matrix4f matrix,
								 Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4,
								 float r, float g, float b, float a) {
		emit(buffer, matrix, p1, r, g, b, a);
		emit(buffer, matrix, p2, r, g, b, a);
		emit(buffer, matrix, p3, r, g, b, a);
		emit(buffer, matrix, p4, r, g, b, a);
	}

	public static void emit(VertexConsumer buffer, Matrix4f matrix,
							 Vec3 pos, float r, float g, float b, float a) {

		buffer.addVertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z)
				.setColor(r, g, b, a)
				.setNormal(0f, 1f, 0f);
	}
}
