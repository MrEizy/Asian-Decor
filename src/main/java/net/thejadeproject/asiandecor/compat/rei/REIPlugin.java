// REIPlugin.java
package net.thejadeproject.asiandecor.compat.rei;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.recipe.CarpenterRecipes;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;
import net.thejadeproject.asiandecor.recipe.ModRecipes;

import java.util.Collections;
import java.util.List;

@REIPluginClient
public class REIPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<CarpenterDisplay> CARPENTER =
            CategoryIdentifier.of(AsianDecor.MOD_ID, "carpenter");

    public static final CategoryIdentifier<ColorMixerDisplay> COLOR_MIXER =
            CategoryIdentifier.of(AsianDecor.MOD_ID, "color_mixer");

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

        public ColorMixerDisplay(ColorMixerRecipe recipe) {
            super(
                    // Convert ingredients to EntryIngredient list
                    recipe.getIngredients().stream()
                            .map(EntryIngredients::ofIngredient)
                            .toList(),
                    Collections.singletonList(EntryIngredients.of(recipe.getResultItem(null)))
            );
            this.isVanillaRecipe = recipe.isVanillaRecipe();
        }

        public boolean isVanillaRecipe() {
            return isVanillaRecipe;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return COLOR_MIXER;
        }
    }
}