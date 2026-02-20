package net.thejadeproject.asiandecor.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.client.renderer.BlueprintPreviewRenderer;
import net.thejadeproject.asiandecor.component.BlueprintData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record BlueprintSetPreviewPacket(BlockPos anchorPos, BlueprintData data) implements CustomPacketPayload {
    public static final Type<BlueprintSetPreviewPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "blueprint_preview_set"));

    public static final StreamCodec<ByteBuf, BlueprintSetPreviewPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public BlueprintSetPreviewPacket decode(ByteBuf buf) {
            BlockPos pos = BlockPos.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf);
            BlueprintData data = BlueprintData.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf);
            return new BlueprintSetPreviewPacket(pos, data);
        }

        @Override
        public void encode(ByteBuf buf, BlueprintSetPreviewPacket packet) {
            BlockPos.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, packet.anchorPos());
            BlueprintData.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, packet.data());
        }
    };

    // Server-side tracking
    private static final Map<UUID, BlockPos> playerPreviews = new HashMap<>();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void sendToPlayer(ServerPlayer player, BlockPos anchorPos, BlueprintData data) {
        playerPreviews.put(player.getUUID(), anchorPos);
        PacketDistributor.sendToPlayer(player, new BlueprintSetPreviewPacket(anchorPos, data));
    }

    public static BlockPos getPlayerPreviewPos(UUID playerId) {
        return playerPreviews.get(playerId);
    }

    public static void handle(BlueprintSetPreviewPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Client-side: set the preview
            if (context.player() != null) {
                BlueprintPreviewRenderer.setPreview(
                        context.player().getUUID(),
                        packet.anchorPos(),
                        packet.data()
                );
            }
        });
    }
}