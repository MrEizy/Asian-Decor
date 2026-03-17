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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ColorMixerCategory implements DisplayCategory<REIPlugin.ColorMixerDisplay> {

    // Custom background texture from jemri folder
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

        // Custom background using texture
        widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, bounds, 0, 0, 121, 57, 121, 57));

        // Input slots - adjusted to match texture positions
        // Base slot (left)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 8, startPoint.y + 13))
                .entries(display.getInputEntries().get(0))
                .markInput());

        // Primary dye slot (middle top)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 28, startPoint.y + 13))
                .entries(display.getInputEntries().get(1))
                .markInput());

        // Secondary dye slot (middle bottom)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 32))
                .entries(display.getInputEntries().get(2))
                .markInput());

        // Output slot (right)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 103, startPoint.y + 20))
                .entries(display.getOutputEntries().get(0))
                .markOutput());

        // If recolor recipe, show "Any" label
        if (display.isRecolorRecipe()) {
            widgets.add(Widgets.createLabel(new Point(startPoint.x + 60, startPoint.y + 5),
                            Component.literal("Any Dyed Brick + Any Dyes"))
                    .color(0xFFAAAAAA, 0xFFAAAAAA)
                    .noShadow());
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 57; // Match texture height
    }
}