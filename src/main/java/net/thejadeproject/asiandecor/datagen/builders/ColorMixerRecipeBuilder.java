package net.thejadeproject.asiandecor.datagen.builders;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;

public class ColorMixerRecipeBuilder {

    public static void generateAllRecipes(RecipeOutput output) {
        generateVanillaBrickRecipes(output);
        generateRecolorRecipes(output);
    }

    private static void generateVanillaBrickRecipes(RecipeOutput output) {
        for (DyedBrickType type : DyedBrickType.values()) {
            DyeColor brickColor = type.getBrickColor();
            DyeColor mortarColor = type.getMortarColor();

            // Create list with ALL 3 ingredients explicitly
            NonNullList<Ingredient> ingredients = NonNullList.create();
            ingredients.add(Ingredient.of(Items.BRICKS));                   // Slot 0: Base
            ingredients.add(Ingredient.of(getDyeItem(brickColor)));         // Slot 1: Primary dye (brick color)
            ingredients.add(Ingredient.of(getDyeItem(mortarColor)));        // Slot 2: Secondary dye (mortar color)

            Block resultBlock = ModBlocks.DYED_BRICKS.get(type).get();
            ItemStack result = new ItemStack(resultBlock, 8);

            ColorMixerRecipe recipe = new ColorMixerRecipe(
                    "dyed_bricks",
                    ingredients,
                    result,
                    100
                    // Removed: null componentData parameter
            );

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    AsianDecor.MOD_ID,
                    "color_mixer/coloring/brick_" + type.getSerializedName()
            );

            output.accept(id, recipe, null);
        }
    }

    private static void generateRecolorRecipes(RecipeOutput output) {
        // For recoloring: any dyed brick + new dyes = new colored brick
        for (DyedBrickType inputType : DyedBrickType.values()) {
            for (DyedBrickType outputType : DyedBrickType.values()) {
                // Skip same color recipes (no change)
                if (inputType == outputType) continue;

                NonNullList<Ingredient> ingredients = NonNullList.create();
                ingredients.add(Ingredient.of(ModBlocks.DYED_BRICKS.get(inputType).get()));  // Slot 0: Input dyed brick
                ingredients.add(Ingredient.of(getDyeItem(outputType.getBrickColor())));      // Slot 1: New brick color
                ingredients.add(Ingredient.of(getDyeItem(outputType.getMortarColor())));     // Slot 2: New mortar color

                Block resultBlock = ModBlocks.DYED_BRICKS.get(outputType).get();
                ItemStack result = new ItemStack(resultBlock, 8);

                ColorMixerRecipe recipe = new ColorMixerRecipe(
                        "dyed_bricks",
                        ingredients,
                        result,
                        100
                        // Removed: null componentData parameter
                );

                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                        AsianDecor.MOD_ID,
                        "color_mixer/recolor/" + inputType.getSerializedName() + "_to_" + outputType.getSerializedName()
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