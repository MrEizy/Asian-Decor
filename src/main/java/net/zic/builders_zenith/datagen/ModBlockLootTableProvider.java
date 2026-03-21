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
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCK.getEntries().stream().map(Holder::value)::iterator;
    }
}