package net.zic.builders_zenith.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.custom.*;
import net.zic.builders_zenith.blocks.custom.furniture.tables.WingedTableBlock;
import net.zic.builders_zenith.items.ModItems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCK =
            DeferredRegister.createBlocks(BuildersZenith.MOD_ID);

    // Maps for all dyed brick variants
    public static final Map<DyedBrickType, DeferredBlock<Block>> DYED_BRICKS = new HashMap<>();
    public static final Map<DyedBrickType, DeferredBlock<SlabBlock>> DYED_BRICK_SLABS = new HashMap<>();
    public static final Map<DyedBrickType, DeferredBlock<StairBlock>> DYED_BRICK_STAIRS = new HashMap<>();
    public static final Map<DyedBrickType, DeferredBlock<WallBlock>> DYED_BRICK_WALLS = new HashMap<>();

    static {
        // Register all 256 dyed brick variants with custom BlockItem for tooltips
        for (DyedBrickType type : DyedBrickType.values()) {
            String baseName = type.getSerializedName();

            // Full block
            String blockName = "dyed_brick_" + baseName;
            DeferredBlock<Block> block = registerDyedBrickBlock(blockName, type);
            DYED_BRICKS.put(type, block);

            // Slab
            String slabName = "dyed_brick_slab_" + baseName;
            DeferredBlock<SlabBlock> slab = registerDyedBrickSlab(slabName, type);
            DYED_BRICK_SLABS.put(type, slab);

            // Stairs - use the full block's default state for stair properties
            String stairName = "dyed_brick_stairs_" + baseName;
            DeferredBlock<StairBlock> stairs = registerDyedBrickStairs(stairName, type, block);
            DYED_BRICK_STAIRS.put(type, stairs);

            // Wall
            String wallName = "dyed_brick_wall_" + baseName;
            DeferredBlock<WallBlock> wall = registerDyedBrickWall(wallName, type);
            DYED_BRICK_WALLS.put(type, wall);
        }
    }

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

    public static final DeferredBlock<Block> COLOR_MIXER = registerBlock("color_mixer",
            () -> new ColorMixerBlock(BlockBehaviour.Properties.of()
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

    private static DeferredBlock<Block> registerDyedBrickBlock(String name, DyedBrickType type) {
        DeferredBlock<Block> deferredBlock = BLOCK.register(name,
                () -> new Block(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(2.0F, 6.0F)
                        .sound(SoundType.STONE)));

        // Register custom BlockItem with type for tooltips
        ModItems.ITEMS.register(name, () -> new DyedBrickBlockItem(
                deferredBlock.get(),
                new Item.Properties(),
                type
        ));

        return deferredBlock;
    }

    private static DeferredBlock<SlabBlock> registerDyedBrickSlab(String name, DyedBrickType type) {
        DeferredBlock<SlabBlock> deferredBlock = BLOCK.register(name,
                () -> new SlabBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(2.0F, 6.0F)
                        .sound(SoundType.STONE)));

        // Register custom BlockItem with type for tooltips
        ModItems.ITEMS.register(name, () -> new DyedBrickBlockItem(
                deferredBlock.get(),
                new Item.Properties(),
                type
        ));

        return deferredBlock;
    }

    private static DeferredBlock<StairBlock> registerDyedBrickStairs(String name, DyedBrickType type,
                                                                     DeferredBlock<Block> baseBlock) {
        DeferredBlock<StairBlock> deferredBlock = BLOCK.register(name,
                () -> new StairBlock(baseBlock.get().defaultBlockState(),
                        BlockBehaviour.Properties.of()
                                .mapColor(MapColor.STONE)
                                .requiresCorrectToolForDrops()
                                .strength(2.0F, 6.0F)
                                .sound(SoundType.STONE)));

        // Register custom BlockItem with type for tooltips
        ModItems.ITEMS.register(name, () -> new DyedBrickBlockItem(
                deferredBlock.get(),
                new Item.Properties(),
                type
        ));

        return deferredBlock;
    }

    private static DeferredBlock<WallBlock> registerDyedBrickWall(String name, DyedBrickType type) {
        DeferredBlock<WallBlock> deferredBlock = BLOCK.register(name,
                () -> new WallBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(2.0F, 6.0F)
                        .sound(SoundType.STONE)));

        // Register custom BlockItem with type for tooltips
        ModItems.ITEMS.register(name, () -> new DyedBrickBlockItem(
                deferredBlock.get(),
                new Item.Properties(),
                type
        ));

        return deferredBlock;
    }

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