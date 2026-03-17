package net.thejadeproject.asiandecor.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record HandheldFillerData(
        int charge,
        Optional<Block> copiedBlock
) {
    public static final int MAX_CHARGE = 100000;

    public static final Codec<HandheldFillerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("charge").forGetter(HandheldFillerData::charge),
                    BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("copied_block").forGetter(HandheldFillerData::copiedBlock)
            ).apply(instance, HandheldFillerData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, HandheldFillerData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HandheldFillerData::charge,
            ByteBufCodecs.optional(ByteBufCodecs.registry(Registries.BLOCK)),
            HandheldFillerData::copiedBlock,
            HandheldFillerData::new
    );

    public HandheldFillerData() {
        this(0, Optional.empty());
    }

    public HandheldFillerData withCharge(int newCharge) {
        return new HandheldFillerData(Math.min(newCharge, MAX_CHARGE), this.copiedBlock);
    }

    public HandheldFillerData withCopiedBlock(Block block) {
        return new HandheldFillerData(this.charge, Optional.of(block));
    }

    public HandheldFillerData clearCopiedBlock() {
        return new HandheldFillerData(this.charge, Optional.empty());
    }
}