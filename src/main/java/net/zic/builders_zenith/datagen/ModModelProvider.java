package net.zic.builders_zenith.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.blocks.custom.blockz.VerticalSlabBlock;
import net.zic.builders_zenith.items.ModItems;

import java.util.Optional;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, BuildersZenith.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // ── Items ──────────────────────────────────────────────────────────────
        itemModels.generateFlatItem(ModItems.WHITE_BLOCK_POUCH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLUEPRINT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TAPE_MEASURE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TROWEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HANDHELD_FILLER.get(), ModelTemplates.FLAT_ITEM);

        // ── Simple full-cube blocks ────────────────────────────────────────────
        blockModels.createTrivialCube(ModBlocks.PREVIEW_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.CARPENTER.get());
        blockModels.createTrivialCube(ModBlocks.COLOR_MIXER.get());

        // ── Wood vertical slabs ──────────────────────────────────────────────
        registerVerticalSlab(blockModels, ModBlocks.OAK_VERTICAL_SLAB.get(), Blocks.OAK_PLANKS, "oak_planks");
        registerVerticalSlab(blockModels, ModBlocks.SPRUCE_VERTICAL_SLAB.get(), Blocks.SPRUCE_PLANKS, "spruce_planks");
        registerVerticalSlab(blockModels, ModBlocks.BIRCH_VERTICAL_SLAB.get(), Blocks.BIRCH_PLANKS, "birch_planks");
        registerVerticalSlab(blockModels, ModBlocks.JUNGLE_VERTICAL_SLAB.get(), Blocks.JUNGLE_PLANKS, "jungle_planks");
        registerVerticalSlab(blockModels, ModBlocks.ACACIA_VERTICAL_SLAB.get(), Blocks.ACACIA_PLANKS, "acacia_planks");
        registerVerticalSlab(blockModels, ModBlocks.DARK_OAK_VERTICAL_SLAB.get(), Blocks.DARK_OAK_PLANKS, "dark_oak_planks");
        registerVerticalSlab(blockModels, ModBlocks.MANGROVE_VERTICAL_SLAB.get(), Blocks.MANGROVE_PLANKS, "mangrove_planks");
        registerVerticalSlab(blockModels, ModBlocks.CHERRY_VERTICAL_SLAB.get(), Blocks.CHERRY_PLANKS, "cherry_planks");
        registerVerticalSlab(blockModels, ModBlocks.BAMBOO_VERTICAL_SLAB.get(), Blocks.BAMBOO_PLANKS, "bamboo_planks");
        registerVerticalSlab(blockModels, ModBlocks.CRIMSON_VERTICAL_SLAB.get(), Blocks.CRIMSON_PLANKS, "crimson_planks");
        registerVerticalSlab(blockModels, ModBlocks.WARPED_VERTICAL_SLAB.get(), Blocks.WARPED_PLANKS, "warped_planks");

        // ── Stone vertical slabs ─────────────────────────────────────────────
        registerVerticalSlab(blockModels, ModBlocks.STONE_VERTICAL_SLAB.get(), Blocks.STONE, "stone");
        registerVerticalSlab(blockModels, ModBlocks.MUD_BRICK_VERTICAL_SLAB.get(), Blocks.MUD_BRICKS, "mud_bricks");
        registerVerticalSlab(blockModels, ModBlocks.COBBLESTONE_VERTICAL_SLAB.get(), Blocks.COBBLESTONE, "cobblestone");
        registerVerticalSlab(blockModels, ModBlocks.MOSSY_COBBLESTONE_VERTICAL_SLAB.get(), Blocks.MOSSY_COBBLESTONE, "mossy_cobblestone");
        registerVerticalSlab(blockModels, ModBlocks.SMOOTH_STONE_VERTICAL_SLAB.get(), Blocks.SMOOTH_STONE, "smooth_stone");
        registerVerticalSlab(blockModels, ModBlocks.STONE_BRICK_VERTICAL_SLAB.get(), Blocks.STONE_BRICKS, "stone_bricks");
        registerVerticalSlab(blockModels, ModBlocks.MOSSY_STONE_BRICK_VERTICAL_SLAB.get(), Blocks.MOSSY_STONE_BRICKS, "mossy_stone_bricks");
        registerVerticalSlab(blockModels, ModBlocks.GRANITE_VERTICAL_SLAB.get(), Blocks.GRANITE, "granite");
        registerVerticalSlab(blockModels, ModBlocks.POLISHED_GRANITE_VERTICAL_SLAB.get(), Blocks.POLISHED_GRANITE, "polished_granite");
        registerVerticalSlab(blockModels, ModBlocks.DIORITE_VERTICAL_SLAB.get(), Blocks.DIORITE, "diorite");
        registerVerticalSlab(blockModels, ModBlocks.POLISHED_DIORITE_VERTICAL_SLAB.get(), Blocks.POLISHED_DIORITE, "polished_diorite");
        registerVerticalSlab(blockModels, ModBlocks.ANDESITE_VERTICAL_SLAB.get(), Blocks.ANDESITE, "andesite");
        registerVerticalSlab(blockModels, ModBlocks.POLISHED_ANDESITE_VERTICAL_SLAB.get(), Blocks.POLISHED_ANDESITE, "polished_andesite");
        registerVerticalSlab(blockModels, ModBlocks.COBBLED_DEEPSLATE_VERTICAL_SLAB.get(), Blocks.COBBLED_DEEPSLATE, "cobbled_deepslate");
        registerVerticalSlab(blockModels, ModBlocks.POLISHED_DEEPSLATE_VERTICAL_SLAB.get(), Blocks.POLISHED_DEEPSLATE, "polished_deepslate");
        registerVerticalSlab(blockModels, ModBlocks.DEEPSLATE_BRICK_VERTICAL_SLAB.get(), Blocks.DEEPSLATE_BRICKS, "deepslate_bricks");
        registerVerticalSlab(blockModels, ModBlocks.DEEPSLATE_TILE_VERTICAL_SLAB.get(), Blocks.DEEPSLATE_TILES, "deepslate_tiles");
        registerVerticalSlab(blockModels, ModBlocks.TUFF_VERTICAL_SLAB.get(), Blocks.TUFF, "tuff");
        registerVerticalSlab(blockModels, ModBlocks.POLISHED_TUFF_VERTICAL_SLAB.get(), Blocks.POLISHED_TUFF, "polished_tuff");
        registerVerticalSlab(blockModels, ModBlocks.TUFF_BRICK_VERTICAL_SLAB.get(), Blocks.TUFF_BRICKS, "tuff_bricks");
        registerVerticalSlab(blockModels, ModBlocks.BRICK_VERTICAL_SLAB.get(), Blocks.BRICKS, "bricks");
        registerVerticalSlab(blockModels, ModBlocks.SANDSTONE_VERTICAL_SLAB.get(), Blocks.SANDSTONE, "sandstone");
        registerVerticalSlab(blockModels, ModBlocks.SMOOTH_SANDSTONE_VERTICAL_SLAB.get(), Blocks.SMOOTH_SANDSTONE, "smooth_sandstone");
        registerVerticalSlab(blockModels, ModBlocks.CUT_SANDSTONE_VERTICAL_SLAB.get(), Blocks.CUT_SANDSTONE, "cut_sandstone");
        registerVerticalSlab(blockModels, ModBlocks.RED_SANDSTONE_VERTICAL_SLAB.get(), Blocks.RED_SANDSTONE, "red_sandstone");
        registerVerticalSlab(blockModels, ModBlocks.SMOOTH_RED_SANDSTONE_VERTICAL_SLAB.get(), Blocks.SMOOTH_RED_SANDSTONE, "smooth_red_sandstone");
        registerVerticalSlab(blockModels, ModBlocks.CUT_RED_SANDSTONE_VERTICAL_SLAB.get(), Blocks.CUT_RED_SANDSTONE, "cut_red_sandstone");
        registerVerticalSlab(blockModels, ModBlocks.PRISMARINE_VERTICAL_SLAB.get(), Blocks.PRISMARINE, "prismarine");
        registerVerticalSlab(blockModels, ModBlocks.PRISMARINE_BRICK_VERTICAL_SLAB.get(), Blocks.PRISMARINE_BRICKS, "prismarine_bricks");
        registerVerticalSlab(blockModels, ModBlocks.DARK_PRISMARINE_VERTICAL_SLAB.get(), Blocks.DARK_PRISMARINE, "dark_prismarine");
        registerVerticalSlab(blockModels, ModBlocks.NETHER_BRICK_VERTICAL_SLAB.get(), Blocks.NETHER_BRICKS, "nether_bricks");
        registerVerticalSlab(blockModels, ModBlocks.RED_NETHER_BRICK_VERTICAL_SLAB.get(), Blocks.RED_NETHER_BRICKS, "red_nether_bricks");
        registerVerticalSlab(blockModels, ModBlocks.BLACKSTONE_VERTICAL_SLAB.get(), Blocks.BLACKSTONE, "blackstone");
        registerVerticalSlab(blockModels, ModBlocks.POLISHED_BLACKSTONE_VERTICAL_SLAB.get(), Blocks.POLISHED_BLACKSTONE, "polished_blackstone");
        registerVerticalSlab(blockModels, ModBlocks.POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB.get(), Blocks.POLISHED_BLACKSTONE_BRICKS, "polished_blackstone_bricks");
        registerVerticalSlab(blockModels, ModBlocks.END_STONE_BRICK_VERTICAL_SLAB.get(), Blocks.END_STONE_BRICKS, "end_stone_bricks");
        registerVerticalSlab(blockModels, ModBlocks.PURPUR_VERTICAL_SLAB.get(), Blocks.PURPUR_BLOCK, "purpur_block");
        registerVerticalSlab(blockModels, ModBlocks.QUARTZ_VERTICAL_SLAB.get(), Blocks.QUARTZ_BLOCK, "quartz_block");
        registerVerticalSlab(blockModels, ModBlocks.SMOOTH_QUARTZ_VERTICAL_SLAB.get(), Blocks.SMOOTH_QUARTZ, "smooth_quartz");

        // ── Dyed bricks ──────────────────────────────────────────────────────
        for (DyedBrickType type : DyedBrickType.values()) {
            registerDyedBrickFamily(blockModels, type);
        }
    }

    // ========================================================================
    // Wood / Stone Vertical Slabs (1 model + rotation in blockstate)
    // ========================================================================

    private void registerVerticalSlab(BlockModelGenerators blockModels, Block vertSlab, Block fullBlock, String texturePath) {
        ModelTemplate template = new ModelTemplate(
                Optional.of(modLoc("block/template_vertical_slab")),
                Optional.empty(),
                TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP
        );

        Material material = new Material(TextureAtlas.LOCATION_BLOCKS);

        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.SIDE,   material)
                .put(TextureSlot.BOTTOM, material)
                .put(TextureSlot.TOP,    material);

        Identifier slabModel = template.create(
                ModelLocationUtils.getModelLocation(vertSlab),
                textures,
                blockModels.modelOutput
        );

        Identifier fullModel = ModelLocationUtils.getModelLocation(fullBlock);

        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(vertSlab)
                        .with(
                                new ConditionBuilder().term(VerticalSlabBlock.DOUBLE, true),
                                BlockModelGenerators.plainVariant(fullModel)
                        )
                        .with(
                                new ConditionBuilder()
                                        .term(VerticalSlabBlock.DOUBLE, false)
                                        .term(VerticalSlabBlock.FACING, Direction.NORTH),
                                BlockModelGenerators.plainVariant(slabModel)
                        )
                        .with(
                                new ConditionBuilder()
                                        .term(VerticalSlabBlock.DOUBLE, false)
                                        .term(VerticalSlabBlock.FACING, Direction.EAST),
                                BlockModelGenerators.plainVariant(slabModel).with(BlockModelGenerators.Y_ROT_90)
                        )
                        .with(
                                new ConditionBuilder()
                                        .term(VerticalSlabBlock.DOUBLE, false)
                                        .term(VerticalSlabBlock.FACING, Direction.SOUTH),
                                BlockModelGenerators.plainVariant(slabModel).with(BlockModelGenerators.Y_ROT_180)
                        )
                        .with(
                                new ConditionBuilder()
                                        .term(VerticalSlabBlock.DOUBLE, false)
                                        .term(VerticalSlabBlock.FACING, Direction.WEST),
                                BlockModelGenerators.plainVariant(slabModel).with(BlockModelGenerators.Y_ROT_270)
                        )
        );

        blockModels.registerSimpleItemModel(vertSlab, slabModel);
    }

    // ========================================================================
    // Dyed Brick Family (shared templates across all colors)
    // ========================================================================

    private void registerDyedBrickFamily(BlockModelGenerators blockModels, DyedBrickType type) {
        Block brick   = ModBlocks.DYED_BRICKS.get(type).get();
        Block slab    = ModBlocks.DYED_BRICK_SLABS.get(type).get();
        Block stairs  = ModBlocks.DYED_BRICK_STAIRS.get(type).get();
        Block wall    = ModBlocks.DYED_BRICK_WALLS.get(type).get();
        Block vSlab   = ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type).get();

        Identifier brickModel     = modLoc("block/template_dyed_brick");
        Identifier slabBottom     = modLoc("block/template_dyed_brick_slab");
        Identifier slabTop        = modLoc("block/template_dyed_brick_slab_top");
        Identifier stairsModel    = modLoc("block/template_dyed_brick_stairs");
        Identifier stairsInner    = modLoc("block/template_dyed_brick_stairs_inner");
        Identifier stairsOuter    = modLoc("block/template_dyed_brick_stairs_outer");
        Identifier wallPost       = modLoc("block/template_dyed_brick_wall_post");
        Identifier wallSide       = modLoc("block/template_dyed_brick_wall_side");
        Identifier wallSideTall   = modLoc("block/template_dyed_brick_wall_side_tall");
        Identifier wallInventory  = modLoc("block/template_dyed_brick_wall_inventory");
        Identifier vSlabModel     = modLoc("block/template_dyed_brick_vertical_slab");

        simpleBlock(blockModels, brick, brickModel);
        slabBlock(blockModels, slab, slabBottom, slabTop, brickModel);
        stairsBlock(blockModels, stairs, stairsModel, stairsInner, stairsOuter);
        wallBlock(blockModels, wall, wallPost, wallSide, wallSideTall, wallInventory);
        verticalSlabBlock(blockModels, vSlab, vSlabModel, brickModel);
    }

    // ========================================================================
    // Blockstate Helper Methods
    // ========================================================================

    private void simpleBlock(BlockModelGenerators blockModels, Block block, Identifier model) {
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model))
        );
        blockModels.registerSimpleItemModel(block, model);
    }

    private void slabBlock(BlockModelGenerators blockModels, Block slab, Identifier bottom, Identifier top, Identifier doubleSlab) {
        PropertyDispatch.C1<net.minecraft.client.data.models.MultiVariant, SlabType> dispatch = PropertyDispatch.initial(SlabBlock.TYPE)
                .select(SlabType.BOTTOM, BlockModelGenerators.plainVariant(bottom))
                .select(SlabType.TOP, BlockModelGenerators.plainVariant(top))
                .select(SlabType.DOUBLE, BlockModelGenerators.plainVariant(doubleSlab));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(slab).with(dispatch)
        );
        blockModels.registerSimpleItemModel(slab, bottom);
    }

    private void stairsBlock(BlockModelGenerators blockModels, Block stairs, Identifier straight, Identifier inner, Identifier outer) {
        PropertyDispatch.C3<net.minecraft.client.data.models.MultiVariant, Direction, Half, StairsShape> dispatch = PropertyDispatch
                .initial(StairBlock.FACING, StairBlock.HALF, StairBlock.SHAPE);

        // BOTTOM half - STRAIGHT
        dispatch.select(Direction.EAST,  Half.BOTTOM, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight));
        dispatch.select(Direction.WEST,  Half.BOTTOM, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight).with(BlockModelGenerators.Y_ROT_270));

        // BOTTOM half - OUTER_RIGHT
        dispatch.select(Direction.EAST,  Half.BOTTOM, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer));
        dispatch.select(Direction.WEST,  Half.BOTTOM, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.Y_ROT_270));

        // BOTTOM half - OUTER_LEFT
        dispatch.select(Direction.EAST,  Half.BOTTOM, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.Y_ROT_270));
        dispatch.select(Direction.WEST,  Half.BOTTOM, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer));

        // BOTTOM half - INNER_RIGHT
        dispatch.select(Direction.EAST,  Half.BOTTOM, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner));
        dispatch.select(Direction.WEST,  Half.BOTTOM, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.Y_ROT_270));

        // BOTTOM half - INNER_LEFT
        dispatch.select(Direction.EAST,  Half.BOTTOM, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.Y_ROT_270));
        dispatch.select(Direction.WEST,  Half.BOTTOM, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner));

        // TOP half - STRAIGHT
        dispatch.select(Direction.EAST,  Half.TOP, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.WEST,  Half.TOP, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270));
        dispatch.select(Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.NORTH, Half.TOP, StairsShape.STRAIGHT,     BlockModelGenerators.plainVariant(straight).with(BlockModelGenerators.X_ROT_180));

        // TOP half - OUTER_RIGHT
        dispatch.select(Direction.EAST,  Half.TOP, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.WEST,  Half.TOP, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270));
        dispatch.select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT,  BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180));

        // TOP half - OUTER_LEFT
        dispatch.select(Direction.EAST,  Half.TOP, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180));
        dispatch.select(Direction.WEST,  Half.TOP, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT,   BlockModelGenerators.plainVariant(outer).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270));

        // TOP half - INNER_RIGHT
        dispatch.select(Direction.EAST,  Half.TOP, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.WEST,  Half.TOP, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270));
        dispatch.select(Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT,  BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180));

        // TOP half - INNER_LEFT
        dispatch.select(Direction.EAST,  Half.TOP, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180));
        dispatch.select(Direction.WEST,  Half.TOP, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180));
        dispatch.select(Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90));
        dispatch.select(Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT,   BlockModelGenerators.plainVariant(inner).with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(stairs).with(dispatch)
        );
        blockModels.registerSimpleItemModel(stairs, straight);
    }

    private void wallBlock(BlockModelGenerators blockModels, Block wall,
                           Identifier post, Identifier side, Identifier sideTall, Identifier inventory) {
        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(wall)
                        .with(new ConditionBuilder().term(WallBlock.UP, true), BlockModelGenerators.plainVariant(post))
                        .with(new ConditionBuilder().term(WallBlock.NORTH, WallSide.LOW), BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_270))
                        .with(new ConditionBuilder().term(WallBlock.NORTH, WallSide.TALL), BlockModelGenerators.plainVariant(sideTall).with(BlockModelGenerators.Y_ROT_270))
                        .with(new ConditionBuilder().term(WallBlock.EAST, WallSide.LOW), BlockModelGenerators.plainVariant(side))
                        .with(new ConditionBuilder().term(WallBlock.EAST, WallSide.TALL), BlockModelGenerators.plainVariant(sideTall))
                        .with(new ConditionBuilder().term(WallBlock.SOUTH, WallSide.LOW), BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_90))
                        .with(new ConditionBuilder().term(WallBlock.SOUTH, WallSide.TALL), BlockModelGenerators.plainVariant(sideTall).with(BlockModelGenerators.Y_ROT_90))
                        .with(new ConditionBuilder().term(WallBlock.WEST, WallSide.LOW), BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_180))
                        .with(new ConditionBuilder().term(WallBlock.WEST, WallSide.TALL), BlockModelGenerators.plainVariant(sideTall).with(BlockModelGenerators.Y_ROT_180))
        );
        blockModels.registerSimpleItemModel(wall, inventory);
    }

    private void verticalSlabBlock(BlockModelGenerators blockModels, Block vSlab,
                                   Identifier slabModel, Identifier doubleModel) {
        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(vSlab)
                        .with(new ConditionBuilder().term(VerticalSlabBlock.DOUBLE, true), BlockModelGenerators.plainVariant(doubleModel))
                        .with(new ConditionBuilder().term(VerticalSlabBlock.DOUBLE, false).term(VerticalSlabBlock.FACING, Direction.NORTH), BlockModelGenerators.plainVariant(slabModel))
                        .with(new ConditionBuilder().term(VerticalSlabBlock.DOUBLE, false).term(VerticalSlabBlock.FACING, Direction.EAST), BlockModelGenerators.plainVariant(slabModel).with(BlockModelGenerators.Y_ROT_90))
                        .with(new ConditionBuilder().term(VerticalSlabBlock.DOUBLE, false).term(VerticalSlabBlock.FACING, Direction.SOUTH), BlockModelGenerators.plainVariant(slabModel).with(BlockModelGenerators.Y_ROT_180))
                        .with(new ConditionBuilder().term(VerticalSlabBlock.DOUBLE, false).term(VerticalSlabBlock.FACING, Direction.WEST), BlockModelGenerators.plainVariant(slabModel).with(BlockModelGenerators.Y_ROT_270))
        );
        blockModels.registerSimpleItemModel(vSlab, slabModel);
    }

    // ========================================================================
    // Helpers
    // ========================================================================

    private Identifier mcLoc(String path) {
        return Identifier.fromNamespaceAndPath("minecraft", path);
    }

    private Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(BuildersZenith.MOD_ID, path);
    }
}