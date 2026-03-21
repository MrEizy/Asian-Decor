package net.zic.builders_zenith.datagen.builders;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;
import net.zic.builders_zenith.util.ModTags;

public class ColorMixerRecipeBuilder {

    public static void generateAllRecipes(RecipeOutput output) {
        // Brick recipes
        generateVanillaRecipe(output);
        generateRecolorRecipe(output);

        // Slab recipes
        generateVanillaSlabRecipe(output);
        generateRecolorSlabRecipe(output);

        // Stairs recipes
        generateVanillaStairsRecipe(output);
        generateRecolorStairsRecipe(output);

        // Wall recipes
        generateVanillaWallRecipe(output);
        generateRecolorWallRecipe(output);
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
                BuildersZenith.MOD_ID,
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
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_any"
        );

        output.accept(id, recipe, null);
    }

    // ========== SLAB RECIPES ==========

    private static void generateVanillaSlabRecipe(RecipeOutput output) {
        // Recipe: Vanilla Brick Slab + Any Dye + Any Dye = Dynamic Dyed Brick Slab
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICK_SLAB));                // Slot 0: Vanilla Brick Slab
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_SLABS instead of DYED_BRICKS
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICK_SLABS.get(DyedBrickType.WHITE_WHITE).get(), 8);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_slabs_vanilla",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_slabs_to_dyed"
        );

        output.accept(id, recipe, null);
    }

    private static void generateRecolorSlabRecipe(RecipeOutput output) {
        // Recipe: Any Dyed Brick Slab + Any Dye + Any Dye = Dynamic Dyed Brick Slab
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModTags.Items.DYED_BRICK_SLABS));  // Slot 0: Any Dyed Brick Slab (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_SLABS instead of DYED_BRICKS
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICK_SLABS.get(DyedBrickType.WHITE_WHITE).get(), 8);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_slabs_recolor",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_slab_any"
        );

        output.accept(id, recipe, null);
    }

    // ========== STAIRS RECIPES ==========

    private static void generateVanillaStairsRecipe(RecipeOutput output) {
        // Recipe: Vanilla Brick Stairs + Any Dye + Any Dye = Dynamic Dyed Brick Stairs
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICK_STAIRS));              // Slot 0: Vanilla Brick Stairs
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_STAIRS instead of DYED_BRICKS
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICK_STAIRS.get(DyedBrickType.WHITE_WHITE).get(), 4);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_stairs_vanilla",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_stairs_to_dyed"
        );

        output.accept(id, recipe, null);
    }

    private static void generateRecolorStairsRecipe(RecipeOutput output) {
        // Recipe: Any Dyed Brick Stairs + Any Dye + Any Dye = Dynamic Dyed Brick Stairs
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModTags.Items.DYED_BRICK_STAIRS)); // Slot 0: Any Dyed Brick Stairs (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_STAIRS instead of DYED_BRICKS
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICK_STAIRS.get(DyedBrickType.WHITE_WHITE).get(), 4);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_stairs_recolor",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_stairs_any"
        );

        output.accept(id, recipe, null);
    }

    // ========== WALL RECIPES ==========

    private static void generateVanillaWallRecipe(RecipeOutput output) {
        // Recipe: Vanilla Brick Wall + Any Dye + Any Dye = Dynamic Dyed Brick Wall
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICK_WALL));                // Slot 0: Vanilla Brick Wall
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_WALLS instead of DYED_BRICKS
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICK_WALLS.get(DyedBrickType.WHITE_WHITE).get(), 6);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_walls_vanilla",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_walls_to_dyed"
        );

        output.accept(id, recipe, null);
    }

    private static void generateRecolorWallRecipe(RecipeOutput output) {
        // Recipe: Any Dyed Brick Wall + Any Dye + Any Dye = Dynamic Dyed Brick Wall
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModTags.Items.DYED_BRICK_WALLS));  // Slot 0: Any Dyed Brick Wall (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(ModTags.Items.DYES));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_WALLS instead of DYED_BRICKS
        ItemStack placeholderResult = new ItemStack(
                ModBlocks.DYED_BRICK_WALLS.get(DyedBrickType.WHITE_WHITE).get(), 6);

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_walls_recolor",
                ingredients,
                placeholderResult,
                100
        );

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_wall_any"
        );

        output.accept(id, recipe, null);
    }
}