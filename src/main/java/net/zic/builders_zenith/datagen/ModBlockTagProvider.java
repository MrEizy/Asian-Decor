package net.zic.builders_zenith.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BuildersZenith.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.CARPENTER.get())
                .add(ModBlocks.OAK_VERTICAL_SLAB.get())
                .add(ModBlocks.SPRUCE_VERTICAL_SLAB.get())
                .add(ModBlocks.BIRCH_VERTICAL_SLAB.get())
                .add(ModBlocks.JUNGLE_VERTICAL_SLAB.get())
                .add(ModBlocks.ACACIA_VERTICAL_SLAB.get())
                .add(ModBlocks.DARK_OAK_VERTICAL_SLAB.get())
                .add(ModBlocks.MANGROVE_VERTICAL_SLAB.get())
                .add(ModBlocks.CHERRY_VERTICAL_SLAB.get())
                .add(ModBlocks.BAMBOO_VERTICAL_SLAB.get())
                .add(ModBlocks.CRIMSON_VERTICAL_SLAB.get())
                .add(ModBlocks.WARPED_VERTICAL_SLAB.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.STONE_VERTICAL_SLAB.get())
                .add(ModBlocks.MUD_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.COBBLESTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.MOSSY_COBBLESTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.SMOOTH_STONE_VERTICAL_SLAB.get())
                .add(ModBlocks.STONE_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.MOSSY_STONE_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.GRANITE_VERTICAL_SLAB.get())
                .add(ModBlocks.POLISHED_GRANITE_VERTICAL_SLAB.get())
                .add(ModBlocks.DIORITE_VERTICAL_SLAB.get())
                .add(ModBlocks.POLISHED_DIORITE_VERTICAL_SLAB.get())
                .add(ModBlocks.ANDESITE_VERTICAL_SLAB.get())
                .add(ModBlocks.POLISHED_ANDESITE_VERTICAL_SLAB.get())
                .add(ModBlocks.COBBLED_DEEPSLATE_VERTICAL_SLAB.get())
                .add(ModBlocks.POLISHED_DEEPSLATE_VERTICAL_SLAB.get())
                .add(ModBlocks.DEEPSLATE_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.DEEPSLATE_TILE_VERTICAL_SLAB.get())
                .add(ModBlocks.TUFF_VERTICAL_SLAB.get())
                .add(ModBlocks.POLISHED_TUFF_VERTICAL_SLAB.get())
                .add(ModBlocks.TUFF_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.SANDSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.SMOOTH_SANDSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.CUT_SANDSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.RED_SANDSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.SMOOTH_RED_SANDSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.CUT_RED_SANDSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.PRISMARINE_VERTICAL_SLAB.get())
                .add(ModBlocks.PRISMARINE_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.DARK_PRISMARINE_VERTICAL_SLAB.get())
                .add(ModBlocks.NETHER_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.RED_NETHER_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.BLACKSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.POLISHED_BLACKSTONE_VERTICAL_SLAB.get())
                .add(ModBlocks.POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.END_STONE_BRICK_VERTICAL_SLAB.get())
                .add(ModBlocks.PURPUR_VERTICAL_SLAB.get())
                .add(ModBlocks.QUARTZ_VERTICAL_SLAB.get())
                .add(ModBlocks.SMOOTH_QUARTZ_VERTICAL_SLAB.get());

        var pickaxeBuilder = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        ModBlocks.DYED_BRICKS.values().forEach(b -> pickaxeBuilder.add(b.get()));
        ModBlocks.DYED_BRICK_SLABS.values().forEach(b -> pickaxeBuilder.add(b.get()));
        ModBlocks.DYED_BRICK_STAIRS.values().forEach(b -> pickaxeBuilder.add(b.get()));
        ModBlocks.DYED_BRICK_WALLS.values().forEach(b -> pickaxeBuilder.add(b.get()));
        ModBlocks.DYED_BRICK_VERTICAL_SLABS.values().forEach(b -> pickaxeBuilder.add(b.get()));

        var dyedBrickBlocksBuilder = tag(ModTags.Blocks.DYED_BRICK_BLOCKS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickBlocksBuilder.add(ModBlocks.DYED_BRICKS.get(type).get());
        }

        var dyedBrickVerticalSlabsBuilder = tag(ModTags.Blocks.DYED_BRICK_VERTICAL_SLABS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickVerticalSlabsBuilder.add(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type).get());
        }

        var dyedBrickSlabsBuilder = tag(ModTags.Blocks.DYED_BRICK_SLABS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickSlabsBuilder.add(ModBlocks.DYED_BRICK_SLABS.get(type).get());
        }

        var dyedBrickStairsBuilder = tag(ModTags.Blocks.DYED_BRICK_STAIRS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickStairsBuilder.add(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
        }

        var dyedBrickWallsBuilder = tag(ModTags.Blocks.DYED_BRICK_WALLS);
        for (DyedBrickType type : DyedBrickType.values()) {
            dyedBrickWallsBuilder.add(ModBlocks.DYED_BRICK_WALLS.get(type).get());
        }

        var slabBuilder = tag(BlockTags.SLABS);
        for (DyedBrickType type : DyedBrickType.values()) {
            slabBuilder.add(ModBlocks.DYED_BRICK_SLABS.get(type).get());
        }

        var stairsBuilder = tag(BlockTags.STAIRS);
        for (DyedBrickType type : DyedBrickType.values()) {
            stairsBuilder.add(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
        }

        var wallsBuilder = tag(BlockTags.WALLS);
        for (DyedBrickType type : DyedBrickType.values()) {
            wallsBuilder.add(ModBlocks.DYED_BRICK_WALLS.get(type).get());
        }

        var wallPostsBuilder = tag(BlockTags.WALL_POST_OVERRIDE);
        for (DyedBrickType type : DyedBrickType.values()) {
            wallPostsBuilder.add(ModBlocks.DYED_BRICK_WALLS.get(type).get());
        }
    }
}