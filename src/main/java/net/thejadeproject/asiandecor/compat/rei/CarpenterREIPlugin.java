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

import java.util.Collections;
import java.util.List;

// This annotation tells REI to load this plugin
@REIPluginClient
public class CarpenterREIPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<CarpenterDisplay> CARPENTER =
            CategoryIdentifier.of(AsianDecor.MOD_ID, "carpenter");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CarpenterCategory());
        registry.addWorkstations(CARPENTER, EntryStacks.of(ModBlocks.CARPENTER.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<RecipeHolder<CarpenterRecipes>> recipes = recipeManager.getAllRecipesFor(
                net.thejadeproject.asiandecor.recipe.ModRecipes.CARPENTER_TYPE.get()
        );

        for (RecipeHolder<CarpenterRecipes> holder : recipes) {
            registry.add(new CarpenterDisplay(holder.value()));
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
}