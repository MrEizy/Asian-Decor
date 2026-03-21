package net.zic.builders_zenith.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.items.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BuildersZenith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.WHITE_BLOCK_POUCH.get());

        basicItem(ModItems.BLUEPRINT.get());
        basicItem(ModItems.TAPE_MEASURE.get());
        basicItem(ModItems.TROWEL.get());



        basicItem(ModItems.HANDHELD_FILLER.get());
    }
}
