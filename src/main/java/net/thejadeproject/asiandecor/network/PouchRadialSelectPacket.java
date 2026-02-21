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

public record PouchRadialSelectPacket(boolean mainHand, int slotIndex) implements CustomPacketPayload {
    public static final Type<PouchRadialSelectPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "pouch_radial_select"));

    public static final StreamCodec<ByteBuf, PouchRadialSelectPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            PouchRadialSelectPacket::mainHand,
            ByteBufCodecs.INT,
            PouchRadialSelectPacket::slotIndex,
            PouchRadialSelectPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PouchRadialSelectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            InteractionHand hand = packet.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() instanceof BlockPouchItem) {
                PouchContents contents = stack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
                stack.set(ModDataComponents.POUCH_CONTENTS.get(), contents.withSelectedSlot(packet.slotIndex()));

                // Find the selected stack from entries
                ItemStack selected = ItemStack.EMPTY;
                for (PouchContents.SlotEntry entry : contents.entries()) {
                    if (entry.slot() == packet.slotIndex()) {
                        selected = entry.stack();
                        break;
                    }
                }

                if (!selected.isEmpty()) {
                    player.displayClientMessage(
                            net.minecraft.network.chat.Component.translatable(
                                    "message.asiandecor.block_pouch.selected",
                                    selected.getHoverName()
                            ),
                            true
                    );
                }
            }
        });
    }
}