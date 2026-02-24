package net.thejadeproject.asiandecor.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, AsianDecor.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, AsianDecor.MOD_ID);

    // NEW: JSON Recipe System
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CarpenterRecipes>> CARPENTER_SERIALIZER =
            SERIALIZERS.register("carpenter", CarpenterRecipes.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<CarpenterRecipes>> CARPENTER_TYPE =
            TYPES.register("carpenter", () -> new RecipeType<CarpenterRecipes>() {
                @Override
                public String toString() {
                    return "carpenter";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ColorMixerRecipe>> COLOR_MIXER_SERIALIZER =
            SERIALIZERS.register("color_mixer", ColorMixerRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ColorMixerRecipe>> COLOR_MIXER_TYPE =
            TYPES.register("color_mixer", () -> new RecipeType<ColorMixerRecipe>() {
                @Override
                public String toString() { return "color_mixer"; }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}