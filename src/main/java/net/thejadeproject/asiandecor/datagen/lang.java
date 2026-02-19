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


        //Action Bar
        add("message.asiandecor.block_pouch.selected", "%s");
        add("message.asiandecor.block_pouch.empty", "No block selected");



        //Compats
        add("emi.category.asiandecor.carpenter", "Carpenter's Table");
        add("jei.category.asiandecor.carpenter", "Carpenter's Table");
        add("rei.category.asiandecor.carpenter", "Carpenter's Table");

    }
}
