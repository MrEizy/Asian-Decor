package net.thejadeproject.asiandecor.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AsianDecor.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        simpleBlockWithItem(ModBlocks.PREVIEW_BLOCK, "translucent");


    }

    private void simpleBlockWithItem(DeferredBlock<?> deferredBlock, String renderType) {
        var model = models().cubeAll(
                deferredBlock.getId().getPath(),
                blockTexture(deferredBlock.get())
        ).renderType(renderType);
        simpleBlockWithItem(deferredBlock.get(), model);
    }
}
