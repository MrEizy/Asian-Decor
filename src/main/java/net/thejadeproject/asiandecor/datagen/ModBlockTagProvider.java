package net.thejadeproject.asiandecor.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;
import net.thejadeproject.asiandecor.util.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AsianDecor.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.CARPENTER.get())
                .add(ModBlocks.WINGED_TABLE.get());

        var pickaxeBuilder = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        ModBlocks.DYED_BRICKS.values().forEach(blockDeferred ->
                pickaxeBuilder.add(blockDeferred.get())
        );

        var dyedBrickBlocksBuilder = tag(ModTags.Blocks.DYED_BRICK_BLOCKS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickBlocksBuilder.add(ModBlocks.DYED_BRICKS.get(type).get());
        }
    }
}
