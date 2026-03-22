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
import net.zic.builders_zenith.blocks.custom.blockz.VerticalSlabBlock;
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
    public static final Map<DyedBrickType, DeferredBlock<VerticalSlabBlock>> DYED_BRICK_VERTICAL_SLABS = new HashMap<>();
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

            // Add vertical slabs
            String verticalSlabName = "dyed_brick_vertical_slab_" + baseName;
            DeferredBlock<VerticalSlabBlock> verticalSlab = registerDyedBrickVerticalSlab(verticalSlabName, type);
            DYED_BRICK_VERTICAL_SLABS.put(type, verticalSlab);
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

    /*public static final DeferredBlock<Block> WINGED_TABLE = registerBlock("winged_table",
            () -> new WingedTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .requiresCorrectToolForDrops()
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            ));*/

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


    //VerticalSlabs
//Wood Vert Slabs
    public static final DeferredBlock<Block> OAK_VERTICAL_SLAB = registerBlock("oak_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredBlock<Block> SPRUCE_VERTICAL_SLAB = registerBlock("spruce_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SLAB)));
    public static final DeferredBlock<Block> BIRCH_VERTICAL_SLAB = registerBlock("birch_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_SLAB)));
    public static final DeferredBlock<Block> JUNGLE_VERTICAL_SLAB = registerBlock("jungle_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_SLAB)));
    public static final DeferredBlock<Block> ACACIA_VERTICAL_SLAB = registerBlock("acacia_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SLAB)));
    public static final DeferredBlock<Block> DARK_OAK_VERTICAL_SLAB = registerBlock("dark_oak_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_SLAB)));
    public static final DeferredBlock<Block> MANGROVE_VERTICAL_SLAB = registerBlock("mangrove_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_SLAB)));
    public static final DeferredBlock<Block> CHERRY_VERTICAL_SLAB = registerBlock("cherry_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SLAB)));
    public static final DeferredBlock<Block> BAMBOO_VERTICAL_SLAB = registerBlock("bamboo_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_SLAB)));
    public static final DeferredBlock<Block> CRIMSON_VERTICAL_SLAB = registerBlock("crimson_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_SLAB)));
    public static final DeferredBlock<Block> WARPED_VERTICAL_SLAB = registerBlock("warped_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SLAB)));

//Stone Vert Slabs
    public static final DeferredBlock<Block> STONE_VERTICAL_SLAB = registerBlock("stone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_SLAB)));
    public static final DeferredBlock<Block> COBBLESTONE_VERTICAL_SLAB = registerBlock("cobblestone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_SLAB)));
    public static final DeferredBlock<Block> MOSSY_COBBLESTONE_VERTICAL_SLAB = registerBlock("mossy_cobblestone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE_SLAB)));
    public static final DeferredBlock<Block> SMOOTH_STONE_VERTICAL_SLAB = registerBlock("smooth_stone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE_SLAB)));
    public static final DeferredBlock<Block> STONE_BRICK_VERTICAL_SLAB = registerBlock("stone_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB)));
    public static final DeferredBlock<Block> MOSSY_STONE_BRICK_VERTICAL_SLAB = registerBlock("mossy_stone_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_STONE_BRICK_SLAB)));
    public static final DeferredBlock<Block> GRANITE_VERTICAL_SLAB = registerBlock("granite_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE_SLAB)));
    public static final DeferredBlock<Block> POLISHED_GRANITE_VERTICAL_SLAB = registerBlock("polished_granite_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE_SLAB)));
    public static final DeferredBlock<Block> DIORITE_VERTICAL_SLAB = registerBlock("diorite_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE_SLAB)));
    public static final DeferredBlock<Block> POLISHED_DIORITE_VERTICAL_SLAB = registerBlock("polished_diorite_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE_SLAB)));
    public static final DeferredBlock<Block> ANDESITE_VERTICAL_SLAB = registerBlock("andesite_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE_SLAB)));
    public static final DeferredBlock<Block> POLISHED_ANDESITE_VERTICAL_SLAB = registerBlock("polished_andesite_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE_SLAB)));
    public static final DeferredBlock<Block> COBBLED_DEEPSLATE_VERTICAL_SLAB = registerBlock("cobbled_deepslate_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final DeferredBlock<Block> POLISHED_DEEPSLATE_VERTICAL_SLAB = registerBlock("polished_deepslate_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE_SLAB)));
    public static final DeferredBlock<Block> DEEPSLATE_BRICK_VERTICAL_SLAB = registerBlock("deepslate_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_SLAB)));
    public static final DeferredBlock<Block> DEEPSLATE_TILE_VERTICAL_SLAB = registerBlock("deepslate_tile_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILE_SLAB)));
    public static final DeferredBlock<Block> TUFF_VERTICAL_SLAB = registerBlock("tuff_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_SLAB)));
    public static final DeferredBlock<Block> POLISHED_TUFF_VERTICAL_SLAB = registerBlock("polished_tuff_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_TUFF_SLAB)));
    public static final DeferredBlock<Block> TUFF_BRICK_VERTICAL_SLAB = registerBlock("tuff_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICK_SLAB)));
    public static final DeferredBlock<Block> BRICK_VERTICAL_SLAB = registerBlock("brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_SLAB)));
    public static final DeferredBlock<Block> MUD_BRICK_VERTICAL_SLAB = registerBlock("mud_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUD_BRICK_SLAB)));
    public static final DeferredBlock<Block> SANDSTONE_VERTICAL_SLAB = registerBlock("sandstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));
    public static final DeferredBlock<Block> SMOOTH_SANDSTONE_VERTICAL_SLAB = registerBlock("smooth_sandstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_SANDSTONE_SLAB)));
    public static final DeferredBlock<Block> CUT_SANDSTONE_VERTICAL_SLAB = registerBlock("cut_sandstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_SANDSTONE_SLAB)));
    public static final DeferredBlock<Block> RED_SANDSTONE_VERTICAL_SLAB = registerBlock("red_sandstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE_SLAB)));
    public static final DeferredBlock<Block> SMOOTH_RED_SANDSTONE_VERTICAL_SLAB = registerBlock("smooth_red_sandstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_RED_SANDSTONE_SLAB)));
    public static final DeferredBlock<Block> CUT_RED_SANDSTONE_VERTICAL_SLAB = registerBlock("cut_red_sandstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_RED_SANDSTONE_SLAB)));
    public static final DeferredBlock<Block> PRISMARINE_VERTICAL_SLAB = registerBlock("prismarine_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE_SLAB)));
    public static final DeferredBlock<Block> PRISMARINE_BRICK_VERTICAL_SLAB = registerBlock("prismarine_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE_BRICK_SLAB)));
    public static final DeferredBlock<Block> DARK_PRISMARINE_VERTICAL_SLAB = registerBlock("dark_prismarine_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_PRISMARINE_SLAB)));
    public static final DeferredBlock<Block> NETHER_BRICK_VERTICAL_SLAB = registerBlock("nether_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICK_SLAB)));
    public static final DeferredBlock<Block> RED_NETHER_BRICK_VERTICAL_SLAB = registerBlock("red_nether_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICK_SLAB)));
    public static final DeferredBlock<Block> BLACKSTONE_VERTICAL_SLAB = registerBlock("blackstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE_SLAB)));
    public static final DeferredBlock<Block> POLISHED_BLACKSTONE_VERTICAL_SLAB = registerBlock("polished_blackstone_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE_SLAB)));
    public static final DeferredBlock<Block> POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB = registerBlock("polished_blackstone_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB)));
    public static final DeferredBlock<Block> END_STONE_BRICK_VERTICAL_SLAB = registerBlock("end_stone_brick_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICK_SLAB)));

//Other Blocks
    public static final DeferredBlock<Block> PURPUR_VERTICAL_SLAB = registerBlock("purpur_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_SLAB)));
    public static final DeferredBlock<Block> QUARTZ_VERTICAL_SLAB = registerBlock("quartz_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_SLAB)));
    public static final DeferredBlock<Block> SMOOTH_QUARTZ_VERTICAL_SLAB = registerBlock("smooth_quartz_vertical_slab",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ_SLAB)));



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

    private static DeferredBlock<VerticalSlabBlock> registerDyedBrickVerticalSlab(String name, DyedBrickType type) {
        DeferredBlock<VerticalSlabBlock> deferredBlock = BLOCK.register(name,
                () -> new VerticalSlabBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(2.0F, 6.0F)
                        .sound(SoundType.STONE)));

        // Register custom BlockItem
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