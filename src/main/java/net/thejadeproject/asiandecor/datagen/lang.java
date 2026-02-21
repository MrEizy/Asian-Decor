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
        add("item.asiandecor.tape_measure", "Tape Measure");
        add("item.asiandecor.trowel", "Builder's Trowel");



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
        add("tooltip.asiandecor.blueprint.facing", "Facing: %s");
        add("tooltip.asiandecor.blueprint.usage.rotate", "Shift+Scroll: Rotate Y (Horizontal)");
        add("tooltip.asiandecor.blueprint.usage.rotate_vertical", "Ctrl+Shift+Scroll: Rotate X (Vertical/Wall)");

        add("tooltip.asiandecor.tape_measure.pos1_set", "§7Pos 1: §f[%s, %s, %s]");
        add("tooltip.asiandecor.tape_measure.pos2_set", "§7Pos 2: §f[%s, %s, %s]");
        add("tooltip.asiandecor.tape_measure.no_selection", "§7Right Click to set §fPosition 1");
        add("tooltip.asiandecor.tape_measure.select_pos2", "§7Right Click to set §fPosition 2");
        add("tooltip.asiandecor.tape_measure.dimensions", "§aDimensions: §f%s§7x§f%s§7x§f%s");
        add("tooltip.asiandecor.tape_measure.clear", "§8Shift+Right Click to clear");


        add("tooltip.asiandecor.trowel.mode", "§7Mode: §f%s");
        add("tooltip.asiandecor.trowel.mode.hotbar", "Hotbar Only");
        add("tooltip.asiandecor.trowel.mode.inventory", "Entire Inventory");
        add("tooltip.asiandecor.trowel.mode.pouch", "Linked Pouch");
        add("tooltip.asiandecor.trowel.linked_pouch", "§aLinked to Block Pouch");
        add("tooltip.asiandecor.trowel.pouch_slots", "§7Pouch: §f%s§7/§f%s slots");
        add("tooltip.asiandecor.trowel.no_pouch_link", "§cNo pouch linked!");
        add("tooltip.asiandecor.trowel.usage", "§8Right Click: Place random block");
        add("tooltip.asiandecor.trowel.toggle_key", "§8Press §f%s§8 to toggle mode");
        add("tooltip.asiandecor.trowel.link_instruction", "§7Link: Hold trowel and click on Block Pouch");



        //Action Bar
        add("message.asiandecor.block_pouch.selected", "%s");
        add("message.asiandecor.block_pouch.empty", "No block selected");


        add("message.asiandecor.blueprint.pos1_set", "Position 1 set: [%s, %s, %s]");
        add("message.asiandecor.blueprint.pos2_set", "Position 2 set: [%s, %s, %s] - Size: %sx%sx%s");
        add("message.asiandecor.blueprint.too_large", "Area too large: %sx%sx%s (max %s)");
        add("message.asiandecor.blueprint.cut", "Cut %sx%sx%s area (%s blocks)");
        add("message.asiandecor.blueprint.empty", "Blueprint empty! Shift+Click to select area");
        add("message.asiandecor.blueprint.ready_to_cut", "Area selected! Shift+Click to CUT");
        add("message.asiandecor.blueprint.preview", "Previewing %sx%sx%s [%s]");
        add("message.asiandecor.blueprint.placed", "Placed %s blocks (%s failed)");
        add("message.asiandecor.blueprint.placed_cleared", "Placed %s blocks (%s failed) - Blueprint cleared");
        add("message.asiandecor.blueprint.rotated", "Rotation: %s | Facing: %s");

        add("message.asiandecor.tape_measure.pos1_set", "§ePosition 1 set §7at §f%s§7, §f%s§7, §f%s");
        add("message.asiandecor.tape_measure.cleared", "§cMeasurement cleared");
        add("message.asiandecor.tape_measure.measured", "§aLength §7| §f%sB  §aWidth §7| §f%sB  §aHeight §7| §f%sB  §7(§f%s§7 blocks total)");


        add("message.asiandecor.trowel.mode_changed", "§eMode: §f%s");
        add("message.asiandecor.trowel.no_blocks", "§cNo blocks available!");
        add("message.asiandecor.trowel.pouch_linked", "§aTrowel linked to Block Pouch!");





        //Keybinds
        add("key.categories.asiandecor", "Asian Decor");
        add("key.asiandecor.radial_menu", "Open Pouch Radial Menu");
        add("key.asiandecor.mode_toggle", "Toggle Between Modes");



        //Compats
        add("emi.category.asiandecor.carpenter", "Carpenter's Table");
        add("jei.category.asiandecor.carpenter", "Carpenter's Table");
        add("rei.category.asiandecor.carpenter", "Carpenter's Table");

    }
}
