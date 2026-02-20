package net.thejadeproject.asiandecor.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.thejadeproject.asiandecor.AsianDecor;

public class lang extends LanguageProvider {
    public lang(PackOutput output, String locale) {
        super(output, AsianDecor.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {

        //Items
        add("item.asiandecor.white_block_pouch", "White Builder's Pouch");
        add("item.asiandecor.blueprint", "Blueprint");



        //Blocks
        add("block.asiandecor.carpenterblock", "Carpenter's Table");


        add("block.asiandecor.oak_winged_table", "Oak Winged Table");

        //GUI & Other Stuff
        add("creativetab.asiandecor.items", "Asian Decor Items");
        add("creativetab.asiandecor.wood", "Wood Decor");


        add("container.asiandecor.carpenter", "Carpenter's Table");
        add("tooltip.asiandecor.ingredient_cost", "Requires: %s");

        add("container.asiandecor.block_pouch", "Builder's Pouch");

        //Tooltips
        add("tooltip.asiandecor.block_pouch.selected", "Selected: %s");
        add("tooltip.asiandecor.block_pouch.count", "Count: %s");
        add("tooltip.asiandecor.block_pouch.empty", "Empty");
        add("tooltip.asiandecor.block_pouch.slots", "%s/%s slots filled");



        add("tooltip.asiandecor.blueprint.saved_size", "Size: %s x %s x %s");
        add("tooltip.asiandecor.blueprint.block_count", "Contains %s blocks");
        add("tooltip.asiandecor.blueprint.cut_mode", "[CUT MODE - Will remove blocks when placed]");
        add("tooltip.asiandecor.blueprint.area_selected", "Selected: %s x %s x %s");
        add("tooltip.asiandecor.blueprint.shift_to_cut", "Shift+Right Click to CUT");
        add("tooltip.asiandecor.blueprint.pos1", "Pos 1: [%s, %s, %s]");
        add("tooltip.asiandecor.blueprint.set_pos2", "Shift+Right Click to set Position 2");
        add("tooltip.asiandecor.blueprint.no_area", "No area selected");
        add("tooltip.asiandecor.blueprint.usage.select", "Shift+Right Click: Select two corners");
        add("tooltip.asiandecor.blueprint.usage.cut", "Shift+Right Click again: CUT the area");
        add("tooltip.asiandecor.blueprint.usage.preview", "Right Click: Preview / Place");
        add("tooltip.asiandecor.blueprint.rotation", "Rotation: %s");
        add("tooltip.asiandecor.blueprint.usage.rotate", "Shift+Scroll: Rotate");


        //Action Bar
        add("message.asiandecor.block_pouch.selected", "%s");
        add("message.asiandecor.block_pouch.empty", "No block selected");


        add("message.asiandecor.blueprint.pos1_set", "Position 1 set: [%s, %s, %s]");
        add("message.asiandecor.blueprint.pos2_set", "Position 2 set: [%s, %s, %s] - Size: %sx%sx%s");
        add("message.asiandecor.blueprint.too_large", "Area too large: %sx%sx%s (max %s)");
        add("message.asiandecor.blueprint.cut", "Cut %sx%sx%s area (%s blocks)");
        add("message.asiandecor.blueprint.empty", "Blueprint empty! Shift+Click to select area");
        add("message.asiandecor.blueprint.ready_to_cut", "Area selected! Shift+Click to CUT");
        add("message.asiandecor.blueprint.preview", "Previewing %sx%sx%s - Click again to place");
        add("message.asiandecor.blueprint.placed", "Placed %s blocks (%s failed)");
        add("message.asiandecor.blueprint.placed_cleared", "Placed %s blocks (%s failed) - Blueprint cleared");
        add("message.asiandecor.blueprint.rotated", "Rotation: %s");




        //Compats
        add("emi.category.asiandecor.carpenter", "Carpenter's Table");
        add("jei.category.asiandecor.carpenter", "Carpenter's Table");
        add("rei.category.asiandecor.carpenter", "Carpenter's Table");

    }
}
