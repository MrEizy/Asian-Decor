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

    // Recipe Types
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

        // Register all recipe categories
        registration.addRecipeCategories(new CarpenterRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ColorMixerRecipeCategory(guiHelper));
        // Add more categories here as you create them
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // Carpenter Recipes
        List<CarpenterRecipes> carpenterRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.CARPENTER_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(CARPENTER_RECIPE_TYPE, carpenterRecipes);

        // Get ALL color mixer recipes
        List<ColorMixerRecipe> allRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.COLOR_MIXER_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();

        // Filter to only vanilla brick recipes (256 total)
        List<ColorMixerRecipe> vanillaRecipes = allRecipes.stream()
                .filter(recipe -> {
                    // Keep only recipes that use vanilla bricks as input
                    if (recipe.getIngredients().isEmpty()) return false;
                    return recipe.getIngredients().get(0).test(new ItemStack(Items.BRICKS));
                })
                .toList();

        ColorMixerRecipe recolorRecipe = createDummyRecolorRecipe();
        List<ColorMixerRecipe> jeiRecipes = new ArrayList<>(vanillaRecipes);
        jeiRecipes.add(recolorRecipe);

        registration.addRecipes(COLOR_MIXER_RECIPE_TYPE, jeiRecipes);
    }

    private ColorMixerRecipe createDummyRecolorRecipe() {
        // Create a dummy recipe that shows the recolor pattern
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModBlocks.DYED_BRICKS.values().stream()
                .map(b -> new ItemStack(b.get())).toArray(ItemStack[]::new)));
        ingredients.add(Ingredient.of(Arrays.stream(DyeColor.values())
                .map(c -> new ItemStack(getDyeItem(c))).toArray(ItemStack[]::new)));
        ingredients.add(Ingredient.of(Arrays.stream(DyeColor.values())
                .map(c -> new ItemStack(getDyeItem(c))).toArray(ItemStack[]::new)));

        return new ColorMixerRecipe("dyed_bricks_recolor_jei", ingredients,
                new ItemStack(ModBlocks.DYED_BRICKS.get(DyedBrickType.WHITE_WHITE).get()), 100);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Carpenter catalyst
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CARPENTER.get()),
                CARPENTER_RECIPE_TYPE
        );

        // Color Mixer catalyst
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.COLOR_MIXER.get()),
                COLOR_MIXER_RECIPE_TYPE
        );

        // Add more catalysts here
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // Carpenter GUI click area
        registration.addRecipeClickArea(
                CarpenterScreen.class,
                79, 34, 24, 17,  // Arrow position in GUI
                CARPENTER_RECIPE_TYPE
        );

        // Color Mixer GUI click area
        registration.addRecipeClickArea(
                ColorMixerScreen.class,
                101, 32, 24, 17,  // Updated arrow position (moved 22 right, 2 up)
                COLOR_MIXER_RECIPE_TYPE
        );

        // Add more GUI handlers here
    }
}