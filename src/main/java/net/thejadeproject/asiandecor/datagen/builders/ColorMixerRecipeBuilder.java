package net.thejadeproject.asiandecor.datagen.builders;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.items.ModItems;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;

public class ColorMixerRecipeBuilder {

    public static void generateAllRecipes(RecipeOutput output) {
        generateVanillaBrickRecipes(output);
        generateRecolorRecipes(output);
    }

    private static void generateVanillaBrickRecipes(RecipeOutput output) {
        for (DyeColor brickColor : DyeColor.values()) {
            for (DyeColor mortarColor : DyeColor.values()) {
                // Create list with ALL 3 ingredients explicitly
                NonNullList<Ingredient> ingredients = NonNullList.create();
                ingredients.add(Ingredient.of(Items.BRICKS));                   // Slot 0: Base
                ingredients.add(Ingredient.of(getDyeItem(brickColor)));         // Slot 1: Primary dye
                ingredients.add(Ingredient.of(getDyeItem(mortarColor)));        // Slot 2: Secondary dye

                ItemStack result = new ItemStack(ModItems.DYED_BRICK.get(), 8);
                DyedBrickData componentData = new DyedBrickData(brickColor, mortarColor);

                ColorMixerRecipe recipe = new ColorMixerRecipe(
                        "dyed_bricks",
                        ingredients,
                        result,
                        100,
                        componentData
                );

                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                        AsianDecor.MOD_ID,
                        "color_mixer/coloring/brick_" + brickColor.getName() + "_" + mortarColor.getName()
                );

                output.accept(id, recipe, null);
            }
        }
    }

    private static void generateRecolorRecipes(RecipeOutput output) {
        for (DyeColor brickColor : DyeColor.values()) {
            for (DyeColor mortarColor : DyeColor.values()) {
                NonNullList<Ingredient> ingredients = NonNullList.create();
                ingredients.add(Ingredient.of(ModItems.DYED_BRICK.get()));      // Slot 0: Base (dyed brick)
                ingredients.add(Ingredient.of(getDyeItem(brickColor)));         // Slot 1: Primary dye
                ingredients.add(Ingredient.of(getDyeItem(mortarColor)));        // Slot 2: Secondary dye

                ItemStack result = new ItemStack(ModItems.DYED_BRICK.get(), 8);
                DyedBrickData componentData = new DyedBrickData(brickColor, mortarColor);

                ColorMixerRecipe recipe = new ColorMixerRecipe(
                        "dyed_bricks_recolor",
                        ingredients,
                        result,
                        100,
                        componentData
                );

                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                        AsianDecor.MOD_ID,
                        "color_mixer/recoloring/recolor_" + brickColor.getName() + "_" + mortarColor.getName()
                );

                output.accept(id, recipe, null);
            }
        }
    }

    private static net.minecraft.world.item.Item getDyeItem(DyeColor color) {
        return switch (color) {
            case WHITE -> Items.WHITE_DYE;
            case ORANGE -> Items.ORANGE_DYE;
            case MAGENTA -> Items.MAGENTA_DYE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
            case YELLOW -> Items.YELLOW_DYE;
            case LIME -> Items.LIME_DYE;
            case PINK -> Items.PINK_DYE;
            case GRAY -> Items.GRAY_DYE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
            case CYAN -> Items.CYAN_DYE;
            case PURPLE -> Items.PURPLE_DYE;
            case BLUE -> Items.BLUE_DYE;
            case BROWN -> Items.BROWN_DYE;
            case GREEN -> Items.GREEN_DYE;
            case RED -> Items.RED_DYE;
            case BLACK -> Items.BLACK_DYE;
        };
    }
}