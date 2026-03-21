package net.zic.builders_zenith.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.util.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BuildersZenith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.CARPENTER.get())
                //.add(ModBlocks.WINGED_TABLE.get())
        ;

        // Pickaxe mineable for all dyed brick variants
        var pickaxeBuilder = tag(BlockTags.MINEABLE_WITH_PICKAXE);

        ModBlocks.DYED_BRICKS.values().forEach(blockDeferred ->
                pickaxeBuilder.add(blockDeferred.get())
        );
        ModBlocks.DYED_BRICK_SLABS.values().forEach(blockDeferred ->
                pickaxeBuilder.add(blockDeferred.get())
        );
        ModBlocks.DYED_BRICK_STAIRS.values().forEach(blockDeferred ->
                pickaxeBuilder.add(blockDeferred.get())
        );
        ModBlocks.DYED_BRICK_WALLS.values().forEach(blockDeferred ->
                pickaxeBuilder.add(blockDeferred.get())
        );

        // Dyed brick blocks tag (full blocks only)
        var dyedBrickBlocksBuilder = tag(ModTags.Blocks.DYED_BRICK_BLOCKS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickBlocksBuilder.add(ModBlocks.DYED_BRICKS.get(type).get());
        }

        // Dyed brick slabs tag
        var dyedBrickSlabsBuilder = tag(ModTags.Blocks.DYED_BRICK_SLABS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickSlabsBuilder.add(ModBlocks.DYED_BRICK_SLABS.get(type).get());
        }

        // Dyed brick stairs tag
        var dyedBrickStairsBuilder = tag(ModTags.Blocks.DYED_BRICK_STAIRS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickStairsBuilder.add(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
        }

        // Dyed brick walls tag
        var dyedBrickWallsBuilder = tag(ModTags.Blocks.DYED_BRICK_WALLS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickWallsBuilder.add(ModBlocks.DYED_BRICK_WALLS.get(type).get());
        }

        // Vanilla slab tag
        var slabBuilder = tag(BlockTags.SLABS);
        for (DyedBrickType type : DyedBrickType.values()) {
            slabBuilder.add(ModBlocks.DYED_BRICK_SLABS.get(type).get());
        }

        // Vanilla stairs tag
        var stairsBuilder = tag(BlockTags.STAIRS);
        for (DyedBrickType type : DyedBrickType.values()) {
            stairsBuilder.add(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
        }

        // Vanilla walls tag
        var wallsBuilder = tag(BlockTags.WALLS);
        for (DyedBrickType type : DyedBrickType.values()) {
            wallsBuilder.add(ModBlocks.DYED_BRICK_WALLS.get(type).get());
        }

        // Wall posts (for wall connection behavior)
        var wallPostsBuilder = tag(BlockTags.WALL_POST_OVERRIDE);
        for (DyedBrickType type : DyedBrickType.values()) {
            wallPostsBuilder.add(ModBlocks.DYED_BRICK_WALLS.get(type).get());
        }
    }
}