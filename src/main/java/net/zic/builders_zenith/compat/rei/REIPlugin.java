// REIPlugin.java
package net.zic.builders_zenith.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.recipe.CarpenterRecipes;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;
import net.zic.builders_zenith.recipe.ModRecipes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@REIPluginClient
public class REIPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<CarpenterDisplay> CARPENTER =
            CategoryIdentifier.of(BuildersZenith.MOD_ID, "carpenter");

    public static final CategoryIdentifier<ColorMixerDisplay> COLOR_MIXER =
            CategoryIdentifier.of(BuildersZenith.MOD_ID, "color_mixer");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CarpenterCategory());
        registry.addWorkstations(CARPENTER, EntryStacks.of(ModBlocks.CARPENTER.get()));

        registry.add(new ColorMixerCategory());
        registry.addWorkstations(COLOR_MIXER, EntryStacks.of(ModBlocks.COLOR_MIXER.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // Carpenter Recipes
        List<RecipeHolder<CarpenterRecipes>> carpenterRecipes = recipeManager.getAllRecipesFor(
                ModRecipes.CARPENTER_TYPE.get()
        );
        for (RecipeHolder<CarpenterRecipes> holder : carpenterRecipes) {
            registry.add(new CarpenterDisplay(holder.value()));
        }

        // Color Mixer - Only 2 recipes!
        List<RecipeHolder<ColorMixerRecipe>> colorMixerRecipes = recipeManager.getAllRecipesFor(
                ModRecipes.COLOR_MIXER_TYPE.get()
        );
        for (RecipeHolder<ColorMixerRecipe> holder : colorMixerRecipes) {
            registry.add(new ColorMixerDisplay(holder.value()));
        }
    }

    public static class CarpenterDisplay extends BasicDisplay {
        private final int ingredientCount;

        public CarpenterDisplay(CarpenterRecipes recipe) {
            super(
                    Collections.singletonList(EntryIngredients.ofIngredient(recipe.getIngredient())),
                    Collections.singletonList(EntryIngredients.of(recipe.getResult()))
            );
            this.ingredientCount = recipe.getIngredientCount();
        }

        public int getIngredientCount() {
            return ingredientCount;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return CARPENTER;
        }
    }

    public static class ColorMixerDisplay extends BasicDisplay {
        private final boolean isVanillaRecipe;
        private final String group;

        public ColorMixerDisplay(ColorMixerRecipe recipe) {
            super(
                    recipe.getIngredients().stream()
                            .map(EntryIngredients::ofIngredient)
                            .toList(),
                    getOutputs(recipe)
            );
            this.isVanillaRecipe = recipe.isVanillaRecipe();
            this.group = recipe.getGroup();
        }

        private static List<EntryIngredient> getOutputs(ColorMixerRecipe recipe) {
            String group = recipe.getGroup();
            boolean isSlab = group.contains("slab");
            boolean isStairs = group.contains("stair");
            boolean isWall = group.contains("wall");

            java.util.function.Function<DyedBrickType, ItemStack> mapper;

            if (isSlab) {
                mapper = type -> new ItemStack(ModBlocks.DYED_BRICK_SLABS.get(type).get());
            } else if (isStairs) {
                mapper = type -> new ItemStack(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
            } else if (isWall) {
                mapper = type -> new ItemStack(ModBlocks.DYED_BRICK_WALLS.get(type).get());
            } else {
                mapper = type -> new ItemStack(ModBlocks.DYED_BRICKS.get(type).get());
            }

            List<ItemStack> outputs = Arrays.stream(DyedBrickType.values())
                    .map(mapper)
                    .toList();

            return Collections.singletonList(EntryIngredients.ofItemStacks(outputs));
        }

        public boolean isVanillaRecipe() {
            return isVanillaRecipe;
        }

        public String getGroup() {
            return group;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return COLOR_MIXER;
        }
    }
}