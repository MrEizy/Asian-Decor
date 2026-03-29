// ColorMixerCategory.java
package net.zic.builders_zenith.compat.rei;

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
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ColorMixerCategory implements DisplayCategory<REIPlugin.ColorMixerDisplay> {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            BuildersZenith.MOD_ID, "textures/gui/jemri/color_mixing.png");

    @Override
    public CategoryIdentifier<REIPlugin.ColorMixerDisplay> getCategoryIdentifier() {
        return REIPlugin.COLOR_MIXER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.category.builders_zenith.color_mixer");
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

        // All possible dyes for cycling display (2 each)
        List<ItemStack> allDyes = Arrays.stream(DyeColor.values())
                .map(color -> new ItemStack(getDyeItem(color), 2))
                .toList();

        // Determine block type from group
        String group = display.getGroup();
        boolean isVerticalSlab = group.contains("vertical_slab");
        boolean isSlab = !isVerticalSlab && group.contains("slab");
        boolean isStairs = group.contains("stair");
        boolean isWall = group.contains("wall");

        int baseCount = 8;
        int outputCount = isStairs ? 4 : (isWall ? 6 : 8);

        Function<DyedBrickType, ItemStack> stackMapper;
        ItemStack vanillaInput;

        if (isSlab) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_SLABS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICK_SLAB, baseCount);
        } else if (isStairs) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICK_STAIRS, baseCount);
        } else if (isWall) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_WALLS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICK_WALL, baseCount);
        } else if (isVerticalSlab) {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type).get());
            vanillaInput = new ItemStack(ModBlocks.BRICK_VERTICAL_SLAB.get(), baseCount);
        } else {
            stackMapper = type -> new ItemStack(ModBlocks.DYED_BRICKS.get(type).get());
            vanillaInput = new ItemStack(Items.BRICKS, baseCount);
        }

        if (display.isVanillaRecipe()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 8, startPoint.y + 13))
                    .entry(EntryStacks.of(vanillaInput))
                    .markInput());

            String label = isSlab ? "Vanilla Slabs + Dyes" :
                    isStairs ? "Vanilla Stairs + Dyes" :
                            isWall ? "Vanilla Walls + Dyes" :
                                    isVerticalSlab ? "Vanilla Vertical Slabs + Dyes" :
                                            "Vanilla Bricks + Dyes";
            widgets.add(Widgets.createLabel(new Point(startPoint.x + 60, startPoint.y + 5),
                            Component.literal(label))
                    .color(0xFFAAAAAA, 0xFFAAAAAA)
                    .noShadow());
        } else {
            List<ItemStack> allDyed = Arrays.stream(DyedBrickType.values())
                    .map(type -> {
                        ItemStack stack = stackMapper.apply(type);
                        stack.setCount(baseCount);
                        return stack;
                    })
                    .toList();
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 8, startPoint.y + 13))
                    .entries(allDyed.stream().map(EntryStacks::of).toList())
                    .markInput());

            String label = isSlab ? "Dyed Slabs + Dyes" :
                    isStairs ? "Dyed Stairs + Dyes" :
                            isWall ? "Dyed Walls + Dyes" :
                                    isVerticalSlab ? "Dyed Vertical Slabs + Dyes" :
                                            "Any Dyed Brick + Dyes";
            widgets.add(Widgets.createLabel(new Point(startPoint.x + 60, startPoint.y + 5),
                            Component.literal(label))
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

        // Output - correct variant type with proper count
        List<ItemStack> allDyedOutputs = Arrays.stream(DyedBrickType.values())
                .map(type -> {
                    ItemStack stack = stackMapper.apply(type);
                    stack.setCount(outputCount);
                    return stack;
                })
                .toList();
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 103, startPoint.y + 20))
                .entries(allDyedOutputs.stream().map(EntryStacks::of).toList())
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