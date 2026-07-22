package io.github.steaf23.ancientwarfare.core.research;

import eu.pb4.trinkets.api.callback.TrinketCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ResearchBook extends Item implements TrinketCallback {

	public ResearchBook(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide()) {
			return InteractionResult.PASS;
		}

		ServerPlayNetworking.send((ServerPlayer)player, ResearchBookPayload.INSTANCE);

		return InteractionResult.SUCCESS;
	}
}
