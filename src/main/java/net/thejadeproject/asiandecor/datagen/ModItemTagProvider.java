package net.thejadeproject.asiandecor.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;
import net.thejadeproject.asiandecor.util.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper){
        super(output, lookupProvider, blockTags, AsianDecor.MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider provider) {




        var dyedBricksBuilder = tag(ModTags.Items.DYED_BRICKS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBricksBuilder.add(ModBlocks.DYED_BRICKS.get(type).get().asItem());
        }

        tag(ModTags.Items.DYES)
                .add(Items.WHITE_DYE)
                .add(Items.ORANGE_DYE)
                .add(Items.MAGENTA_DYE)
                .add(Items.LIGHT_BLUE_DYE)
                .add(Items.YELLOW_DYE)
                .add(Items.LIME_DYE)
                .add(Items.PINK_DYE)
                .add(Items.GRAY_DYE)
                .add(Items.LIGHT_GRAY_DYE)
                .add(Items.CYAN_DYE)
                .add(Items.PURPLE_DYE)
                .add(Items.BLUE_DYE)
                .add(Items.BROWN_DYE)
                .add(Items.GREEN_DYE)
                .add(Items.RED_DYE)
                .add(Items.BLACK_DYE);
    }
}