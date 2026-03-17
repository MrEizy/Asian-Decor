// ColorMixerRecipeCategory.java
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
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;

import java.util.Arrays;
import java.util.List;

public class ColorMixerRecipeCategory implements IRecipeCategory<ColorMixerRecipe> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            AsianDecor.MOD_ID, "textures/gui/jemri/color_mixing.png");

    private final IDrawable icon;
    private final int width;
    private final int height;

    public ColorMixerRecipeCategory(IGuiHelper helper) {
        this.width = 121;
        this.height = 57;
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
        if (recipe.isVanillaRecipe()) {
            setupVanillaRecipe(builder);
        } else {
            setupRecolorRecipe(builder);
        }
    }

    private void setupVanillaRecipe(IRecipeLayoutBuilder builder) {
        // Base slot - Vanilla Bricks
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 13)
                .addIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, new ItemStack(Items.BRICKS));

        // Primary dye slot - All dyes (JEI will expand this)
        List<ItemStack> allDyes = Arrays.stream(DyeColor.values())
                .map(color -> new ItemStack(getDyeItem(color)))
                .toList();
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 13)
                .addItemStacks(allDyes);

        // Secondary dye slot - All dyes
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 32)
                .addItemStacks(allDyes);

        // Output slot - All possible dyed bricks (JEI will match combinations)
        List<ItemStack> allDyedBricks = Arrays.stream(DyedBrickType.values())
                .map(type -> new ItemStack(ModBlocks.DYED_BRICKS.get(type).get()))
                .toList();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 20)
                .addItemStacks(allDyedBricks);
    }

    private void setupRecolorRecipe(IRecipeLayoutBuilder builder) {
        // Base slot - All dyed bricks
        List<ItemStack> allDyedBricks = Arrays.stream(DyedBrickType.values())
                .map(type -> new ItemStack(ModBlocks.DYED_BRICKS.get(type).get()))
                .toList();
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 13)
                .addItemStacks(allDyedBricks);

        // Primary dye slot - All dyes
        List<ItemStack> allDyes = Arrays.stream(DyeColor.values())
                .map(color -> new ItemStack(getDyeItem(color)))
                .toList();
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 13)
                .addItemStacks(allDyes);

        // Secondary dye slot - All dyes
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 32)
                .addItemStacks(allDyes);

        // Output slot - All possible dyed bricks
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 20)
                .addItemStacks(allDyedBricks);
    }

    private static net.minecraft.world.item.Item getDyeItem(DyeColor color) {
        return switch (color) {
            case WHITE -> net.minecraft.world.item.Items.WHITE_DYE;
            case ORANGE -> net.minecraft.world.item.Items.ORANGE_DYE;
            case MAGENTA -> net.minecraft.world.item.Items.MAGENTA_DYE;
            case LIGHT_BLUE -> net.minecraft.world.item.Items.LIGHT_BLUE_DYE;
            case YELLOW -> net.minecraft.world.item.Items.YELLOW_DYE;
            case LIME -> net.minecraft.world.item.Items.LIME_DYE;
            case PINK -> net.minecraft.world.item.Items.PINK_DYE;
            case GRAY -> net.minecraft.world.item.Items.GRAY_DYE;
            case LIGHT_GRAY -> net.minecraft.world.item.Items.LIGHT_GRAY_DYE;
            case CYAN -> net.minecraft.world.item.Items.CYAN_DYE;
            case PURPLE -> net.minecraft.world.item.Items.PURPLE_DYE;
            case BLUE -> net.minecraft.world.item.Items.BLUE_DYE;
            case BROWN -> net.minecraft.world.item.Items.BROWN_DYE;
            case GREEN -> net.minecraft.world.item.Items.GREEN_DYE;
            case RED -> net.minecraft.world.item.Items.RED_DYE;
            case BLACK -> net.minecraft.world.item.Items.BLACK_DYE;
        };
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