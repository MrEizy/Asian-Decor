package net.thejadeproject.asiandecor.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.items.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AsianDecor.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.WHITE_BLOCK_POUCH.get());

        basicItem(ModItems.BLUEPRINT.get());
        basicItem(ModItems.TAPE_MEASURE.get());
        basicItem(ModItems.TROWEL.get());
    }
}
