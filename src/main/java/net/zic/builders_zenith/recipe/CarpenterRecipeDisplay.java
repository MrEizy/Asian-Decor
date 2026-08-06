package net.zic.builders_zenith.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record CarpenterRecipeDisplay(
        SlotDisplay ingredient,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<CarpenterRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SlotDisplay.CODEC.fieldOf("ingredient").forGetter(CarpenterRecipeDisplay::ingredient),
            SlotDisplay.CODEC.fieldOf("result").forGetter(CarpenterRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(CarpenterRecipeDisplay::craftingStation)
    ).apply(inst, CarpenterRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CarpenterRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, CarpenterRecipeDisplay::ingredient,
            SlotDisplay.STREAM_CODEC, CarpenterRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, CarpenterRecipeDisplay::craftingStation,
            CarpenterRecipeDisplay::new
    );

    @Override
    public RecipeDisplay.Type<? extends RecipeDisplay> type() {
        return ModRecipeDisplays.CARPENTER.get(); // See registration note below
    }
}