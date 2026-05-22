package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import io.github.steaf23.ancientwarfare.structure.block.WardedBlockData;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlock;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class WardedBlockRenderState extends BlockEntityRenderState {
	CapturedBlock capturedBlock = CapturedBlock.EMPTY;
	WardedBlockData wardData = new WardedBlockData(null, null);
	BlockModelRenderState blockModel = new BlockModelRenderState();
	boolean showDebugLabel = false;
}
