package net.zic.builders_zenith.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;

public class lang extends LanguageProvider {
    public lang(PackOutput output, String locale) {
        super(output, BuildersZenith.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {

        //Items
        add("item.builders_zenith.white_block_pouch", "White Builder's Pouch");
        add("item.builders_zenith.blueprint", "Blueprint");
        add("item.builders_zenith.tape_measure", "Tape Measure");
        add("item.builders_zenith.trowel", "Builder's Trowel");
        add("item.builders_zenith.handheld_filler", "Handheld Filler");



        //Blocks
        add("block.builders_zenith.carpenterblock", "Carpenter's Table");
        add("block.builders_zenith.color_mixer", "Color Mixer");
        add("block.builders_zenith.brick_mixer", "Brick Mixer");
        add("block.builders_zenith.preview_block", "Preview Block");
        add("block.builders_zenith.shape_maker", "Shape Builder");



// Vertical slabs - Wood
        add("block.builders_zenith.oak_vertical_slab", "Oak Vertical Slab");
        add("block.builders_zenith.spruce_vertical_slab", "Spruce Vertical Slab");
        add("block.builders_zenith.birch_vertical_slab", "Birch Vertical Slab");
        add("block.builders_zenith.jungle_vertical_slab", "Jungle Vertical Slab");
        add("block.builders_zenith.acacia_vertical_slab", "Acacia Vertical Slab");
        add("block.builders_zenith.dark_oak_vertical_slab", "Dark Oak Vertical Slab");
        add("block.builders_zenith.mangrove_vertical_slab", "Mangrove Vertical Slab");
        add("block.builders_zenith.cherry_vertical_slab", "Cherry Vertical Slab");
        add("block.builders_zenith.bamboo_vertical_slab", "Bamboo Vertical Slab");
        add("block.builders_zenith.crimson_vertical_slab", "Crimson Vertical Slab");
        add("block.builders_zenith.warped_vertical_slab", "Warped Vertical Slab");

// Vertical slabs - Stone
        add("block.builders_zenith.stone_vertical_slab", "Stone Vertical Slab");
        add("block.builders_zenith.mud_brick_vertical_slab", "Mud Brick Vertical Slab");
        add("block.builders_zenith.cobblestone_vertical_slab", "Cobblestone Vertical Slab");
        add("block.builders_zenith.mossy_cobblestone_vertical_slab", "Mossy Cobblestone Vertical Slab");
        add("block.builders_zenith.smooth_stone_vertical_slab", "Smooth Stone Vertical Slab");
        add("block.builders_zenith.stone_brick_vertical_slab", "Stone Brick Vertical Slab");
        add("block.builders_zenith.mossy_stone_brick_vertical_slab", "Mossy Stone Brick Vertical Slab");
        add("block.builders_zenith.granite_vertical_slab", "Granite Vertical Slab");
        add("block.builders_zenith.polished_granite_vertical_slab", "Polished Granite Vertical Slab");
        add("block.builders_zenith.diorite_vertical_slab", "Diorite Vertical Slab");
        add("block.builders_zenith.polished_diorite_vertical_slab", "Polished Diorite Vertical Slab");
        add("block.builders_zenith.andesite_vertical_slab", "Andesite Vertical Slab");
        add("block.builders_zenith.polished_andesite_vertical_slab", "Polished Andesite Vertical Slab");
        add("block.builders_zenith.cobbled_deepslate_vertical_slab", "Cobbled Deepslate Vertical Slab");
        add("block.builders_zenith.polished_deepslate_vertical_slab", "Polished Deepslate Vertical Slab");
        add("block.builders_zenith.deepslate_brick_vertical_slab", "Deepslate Brick Vertical Slab");
        add("block.builders_zenith.deepslate_tile_vertical_slab", "Deepslate Tile Vertical Slab");
        add("block.builders_zenith.tuff_vertical_slab", "Tuff Vertical Slab");
        add("block.builders_zenith.polished_tuff_vertical_slab", "Polished Tuff Vertical Slab");
        add("block.builders_zenith.tuff_brick_vertical_slab", "Tuff Brick Vertical Slab");
        add("block.builders_zenith.brick_vertical_slab", "Brick Vertical Slab");
        add("block.builders_zenith.sandstone_vertical_slab", "Sandstone Vertical Slab");
        add("block.builders_zenith.smooth_sandstone_vertical_slab", "Smooth Sandstone Vertical Slab");
        add("block.builders_zenith.cut_sandstone_vertical_slab", "Cut Sandstone Vertical Slab");
        add("block.builders_zenith.red_sandstone_vertical_slab", "Red Sandstone Vertical Slab");
        add("block.builders_zenith.smooth_red_sandstone_vertical_slab", "Smooth Red Sandstone Vertical Slab");
        add("block.builders_zenith.cut_red_sandstone_vertical_slab", "Cut Red Sandstone Vertical Slab");
        add("block.builders_zenith.prismarine_vertical_slab", "Prismarine Vertical Slab");
        add("block.builders_zenith.prismarine_brick_vertical_slab", "Prismarine Brick Vertical Slab");
        add("block.builders_zenith.dark_prismarine_vertical_slab", "Dark Prismarine Vertical Slab");
        add("block.builders_zenith.nether_brick_vertical_slab", "Nether Brick Vertical Slab");
        add("block.builders_zenith.red_nether_brick_vertical_slab", "Red Nether Brick Vertical Slab");
        add("block.builders_zenith.blackstone_vertical_slab", "Blackstone Vertical Slab");
        add("block.builders_zenith.polished_blackstone_vertical_slab", "Polished Blackstone Vertical Slab");
        add("block.builders_zenith.polished_blackstone_brick_vertical_slab", "Polished Blackstone Brick Vertical Slab");
        add("block.builders_zenith.end_stone_brick_vertical_slab", "End Stone Brick Vertical Slab");
        add("block.builders_zenith.purpur_vertical_slab", "Purpur Vertical Slab");
        add("block.builders_zenith.quartz_vertical_slab", "Quartz Vertical Slab");
        add("block.builders_zenith.smooth_quartz_vertical_slab", "Smooth Quartz Vertical Slab");


        // All Dyed Bricks - same name for all, tooltips show the difference
        for (DyedBrickType type : DyedBrickType.values()) {
            String blockId = "block.builders_zenith.dyed_brick_" + type.getSerializedName();
            add(blockId, "Dyed Bricks");

            // Stairs
            String stairsId = "block.builders_zenith.dyed_brick_stairs_" + type.getSerializedName();
            add(stairsId, "Dyed Brick Stairs");

            // Slabs
            String slabId = "block.builders_zenith.dyed_brick_slab_" + type.getSerializedName();
            add(slabId, "Dyed Brick Slab");

            // Slabs
            String slabVertId = "block.builders_zenith.dyed_brick_vertical_slab_" + type.getSerializedName();
            add(slabVertId, "Dyed Brick Vertical Slab");

            // Walls
            String wallId = "block.builders_zenith.dyed_brick_wall_" + type.getSerializedName();
            add(wallId, "Dyed Brick Wall");
        }


        add("block.builders_zenith.oak_winged_table", "Oak Winged Table");

        //GUI & Other Stuff
        add("creativetab.builders_zenith.items", "Builder's Items");
        add("creativetab.builders_zenith.blocks", "Zenith Blocks");


        add("container.builders_zenith.carpenter", "Carpenter's Table");
        add("container.builders_zenith.color_mixer", "Color Mixer");
        add("tooltip.builders_zenith.ingredient_cost", "Requires: %s");

        add("container.builders_zenith.block_pouch", "Builder's Pouch");

        //Tooltips
        add("tooltip.builders_zenith.block_pouch.selected", "Selected: %s");
        add("tooltip.builders_zenith.block_pouch.count", "Count: %s");
        add("tooltip.builders_zenith.block_pouch.empty", "Empty");
        add("tooltip.builders_zenith.block_pouch.slots", "%s/%s slots filled");



        add("tooltip.builders_zenith.blueprint.saved_size", "Size: %s x %s x %s");
        add("tooltip.builders_zenith.blueprint.block_count", "Contains %s blocks");
        add("tooltip.builders_zenith.blueprint.cut_mode", "[CUT MODE - Will remove blocks when placed]");
        add("tooltip.builders_zenith.blueprint.area_selected", "Selected: %s x %s x %s");
        add("tooltip.builders_zenith.blueprint.shift_to_cut", "Shift+Right Click to CUT");
        add("tooltip.builders_zenith.blueprint.pos1", "Pos 1: [%s, %s, %s]");
        add("tooltip.builders_zenith.blueprint.set_pos2", "Shift+Right Click to set Position 2");
        add("tooltip.builders_zenith.blueprint.no_area", "No area selected");
        add("tooltip.builders_zenith.blueprint.usage.select", "Shift+Right Click: Select two corners");
        add("tooltip.builders_zenith.blueprint.usage.cut", "Shift+Right Click again: CUT the area");
        add("tooltip.builders_zenith.blueprint.usage.preview", "Right Click: Preview / Place");
        add("tooltip.builders_zenith.blueprint.rotation", "Rotation: %s");
        add("tooltip.builders_zenith.blueprint.facing", "Facing: %s");
        add("tooltip.builders_zenith.blueprint.usage.rotate", "Shift+Scroll: Rotate Y (Horizontal)");
        add("tooltip.builders_zenith.blueprint.usage.rotate_vertical", "Ctrl+Shift+Scroll: Rotate X (Vertical/Wall)");

        add("tooltip.builders_zenith.tape_measure.pos1_set", "§7Pos 1: §f[%s, %s, %s]");
        add("tooltip.builders_zenith.tape_measure.pos2_set", "§7Pos 2: §f[%s, %s, %s]");
        add("tooltip.builders_zenith.tape_measure.no_selection", "§7Right Click to set §fPosition 1");
        add("tooltip.builders_zenith.tape_measure.select_pos2", "§7Right Click to set §fPosition 2");
        add("tooltip.builders_zenith.tape_measure.dimensions", "§aDimensions: §f%s§7x§f%s§7x§f%s");
        add("tooltip.builders_zenith.tape_measure.clear", "§8Shift+Right Click to clear");


        add("tooltip.builders_zenith.trowel.mode", "§7Mode: §f%s");
        add("tooltip.builders_zenith.trowel.mode.hotbar", "Hotbar Only");
        add("tooltip.builders_zenith.trowel.mode.inventory", "Entire Inventory");
        add("tooltip.builders_zenith.trowel.mode.pouch", "Linked Pouch");
        add("tooltip.builders_zenith.trowel.linked_pouch", "§aLinked to Block Pouch");
        add("tooltip.builders_zenith.trowel.pouch_slots", "§7Pouch: §f%s§7/§f%s slots");
        add("tooltip.builders_zenith.trowel.no_pouch_link", "§cNo pouch linked!");
        add("tooltip.builders_zenith.trowel.usage", "§8Right Click: Place random block");
        add("tooltip.builders_zenith.trowel.toggle_key", "§8Press §f%s§8 to toggle mode");
        add("tooltip.builders_zenith.trowel.link_instruction", "§7Link: Hold trowel and click on Block Pouch");



        //Action Bar
        add("message.builders_zenith.block_pouch.selected", "%s");
        add("message.builders_zenith.block_pouch.empty", "No block selected");


        add("message.builders_zenith.blueprint.pos1_set", "Position 1 set: [%s, %s, %s]");
        add("message.builders_zenith.blueprint.pos2_set", "Position 2 set: [%s, %s, %s] - Size: %sx%sx%s");
        add("message.builders_zenith.blueprint.too_large", "Area too large: %sx%sx%s (max %s)");
        add("message.builders_zenith.blueprint.cut", "Cut %sx%sx%s area (%s blocks)");
        add("message.builders_zenith.blueprint.empty", "Blueprint empty! Shift+Click to select area");
        add("message.builders_zenith.blueprint.ready_to_cut", "Area selected! Shift+Click to CUT");
        add("message.builders_zenith.blueprint.preview", "Previewing %sx%sx%s [%s]");
        add("message.builders_zenith.blueprint.placed", "Placed %s blocks (%s failed)");
        add("message.builders_zenith.blueprint.placed_cleared", "Placed %s blocks (%s failed) - Blueprint cleared");
        add("message.builders_zenith.blueprint.rotated", "Rotation: %s | Facing: %s");

        add("message.builders_zenith.tape_measure.pos1_set", "§ePosition 1 set §7at §f%s§7, §f%s§7, §f%s");
        add("message.builders_zenith.tape_measure.cleared", "§cMeasurement cleared");
        add("message.builders_zenith.tape_measure.measured", "§aLength §7| §f%sB  §aWidth §7| §f%sB  §aHeight §7| §f%sB  §7(§f%s§7 blocks total)");


        add("message.builders_zenith.trowel.mode_changed", "§eMode: §f%s");
        add("message.builders_zenith.trowel.no_blocks", "§cNo blocks available!");
        add("message.builders_zenith.trowel.pouch_linked", "§aTrowel linked to Block Pouch!");





        //Keybinds
        add("key.categories.builders_zenith", "Asian Decor");
        add("key.builders_zenith.radial_menu", "Open Pouch Radial Menu");
        add("key.builders_zenith.mode_toggle", "Toggle Between Modes");



        //Compats
        add("emi.category.builders_zenith.carpenter", "Carpenter's Table");
        add("jei.category.builders_zenith.carpenter", "Carpenter's Table");
        add("rei.category.builders_zenith.carpenter", "Carpenter's Table");

        add("jei.category.builders_zenith.color_mixer", "Color Mixer");
        add("rei.category.builders_zenith.color_mixer", "Color Mixer");

    }
}
