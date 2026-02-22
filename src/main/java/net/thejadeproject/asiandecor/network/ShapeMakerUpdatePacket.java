package net.thejadeproject.asiandecor.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.asiandecor.AsianDecor;

public record ShapeMakerUpdatePacket(
        BlockPos pos,
        int shapeOrdinal,
        int xOffset,
        int yOffset,
        int zOffset,
        int radius,
        int thickness,
        boolean previewEnabled,
        int redstoneModeOrdinal
) implements CustomPacketPayload {

    public static final Type<ShapeMakerUpdatePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "shape_maker_update")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapeMakerUpdatePacket> STREAM_CODEC =
            StreamCodec.ofMember(
                    ShapeMakerUpdatePacket::write,
                    ShapeMakerUpdatePacket::read
            );

    private static ShapeMakerUpdatePacket read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = BlockPos.STREAM_CODEC.decode(buf);
        int shapeOrdinal = buf.readVarInt();
        int xOffset = buf.readVarInt();
        int yOffset = buf.readVarInt();
        int zOffset = buf.readVarInt();
        int radius = buf.readVarInt();
        int thickness = buf.readVarInt();
        boolean previewEnabled = buf.readBoolean();
        int redstoneModeOrdinal = buf.readVarInt();
        return new ShapeMakerUpdatePacket(pos, shapeOrdinal, xOffset, yOffset, zOffset,
                radius, thickness, previewEnabled, redstoneModeOrdinal);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        BlockPos.STREAM_CODEC.encode(buf, pos);
        buf.writeVarInt(shapeOrdinal);
        buf.writeVarInt(xOffset);
        buf.writeVarInt(yOffset);
        buf.writeVarInt(zOffset);
        buf.writeVarInt(radius);
        buf.writeVarInt(thickness);
        buf.writeBoolean(previewEnabled);
        buf.writeVarInt(redstoneModeOrdinal);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}