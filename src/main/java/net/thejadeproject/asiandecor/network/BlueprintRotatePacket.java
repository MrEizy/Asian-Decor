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
import net.thejadeproject.asiandecor.component.BlueprintData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlueprintItem;


public record BlueprintRotatePacket(boolean mainHand, int direction) implements CustomPacketPayload {
    public static final Type<BlueprintRotatePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "blueprint_rotate"));

    public static final StreamCodec<ByteBuf, BlueprintRotatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            BlueprintRotatePacket::mainHand,
            ByteBufCodecs.INT,
            BlueprintRotatePacket::direction,
            BlueprintRotatePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BlueprintRotatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            InteractionHand hand = packet.mainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() instanceof BlueprintItem) {
                BlueprintData data = stack.getOrDefault(ModDataComponents.BLUEPRINT_DATA.get(), BlueprintData.EMPTY);

                if (data.hasData()) {
                    int newRotation = (data.rotation() + packet.direction() + 4) % 4;
                    BlueprintData rotated = data.withRotation(newRotation);
                    stack.set(ModDataComponents.BLUEPRINT_DATA.get(), rotated);

                    // Show rotation in action bar
                    player.displayClientMessage(
                            net.minecraft.network.chat.Component.translatable(
                                    "message.asiandecor.blueprint.rotated",
                                    rotated.getRotationName()
                            ),
                            true
                    );
                }
            }
        });
    }
}