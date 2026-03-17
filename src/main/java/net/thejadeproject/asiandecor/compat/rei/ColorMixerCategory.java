// ColorMixerCategory.java
package net.thejadeproject.asiandecor.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorMixerCategory implements DisplayCategory<REIPlugin.ColorMixerDisplay> {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            AsianDecor.MOD_ID, "textures/gui/jemri/color_mixing.png");

    @Override
    public CategoryIdentifier<REIPlugin.ColorMixerDisplay> getCategoryIdentifier() {
        return REIPlugin.COLOR_MIXER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.category.asiandecor.color_mixer");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.COLOR_MIXER.get());
    }

    @Override
    public List<Widget> setupDisplay(REIPlugin.ColorMixerDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 60, bounds.getCenterY() - 28);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, bounds, 0, 0, 121, 57, 121, 57));

        // All possible dyes for cycling display
        List<ItemStack> allDyes = Arrays.stream(DyeColor.values())
                .map(color -> new ItemStack(getDyeItem(color)))
                .toList();

        if (display.isVanillaRecipe()) {
            // Vanilla recipe: Bricks + Dye + Dye
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 8, startPoint.y + 13))
                    .entry(EntryStacks.of(Items.BRICKS))
                    .markInput());

            widgets.add(Widgets.createLabel(new Point(startPoint.x + 60, startPoint.y + 5),
                            Component.literal("Vanilla Bricks + Dyes"))
                    .color(0xFFAAAAAA, 0xFFAAAAAA)
                    .noShadow());
        } else {
            // Recolor recipe: Any Dyed Brick + Dye + Dye
            List<ItemStack> allDyedBricks = Arrays.stream(DyedBrickType.values())
                    .map(type -> new ItemStack(ModBlocks.DYED_BRICKS.get(type).get()))
                    .toList();
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 8, startPoint.y + 13))
                    .entries(allDyedBricks.stream().map(EntryStacks::of).toList())
                    .markInput());

            widgets.add(Widgets.createLabel(new Point(startPoint.x + 60, startPoint.y + 5),
                            Component.literal("Any Dyed Brick + Dyes"))
                    .color(0xFFAAAAAA, 0xFFAAAAAA)
                    .noShadow());
        }

        // Primary dye slot
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 28, startPoint.y + 13))
                .entries(allDyes.stream().map(EntryStacks::of).toList())
                .markInput());

        // Secondary dye slot
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 32))
                .entries(allDyes.stream().map(EntryStacks::of).toList())
                .markInput());

        // Output - all possible dyed bricks
        List<ItemStack> allDyedBricks = Arrays.stream(DyedBrickType.values())
                .map(type -> new ItemStack(ModBlocks.DYED_BRICKS.get(type).get()))
                .toList();
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 103, startPoint.y + 20))
                .entries(allDyedBricks.stream().map(EntryStacks::of).toList())
                .markOutput());

        return widgets;
    }

    private static net.minecraft.world.item.Item getDyeItem(DyeColor color) {
        return switch (color) {
            case WHITE -> Items.WHITE_DYE;
            case ORANGE -> Items.ORANGE_DYE;
            case MAGENTA -> Items.MAGENTA_DYE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
            case YELLOW -> Items.YELLOW_DYE;
            case LIME -> Items.LIME_DYE;
            case PINK -> Items.PINK_DYE;
            case GRAY -> Items.GRAY_DYE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
            case CYAN -> Items.CYAN_DYE;
            case PURPLE -> Items.PURPLE_DYE;
            case BLUE -> Items.BLUE_DYE;
            case BROWN -> Items.BROWN_DYE;
            case GREEN -> Items.GREEN_DYE;
            case RED -> Items.RED_DYE;
            case BLACK -> Items.BLACK_DYE;
        };
    }

    @Override
    public int getDisplayHeight() {
        return 57;
    }
}