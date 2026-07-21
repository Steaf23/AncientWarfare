package io.github.steaf23.ancientwarfare.structure.block.invalidconversionblock;

import com.mojang.serialization.Codec;
import io.github.steaf23.ancientwarfare.core.registry.AWBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

public class InvalidConversionBlockEntity extends BlockEntity {

	List<String> comment = List.of();

	public InvalidConversionBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(AWBlockEntities.INVALID_CONVERSION, worldPosition, blockState);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		comment = input.read("comment", Codec.STRING.listOf()).orElse(List.of());
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.store("comment", Codec.STRING.listOf(), comment);
	}

	public List<String> getComment() {
		return comment;
	}
}
