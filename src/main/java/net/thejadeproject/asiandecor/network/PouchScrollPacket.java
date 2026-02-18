package net.thejadeproject.asiandecor.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.component.PouchContents;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlockPouchItem;

public record PouchScrollPacket(boolean mainHand, int direction) implements CustomPacketPayload {
    public static final Type<PouchScrollPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "pouch_scroll"));

    public static final StreamCodec<ByteBuf, PouchScrollPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            PouchScrollPacket::mainHand,
            ByteBufCodecs.INT,
            PouchScrollPacket::direction,
            PouchScrollPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PouchScrollPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            InteractionHand hand = packet.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() instanceof BlockPouchItem) {
                PouchContents contents = stack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
                PouchContents newContents = contents.cycleSelection(packet.direction());
                stack.set(ModDataComponents.POUCH_CONTENTS.get(), newContents);

                ItemStack selected = newContents.getSelectedStack();
                if (!selected.isEmpty()) {
                    player.displayClientMessage(
                            net.minecraft.network.chat.Component.translatable(
                                    "message.asiandecor.block_pouch.selected",
                                    selected.getHoverName(),
                                    selected.getCount()
                            ),
                            true
                    );
                } else {
                    player.displayClientMessage(
                            net.minecraft.network.chat.Component.translatable("message.asiandecor.block_pouch.empty"),
                            true
                    );
                }
            }
        });
    }
}

