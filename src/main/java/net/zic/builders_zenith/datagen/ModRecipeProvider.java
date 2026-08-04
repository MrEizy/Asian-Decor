package net.zic.builders_zenith.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.datagen.builders.ColorMixerRecipeBuilder;
import net.zic.builders_zenith.items.ModItems;
import net.zic.builders_zenith.recipe.CarpenterRecipes;
import net.zic.builders_zenith.util.ModTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "BuildersZenith Recipes";
        }
    }

    @Override
    protected void buildRecipes() {
        generateCarpenterRecipes(output);
        generateColorMixerRecipes(output);

        shaped(RecipeCategory.MISC, ModItems.BLUEPRINT.get())
                .pattern("PBP")
                .pattern("BSB")
                .pattern("PBP")
                .define('S', Items.STRING)
                .define('P', Items.PAPER)
                .define('B', Items.BLUE_DYE)
                .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
                .save(output, "builders_zenith:shaped/blueprint");

        shaped(RecipeCategory.MISC, ModItems.TAPE_MEASURE.get())
                .pattern("   ")
                .pattern("II ")
                .pattern("IIW")
                .define('I', Items.IRON_INGOT)
                .define('W', ItemTags.WOOL_CARPETS)
                .unlockedBy("has_wool_carpet", has(ItemTags.WOOL_CARPETS))
                .save(output, "builders_zenith:shaped/tape_measure");

        shaped(RecipeCategory.MISC, ModItems.TROWEL.get())
                .pattern(" II")
                .pattern(" II")
                .pattern("S  ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(output, "builders_zenith:shaped/trowel");

        shaped(RecipeCategory.MISC, ModItems.HANDHELD_FILLER.get())
                .pattern("III")
                .pattern(" II")
                .pattern("S I")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(output, "builders_zenith:shaped/handheld_filler");

        shaped(RecipeCategory.MISC, ModItems.WHITE_BLOCK_POUCH.get())
                .pattern("WWW")
                .pattern("WCW")
                .pattern("WWW")
                .define('W', Items.WHITE_WOOL)
                .define('C', Items.CHEST)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(output, "builders_zenith:shaped/white_block_pouch");

        shaped(RecipeCategory.MISC, ModBlocks.CARPENTER.get())
                .pattern("SIS")
                .pattern("EAE")
                .pattern("EEE")
                .define('E', ItemTags.PLANKS)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('A', ItemTags.AXES)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, "builders_zenith:shaped/carpenters_block");

        shaped(RecipeCategory.MISC, ModBlocks.COLOR_MIXER.get())
                .pattern("SIS")
                .pattern("EAE")
                .pattern("EEE")
                .define('S', ModTags.Items.DYES)
                .define('E', ItemTags.WOOL)
                .define('A', Items.STICK)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, "builders_zenith:shaped/color_mixer_block");

        record VerticalSlabRecipeData(String name, Supplier<Block> verticalSlab, Block vanillaSlab) {}

        List<VerticalSlabRecipeData> verticalSlabs = List.of(
                new VerticalSlabRecipeData("oak", ModBlocks.OAK_VERTICAL_SLAB::get, Blocks.OAK_PLANKS),
                new VerticalSlabRecipeData("spruce", ModBlocks.SPRUCE_VERTICAL_SLAB::get, Blocks.SPRUCE_PLANKS),
                new VerticalSlabRecipeData("birch", ModBlocks.BIRCH_VERTICAL_SLAB::get, Blocks.BIRCH_PLANKS),
                new VerticalSlabRecipeData("jungle", ModBlocks.JUNGLE_VERTICAL_SLAB::get, Blocks.JUNGLE_PLANKS),
                new VerticalSlabRecipeData("acacia", ModBlocks.ACACIA_VERTICAL_SLAB::get, Blocks.ACACIA_PLANKS),
                new VerticalSlabRecipeData("dark_oak", ModBlocks.DARK_OAK_VERTICAL_SLAB::get, Blocks.DARK_OAK_PLANKS),
                new VerticalSlabRecipeData("mangrove", ModBlocks.MANGROVE_VERTICAL_SLAB::get, Blocks.MANGROVE_PLANKS),
                new VerticalSlabRecipeData("cherry", ModBlocks.CHERRY_VERTICAL_SLAB::get, Blocks.CHERRY_PLANKS),
                new VerticalSlabRecipeData("bamboo", ModBlocks.BAMBOO_VERTICAL_SLAB::get, Blocks.BAMBOO_PLANKS),
                new VerticalSlabRecipeData("crimson", ModBlocks.CRIMSON_VERTICAL_SLAB::get, Blocks.CRIMSON_PLANKS),
                new VerticalSlabRecipeData("warped", ModBlocks.WARPED_VERTICAL_SLAB::get, Blocks.WARPED_PLANKS),
                new VerticalSlabRecipeData("stone", ModBlocks.STONE_VERTICAL_SLAB::get, Blocks.STONE),
                new VerticalSlabRecipeData("cobblestone", ModBlocks.COBBLESTONE_VERTICAL_SLAB::get, Blocks.COBBLESTONE),
                new VerticalSlabRecipeData("mossy_cobblestone", ModBlocks.MOSSY_COBBLESTONE_VERTICAL_SLAB::get, Blocks.MOSSY_COBBLESTONE),
                new VerticalSlabRecipeData("smooth_stone", ModBlocks.SMOOTH_STONE_VERTICAL_SLAB::get, Blocks.SMOOTH_STONE),
                new VerticalSlabRecipeData("stone_brick", ModBlocks.STONE_BRICK_VERTICAL_SLAB::get, Blocks.STONE_BRICKS),
                new VerticalSlabRecipeData("mossy_stone_brick", ModBlocks.MOSSY_STONE_BRICK_VERTICAL_SLAB::get, Blocks.MOSSY_STONE_BRICKS),
                new VerticalSlabRecipeData("granite", ModBlocks.GRANITE_VERTICAL_SLAB::get, Blocks.GRANITE),
                new VerticalSlabRecipeData("polished_granite", ModBlocks.POLISHED_GRANITE_VERTICAL_SLAB::get, Blocks.POLISHED_GRANITE),
                new VerticalSlabRecipeData("diorite", ModBlocks.DIORITE_VERTICAL_SLAB::get, Blocks.DIORITE),
                new VerticalSlabRecipeData("polished_diorite", ModBlocks.POLISHED_DIORITE_VERTICAL_SLAB::get, Blocks.POLISHED_DIORITE),
                new VerticalSlabRecipeData("andesite", ModBlocks.ANDESITE_VERTICAL_SLAB::get, Blocks.ANDESITE),
                new VerticalSlabRecipeData("polished_andesite", ModBlocks.POLISHED_ANDESITE_VERTICAL_SLAB::get, Blocks.POLISHED_ANDESITE),
                new VerticalSlabRecipeData("cobbled_deepslate", ModBlocks.COBBLED_DEEPSLATE_VERTICAL_SLAB::get, Blocks.COBBLED_DEEPSLATE),
                new VerticalSlabRecipeData("polished_deepslate", ModBlocks.POLISHED_DEEPSLATE_VERTICAL_SLAB::get, Blocks.POLISHED_DEEPSLATE),
                new VerticalSlabRecipeData("deepslate_brick", ModBlocks.DEEPSLATE_BRICK_VERTICAL_SLAB::get, Blocks.DEEPSLATE_BRICKS),
                new VerticalSlabRecipeData("deepslate_tile", ModBlocks.DEEPSLATE_TILE_VERTICAL_SLAB::get, Blocks.DEEPSLATE_TILES),
                new VerticalSlabRecipeData("tuff", ModBlocks.TUFF_VERTICAL_SLAB::get, Blocks.TUFF),
                new VerticalSlabRecipeData("polished_tuff", ModBlocks.POLISHED_TUFF_VERTICAL_SLAB::get, Blocks.POLISHED_TUFF),
                new VerticalSlabRecipeData("tuff_brick", ModBlocks.TUFF_BRICK_VERTICAL_SLAB::get, Blocks.TUFF_BRICKS),
                new VerticalSlabRecipeData("brick", ModBlocks.BRICK_VERTICAL_SLAB::get, Blocks.BRICKS),
                new VerticalSlabRecipeData("mud_brick", ModBlocks.MUD_BRICK_VERTICAL_SLAB::get, Blocks.MUD_BRICKS),
                new VerticalSlabRecipeData("sandstone", ModBlocks.SANDSTONE_VERTICAL_SLAB::get, Blocks.SANDSTONE),
                new VerticalSlabRecipeData("smooth_sandstone", ModBlocks.SMOOTH_SANDSTONE_VERTICAL_SLAB::get, Blocks.SMOOTH_SANDSTONE),
                new VerticalSlabRecipeData("cut_sandstone", ModBlocks.CUT_SANDSTONE_VERTICAL_SLAB::get, Blocks.CUT_SANDSTONE),
                new VerticalSlabRecipeData("red_sandstone", ModBlocks.RED_SANDSTONE_VERTICAL_SLAB::get, Blocks.RED_SANDSTONE),
                new VerticalSlabRecipeData("smooth_red_sandstone", ModBlocks.SMOOTH_RED_SANDSTONE_VERTICAL_SLAB::get, Blocks.SMOOTH_RED_SANDSTONE),
                new VerticalSlabRecipeData("cut_red_sandstone", ModBlocks.CUT_RED_SANDSTONE_VERTICAL_SLAB::get, Blocks.CUT_RED_SANDSTONE),
                new VerticalSlabRecipeData("prismarine", ModBlocks.PRISMARINE_VERTICAL_SLAB::get, Blocks.PRISMARINE),
                new VerticalSlabRecipeData("prismarine_brick", ModBlocks.PRISMARINE_BRICK_VERTICAL_SLAB::get, Blocks.PRISMARINE_BRICKS),
                new VerticalSlabRecipeData("dark_prismarine", ModBlocks.DARK_PRISMARINE_VERTICAL_SLAB::get, Blocks.DARK_PRISMARINE),
                new VerticalSlabRecipeData("nether_brick", ModBlocks.NETHER_BRICK_VERTICAL_SLAB::get, Blocks.NETHER_BRICKS),
                new VerticalSlabRecipeData("red_nether_brick", ModBlocks.RED_NETHER_BRICK_VERTICAL_SLAB::get, Blocks.RED_NETHER_BRICKS),
                new VerticalSlabRecipeData("blackstone", ModBlocks.BLACKSTONE_VERTICAL_SLAB::get, Blocks.BLACKSTONE),
                new VerticalSlabRecipeData("polished_blackstone", ModBlocks.POLISHED_BLACKSTONE_VERTICAL_SLAB::get, Blocks.POLISHED_BLACKSTONE),
                new VerticalSlabRecipeData("polished_blackstone_brick", ModBlocks.POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB::get, Blocks.POLISHED_BLACKSTONE_BRICKS),
                new VerticalSlabRecipeData("end_stone_brick", ModBlocks.END_STONE_BRICK_VERTICAL_SLAB::get, Blocks.END_STONE_BRICKS),
                new VerticalSlabRecipeData("purpur", ModBlocks.PURPUR_VERTICAL_SLAB::get, Blocks.PURPUR_BLOCK),
                new VerticalSlabRecipeData("quartz", ModBlocks.QUARTZ_VERTICAL_SLAB::get, Blocks.QUARTZ_BLOCK),
                new VerticalSlabRecipeData("smooth_quartz", ModBlocks.SMOOTH_QUARTZ_VERTICAL_SLAB::get, Blocks.SMOOTH_QUARTZ)
        );

        for (VerticalSlabRecipeData data : verticalSlabs) {
            shaped(RecipeCategory.BUILDING_BLOCKS, data.verticalSlab().get(), 6)
                    .pattern("E  ")
                    .pattern("E  ")
                    .pattern("E  ")
                    .define('E', data.vanillaSlab())
                    .unlockedBy("has_" + data.name() + "_slab", has(data.vanillaSlab()))
                    .save(output, "builders_zenith:vertical_slabs/" + data.name() + "_vertical_slab");
        }
    }

    private void generateColorMixerRecipes(RecipeOutput recipeOutput) {
        ColorMixerRecipeBuilder.generateAllRecipes(recipeOutput);
    }

    private void generateCarpenterRecipes(RecipeOutput recipeOutput) {
        // ========== OAK ==========
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, 4, 16, "oak_stairs");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, ModBlocks.OAK_VERTICAL_SLAB.get(), 2, 16, "oak_vertical_slab");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_SLAB, 2, 16, "oak_slab");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_FENCE, 4, 12, "oak_fence");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_FENCE_GATE, 2, 4, "oak_fence_gate");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_DOOR, 6, 4, "oak_door");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_TRAPDOOR, 3, 8, "oak_trapdoor");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_PRESSURE_PLATE, 2, 8, "oak_pressure_plate");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_BUTTON, 1, 8, "oak_button");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_SIGN, 3, 6, "oak_sign");
        carpenter(recipeOutput, Blocks.OAK_PLANKS, Blocks.OAK_HANGING_SIGN, 6, 4, "oak_hanging_sign");
        carpenter(recipeOutput, Blocks.OAK_LOG, Blocks.OAK_PLANKS, 1, 6, "oak_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_PLANKS, 1, 6, "stripped_oak_log_to_planks");
        carpenter(recipeOutput, Blocks.OAK_WOOD, Blocks.OAK_PLANKS, 1, 6, "oak_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_PLANKS, 1, 6, "stripped_oak_wood_to_planks");
        carpenter(recipeOutput, Blocks.OAK_LOG, Blocks.OAK_WOOD, 1, 2, "oak_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_OAK_WOOD, 1, 2, "stripped_oak_log_to_stripped_wood");

        // ========== SPRUCE ==========
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, 4, 16, "spruce_stairs");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, ModBlocks.SPRUCE_VERTICAL_SLAB.get(), 2, 16, "spruce_vertical_slab");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, 2, 16, "spruce_slab");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE, 4, 12, "spruce_fence");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE_GATE, 2, 4, "spruce_fence_gate");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_DOOR, 6, 4, "spruce_door");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_TRAPDOOR, 3, 8, "spruce_trapdoor");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_PRESSURE_PLATE, 2, 8, "spruce_pressure_plate");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_BUTTON, 1, 8, "spruce_button");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SIGN, 3, 6, "spruce_sign");
        carpenter(recipeOutput, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_HANGING_SIGN, 6, 4, "spruce_hanging_sign");
        carpenter(recipeOutput, Blocks.SPRUCE_LOG, Blocks.SPRUCE_PLANKS, 1, 6, "spruce_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_PLANKS, 1, 6, "stripped_spruce_log_to_planks");
        carpenter(recipeOutput, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_PLANKS, 1, 6, "spruce_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_PLANKS, 1, 6, "stripped_spruce_wood_to_planks");
        carpenter(recipeOutput, Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, 1, 2, "spruce_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_WOOD, 1, 2, "stripped_spruce_log_to_stripped_wood");

        // ========== BIRCH ==========
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, 4, 16, "birch_stairs");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, ModBlocks.BIRCH_VERTICAL_SLAB.get(), 2, 16, "birch_vertical_slab");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, 2, 16, "birch_slab");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_FENCE, 4, 12, "birch_fence");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_FENCE_GATE, 2, 4, "birch_fence_gate");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_DOOR, 6, 4, "birch_door");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_TRAPDOOR, 3, 8, "birch_trapdoor");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_PRESSURE_PLATE, 2, 8, "birch_pressure_plate");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_BUTTON, 1, 8, "birch_button");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_SIGN, 3, 6, "birch_sign");
        carpenter(recipeOutput, Blocks.BIRCH_PLANKS, Blocks.BIRCH_HANGING_SIGN, 6, 4, "birch_hanging_sign");
        carpenter(recipeOutput, Blocks.BIRCH_LOG, Blocks.BIRCH_PLANKS, 1, 6, "birch_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_PLANKS, 1, 6, "stripped_birch_log_to_planks");
        carpenter(recipeOutput, Blocks.BIRCH_WOOD, Blocks.BIRCH_PLANKS, 1, 6, "birch_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_PLANKS, 1, 6, "stripped_birch_wood_to_planks");
        carpenter(recipeOutput, Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, 1, 2, "birch_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_BIRCH_WOOD, 1, 2, "stripped_birch_log_to_stripped_wood");

        // ========== JUNGLE ==========
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, 4, 16, "jungle_stairs");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, ModBlocks.JUNGLE_VERTICAL_SLAB.get(), 2, 16, "jungle_vertical_slab");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, 2, 16, "jungle_slab");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_FENCE, 4, 12, "jungle_fence");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_FENCE_GATE, 2, 4, "jungle_fence_gate");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_DOOR, 6, 4, "jungle_door");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_TRAPDOOR, 3, 8, "jungle_trapdoor");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_PRESSURE_PLATE, 2, 8, "jungle_pressure_plate");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_BUTTON, 1, 8, "jungle_button");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SIGN, 3, 6, "jungle_sign");
        carpenter(recipeOutput, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_HANGING_SIGN, 6, 4, "jungle_hanging_sign");
        carpenter(recipeOutput, Blocks.JUNGLE_LOG, Blocks.JUNGLE_PLANKS, 1, 6, "jungle_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_PLANKS, 1, 6, "stripped_jungle_log_to_planks");
        carpenter(recipeOutput, Blocks.JUNGLE_WOOD, Blocks.JUNGLE_PLANKS, 1, 6, "jungle_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_PLANKS, 1, 6, "stripped_jungle_wood_to_planks");
        carpenter(recipeOutput, Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, 1, 2, "jungle_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD, 1, 2, "stripped_jungle_log_to_stripped_wood");

        // ========== ACACIA ==========
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, 4, 16, "acacia_stairs");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, ModBlocks.ACACIA_VERTICAL_SLAB.get(), 2, 16, "acacia_vertical_slab");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, 2, 16, "acacia_slab");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_FENCE, 4, 12, "acacia_fence");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_FENCE_GATE, 2, 4, "acacia_fence_gate");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_DOOR, 6, 4, "acacia_door");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_TRAPDOOR, 3, 8, "acacia_trapdoor");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_PRESSURE_PLATE, 2, 8, "acacia_pressure_plate");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_BUTTON, 1, 8, "acacia_button");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_SIGN, 3, 6, "acacia_sign");
        carpenter(recipeOutput, Blocks.ACACIA_PLANKS, Blocks.ACACIA_HANGING_SIGN, 6, 4, "acacia_hanging_sign");
        carpenter(recipeOutput, Blocks.ACACIA_LOG, Blocks.ACACIA_PLANKS, 1, 6, "acacia_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_PLANKS, 1, 6, "stripped_acacia_log_to_planks");
        carpenter(recipeOutput, Blocks.ACACIA_WOOD, Blocks.ACACIA_PLANKS, 1, 6, "acacia_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_PLANKS, 1, 6, "stripped_acacia_wood_to_planks");
        carpenter(recipeOutput, Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, 1, 2, "acacia_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_ACACIA_WOOD, 1, 2, "stripped_acacia_log_to_stripped_wood");

        // ========== DARK OAK ==========
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, 4, 16, "dark_oak_stairs");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, ModBlocks.DARK_OAK_VERTICAL_SLAB.get(), 2, 16, "dark_oak_vertical_slab");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, 2, 16, "dark_oak_slab");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE, 4, 12, "dark_oak_fence");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE_GATE, 2, 4, "dark_oak_fence_gate");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_DOOR, 6, 4, "dark_oak_door");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_TRAPDOOR, 3, 8, "dark_oak_trapdoor");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_PRESSURE_PLATE, 2, 8, "dark_oak_pressure_plate");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_BUTTON, 1, 8, "dark_oak_button");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SIGN, 3, 6, "dark_oak_sign");
        carpenter(recipeOutput, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_HANGING_SIGN, 6, 4, "dark_oak_hanging_sign");
        carpenter(recipeOutput, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS, 1, 6, "dark_oak_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS, 1, 6, "stripped_dark_oak_log_to_planks");
        carpenter(recipeOutput, Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_PLANKS, 1, 6, "dark_oak_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_PLANKS, 1, 6, "stripped_dark_oak_wood_to_planks");
        carpenter(recipeOutput, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, 1, 2, "dark_oak_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD, 1, 2, "stripped_dark_oak_log_to_stripped_wood");

        // ========== MANGROVE ==========
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_STAIRS, 4, 16, "mangrove_stairs");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, ModBlocks.MANGROVE_VERTICAL_SLAB.get(), 2, 16, "mangrove_vertical_slab");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SLAB, 2, 16, "mangrove_slab");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE, 4, 12, "mangrove_fence");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE_GATE, 2, 4, "mangrove_fence_gate");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_DOOR, 6, 4, "mangrove_door");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_TRAPDOOR, 3, 8, "mangrove_trapdoor");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_PRESSURE_PLATE, 2, 8, "mangrove_pressure_plate");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_BUTTON, 1, 8, "mangrove_button");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SIGN, 3, 6, "mangrove_sign");
        carpenter(recipeOutput, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_HANGING_SIGN, 6, 4, "mangrove_hanging_sign");
        carpenter(recipeOutput, Blocks.MANGROVE_LOG, Blocks.MANGROVE_PLANKS, 1, 6, "mangrove_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_PLANKS, 1, 6, "stripped_mangrove_log_to_planks");
        carpenter(recipeOutput, Blocks.MANGROVE_WOOD, Blocks.MANGROVE_PLANKS, 1, 6, "mangrove_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.MANGROVE_PLANKS, 1, 6, "stripped_mangrove_wood_to_planks");
        carpenter(recipeOutput, Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, 1, 2, "mangrove_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD, 1, 2, "stripped_mangrove_log_to_stripped_wood");

        // ========== CHERRY ==========
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_STAIRS, 4, 16, "cherry_stairs");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, ModBlocks.CHERRY_VERTICAL_SLAB.get(), 2, 16, "cherry_vertical_slab");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_SLAB, 2, 16, "cherry_slab");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_FENCE, 4, 12, "cherry_fence");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_FENCE_GATE, 2, 4, "cherry_fence_gate");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_DOOR, 6, 4, "cherry_door");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_TRAPDOOR, 3, 8, "cherry_trapdoor");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_PRESSURE_PLATE, 2, 8, "cherry_pressure_plate");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_BUTTON, 1, 8, "cherry_button");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_SIGN, 3, 6, "cherry_sign");
        carpenter(recipeOutput, Blocks.CHERRY_PLANKS, Blocks.CHERRY_HANGING_SIGN, 6, 4, "cherry_hanging_sign");
        carpenter(recipeOutput, Blocks.CHERRY_LOG, Blocks.CHERRY_PLANKS, 1, 6, "cherry_log_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_CHERRY_LOG, Blocks.CHERRY_PLANKS, 1, 6, "stripped_cherry_log_to_planks");
        carpenter(recipeOutput, Blocks.CHERRY_WOOD, Blocks.CHERRY_PLANKS, 1, 6, "cherry_wood_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_CHERRY_WOOD, Blocks.CHERRY_PLANKS, 1, 6, "stripped_cherry_wood_to_planks");
        carpenter(recipeOutput, Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, 1, 2, "cherry_log_to_wood");
        carpenter(recipeOutput, Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_CHERRY_WOOD, 1, 2, "stripped_cherry_log_to_stripped_wood");

        // ========== BAMBOO ==========
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_STAIRS, 4, 16, "bamboo_stairs");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, ModBlocks.BAMBOO_VERTICAL_SLAB.get(), 2, 16, "bamboo_vertical_slab");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_SLAB, 2, 16, "bamboo_slab");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_FENCE, 4, 12, "bamboo_fence");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_FENCE_GATE, 2, 4, "bamboo_fence_gate");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_DOOR, 6, 4, "bamboo_door");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_TRAPDOOR, 3, 8, "bamboo_trapdoor");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_PRESSURE_PLATE, 2, 8, "bamboo_pressure_plate");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BUTTON, 1, 8, "bamboo_button");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_SIGN, 3, 6, "bamboo_sign");
        carpenter(recipeOutput, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_HANGING_SIGN, 6, 4, "bamboo_hanging_sign");
        carpenter(recipeOutput, Blocks.BAMBOO_BLOCK, Blocks.BAMBOO_PLANKS, 1, 6, "bamboo_block_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_BAMBOO_BLOCK, Blocks.BAMBOO_PLANKS, 1, 6, "stripped_bamboo_block_to_planks");

        // ========== CRIMSON ==========
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STAIRS, 4, 16, "crimson_stairs");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, ModBlocks.CRIMSON_VERTICAL_SLAB.get(), 2, 16, "crimson_vertical_slab");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB, 2, 16, "crimson_slab");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE, 4, 12, "crimson_fence");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE_GATE, 2, 4, "crimson_fence_gate");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_DOOR, 6, 4, "crimson_door");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_TRAPDOOR, 3, 8, "crimson_trapdoor");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_PRESSURE_PLATE, 2, 8, "crimson_pressure_plate");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_BUTTON, 1, 8, "crimson_button");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SIGN, 3, 6, "crimson_sign");
        carpenter(recipeOutput, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_HANGING_SIGN, 6, 4, "crimson_hanging_sign");
        carpenter(recipeOutput, Blocks.CRIMSON_STEM, Blocks.CRIMSON_PLANKS, 1, 6, "crimson_stem_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_PLANKS, 1, 6, "stripped_crimson_stem_to_planks");
        carpenter(recipeOutput, Blocks.CRIMSON_HYPHAE, Blocks.CRIMSON_PLANKS, 1, 6, "crimson_hyphae_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_PLANKS, 1, 6, "stripped_crimson_hyphae_to_planks");
        carpenter(recipeOutput, Blocks.CRIMSON_STEM, Blocks.CRIMSON_HYPHAE, 1, 2, "crimson_stem_to_hyphae");
        carpenter(recipeOutput, Blocks.STRIPPED_CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_HYPHAE, 1, 2, "stripped_crimson_stem_to_stripped_hyphae");

        // ========== WARPED ==========
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_STAIRS, 4, 16, "warped_stairs");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, ModBlocks.WARPED_VERTICAL_SLAB.get(), 2, 16, "warped_vertical_slab");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB, 2, 16, "warped_slab");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE, 4, 12, "warped_fence");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE_GATE, 2, 4, "warped_fence_gate");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_DOOR, 6, 4, "warped_door");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_TRAPDOOR, 3, 8, "warped_trapdoor");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_PRESSURE_PLATE, 2, 8, "warped_pressure_plate");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_BUTTON, 1, 8, "warped_button");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_SIGN, 3, 6, "warped_sign");
        carpenter(recipeOutput, Blocks.WARPED_PLANKS, Blocks.WARPED_HANGING_SIGN, 6, 4, "warped_hanging_sign");
        carpenter(recipeOutput, Blocks.WARPED_STEM, Blocks.WARPED_PLANKS, 1, 6, "warped_stem_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_PLANKS, 1, 6, "stripped_warped_stem_to_planks");
        carpenter(recipeOutput, Blocks.WARPED_HYPHAE, Blocks.WARPED_PLANKS, 1, 6, "warped_hyphae_to_planks");
        carpenter(recipeOutput, Blocks.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_PLANKS, 1, 6, "stripped_warped_hyphae_to_planks");
        carpenter(recipeOutput, Blocks.WARPED_STEM, Blocks.WARPED_HYPHAE, 1, 2, "warped_stem_to_hyphae");
        carpenter(recipeOutput, Blocks.STRIPPED_WARPED_STEM, Blocks.STRIPPED_WARPED_HYPHAE, 1, 2, "stripped_warped_stem_to_stripped_hyphae");
    }

    private void carpenter(RecipeOutput recipeOutput, ItemLike input, ItemLike result,
                           int ingredientCount, int resultCount, String recipeName) {
        Ingredient ingredient = Ingredient.of(input);
        ItemStack resultStack = new ItemStack(result.asItem(), resultCount);
        CarpenterRecipes recipe = new CarpenterRecipes("building", ingredient, resultStack, ingredientCount);
        Identifier id = Identifier.fromNamespaceAndPath(BuildersZenith.MOD_ID, "carpenter/" + recipeName);
        recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), recipe, null);
    }
}