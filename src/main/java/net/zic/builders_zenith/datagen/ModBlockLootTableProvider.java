package net.zic.builders_zenith.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.zic.builders_zenith.blocks.ModBlocks;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.CARPENTER.get());
        dropSelf(ModBlocks.SHAPE_MAKER.get());
        dropSelf(ModBlocks.COLOR_MIXER.get());
        //dropSelf(ModBlocks.WINGED_TABLE.get());



        // Wood vertical slabs - Axe mineable
        dropSelf(ModBlocks.OAK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.SPRUCE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.BIRCH_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.JUNGLE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.ACACIA_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.DARK_OAK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.MANGROVE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.CHERRY_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.BAMBOO_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.CRIMSON_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.WARPED_VERTICAL_SLAB.get());

        // Stone vertical slabs - Pickaxe mineable
        dropSelf(ModBlocks.STONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.COBBLESTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.MOSSY_COBBLESTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.SMOOTH_STONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.STONE_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.MOSSY_STONE_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.GRANITE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.POLISHED_GRANITE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.DIORITE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.POLISHED_DIORITE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.ANDESITE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.POLISHED_ANDESITE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.COBBLED_DEEPSLATE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.POLISHED_DEEPSLATE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.DEEPSLATE_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.DEEPSLATE_TILE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.TUFF_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.POLISHED_TUFF_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.TUFF_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.MUD_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.SANDSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.SMOOTH_SANDSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.CUT_SANDSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.RED_SANDSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.SMOOTH_RED_SANDSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.CUT_RED_SANDSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.PRISMARINE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.PRISMARINE_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.DARK_PRISMARINE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.NETHER_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.RED_NETHER_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.BLACKSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.POLISHED_BLACKSTONE_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.END_STONE_BRICK_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.PURPUR_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.QUARTZ_VERTICAL_SLAB.get());
        dropSelf(ModBlocks.SMOOTH_QUARTZ_VERTICAL_SLAB.get());



        ModBlocks.DYED_BRICKS.values().forEach(blockDeferred ->
                dropSelf(blockDeferred.get())
        );
        ModBlocks.DYED_BRICK_SLABS.values().forEach(blockDeferred ->
                dropSelf(blockDeferred.get())
        );
        ModBlocks.DYED_BRICK_STAIRS.values().forEach(blockDeferred ->
                dropSelf(blockDeferred.get())
        );
        ModBlocks.DYED_BRICK_WALLS.values().forEach(blockDeferred ->
                dropSelf(blockDeferred.get())
        );
        ModBlocks.DYED_BRICK_VERTICAL_SLABS.values().forEach(blockDeferred ->
                dropSelf(blockDeferred.get())
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCK.getEntries().stream().map(Holder::value)::iterator;
    }
}