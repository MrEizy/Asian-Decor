// CarpenterCategory.java
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
import net.zic.builders_zenith.blocks.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class CarpenterCategory implements DisplayCategory<REIPlugin.CarpenterDisplay> {

    @Override
    public CategoryIdentifier<REIPlugin.CarpenterDisplay> getCategoryIdentifier() {
        return REIPlugin.CARPENTER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.category.asiandecor.carpenter");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.CARPENTER.get());
    }

    @Override
    public List<Widget> setupDisplay(REIPlugin.CarpenterDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 50, bounds.getCenterY() - 27);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        // Input slot
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 18))
                .entries(display.getInputEntries().get(0))
                .markInput());

        // Arrow
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 30, startPoint.y + 18)));

        // Output slot
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 76, startPoint.y + 18))
                .entries(display.getOutputEntries().get(0))
                .markOutput());

        // Ingredient count label
        widgets.add(Widgets.createLabel(new Point(startPoint.x + 4, startPoint.y + 40),
                        Component.translatable("tooltip.asiandecor.ingredient_cost", display.getIngredientCount()))
                .color(0xFF808080, 0xFF808080)
                .noShadow());

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 60;
    }
}