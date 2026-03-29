// ColorMixerRecipeCategory.java
package net.zic.builders_zenith.compat.jei;

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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ColorMixerRecipeCategory implements IRecipeCategory<ColorMixerRecipe> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            BuildersZenith.MOD_ID, "textures/gui/jemri/color_mixing.png");

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
        return Component.translatable("jei.category.builders_zenith.color_mixer");
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
        String group = recipe.getGroup();
        boolean isVerticalSlab = group.contains("vertical_slab");
        boolean isSlab = !isVerticalSlab && group.contains("slab");
        boolean isStairs = group.contains("stair");
        boolean isWall = group.contains("wall");

        Function<DyedBrickType, ItemStack> stackMapper;
        ItemStack vanillaInput;

        if (isSlab) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_SLABS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICK_SLAB, 8);  // ADD COUNT HERE
        } else if (isStairs) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICK_STAIRS, 8);  // ADD COUNT HERE
        } else if (isWall) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_WALLS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICK_WALL, 8);  // ADD COUNT HERE (or 6 for walls)
        } else if (isVerticalSlab) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type).get());
            vanillaInput = new ItemStack(ModBlocks.BRICK_VERTICAL_SLAB.get(), 8);  // ADD COUNT HERE
        } else {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICKS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICKS, 8);  // ADD COUNT HERE
        }

        List<ItemStack> allDyedVariants = Arrays.stream(DyedBrickType.values())
                .map(stackMapper)
                .toList();

        if (recipe.isVanillaRecipe()) {
            // Vanilla recipe: Vanilla block + Dye + Dye
            builder.addSlot(RecipeIngredientRole.INPUT, 8, 13)
                    .addIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, vanillaInput)
                    .setSlotName("base");  // Optional: name the slot
        } else {
            // Recolor recipe: Any dyed variant + Dye + Dye
            // Create stacks with proper count
            List<ItemStack> dyedWithCount = allDyedVariants.stream()
                    .map(stack -> {
                        ItemStack copy = stack.copy();
                        copy.setCount(8);  // SET COUNT TO 8
                        return copy;
                    })
                    .toList();
            builder.addSlot(RecipeIngredientRole.INPUT, 8, 13)
                    .addItemStacks(dyedWithCount);
        }

        // Primary dye slot - All dyes (count 1, but you might want 2)
        List<ItemStack> allDyes = Arrays.stream(DyeColor.values())
                .map(color -> new ItemStack(getDyeItem(color), 1))  // 2 DYES NEEDED
                .toList();
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 13)
                .addItemStacks(allDyes);

        // Secondary dye slot - All dyes (count 1, but you might want 2)
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 32)
                .addItemStacks(allDyes);

        // Output slot - All possible dyed variants with proper output counts
        List<ItemStack> outputsWithCount = Arrays.stream(DyedBrickType.values())
                .map(type -> {
                    int count = isStairs ? 4 : (isWall ? 6 : 8);
                    ItemStack stack = stackMapper.apply(type);
                    stack.setCount(count);
                    return stack;
                })
                .toList();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 20)
                .addItemStacks(outputsWithCount);
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