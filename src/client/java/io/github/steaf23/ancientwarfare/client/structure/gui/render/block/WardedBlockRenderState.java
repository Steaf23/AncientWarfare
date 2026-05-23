package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import io.github.steaf23.ancientwarfare.structure.block.entity.wardedblock.WardInfo;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlockInfo;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class WardedBlockRenderState extends BlockEntityRenderState {
	CapturedBlockInfo capturedBlockInfo = CapturedBlockInfo.EMPTY;
	WardInfo wardInfo = new WardInfo(null, null);
	BlockModelRenderState blockModel = new BlockModelRenderState();
	boolean showDebugLabel = false;
}
