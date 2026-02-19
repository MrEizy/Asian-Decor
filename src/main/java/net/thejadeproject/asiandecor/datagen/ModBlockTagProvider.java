package net.thejadeproject.asiandecor.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;

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


    }
}
