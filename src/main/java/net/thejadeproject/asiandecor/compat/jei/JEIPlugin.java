// JEIPlugin.java
package net.thejadeproject.asiandecor.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;
import net.thejadeproject.asiandecor.recipe.CarpenterRecipes;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;
import net.thejadeproject.asiandecor.recipe.ModRecipes;
import net.thejadeproject.asiandecor.screen.custom.CarpenterScreen;
import net.thejadeproject.asiandecor.screen.custom.ColorMixerScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.thejadeproject.asiandecor.recipe.ColorMixerRecipe.getDyeItem;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final RecipeType<CarpenterRecipes> CARPENTER_RECIPE_TYPE =
            RecipeType.create(AsianDecor.MOD_ID, "carpenter", CarpenterRecipes.class);

    public static final RecipeType<ColorMixerRecipe> COLOR_MIXER_RECIPE_TYPE =
            RecipeType.create(AsianDecor.MOD_ID, "color_mixer", ColorMixerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CarpenterRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ColorMixerRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // Carpenter Recipes (unchanged)
        List<CarpenterRecipes> carpenterRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.CARPENTER_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(CARPENTER_RECIPE_TYPE, carpenterRecipes);

        // Color Mixer - Only 2 recipes, but show all 256 combinations via JEI's recipe filling
        List<ColorMixerRecipe> colorMixerRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.COLOR_MIXER_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();

        // Register the 2 base recipes
        registration.addRecipes(COLOR_MIXER_RECIPE_TYPE, colorMixerRecipes);

        // JEI will automatically expand tag ingredients to show all combinations!
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CARPENTER.get()),
                CARPENTER_RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.COLOR_MIXER.get()),
                COLOR_MIXER_RECIPE_TYPE
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                CarpenterScreen.class,
                79, 34, 24, 17,
                CARPENTER_RECIPE_TYPE
        );
        registration.addRecipeClickArea(
                ColorMixerScreen.class,
                101, 32, 24, 17,
                COLOR_MIXER_RECIPE_TYPE
        );
    }
}