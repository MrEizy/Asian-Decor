package net.thejadeproject.asiandecor.datagen;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.items.ModItems;
import net.thejadeproject.asiandecor.recipe.BrickMixerRecipe;

public class BrickMixerRecipeBuilder {

    public static void generateAllRecipes(RecipeOutput output) {
        for (DyeColor brickColor : DyeColor.values()) {
            for (DyeColor mortarColor : DyeColor.values()) {
                generateColorRecipe(output, brickColor, mortarColor);
            }
        }
    }

    private static void generateColorRecipe(RecipeOutput output, DyeColor brickColor, DyeColor mortarColor) {
        // Create ingredient from item - this should match any stack of this item regardless of components
        Ingredient baseIngredient = Ingredient.of(ModItems.DYED_BRICK);

        ItemStack result = new ItemStack(ModItems.DYED_BRICK.get(), 8);
        DyedBrickData data = new DyedBrickData(brickColor, mortarColor);
        result.set(ModDataComponents.BRICK_DATA.get(), data);

        BrickMixerRecipe recipe = new BrickMixerRecipe(
                "dyed_bricks",
                baseIngredient,
                result,
                8,
                brickColor,
                mortarColor
        );

        String recipeName = brickColor.getName() + "_" + mortarColor.getName();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "brick_mixer/" + recipeName);

        output.accept(id, recipe, null);
    }
}