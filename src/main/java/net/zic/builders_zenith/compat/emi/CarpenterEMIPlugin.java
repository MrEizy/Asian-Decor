package net.zic.builders_zenith.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.recipe.CarpenterRecipes;

import java.util.List;

@EmiEntrypoint
public class CarpenterEMIPlugin implements EmiPlugin {

    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(BuildersZenith.MOD_ID, "carpenter"),
            EmiStack.of(ModBlocks.CARPENTER.get())
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.CARPENTER.get()));

        RecipeManager manager = registry.getRecipeManager();
        List<RecipeHolder<CarpenterRecipes>> recipes = manager.getAllRecipesFor(
                net.zic.builders_zenith.recipe.ModRecipes.CARPENTER_TYPE.get()
        );

        for (RecipeHolder<CarpenterRecipes> holder : recipes) {
            // Use the actual recipe ID from the holder (points to JSON file)
            registry.addRecipe(new CarpenterEmiRecipe(holder));
        }
    }

    public static class CarpenterEmiRecipe implements EmiRecipe {
        private final CarpenterRecipes recipe;
        private final ResourceLocation id;

        public CarpenterEmiRecipe(RecipeHolder<CarpenterRecipes> holder) {
            this.recipe = holder.value();
            // Use the actual recipe ID from the holder - this points to the JSON file
            this.id = holder.id();
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return CATEGORY;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(EmiIngredient.of(recipe.getIngredient()));
        }

        @Override
        public List<EmiStack> getOutputs() {
            return List.of(EmiStack.of(recipe.getResult()));
        }

        @Override
        public int getDisplayWidth() {
            return 118;
        }

        @Override
        public int getDisplayHeight() {
            return 54;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            widgets.addSlot(getInputs().get(0), 4, 18);

            widgets.addTexture(EmiTexture.EMPTY_ARROW, 30, 18);

            widgets.addSlot(getOutputs().get(0), 76, 18).recipeContext(this);

            widgets.addText(
                    net.minecraft.network.chat.Component.translatable("tooltip.builders_zenith.ingredient_cost",
                            recipe.getIngredientCount()),
                    4, 40, 0x808080, false
            );
        }
    }
}