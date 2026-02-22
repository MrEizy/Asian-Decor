package net.thejadeproject.asiandecor.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;

public record DyedBrickData(DyeColor brickColor, DyeColor mortarColor) {
    public static final Codec<DyedBrickData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    DyeColor.CODEC.fieldOf("brick_color").forGetter(DyedBrickData::brickColor),
                    DyeColor.CODEC.fieldOf("mortar_color").forGetter(DyedBrickData::mortarColor)
            ).apply(instance, DyedBrickData::new)
    );

    public static final StreamCodec<ByteBuf, DyedBrickData> STREAM_CODEC = StreamCodec.composite(
            DyeColor.STREAM_CODEC, DyedBrickData::brickColor,
            DyeColor.STREAM_CODEC, DyedBrickData::mortarColor,
            DyedBrickData::new
    );
}
