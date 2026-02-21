package net.thejadeproject.asiandecor.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.items.buildersgadgets.TrowelItem;

public record TrowelTogglePacket(boolean mainHand) implements CustomPacketPayload {

    public static final Type<TrowelTogglePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "trowel_toggle"));

    public static final StreamCodec<ByteBuf, TrowelTogglePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            TrowelTogglePacket::mainHand,
            TrowelTogglePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TrowelTogglePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            InteractionHand hand = packet.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() instanceof TrowelItem) {
                TrowelItem.Mode newMode = TrowelItem.toggleMode(stack);

                // Send message to player
                player.displayClientMessage(Component.translatable(
                        "message.asiandecor.trowel.mode_changed",
                        Component.translatable("tooltip.asiandecor.trowel.mode." + newMode.getName())
                ), true);
            }
        });
    }
}