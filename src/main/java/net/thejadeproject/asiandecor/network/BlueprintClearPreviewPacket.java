package net.thejadeproject.asiandecor.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.client.renderer.BlueprintPreviewRenderer;

public record BlueprintClearPreviewPacket() implements CustomPacketPayload {
    public static final Type<BlueprintClearPreviewPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "blueprint_preview_clear"));

    public static final StreamCodec<ByteBuf, BlueprintClearPreviewPacket> STREAM_CODEC = StreamCodec.unit(new BlueprintClearPreviewPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void sendToPlayer(ServerPlayer player) {
        BlueprintSetPreviewPacket.getPlayerPreviewPos(player.getUUID()); // Just to clear server-side
        PacketDistributor.sendToPlayer(player, new BlueprintClearPreviewPacket());
    }

    public static void handle(BlueprintClearPreviewPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                BlueprintPreviewRenderer.clearPreview(context.player().getUUID());
            }
        });
    }
}