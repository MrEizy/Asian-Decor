// ColorMixerEMIPlugin.java
package net.zic.builders_zenith.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;
import net.zic.builders_zenith.recipe.ModRecipes;

import java.util.Arrays;
import java.util.List;

import static net.zic.builders_zenith.recipe.ColorMixerRecipe.getDyeItem;

@EmiEntrypoint
public class ColorMixerEMIPlugin implements EmiPlugin {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            BuildersZenith.MOD_ID, "textures/gui/jemri/color_mixing.png");

    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(BuildersZenith.MOD_ID, "color_mixer"),
            EmiStack.of(ModBlocks.COLOR_MIXER.get())
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.COLOR_MIXER.get()));

        RecipeManager manager = registry.getRecipeManager();
        List<RecipeHolder<ColorMixerRecipe>> recipes = manager.getAllRecipesFor(
                ModRecipes.COLOR_MIXER_TYPE.get()
        );

        // Only 2 recipes!
        for (RecipeHolder<ColorMixerRecipe> holder : recipes) {
            registry.addRecipe(new ColorMixerEmiRecipe(holder));
        }
    }

    public static class ColorMixerEmiRecipe implements EmiRecipe {
        private final ColorMixerRecipe recipe;
        private final ResourceLocation id;

        public ColorMixerEmiRecipe(RecipeHolder<ColorMixerRecipe> holder) {
            this.recipe = holder.value();
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
            // Draw custom background
            widgets.addTexture(new dev.emi.emi.api.render.EmiTexture(
                    BACKGROUND_TEXTURE, 0, 0, 121, 57), 0, 0);

            // All dyes for cycling
            List<EmiStack> allDyes = Arrays.stream(DyeColor.values())
                    .map(color -> EmiStack.of(getDyeItem(color)))
                    .toList();

            if (recipe.isVanillaRecipe()) {
                // Vanilla: Bricks + Dye + Dye
                widgets.addSlot(EmiStack.of(Items.BRICKS), 8, 13);

                widgets.addText(Component.literal("Vanilla Bricks + Dyes"), 10, 5, 0xAAAAAA, false);
            } else {
                // Recolor: Any Dyed Brick + Dye + Dye
                List<EmiStack> allDyedBricks = Arrays.stream(DyedBrickType.values())
                        .map(type -> EmiStack.of(ModBlocks.DYED_BRICKS.get(type).get()))
                        .toList();
                widgets.addSlot(EmiIngredient.of(allDyedBricks), 8, 13);

                widgets.addText(Component.literal("Any Dyed Brick + Dyes"), 10, 5, 0xAAAAAA, false);
            }

            // Primary dye
            widgets.addSlot(EmiIngredient.of(allDyes), 28, 13);

            // Secondary dye
            widgets.addSlot(EmiIngredient.of(allDyes), 18, 32);

            // Output - all possible results
            List<EmiStack> allDyedBricks = Arrays.stream(DyedBrickType.values())
                    .map(type -> EmiStack.of(ModBlocks.DYED_BRICKS.get(type).get()))
                    .toList();
            widgets.addSlot(EmiIngredient.of(allDyedBricks), 103, 20).recipeContext(this);

            // Processing time
            int processingTime = recipe.getProcessingTime();
            if (processingTime > 0) {
                String timeText = (processingTime / 20) + "s";
                int textWidth = Minecraft.getInstance().font.width(timeText);
                int xPos = (getDisplayWidth() - textWidth) / 2;
                widgets.addText(Component.literal(timeText), xPos, 47, 0xFFFFFF, false);
            }
        }
    }
}