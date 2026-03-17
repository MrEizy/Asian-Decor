// ColorMixerRecipeBuilder.java
package net.thejadeproject.asiandecor.datagen.builders;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;
import net.thejadeproject.asiandecor.util.ModTags;

public class ColorMixerRecipeBuilder {

    public static void generateAllRecipes(RecipeOutput output) {
        // Only 2 recipes total!
        generateVanillaRecipe(output);
        generateRecolorRecipe(output);
    }

    private static void generateVanillaRecipe(RecipeOutput output) {
        // Recipe 1: Vanilla Bricks + Any Dye + Any Dye = Dynamic Dyed Brick
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICKS));                    // Slot 0: Vanilla Bricks
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // Placeholder result - actual result determined by dye colors
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICKS.get(DyedBrickType.WHITE_WHITE).get(), 8);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_bricks_vanilla",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                AsianDecor.MOD_ID,
                "color_mixer/vanilla_bricks_to_dyed"
        );

        output.accept(id, recipe, null);
    }

    private static void generateRecolorRecipe(RecipeOutput output) {
        // Recipe 2: Any Dyed Brick + Any Dye + Any Dye = Dynamic Dyed Brick
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModTags.Items.DYED_BRICKS));       // Slot 0: Any Dyed Brick (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // Placeholder result - actual result determined by dye colors
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICKS.get(DyedBrickType.WHITE_WHITE).get(), 8);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_bricks_recolor",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                AsianDecor.MOD_ID,
                "color_mixer/recolor_any"
        );

        output.accept(id, recipe, null);
    }
}