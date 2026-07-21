package io.github.steaf23.ancientwarfare.client.structure.gui.render.block;

import io.github.steaf23.ancientwarfare.structure.block.wardedblock.WardInfo;
import io.github.steaf23.ancientwarfare.structure.component.CapturedBlockInfo;
//? > 1.21.11
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class WardedBlockRenderState extends BlockEntityRenderState {
	public CapturedBlockInfo capturedBlockInfo = CapturedBlockInfo.EMPTY;
	public WardInfo wardInfo = new WardInfo(null, null);
	public boolean showDebugLabel = false;

	//? if <=1.21.11 {
	/*public Optional<BlockState> blockModel = Optional.empty();
	*///? } else {
	public BlockModelRenderState blockModel = new BlockModelRenderState();
	//?}

}
