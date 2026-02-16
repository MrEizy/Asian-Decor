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


        //Blocks
        add("block.asiandecor.carpenterblock", "Carpenter's Table");

        //GUI & Other Stuff
        add("creativetab.asiandecor.wood", "Wood Decor");


        add("container.asiandecor.carpenter", "Carpenter's Table");
        add("tooltip.asiandecor.ingredient_cost", "Requires: %s");



        //Compats
        add("emi.category.asiandecor.carpenter", "Carpenter's Table");
        add("jei.category.asiandecor.carpenter", "Carpenter's Table");
        add("rei.category.asiandecor.carpenter", "Carpenter's Table");

    }
}
