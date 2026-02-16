package net.thejadeproject.asiandecor.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.recipe.CarpenterRecipes;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        generateCarpenterRecipes(recipeOutput);
    }

    private void generateCarpenterRecipes(RecipeOutput output) {
        // ========== OAK ==========
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, 4, 16, "oak_stairs");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_SLAB, 2, 16, "oak_slab");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_FENCE, 4, 12, "oak_fence");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_FENCE_GATE, 2, 4, "oak_fence_gate");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_DOOR, 6, 4, "oak_door");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_TRAPDOOR, 3, 8, "oak_trapdoor");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_PRESSURE_PLATE, 2, 8, "oak_pressure_plate");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_BUTTON, 1, 8, "oak_button");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_SIGN, 3, 6, "oak_sign");
        carpenter(output, Blocks.OAK_PLANKS, Blocks.OAK_HANGING_SIGN, 6, 4, "oak_hanging_sign");

        // ========== SPRUCE ==========
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, 4, 16, "spruce_stairs");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, 2, 16, "spruce_slab");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE, 4, 12, "spruce_fence");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE_GATE, 2, 4, "spruce_fence_gate");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_DOOR, 6, 4, "spruce_door");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_TRAPDOOR, 3, 8, "spruce_trapdoor");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_PRESSURE_PLATE, 2, 8, "spruce_pressure_plate");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_BUTTON, 1, 8, "spruce_button");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SIGN, 3, 6, "spruce_sign");
        carpenter(output, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_HANGING_SIGN, 6, 4, "spruce_hanging_sign");

        // ========== BIRCH ==========
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, 4, 16, "birch_stairs");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, 2, 16, "birch_slab");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_FENCE, 4, 12, "birch_fence");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_FENCE_GATE, 2, 4, "birch_fence_gate");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_DOOR, 6, 4, "birch_door");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_TRAPDOOR, 3, 8, "birch_trapdoor");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_PRESSURE_PLATE, 2, 8, "birch_pressure_plate");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_BUTTON, 1, 8, "birch_button");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_SIGN, 3, 6, "birch_sign");
        carpenter(output, Blocks.BIRCH_PLANKS, Blocks.BIRCH_HANGING_SIGN, 6, 4, "birch_hanging_sign");

        // ========== JUNGLE ==========
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, 4, 16, "jungle_stairs");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, 2, 16, "jungle_slab");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_FENCE, 4, 12, "jungle_fence");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_FENCE_GATE, 2, 4, "jungle_fence_gate");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_DOOR, 6, 4, "jungle_door");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_TRAPDOOR, 3, 8, "jungle_trapdoor");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_PRESSURE_PLATE, 2, 8, "jungle_pressure_plate");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_BUTTON, 1, 8, "jungle_button");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SIGN, 3, 6, "jungle_sign");
        carpenter(output, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_HANGING_SIGN, 6, 4, "jungle_hanging_sign");

        // ========== ACACIA ==========
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, 4, 16, "acacia_stairs");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, 2, 16, "acacia_slab");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_FENCE, 4, 12, "acacia_fence");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_FENCE_GATE, 2, 4, "acacia_fence_gate");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_DOOR, 6, 4, "acacia_door");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_TRAPDOOR, 3, 8, "acacia_trapdoor");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_PRESSURE_PLATE, 2, 8, "acacia_pressure_plate");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_BUTTON, 1, 8, "acacia_button");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_SIGN, 3, 6, "acacia_sign");
        carpenter(output, Blocks.ACACIA_PLANKS, Blocks.ACACIA_HANGING_SIGN, 6, 4, "acacia_hanging_sign");

        // ========== DARK OAK ==========
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, 4, 16, "dark_oak_stairs");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, 2, 16, "dark_oak_slab");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE, 4, 12, "dark_oak_fence");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE_GATE, 2, 4, "dark_oak_fence_gate");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_DOOR, 6, 4, "dark_oak_door");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_TRAPDOOR, 3, 8, "dark_oak_trapdoor");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_PRESSURE_PLATE, 2, 8, "dark_oak_pressure_plate");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_BUTTON, 1, 8, "dark_oak_button");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SIGN, 3, 6, "dark_oak_sign");
        carpenter(output, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_HANGING_SIGN, 6, 4, "dark_oak_hanging_sign");

        // ========== MANGROVE ==========
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_STAIRS, 4, 16, "mangrove_stairs");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SLAB, 2, 16, "mangrove_slab");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE, 4, 12, "mangrove_fence");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE_GATE, 2, 4, "mangrove_fence_gate");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_DOOR, 6, 4, "mangrove_door");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_TRAPDOOR, 3, 8, "mangrove_trapdoor");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_PRESSURE_PLATE, 2, 8, "mangrove_pressure_plate");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_BUTTON, 1, 8, "mangrove_button");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SIGN, 3, 6, "mangrove_sign");
        carpenter(output, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_HANGING_SIGN, 6, 4, "mangrove_hanging_sign");

        // ========== CHERRY ==========
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_STAIRS, 4, 16, "cherry_stairs");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_SLAB, 2, 16, "cherry_slab");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_FENCE, 4, 12, "cherry_fence");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_FENCE_GATE, 2, 4, "cherry_fence_gate");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_DOOR, 6, 4, "cherry_door");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_TRAPDOOR, 3, 8, "cherry_trapdoor");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_PRESSURE_PLATE, 2, 8, "cherry_pressure_plate");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_BUTTON, 1, 8, "cherry_button");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_SIGN, 3, 6, "cherry_sign");
        carpenter(output, Blocks.CHERRY_PLANKS, Blocks.CHERRY_HANGING_SIGN, 6, 4, "cherry_hanging_sign");

        // ========== BAMBOO ==========
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_STAIRS, 4, 16, "bamboo_stairs");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_SLAB, 2, 16, "bamboo_slab");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_FENCE, 4, 12, "bamboo_fence");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_FENCE_GATE, 2, 4, "bamboo_fence_gate");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_DOOR, 6, 4, "bamboo_door");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_TRAPDOOR, 3, 8, "bamboo_trapdoor");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_PRESSURE_PLATE, 2, 8, "bamboo_pressure_plate");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BUTTON, 1, 8, "bamboo_button");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_SIGN, 3, 6, "bamboo_sign");
        carpenter(output, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_HANGING_SIGN, 6, 4, "bamboo_hanging_sign");

        // ========== CRIMSON (Nether) ==========
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STAIRS, 4, 16, "crimson_stairs");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB, 2, 16, "crimson_slab");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE, 4, 12, "crimson_fence");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE_GATE, 2, 4, "crimson_fence_gate");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_DOOR, 6, 4, "crimson_door");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_TRAPDOOR, 3, 8, "crimson_trapdoor");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_PRESSURE_PLATE, 2, 8, "crimson_pressure_plate");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_BUTTON, 1, 8, "crimson_button");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SIGN, 3, 6, "crimson_sign");
        carpenter(output, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_HANGING_SIGN, 6, 4, "crimson_hanging_sign");

        // ========== WARPED (Nether) ==========
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_STAIRS, 4, 16, "warped_stairs");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB, 2, 16, "warped_slab");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE, 4, 12, "warped_fence");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE_GATE, 2, 4, "warped_fence_gate");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_DOOR, 6, 4, "warped_door");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_TRAPDOOR, 3, 8, "warped_trapdoor");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_PRESSURE_PLATE, 2, 8, "warped_pressure_plate");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_BUTTON, 1, 8, "warped_button");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_SIGN, 3, 6, "warped_sign");
        carpenter(output, Blocks.WARPED_PLANKS, Blocks.WARPED_HANGING_SIGN, 6, 4, "warped_hanging_sign");

        // ========== PALE OAK (1.21.4+) ==========
        // Todo Uncomment when we update to later versions
        /*
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_STAIRS, 4, 16, "pale_oak_stairs");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_SLAB, 2, 16, "pale_oak_slab");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_FENCE, 4, 12, "pale_oak_fence");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_FENCE_GATE, 2, 4, "pale_oak_fence_gate");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_DOOR, 6, 4, "pale_oak_door");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_TRAPDOOR, 3, 8, "pale_oak_trapdoor");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_PRESSURE_PLATE, 2, 8, "pale_oak_pressure_plate");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_BUTTON, 1, 8, "pale_oak_button");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_SIGN, 3, 6, "pale_oak_sign");
        carpenter(output, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_HANGING_SIGN, 6, 4, "pale_oak_hanging_sign");
        */
    }

    /**
     * Creates a carpenter recipe
     */
    private void carpenter(RecipeOutput output, ItemLike input, ItemLike result,
                           int ingredientCount, int resultCount, String recipeName) {

        Ingredient ingredient = Ingredient.of(input);
        ItemStack resultStack = new ItemStack(result.asItem(), resultCount);

        CarpenterRecipes recipe = new CarpenterRecipes("building", ingredient, resultStack, ingredientCount);

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "carpenter/" + recipeName);
        output.accept(id, recipe, null);
    }
}