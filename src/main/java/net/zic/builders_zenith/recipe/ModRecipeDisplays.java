package net.zic.builders_zenith.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.builders_zenith.BuildersZenith;

import java.util.function.Supplier;

public class ModRecipeDisplays {

    public static final DeferredRegister<RecipeDisplay.Type<?>> RECIPE_DISPLAY_TYPES =
            DeferredRegister.create(Registries.RECIPE_DISPLAY, BuildersZenith.MOD_ID);

    public static final Supplier<RecipeDisplay.Type<CarpenterRecipeDisplay>> CARPENTER =
            RECIPE_DISPLAY_TYPES.register("carpenter", () -> new RecipeDisplay.Type<>(
                    CarpenterRecipeDisplay.MAP_CODEC,
                    CarpenterRecipeDisplay.STREAM_CODEC
            ));
}
