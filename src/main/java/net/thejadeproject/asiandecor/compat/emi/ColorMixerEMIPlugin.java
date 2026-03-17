package net.thejadeproject.asiandecor.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
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
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;
import net.thejadeproject.asiandecor.recipe.ModRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.thejadeproject.asiandecor.recipe.ColorMixerRecipe.getDyeItem;

@EmiEntrypoint
public class ColorMixerEMIPlugin implements EmiPlugin {

    // Custom background texture from jemri folder
    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            AsianDecor.MOD_ID, "textures/gui/jemri/color_mixing.png");

    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "color_mixer"),
            EmiStack.of(ModBlocks.COLOR_MIXER.get())
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.COLOR_MIXER.get()));

        RecipeManager manager = registry.getRecipeManager();
        List<RecipeHolder<ColorMixerRecipe>> allRecipes = manager.getAllRecipesFor(
                ModRecipes.COLOR_MIXER_TYPE.get()
        );

        // Filter to only vanilla brick recipes (256 total)
        List<RecipeHolder<ColorMixerRecipe>> vanillaRecipes = new ArrayList<>();
        for (RecipeHolder<ColorMixerRecipe> holder : allRecipes) {
            if (isVanillaBrickRecipe(holder.value())) {
                vanillaRecipes.add(holder);
            }
        }

        // Add vanilla recipes to EMI
        for (RecipeHolder<ColorMixerRecipe> holder : vanillaRecipes) {
            registry.addRecipe(new ColorMixerEmiRecipe(holder, false));
        }

        // Add ONE representative recolor recipe
        ColorMixerRecipe recolorRecipe = createDummyRecolorRecipe();
        ResourceLocation recolorId = ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "color_mixer/recolor/any");
        registry.addRecipe(new ColorMixerEmiRecipe(recolorRecipe, recolorId, true));
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

        return new ColorMixerRecipe("dyed_bricks_recolor_emi", ingredients,
                new ItemStack(ModBlocks.DYED_BRICKS.get(DyedBrickType.WHITE_WHITE).get()), 100);
    }

    public static class ColorMixerEmiRecipe implements EmiRecipe {
        private final ColorMixerRecipe recipe;
        private final ResourceLocation id;
        private final boolean isRecolorRecipe;

        public ColorMixerEmiRecipe(RecipeHolder<ColorMixerRecipe> holder, boolean isRecolorRecipe) {
            this.recipe = holder.value();
            this.id = holder.id();
            this.isRecolorRecipe = isRecolorRecipe;
        }

        public ColorMixerEmiRecipe(ColorMixerRecipe recipe, ResourceLocation id, boolean isRecolorRecipe) {
            this.recipe = recipe;
            this.id = id;
            this.isRecolorRecipe = isRecolorRecipe;
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
            return List.of(
                    EmiIngredient.of(recipe.getIngredients().get(0)),
                    EmiIngredient.of(recipe.getIngredients().get(1)),
                    EmiIngredient.of(recipe.getIngredients().get(2))
            );
        }

        @Override
        public List<EmiStack> getOutputs() {
            return List.of(EmiStack.of(recipe.getResultItem(null)));
        }

        @Override
        public int getDisplayWidth() {
            return 121;
        }

        @Override
        public int getDisplayHeight() {
            return 57;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            // Draw custom background texture
            widgets.addTexture(new EmiTexture(BACKGROUND_TEXTURE, 0, 0, 121, 57), 0, 0);

            // Base slot (left) - adjusted for texture
            widgets.addSlot(getInputs().get(0), 8, 13);

            // Primary dye slot (middle top)
            widgets.addSlot(getInputs().get(1), 28, 13);

            // Secondary dye slot (middle bottom)
            widgets.addSlot(getInputs().get(2), 18, 32);

            // Output slot (right)
            widgets.addSlot(getOutputs().get(0), 103, 20).recipeContext(this);

            // Processing time at bottom
            int processingTime = recipe.getProcessingTime();
            if (processingTime > 0) {
                String timeText = (processingTime / 20) + "s";
                int textWidth = Minecraft.getInstance().font.width(timeText);
                int xPos = (getDisplayWidth() - textWidth) / 2;
                widgets.addText(Component.literal(timeText), xPos, 47, 0xFFFFFF, false);
            }

            // If recolor recipe, show "Any" label at top
            if (isRecolorRecipe) {
                widgets.addText(
                        Component.literal("Any Dyed Brick + Any Dyes"),
                        10, 5, 0xAAAAAA, false
                );
            }
        }
    }
}