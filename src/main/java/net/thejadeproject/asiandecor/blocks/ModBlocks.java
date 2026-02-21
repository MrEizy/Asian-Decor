package net.thejadeproject.asiandecor.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.custom.CarpenterBlock;
import net.thejadeproject.asiandecor.blocks.custom.furniture.tables.WingedTableBlock;
import net.thejadeproject.asiandecor.items.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCK =
            DeferredRegister.createBlocks(AsianDecor.MOD_ID);

    public static final DeferredBlock<Block> CARPENTER = registerBlock("carpenterblock",
            () -> new CarpenterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .requiresCorrectToolForDrops()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> WINGED_TABLE = registerBlock("winged_table",
            () -> new WingedTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            ));


    public static final DeferredBlock<Block> PREVIEW_BLOCK = registerBlock("preview_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(0.0F, 0.0F)
                    .sound(SoundType.AMETHYST)
                    .noLootTable()
                    .noCollission()
                    .noOcclusion()
                    .instabreak()
                    .replaceable()
            ));





    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCK.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCK.register(eventBus);
    }
}
