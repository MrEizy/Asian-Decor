package net.thejadeproject.asiandecor.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AsianDecor.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.PREVIEW_BLOCK, "translucent");
        simpleBlockWithItem(ModBlocks.SHAPE_MAKER, "full");
        simpleBlockWithItem(ModBlocks.COLOR_MIXER, "full");

        // Register all 256 dyed brick variants
        for (DyedBrickType type : DyedBrickType.values()) {
            DeferredBlock<?> block = ModBlocks.DYED_BRICKS.get(type);
            DeferredBlock<?> slab = ModBlocks.DYED_BRICK_SLABS.get(type);
            DeferredBlock<?> stairs = ModBlocks.DYED_BRICK_STAIRS.get(type);
            DeferredBlock<?> wall = ModBlocks.DYED_BRICK_WALLS.get(type);

            registerDyedBrickBlock(block, type);
            registerDyedBrickSlab(slab, block);
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

        // BOTTOM SLAB
        var slabBottom = models().getBuilder(name)
                .parent(models().getExistingFile(mcLoc("block/slab")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("bottom", modLoc("block/brick_base"))
                .texture("top", modLoc("block/brick_base"))
                .texture("side", modLoc("block/brick_base"))
                // Brick layer
                .element()
                .from(0, 0, 0).to(16, 8, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end()
                // Mortar layer
                .element()
                .from(0, 0, 0).to(16, 8, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(0, 8, 16, 16).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 8, 16, 16).end()
                .end();

        // TOP SLAB
        var slabTop = models().getBuilder(name + "_top")
                .parent(models().getExistingFile(mcLoc("block/slab_top")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("bottom", modLoc("block/brick_base"))
                .texture("top", modLoc("block/brick_base"))
                .texture("side", modLoc("block/brick_base"))
                // Brick layer
                .element()
                .from(0, 8, 0).to(16, 16, 16)
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).end()
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(0, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 0, 16, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(0, 0, 16, 8).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                .end()
                // Mortar layer
                .element()
                .from(0, 8, 0).to(16, 16, 16)
                .face(Direction.DOWN).texture("#mortar_overlay").tintindex(1).end()
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

        // REGULAR STAIRS - Fixed UV mapping
        var stairs = models().getBuilder(name)
                .parent(models().getExistingFile(mcLoc("block/stairs")))
                .renderType("cutout")
                .texture("particle", modLoc("block/brick_base"))
                .texture("brick_base", modLoc("block/brick_base"))
                .texture("mortar_overlay", modLoc("block/mortar_overlay"))
                .texture("bottom", modLoc("block/brick_base"))
                .texture("top", modLoc("block/brick_base"))
                .texture("side", modLoc("block/brick_base"))
                // Bottom step (full layer at bottom)
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
                // Top step - BACK half only (z: 8 to 16) - FIXED UV
                .element().from(0, 8, 8).to(16, 16, 16)
                // UP face - full width, half depth (back half of texture)
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(0, 8, 16, 16).end()
                // DOWN face - facing the bottom step, no cullface
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).uvs(0, 8, 16, 16).end()
                // NORTH face - facing player (inner step face), no cullface
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).uvs(0, 8, 16, 16).end()
                // SOUTH face - outside
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                // WEST face - side (only back half shows)
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(8, 8, 16, 16).end()
                // EAST face - side (only back half shows)
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(8, 8, 16, 16).end()
                .end()
                // Top step mortar - FIXED UV
                .element().from(0, 8, 8).to(16, 16, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(0, 8, 16, 16).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).uvs(0, 8, 16, 16).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 8, 16, 16).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(8, 8, 16, 16).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(8, 8, 16, 16).end()
                .end();

        // INNER STAIRS - Corrected UV mapping
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
                // Top step 1 - RIGHT side (x: 8 to 16, full depth z: 0-16)
                .element().from(8, 8, 0).to(16, 16, 16)
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(0, 0, 16, 16).end()
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).uvs(0, 0, 16, 16).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).cullface(Direction.NORTH).uvs(8, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).uvs(0, 0, 16, 8).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                .end()
                // Top step 1 mortar
                .element().from(8, 8, 0).to(16, 16, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(0, 0, 16, 16).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.NORTH).uvs(8, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).uvs(0, 0, 16, 8).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(0, 0, 16, 8).end()
                .end()
                // Top step 2 - FRONT-LEFT corner (x: 0 to 8, z: 8 to 16)
                .element().from(0, 8, 8).to(8, 16, 16)
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(0, 8, 8, 16).end()
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).uvs(0, 8, 8, 16).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).uvs(0, 0, 8, 8).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(0, 0, 8, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).cullface(Direction.WEST).uvs(8, 0, 16, 8).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).uvs(8, 0, 16, 8).end()
                .end()
                // Top step 2 mortar
                .element().from(0, 8, 8).to(8, 16, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(0, 8, 8, 16).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).uvs(0, 0, 8, 8).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(0, 0, 8, 8).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).cullface(Direction.WEST).uvs(8, 0, 16, 8).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).uvs(8, 0, 16, 8).end()
                .end();

        // OUTER STAIRS - Corrected UV mapping
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
                // Top step - FRONT-RIGHT corner only (x: 8 to 16, z: 8 to 16)
                .element().from(8, 8, 8).to(16, 16, 16)
                .face(Direction.UP).texture("#brick_base").tintindex(0).cullface(Direction.UP).uvs(8, 8, 16, 16).end()
                .face(Direction.DOWN).texture("#brick_base").tintindex(0).uvs(8, 8, 16, 16).end()
                .face(Direction.NORTH).texture("#brick_base").tintindex(0).uvs(8, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#brick_base").tintindex(0).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#brick_base").tintindex(0).uvs(8, 0, 16, 8).end()
                .face(Direction.EAST).texture("#brick_base").tintindex(0).cullface(Direction.EAST).uvs(8, 0, 16, 8).end()
                .end()
                // Top step mortar
                .element().from(8, 8, 8).to(16, 16, 16)
                .face(Direction.UP).texture("#mortar_overlay").tintindex(1).cullface(Direction.UP).uvs(8, 8, 16, 16).end()
                .face(Direction.NORTH).texture("#mortar_overlay").tintindex(1).uvs(8, 0, 16, 8).end()
                .face(Direction.SOUTH).texture("#mortar_overlay").tintindex(1).cullface(Direction.SOUTH).uvs(8, 0, 16, 8).end()
                .face(Direction.WEST).texture("#mortar_overlay").tintindex(1).uvs(8, 0, 16, 8).end()
                .face(Direction.EAST).texture("#mortar_overlay").tintindex(1).cullface(Direction.EAST).uvs(8, 0, 16, 8).end()
                .end();

        stairsBlock((net.minecraft.world.level.block.StairBlock) stairBlock.get(), stairs, stairsInner, stairsOuter);

        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    private void registerDyedBrickWall(DeferredBlock<?> wallBlock, DeferredBlock<?> baseBlock) {
        String name = wallBlock.getId().getPath();

        // WALL POST
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

        // WALL SIDE (short - 14 tall)
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

        // WALL SIDE TALL (16 tall)
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

        itemModels().withExistingParent(name, mcLoc("block/wall_inventory"))
                .texture("wall", modLoc("block/brick_base"));
    }

    private void simpleBlockWithItem(DeferredBlock<?> deferredBlock, String renderType) {
        var model = models().cubeAll(
                deferredBlock.getId().getPath(),
                blockTexture(deferredBlock.get())
        ).renderType(renderType);
        simpleBlockWithItem(deferredBlock.get(), model);
    }
}