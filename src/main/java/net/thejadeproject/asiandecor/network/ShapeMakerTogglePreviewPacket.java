package net.thejadeproject.asiandecor.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.asiandecor.AsianDecor;

public record ShapeMakerTogglePreviewPacket(BlockPos pos, boolean enabled) implements CustomPacketPayload {

    public static final Type<ShapeMakerTogglePreviewPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "shape_maker_preview")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapeMakerTogglePreviewPacket> STREAM_CODEC =
            StreamCodec.ofMember(
                    ShapeMakerTogglePreviewPacket::write,
                    ShapeMakerTogglePreviewPacket::read
            );

    private static ShapeMakerTogglePreviewPacket read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = BlockPos.STREAM_CODEC.decode(buf);
        boolean enabled = buf.readBoolean();
        return new ShapeMakerTogglePreviewPacket(pos, enabled);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        BlockPos.STREAM_CODEC.encode(buf, pos);
        buf.writeBoolean(enabled);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}