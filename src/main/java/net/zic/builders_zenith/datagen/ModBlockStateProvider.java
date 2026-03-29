package net.zic.builders_zenith.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.blocks.custom.blockz.VerticalSlabBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BuildersZenith.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.PREVIEW_BLOCK, "translucent");


        simpleBlockWithItem(ModBlocks.SHAPE_MAKER, "full");
        simpleBlockWithItem(ModBlocks.COLOR_MIXER, "full");


        // Wood vertical slabs
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.OAK_VERTICAL_SLAB.get(), mcLoc("block/oak_planks"), mcLoc("block/oak_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.SPRUCE_VERTICAL_SLAB.get(), mcLoc("block/spruce_planks"), mcLoc("block/spruce_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.BIRCH_VERTICAL_SLAB.get(), mcLoc("block/birch_planks"), mcLoc("block/birch_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.JUNGLE_VERTICAL_SLAB.get(), mcLoc("block/jungle_planks"), mcLoc("block/jungle_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.ACACIA_VERTICAL_SLAB.get(), mcLoc("block/acacia_planks"), mcLoc("block/acacia_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.DARK_OAK_VERTICAL_SLAB.get(), mcLoc("block/dark_oak_planks"), mcLoc("block/dark_oak_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.MANGROVE_VERTICAL_SLAB.get(), mcLoc("block/mangrove_planks"), mcLoc("block/mangrove_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.CHERRY_VERTICAL_SLAB.get(), mcLoc("block/cherry_planks"), mcLoc("block/cherry_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.BAMBOO_VERTICAL_SLAB.get(), mcLoc("block/bamboo_planks"), mcLoc("block/bamboo_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.CRIMSON_VERTICAL_SLAB.get(), mcLoc("block/crimson_planks"), mcLoc("block/crimson_planks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.WARPED_VERTICAL_SLAB.get(), mcLoc("block/warped_planks"), mcLoc("block/warped_planks"));

        // Stone vertical slabs
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.STONE_VERTICAL_SLAB.get(), mcLoc("block/stone"), mcLoc("block/stone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.MUD_BRICK_VERTICAL_SLAB.get(), mcLoc("block/mud_bricks"), mcLoc("block/mud_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.COBBLESTONE_VERTICAL_SLAB.get(), mcLoc("block/cobblestone"), mcLoc("block/cobblestone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.MOSSY_COBBLESTONE_VERTICAL_SLAB.get(), mcLoc("block/mossy_cobblestone"), mcLoc("block/mossy_cobblestone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.SMOOTH_STONE_VERTICAL_SLAB.get(), mcLoc("block/smooth_stone"), mcLoc("block/smooth_stone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.STONE_BRICK_VERTICAL_SLAB.get(), mcLoc("block/stone_bricks"), mcLoc("block/stone_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.MOSSY_STONE_BRICK_VERTICAL_SLAB.get(), mcLoc("block/mossy_stone_bricks"), mcLoc("block/mossy_stone_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.GRANITE_VERTICAL_SLAB.get(), mcLoc("block/granite"), mcLoc("block/granite"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.POLISHED_GRANITE_VERTICAL_SLAB.get(), mcLoc("block/polished_granite"), mcLoc("block/polished_granite"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.DIORITE_VERTICAL_SLAB.get(), mcLoc("block/diorite"), mcLoc("block/diorite"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.POLISHED_DIORITE_VERTICAL_SLAB.get(), mcLoc("block/polished_diorite"), mcLoc("block/polished_diorite"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.ANDESITE_VERTICAL_SLAB.get(), mcLoc("block/andesite"), mcLoc("block/andesite"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.POLISHED_ANDESITE_VERTICAL_SLAB.get(), mcLoc("block/polished_andesite"), mcLoc("block/polished_andesite"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.COBBLED_DEEPSLATE_VERTICAL_SLAB.get(), mcLoc("block/cobbled_deepslate"), mcLoc("block/cobbled_deepslate"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.POLISHED_DEEPSLATE_VERTICAL_SLAB.get(), mcLoc("block/polished_deepslate"), mcLoc("block/polished_deepslate"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.DEEPSLATE_BRICK_VERTICAL_SLAB.get(), mcLoc("block/deepslate_bricks"), mcLoc("block/deepslate_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.DEEPSLATE_TILE_VERTICAL_SLAB.get(), mcLoc("block/deepslate_tiles"), mcLoc("block/deepslate_tiles"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.TUFF_VERTICAL_SLAB.get(), mcLoc("block/tuff"), mcLoc("block/tuff"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.POLISHED_TUFF_VERTICAL_SLAB.get(), mcLoc("block/polished_tuff"), mcLoc("block/polished_tuff"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.TUFF_BRICK_VERTICAL_SLAB.get(), mcLoc("block/tuff_bricks"), mcLoc("block/tuff_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.BRICK_VERTICAL_SLAB.get(), mcLoc("block/bricks"), mcLoc("block/bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.SANDSTONE_VERTICAL_SLAB.get(), mcLoc("block/sandstone"), mcLoc("block/sandstone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.SMOOTH_SANDSTONE_VERTICAL_SLAB.get(), mcLoc("block/smooth_sandstone"), mcLoc("block/sandstone_top"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.CUT_SANDSTONE_VERTICAL_SLAB.get(), mcLoc("block/cut_sandstone"), mcLoc("block/cut_sandstone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.RED_SANDSTONE_VERTICAL_SLAB.get(), mcLoc("block/red_sandstone"), mcLoc("block/red_sandstone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.SMOOTH_RED_SANDSTONE_VERTICAL_SLAB.get(), mcLoc("block/red_sandstone"), mcLoc("block/red_sandstone_top"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.CUT_RED_SANDSTONE_VERTICAL_SLAB.get(), mcLoc("block/cut_red_sandstone"), mcLoc("block/cut_red_sandstone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.PRISMARINE_VERTICAL_SLAB.get(), mcLoc("block/prismarine"), mcLoc("block/prismarine"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.PRISMARINE_BRICK_VERTICAL_SLAB.get(), mcLoc("block/prismarine_bricks"), mcLoc("block/prismarine_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.DARK_PRISMARINE_VERTICAL_SLAB.get(), mcLoc("block/dark_prismarine"), mcLoc("block/dark_prismarine"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.NETHER_BRICK_VERTICAL_SLAB.get(), mcLoc("block/nether_bricks"), mcLoc("block/nether_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.RED_NETHER_BRICK_VERTICAL_SLAB.get(), mcLoc("block/red_nether_bricks"), mcLoc("block/red_nether_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.BLACKSTONE_VERTICAL_SLAB.get(), mcLoc("block/blackstone"), mcLoc("block/blackstone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.POLISHED_BLACKSTONE_VERTICAL_SLAB.get(), mcLoc("block/polished_blackstone"), mcLoc("block/polished_blackstone"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB.get(), mcLoc("block/polished_blackstone_bricks"), mcLoc("block/polished_blackstone_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.END_STONE_BRICK_VERTICAL_SLAB.get(), mcLoc("block/end_stone_bricks"), mcLoc("block/end_stone_bricks"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.PURPUR_VERTICAL_SLAB.get(), mcLoc("block/purpur_block"), mcLoc("block/purpur_block"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.QUARTZ_VERTICAL_SLAB.get(), mcLoc("block/quartz_block"), mcLoc("block/quartz_block_side"));
        verticalSlabBlock((VerticalSlabBlock) ModBlocks.SMOOTH_QUARTZ_VERTICAL_SLAB.get(), mcLoc("block/quartz_block"), mcLoc("block/quartz_block_bottom"));




        for (DyedBrickType type : DyedBrickType.values()) {
            DeferredBlock<?> block = ModBlocks.DYED_BRICKS.get(type);
            DeferredBlock<?> slab = ModBlocks.DYED_BRICK_SLABS.get(type);
            DeferredBlock<?> verticalSlab = ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type);
            DeferredBlock<?> stairs = ModBlocks.DYED_BRICK_STAIRS.get(type);
            DeferredBlock<?> wall = ModBlocks.DYED_BRICK_WALLS.get(type);

            registerDyedBrickBlock(block, type);
            registerDyedBrickSlab(slab, block);
            registerDyedBrickVerticalSlab(verticalSlab, block);
            registerDyedBrickStairs(stairs, block);
            registerDyedBrickWall(wall, block);
        }
    }

    private void registerDyedBrickBlock(DeferredBlock<?> deferredBlock, DyedBrickType type) {
        String name = deferredBlock.getId().getPath();

        var model = models().getBuilder(name)
                .parent(models().getExistingFile(mcLoc("block/block")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .element()
                .from(0, 0, 0).to(16, 16, 16)
                .allFaces((dir, face) -> face
                        .texture("#brick_base")
                        .tintindex(0)
                        .cullface(dir))
                .end()
                .element()
                .from(0, 0, 0).to(16, 16, 16)
                .allFaces((dir, face) -> face
                        .texture("#mortar_overlay")
                        .tintindex(1)
                        .cullface(dir))
                .end();

        simpleBlock(deferredBlock.get(), model);
        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    private void registerDyedBrickSlab(DeferredBlock<?> slabBlock, DeferredBlock<?> baseBlock) {
        String name = slabBlock.getId().getPath();
        String baseName = baseBlock.getId().getPath();

        var slabBottom = models().getBuilder(name)
                .parent(models().getExistingFile(mcLoc("block/slab")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("bottom", modLoc("block/brick_base"))
                .texture("top", modLoc("block/brick_base"))
                .texture("side", modLoc("block/brick_base"))
                // Brick base element
                .element()
                .from(0, 0, 0).to(16, 8, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end()
                // Mortar overlay element - ADDED DOWN FACE
                .element()
                .from(0, 0, 0).to(16, 8, 16)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).end()  // ADD THIS
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end();

        var slabTop = models().getBuilder(name + "_top")
                .parent(models().getExistingFile(mcLoc("block/slab_top")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("bottom", modLoc("block/brick_base"))
                .texture("top", modLoc("block/brick_base"))
                .texture("side", modLoc("block/brick_base"))
                // Brick base element
                .element()
                .from(0, 8, 0).to(16, 16, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 0, 16, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 0, 16, 8).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                .end()
                // Mortar overlay element - ADDED UP FACE
                .element()
                .from(0, 8, 0).to(16, 16, 16)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).end()  // ADD THIS
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 0, 16, 8).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 0, 16, 8).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                .end();

        ModelFile doubleSlab = models().getExistingFile(modLoc("block/" + baseName));

        slabBlock((net.minecraft.world.level.block.SlabBlock) slabBlock.get(), slabBottom, slabTop, doubleSlab);

        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    private void registerDyedBrickStairs(DeferredBlock<?> stairBlock, DeferredBlock<?> baseBlock) {
        String name = stairBlock.getId().getPath();

            // REGULAR STAIRS - Proper UV mapping matching your template
            var stairs = models().getBuilder(name)
                    .parent(models().getExistingFile(mcLoc("block/stairs")))
                    .renderType("cutout")
                    .texture("particle", modLoc("block/brick_base"))
                    .texture("brick_base", modLoc("block/brick_base"))
                    .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                    .texture("bottom", modLoc("block/brick_base"))
                    .texture("top", modLoc("block/brick_base"))
                    .texture("side", modLoc("block/brick_base"))
                    // Bottom step (full 16x16x8)
                    .element().from(0, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).uvs(0, 0, 16, 16).end()
                    .face(Direction.UP).texture("#brick_base").tintindex(0).uvs(0, 0, 16, 16).end()
                    .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                    .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                    .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                    .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                    .end()
                    // Bottom step mortar
                    .element().from(0, 0, 0).to(16, 8, 16)
                    .face(Direction.UP).texture("#mortar_overlay").tintindex(1).uvs(0, 0, 16, 16).end()
                    .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                    .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                    .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                    .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                    .end()
                    // Top step - RIGHT side
                    .element().from(8, 8, 0).to(16, 16, 16)
                    .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(8, 0, 16, 16).end()
                    .face(Direction.DOWN).texture("#brick_base").tintindex(0).uvs(8, 0, 16, 16).end()
                    .face(Direction.NORTH).texture("#brick_base").tintindex(0).uvs(8, 0, 16, 8).end()
                    .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                    .face(Direction.WEST).texture("#brick_base").tintindex(0).uvs(0, 0, 16, 8).end()
                    .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                    .end()
                    // Bottom step mortar
                    .element().from(0, 0, 0).to(16, 8, 16)
                    .face(Direction.UP).texture("#mortar_overlay").tintindex(1).uvs(0, 0, 16, 16).end()
                    .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).end()  // ADD THIS
                    .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                    .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                    .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                    .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                    .end()
                    // Mortar layer - same UVs
                    .element().from(8, 8, 0).to(16, 16, 16)
                    .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(8, 0, 16, 16).end()
                    .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).uvs(8, 0, 16, 8).end()
                    .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                    .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).uvs(0, 0, 16, 8).end()
                    .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                    .end();

        // INNER STAIRS
        var stairsInner = models().getBuilder(name + "_inner")
                .parent(models().getExistingFile(mcLoc("block/inner_stairs")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("bottom", modLoc("block/brick_base"))
                .texture("top", modLoc("block/brick_base"))
                .texture("side", modLoc("block/brick_base"))
                // Bottom step
                .element().from(0, 0, 0).to(16, 8, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end()
                // Bottom step mortar
                .element().from(0, 0, 0).to(16, 8, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end()
                // Top step 1
                .element().from(8, 8, 0).to(16, 16, 16)
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).end()
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(8, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                .end()
                // Top step 1 mortar
                .element().from(8, 8, 0).to(16, 16, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(8, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                .end()
                // Top step 2
                .element().from(0, 8, 8).to(8, 16, 16)
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).end()
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 0, 8, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(8, 0, 16, 8).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).end()
                .end()
                // Top step 2 mortar
                .element().from(0, 8, 8).to(8, 16, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 0, 8, 8).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(8, 0, 16, 8).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).end()
                .end();

        // OUTER STAIRS
        var stairsOuter = models().getBuilder(name + "_outer")
                .parent(models().getExistingFile(mcLoc("block/outer_stairs")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("bottom", modLoc("block/brick_base"))
                .texture("top", modLoc("block/brick_base"))
                .texture("side", modLoc("block/brick_base"))
                // Bottom step
                .element().from(0, 0, 0).to(16, 8, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end()
                // Bottom step mortar
                .element().from(0, 0, 0).to(16, 8, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end()
                // Top step
                .element().from(8, 8, 8).to(16, 16, 16)
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).end()
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(8, 0, 16, 8).end()
                .end()
                // Top step mortar
                .element().from(8, 8, 8).to(16, 16, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(8, 0, 16, 8).end()
                .end();

        stairsBlock((net.minecraft.world.level.block.StairBlock) stairBlock.get(), stairs, stairsInner, stairsOuter);

        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    private void registerDyedBrickWall(DeferredBlock<?> wallBlock, DeferredBlock<?> baseBlock) {
        String name = wallBlock.getId().getPath();

        var post = models().getBuilder(name + "_post")
                .parent(models().getExistingFile(mcLoc("block/template_wall_post")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("wall", modLoc("block/brick_base"))
                .element().from(4, 0, 4).to(12, 16, 12)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).end()
                .end()
                .element().from(4, 0, 4).to(12, 16, 12)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).end()
                .end();

        var side = models().getBuilder(name + "_side")
                .parent(models().getExistingFile(mcLoc("block/template_wall_side")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("wall", modLoc("block/brick_base"))
                .element().from(5, 0, 0).to(11, 14, 8)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).end()
                .end()
                .element().from(5, 0, 0).to(11, 14, 8)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).end()
                .end();

        var sideTall = models().getBuilder(name + "_side_tall")
                .parent(models().getExistingFile(mcLoc("block/template_wall_side_tall")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("wall", modLoc("block/brick_base"))
                .element().from(5, 0, 0).to(11, 16, 8)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).end()
                .end()
                .element().from(5, 0, 0).to(11, 16, 8)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).end()
                .end();

        wallBlock((net.minecraft.world.level.block.WallBlock) wallBlock.get(), post, side, sideTall);

        var inventoryModel = models().getBuilder(name + "_inventory")
                .parent(models().getExistingFile(mcLoc("block/block")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                // Wall post
                .element().from(4, 0, 4).to(12, 16, 12)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).end()
                .end()
                .element().from(4, 0, 4).to(12, 16, 12)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).end()
                .end()
                // Wall side (the connector part)
                .element().from(5, 0, 0).to(11, 14, 8)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).end()
                .end()
                .element().from(5, 0, 0).to(11, 14, 8)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).end()
                .end();

        itemModels().withExistingParent(name, modLoc("block/" + name + "_inventory"));
    }

    // Vertical Slab registration
    private void registerDyedBrickVerticalSlab(DeferredBlock<?> verticalSlabBlock, DeferredBlock<?> baseBlock) {
        String name = verticalSlabBlock.getId().getPath();
        String baseName = baseBlock.getId().getPath();

        var northModel = models().getBuilder(name)
                .parent(models().getExistingFile(mcLoc("block/block")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .element().from(0, 0, 0).to(16, 16, 8)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).uvs(0, 0, 16, 8).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(0, 0, 16, 8).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 0, 8, 16).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 0, 8, 16).end()
                .end()
                .element().from(0, 0, 0).to(16, 16, 8)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).uvs(0, 0, 16, 8).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(0, 0, 16, 8).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 0, 8, 16).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 0, 8, 16).end()
                .end();

        var southModel = models().getBuilder(name + "_south")
                .parent(models().getExistingFile(mcLoc("block/block")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .element().from(0, 0, 8).to(16, 16, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).uvs(0, 8, 16, 16).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(0, 8, 16, 16).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(8, 0, 16, 16).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(8, 0, 16, 16).end()
                .end()
                .element().from(0, 0, 8).to(16, 16, 16)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).uvs(0, 8, 16, 16).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(0, 8, 16, 16).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(8, 0, 16, 16).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(8, 0, 16, 16).end()
                .end();

        var westModel = models().getBuilder(name + "_west")
                .parent(models().getExistingFile(mcLoc("block/block")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .element().from(0, 0, 0).to(8, 16, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).uvs(0, 0, 8, 16).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(0, 0, 8, 16).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 0, 8, 16).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 0, 8, 16).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).end()
                .end()
                .element().from(0, 0, 0).to(8, 16, 16)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).uvs(0, 0, 8, 16).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(0, 0, 8, 16).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 0, 8, 16).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 0, 8, 16).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).end()
                .end();

        var eastModel = models().getBuilder(name + "_east")
                .parent(models().getExistingFile(mcLoc("block/block")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .element().from(8, 0, 0).to(16, 16, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).uvs(8, 0, 16, 16).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(8, 0, 16, 16).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(8, 0, 16, 16).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(8, 0, 16, 16).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).end()
                .end()
                .element().from(8, 0, 0).to(16, 16, 16)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).cullface(Direction.DOWN).uvs(8, 0, 16, 16).end()
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(8, 0, 16, 16).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(8, 0, 16, 16).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(8, 0, 16, 16).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).end()
                .end();

        ModelFile doubleModel = models().getExistingFile(modLoc("block/" + baseName));

        getVariantBuilder(verticalSlabBlock.get())
                .forAllStates(state -> {
                    if (state.getValue(VerticalSlabBlock.DOUBLE)) {
                        return ConfiguredModel.builder().modelFile(doubleModel).build();
                    }

                    Direction facing = state.getValue(VerticalSlabBlock.FACING);
                    ModelFile model = switch (facing) {
                        case SOUTH -> southModel;
                        case WEST -> westModel;
                        case EAST -> eastModel;
                        default -> northModel;
                    };
                    return ConfiguredModel.builder().modelFile(model).build();
                });

        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    public void verticalSlabBlock(VerticalSlabBlock block, ResourceLocation doubleSlab, ResourceLocation texture) {
        this.verticalSlabBlock(block, doubleSlab, texture, texture, texture);
    }

    /**
     * Vertical slab with different side/bottom/top textures
     */
    public void verticalSlabBlock(VerticalSlabBlock block, ResourceLocation doubleSlab, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        String name = this.name(block);

        // Create the four directional models
        ModelFile northModel = this.models().getBuilder(name)
                .parent(this.models().getExistingFile(mcLoc("block/block")))
                .texture("particle", side)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top)
                // North-facing: back half (z: 0-8)
                .element().from(0, 0, 0).to(16, 16, 8)
                .face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).uvs(0, 0, 16, 8).end()
                .face(Direction.UP).texture("#top").cullface(Direction.UP).uvs(0, 0, 16, 8).end()
                .face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#side").end()
                .face(Direction.WEST).texture("#side").cullface(Direction.WEST).uvs(0, 0, 8, 16).end()
                .face(Direction.EAST).texture("#side").cullface(Direction.EAST).uvs(0, 0, 8, 16).end()
                .end();

        ModelFile southModel = this.models().getBuilder(name + "_south")
                .parent(this.models().getExistingFile(mcLoc("block/block")))
                .texture("particle", side)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top)
                // South-facing: front half (z: 8-16)
                .element().from(0, 0, 8).to(16, 16, 16)
                .face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).uvs(0, 8, 16, 16).end()
                .face(Direction.UP).texture("#top").cullface(Direction.UP).uvs(0, 8, 16, 16).end()
                .face(Direction.NORTH).texture("#side").end()
                .face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture("#side").cullface(Direction.WEST).uvs(8, 0, 16, 16).end()
                .face(Direction.EAST).texture("#side").cullface(Direction.EAST).uvs(8, 0, 16, 16).end()
                .end();

        ModelFile westModel = this.models().getBuilder(name + "_west")
                .parent(this.models().getExistingFile(mcLoc("block/block")))
                .texture("particle", side)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top)
                // West-facing: left half (x: 0-8)
                .element().from(0, 0, 0).to(8, 16, 16)
                .face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).uvs(0, 0, 8, 16).end()
                .face(Direction.UP).texture("#top").cullface(Direction.UP).uvs(0, 0, 8, 16).end()
                .face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).uvs(0, 0, 8, 16).end()
                .face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).uvs(0, 0, 8, 16).end()
                .face(Direction.WEST).texture("#side").cullface(Direction.WEST).end()
                .face(Direction.EAST).texture("#side").end()
                .end();

        ModelFile eastModel = this.models().getBuilder(name + "_east")
                .parent(this.models().getExistingFile(mcLoc("block/block")))
                .texture("particle", side)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top)
                // East-facing: right half (x: 8-16)
                .element().from(8, 0, 0).to(16, 16, 16)
                .face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).uvs(8, 0, 16, 16).end()
                .face(Direction.UP).texture("#top").cullface(Direction.UP).uvs(8, 0, 16, 16).end()
                .face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).uvs(8, 0, 16, 16).end()
                .face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).uvs(8, 0, 16, 16).end()
                .face(Direction.WEST).texture("#side").end()
                .face(Direction.EAST).texture("#side").cullface(Direction.EAST).end()
                .end();

        ModelFile doubleModel = this.models().getExistingFile(doubleSlab);

        this.verticalSlabBlock(block, northModel, southModel, westModel, eastModel, doubleModel);
    }

    /**
     * Full vertical slab registration with all directional models
     */
    public void verticalSlabBlock(VerticalSlabBlock block, ModelFile north, ModelFile south, ModelFile west, ModelFile east, ModelFile doubleSlab) {
        this.getVariantBuilder(block)
                .forAllStates(state -> {
                    // Double slab uses full block model
                    if (state.getValue(VerticalSlabBlock.DOUBLE)) {
                        return ConfiguredModel.builder().modelFile(doubleSlab).build();
                    }

                    // Single slab uses directional model based on FACING
                    Direction facing = state.getValue(VerticalSlabBlock.FACING);
                    ModelFile model = switch (facing) {
                        case SOUTH -> south;
                        case WEST -> west;
                        case EAST -> east;
                        default -> north; // NORTH and fallback
                    };
                    return ConfiguredModel.builder().modelFile(model).build();
                });

        // Item model uses north facing
        this.itemModels().withExistingParent(this.name(block), modLoc("block/" + this.name(block)));
    }

    /**
     * Helper to get block name from registry
     */
    private String name(Block block) {
        return block.builtInRegistryHolder().key().location().getPath();
    }

    private void simpleBlockWithItem(DeferredBlock<?> deferredBlock, String renderType) {
        var model = models().cubeAll(
                deferredBlock.getId().getPath(),
                blockTexture(deferredBlock.get())
        ).renderType(renderType);
        simpleBlockWithItem(deferredBlock.get(), model);
    }
}