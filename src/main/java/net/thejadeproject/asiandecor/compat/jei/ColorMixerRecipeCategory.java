package net.thejadeproject.asiandecor.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;

public class ColorMixerRecipeCategory implements IRecipeCategory<ColorMixerRecipe> {

    // Your custom JEI texture
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            AsianDecor.MOD_ID, "textures/gui/jemri/color_mixing.png");

    private final IDrawable icon;
    private final int width;
    private final int height;

    public ColorMixerRecipeCategory(IGuiHelper helper) {
        this.width = 121;  // Adjust to match your texture
        this.height = 57;  // Adjust to match your texture
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.COLOR_MIXER.get()));
    }

    @Override
    public RecipeType<ColorMixerRecipe> getRecipeType() {
        return JEIPlugin.COLOR_MIXER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.category.asiandecor.color_mixer");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ColorMixerRecipe recipe, IFocusGroup focuses) {
        // Base slot (left side) - Slot 0: Bricks or Dyed Bricks
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 13)
                .addIngredients(recipe.getIngredients().get(0));

        // Primary dye slot (top right of center) - Slot 1: Primary dye (brick color)
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 13)
                .addIngredients(recipe.getIngredients().get(1));

        // Secondary dye slot (bottom center) - Slot 2: Secondary dye (mortar color)
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 32)
                .addIngredients(recipe.getIngredients().get(2));

        // Output slot (right side) - Result: Dyed Bricks
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 20)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(ColorMixerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        int processingTime = recipe.getProcessingTime();
        if (processingTime > 0) {
            String timeText = (processingTime / 20) + "s";
            var font = net.minecraft.client.Minecraft.getInstance().font;
            int textWidth = font.width(timeText);

            int xPos = (this.width - textWidth) / 2;
            int yPos = 47;

            guiGraphics.drawString(font, timeText, xPos + 1, yPos + 1, 0x000000, false);
            guiGraphics.drawString(font, timeText, xPos, yPos, 0xFFFFFF, false);
        }
    }
}
