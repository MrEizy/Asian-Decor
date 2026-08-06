package net.zic.builders_zenith.datagen.builders;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;
import net.zic.builders_zenith.util.ModTags;

public class ColorMixerRecipeBuilder {

    public static void generateAllRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        // Brick recipes
        generateVanillaRecipe(registries, output);
        generateRecolorRecipe(registries, output);

        // Slab recipes
        generateVanillaSlabRecipe(registries, output);
        generateRecolorSlabRecipe(registries, output);

        // Vertical Slab recipes
        generateVanillaVerticalSlabRecipe(registries, output);
        generateRecolorVerticalSlabRecipe(registries, output);

        // Stairs recipes
        generateVanillaStairsRecipe(registries, output);
        generateRecolorStairsRecipe(registries, output);

        // Wall recipes
        generateVanillaWallRecipe(registries, output);
        generateRecolorWallRecipe(registries, output);
    }

    private static void generateVanillaRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe 1: Vanilla Bricks + Any Dye + Any Dye = Dynamic Dyed Brick
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICKS));                    // Slot 0: Vanilla Bricks
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 2: Any Dye (Tag)

        // Placeholder result - actual result determined by dye colors
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_bricks_vanilla",
                ingredients,
                ModBlocks.DYED_BRICKS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_bricks_to_dyed"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    private static void generateRecolorRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe 2: Any Dyed Brick + Any Dye + Any Dye = Dynamic Dyed Brick
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYED_BRICKS)));       // Slot 0: Any Dyed Brick (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 2: Any Dye (Tag)

        // Placeholder result - actual result determined by dye colors
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_bricks_recolor",
                ingredients,
                ModBlocks.DYED_BRICKS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_any"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    // ========== SLAB RECIPES ==========

    private static void generateVanillaVerticalSlabRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModBlocks.BRICK_VERTICAL_SLAB)); // Or a new vertical slab item
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_vertical_slabs_vanilla",
                ingredients,
                ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_vertical_slabs_to_dyed"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    private static void generateRecolorVerticalSlabRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYED_BRICK_VERTICAL_SLABS)));
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));

        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_vertical_slabs_recolor",
                ingredients,
                ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_vertical_slab_any"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    private static void generateVanillaSlabRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe: Vanilla Brick Slab + Any Dye + Any Dye = Dynamic Dyed Brick Slab
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICK_SLAB));                // Slot 0: Vanilla Brick Slab
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_SLABS instead of DYED_BRICKS
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_slabs_vanilla",
                ingredients,
                ModBlocks.DYED_BRICK_SLABS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_slabs_to_dyed"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    private static void generateRecolorSlabRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe: Any Dyed Brick Slab + Any Dye + Any Dye = Dynamic Dyed Brick Slab
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYED_BRICK_SLABS))); // Slot 0: Any Dyed Brick Slab (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES))); // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES))); // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_SLABS instead of DYED_BRICKS
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_slabs_recolor",
                ingredients,
                ModBlocks.DYED_BRICK_SLABS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_slab_any"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    // ========== STAIRS RECIPES ==========

    private static void generateVanillaStairsRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe: Vanilla Brick Stairs + Any Dye + Any Dye = Dynamic Dyed Brick Stairs
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICK_STAIRS)); // Slot 0: Vanilla Brick Stairs
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES))); // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES))); // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_STAIRS instead of DYED_BRICKS
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_stairs_vanilla",
                ingredients,
                ModBlocks.DYED_BRICK_STAIRS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_stairs_to_dyed"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    private static void generateRecolorStairsRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe: Any Dyed Brick Stairs + Any Dye + Any Dye = Dynamic Dyed Brick Stairs
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYED_BRICK_STAIRS))); // Slot 0: Any Dyed Brick Stairs (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_STAIRS instead of DYED_BRICKS
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_stairs_recolor",
                ingredients,
                ModBlocks.DYED_BRICK_STAIRS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_stairs_any"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    // ========== WALL RECIPES ==========

    private static void generateVanillaWallRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe: Vanilla Brick Wall + Any Dye + Any Dye = Dynamic Dyed Brick Wall
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(Items.BRICK_WALL));                // Slot 0: Vanilla Brick Wall
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES)));              // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_WALLS instead of DYED_BRICKS
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_walls_vanilla",
                ingredients,
                ModBlocks.DYED_BRICK_WALLS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/vanilla_walls_to_dyed"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }

    private static void generateRecolorWallRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        // Recipe: Any Dyed Brick Wall + Any Dye + Any Dye = Dynamic Dyed Brick Wall
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYED_BRICK_WALLS))); // Slot 0: Any Dyed Brick Wall (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES))); // Slot 1: Any Dye (Tag)
        ingredients.add(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModTags.Items.DYES))); // Slot 2: Any Dye (Tag)

        // FIXED: Use DYED_BRICK_WALLS instead of DYED_BRICKS
        ColorMixerRecipe recipe = new ColorMixerRecipe(
                "dyed_brick_walls_recolor",
                ingredients,
                ModBlocks.DYED_BRICK_WALLS.get(DyedBrickType.WHITE_WHITE).get().asItem(),
                8,
                100
        );

        Identifier id = Identifier.fromNamespaceAndPath(
                BuildersZenith.MOD_ID,
                "color_mixer/recolor_wall_any"
        );

        output.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }
}