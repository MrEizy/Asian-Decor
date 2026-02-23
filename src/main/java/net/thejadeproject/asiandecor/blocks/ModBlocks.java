package net.thejadeproject.asiandecor.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.custom.BrickMixerBlock;
import net.thejadeproject.asiandecor.blocks.custom.CarpenterBlock;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickBlock;
import net.thejadeproject.asiandecor.blocks.custom.ShapeMakerBlock;
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

    public static final DeferredBlock<Block> SHAPE_MAKER = registerBlock("shape_maker",
            () -> new ShapeMakerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> BRICK_MIXER = registerBlock("brick_mixer",
            () -> new BrickMixerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F)
                    .sound(SoundType.STONE)
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

    public static final DeferredBlock<Block> DYED_BRICK = registerBlockWithoutItem("dyed_brick",
            () -> new DyedBrickBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.STONE)));





    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCK.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCK.register(name, block);
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCK.register(eventBus);
    }
}
