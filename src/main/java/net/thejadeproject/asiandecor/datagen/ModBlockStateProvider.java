package net.thejadeproject.asiandecor.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AsianDecor.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        simpleBlockWithItem(ModBlocks.PREVIEW_BLOCK, "translucent");

        simpleBlockWithItem(ModBlocks.SHAPE_MAKER, "full");
        simpleBlockWithItem(ModBlocks.COLOR_MIXER, "full");



        for (DyedBrickType type : DyedBrickType.values()) {
            registerDyedBrickBlock(ModBlocks.DYED_BRICKS.get(type), type);
        }

    }


    private void registerDyedBrickBlock(DeferredBlock<?> deferredBlock, DyedBrickType type) {
        String name = deferredBlock.getId().getPath();
        ModelFile model = models().withExistingParent(name, modLoc("block/dyed_brick_template"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .renderType("cutout");
        simpleBlock(deferredBlock.get(), model);
        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    private void simpleBlockWithItem(DeferredBlock<?> deferredBlock, String renderType) {
        var model = models().cubeAll(
                deferredBlock.getId().getPath(),
                blockTexture(deferredBlock.get())
        ).renderType(renderType);
        simpleBlockWithItem(deferredBlock.get(), model);
    }
}
