package net.thejadeproject.asiandecor.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.recipe.CarpenterRecipes;

import java.util.List;

@JeiPlugin
public class CarpenterJEIPlugin implements IModPlugin {

    public static final RecipeType<CarpenterRecipes> RECIPE_TYPE =
            RecipeType.create(AsianDecor.MOD_ID, "carpenter", CarpenterRecipes.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "carpenter");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CarpenterRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CarpenterRecipes> recipes = recipeManager.getAllRecipesFor(
                net.thejadeproject.asiandecor.recipe.ModRecipes.CARPENTER_TYPE.get()
        ).stream().map(RecipeHolder::value).toList();

        registration.addRecipes(RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CARPENTER.get()), RECIPE_TYPE);
    }
}