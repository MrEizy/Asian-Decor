package net.zic.builders_zenith.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;

public record ColorMixerData(DyeColor primaryColor, DyeColor secondaryColor) {
    public static final Codec<ColorMixerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    DyeColor.CODEC.fieldOf("primary_color").forGetter(ColorMixerData::primaryColor),
                    DyeColor.CODEC.fieldOf("secondary_color").forGetter(ColorMixerData::secondaryColor)
            ).apply(instance, ColorMixerData::new)
    );

    public static final StreamCodec<ByteBuf, ColorMixerData> STREAM_CODEC = StreamCodec.composite(
            DyeColor.STREAM_CODEC, ColorMixerData::primaryColor,
            DyeColor.STREAM_CODEC, ColorMixerData::secondaryColor,
            ColorMixerData::new
    );
}