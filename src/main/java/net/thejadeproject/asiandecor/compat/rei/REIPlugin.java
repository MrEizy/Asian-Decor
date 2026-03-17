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
import net.minecraft.core.NonNullList;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.thejadeproject.asiandecor.recipe.ColorMixerRecipe.getDyeItem;

@REIPluginClient
public class REIPlugin implements REIClientPlugin {

    // Category Identifiers
    public static final CategoryIdentifier<CarpenterDisplay> CARPENTER =
            CategoryIdentifier.of(AsianDecor.MOD_ID, "carpenter");

    public static final CategoryIdentifier<ColorMixerDisplay> COLOR_MIXER =
            CategoryIdentifier.of(AsianDecor.MOD_ID, "color_mixer");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        // Register Carpenter Category
        registry.add(new CarpenterCategory());
        registry.addWorkstations(CARPENTER, EntryStacks.of(ModBlocks.CARPENTER.get()));

        // Register Color Mixer Category
        registry.add(new ColorMixerCategory());
        registry.addWorkstations(COLOR_MIXER, EntryStacks.of(ModBlocks.COLOR_MIXER.get()));

        // Add more categories here as you create them
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // === CARPENTER RECIPES ===
        List<RecipeHolder<CarpenterRecipes>> carpenterRecipes = recipeManager.getAllRecipesFor(
                ModRecipes.CARPENTER_TYPE.get()
        );
        for (RecipeHolder<CarpenterRecipes> holder : carpenterRecipes) {
            registry.add(new CarpenterDisplay(holder.value()));
        }

        // === COLOR MIXER RECIPES (Optimized: 257 instead of 65k) ===
        List<RecipeHolder<ColorMixerRecipe>> allColorMixerRecipes = recipeManager.getAllRecipesFor(
                ModRecipes.COLOR_MIXER_TYPE.get()
        );

        // Filter to only vanilla brick recipes (256 total)
        List<ColorMixerRecipe> vanillaRecipes = new ArrayList<>();
        for (RecipeHolder<ColorMixerRecipe> holder : allColorMixerRecipes) {
            ColorMixerRecipe recipe = holder.value();
            if (isVanillaBrickRecipe(recipe)) {
                vanillaRecipes.add(recipe);
            }
        }

        // Add vanilla recipes to REI
        for (ColorMixerRecipe recipe : vanillaRecipes) {
            registry.add(new ColorMixerDisplay(recipe));
        }

        // Add ONE representative recolor recipe
        ColorMixerRecipe recolorRecipe = createDummyRecolorRecipe();
        registry.add(new ColorMixerDisplay(recolorRecipe, true)); // true = isRecolorRecipe
    }

    private boolean isVanillaBrickRecipe(ColorMixerRecipe recipe) {
        if (recipe.getIngredients().isEmpty()) return false;
        return recipe.getIngredients().get(0).test(new ItemStack(Items.BRICKS));
    }

    private ColorMixerRecipe createDummyRecolorRecipe() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModBlocks.DYED_BRICKS.values().stream()
                .map(b -> new ItemStack(b.get())).toArray(ItemStack[]::new)));
        ingredients.add(Ingredient.of(Arrays.stream(DyeColor.values())
                .map(c -> new ItemStack(getDyeItem(c))).toArray(ItemStack[]::new)));
        ingredients.add(Ingredient.of(Arrays.stream(DyeColor.values())
                .map(c -> new ItemStack(getDyeItem(c))).toArray(ItemStack[]::new)));

        return new ColorMixerRecipe("dyed_bricks_recolor_rei", ingredients,
                new ItemStack(ModBlocks.DYED_BRICKS.get(DyedBrickType.WHITE_WHITE).get()), 100);
    }

    // === DISPLAY CLASSES ===

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
        private final boolean isRecolorRecipe;

        public ColorMixerDisplay(ColorMixerRecipe recipe) {
            this(recipe, false);
        }

        public ColorMixerDisplay(ColorMixerRecipe recipe, boolean isRecolorRecipe) {
            super(
                    Arrays.asList(
                            EntryIngredients.ofIngredient(recipe.getIngredients().get(0)),
                            EntryIngredients.ofIngredient(recipe.getIngredients().get(1)),
                            EntryIngredients.ofIngredient(recipe.getIngredients().get(2))
                    ),
                    Collections.singletonList(EntryIngredients.of(recipe.getResultItem(null)))
            );
            this.isRecolorRecipe = isRecolorRecipe;
        }

        public boolean isRecolorRecipe() {
            return isRecolorRecipe;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return COLOR_MIXER;
        }
    }
}