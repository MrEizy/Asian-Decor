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
import net.thejadeproject.asiandecor.recipe.CarpenterRecipes;

public class CarpenterRecipeCategory implements IRecipeCategory<CarpenterRecipes> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID,
            "textures/gui/container/carpenter.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final int width;
    private final int height;

    public CarpenterRecipeCategory(IGuiHelper helper) {
        this.width = 100;
        this.height = 54;
        this.background = helper.createDrawable(TEXTURE, 52, 14, this.width, this.height);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.CARPENTER.get()));
    }

    @Override
    public RecipeType<CarpenterRecipes> getRecipeType() {
        return CarpenterJEIPlugin.RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.asiandecor.carpenter");
    }


    @Override
    public IDrawable getIcon() {
        return icon;
    }

    // MUST OVERRIDE THESE METHODS
    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CarpenterRecipes recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 18)
                .addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 18)
                .addItemStack(recipe.getResult());
    }

    @Override
    public void draw(CarpenterRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        // Draw ingredient count requirement
        Component costText = Component.translatable("tooltip.asiandecor.ingredient_cost",
                recipe.getIngredientCount());
        guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font,
                costText, 4, 40, 0xFF808080, false);
    }
}