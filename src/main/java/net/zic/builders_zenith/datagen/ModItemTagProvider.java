package net.zic.builders_zenith.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends net.neoforged.neoforge.common.data.ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookupProvider, BuildersZenith.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var dyedBricksBuilder = tag(ModTags.Items.DYED_BRICKS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBricksBuilder.add(ModBlocks.DYED_BRICKS.get(type).get().asItem());
        }

        var dyedBrickSlabsBuilder = tag(ModTags.Items.DYED_BRICK_SLABS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickSlabsBuilder.add(ModBlocks.DYED_BRICK_SLABS.get(type).get().asItem());
        }

        var dyedBrickStairsBuilder = tag(ModTags.Items.DYED_BRICK_STAIRS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickStairsBuilder.add(ModBlocks.DYED_BRICK_STAIRS.get(type).get().asItem());
        }

        var dyedBrickWallsBuilder = tag(ModTags.Items.DYED_BRICK_WALLS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickWallsBuilder.add(ModBlocks.DYED_BRICK_WALLS.get(type).get().asItem());
        }

        var dyedBrickVerticalSlabsBuilder = tag(ModTags.Items.DYED_BRICK_VERTICAL_SLABS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickVerticalSlabsBuilder.add(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type).get().asItem());
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