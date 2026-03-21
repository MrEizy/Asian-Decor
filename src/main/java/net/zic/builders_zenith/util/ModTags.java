package net.zic.builders_zenith.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.zic.builders_zenith.BuildersZenith;


public class ModTags {

    public static class Items {


        //public static final TagKey<Item> BLACK_IRON_RAW = createCommonTag("raw_materials/black_iron");
        //public static final TagKey<Item> WOOLABLE = createTag("crafting/woolable");

        public static final TagKey<Item> DYED_BRICKS = createTag("dyed_bricks");
        public static final TagKey<Item> DYED_BRICK_SLABS = createTag("dyed_brick_slabs");
        public static final TagKey<Item> DYED_BRICK_STAIRS = createTag("dyed_brick_stairs");
        public static final TagKey<Item> DYED_BRICK_WALLS = createTag("dyed_brick_walls");

        public static final TagKey<Item> DYES = createCommonTag("dyes");



        private static TagKey<Item> createCommonTag(String path) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(BuildersZenith.MOD_ID, name));
        }
    }

    public static class Blocks {
        //public static final TagKey<Block> DESTRUCTIBLE_BLOCKS = createTag("blocks_destruction");
        //public static final TagKey<Block> STORAGE_BLOCKS_BLACK_IRON = createCommonTag("storage_blocks/black_iron");

        public static final TagKey<Block> DYED_BRICK_BLOCKS = createTag("dyed_brick_blocks");
        public static final TagKey<Block> DYED_BRICK_SLABS = createTag("dyed_brick_slabs");
        public static final TagKey<Block> DYED_BRICK_STAIRS = createTag("dyed_brick_stairs");
        public static final TagKey<Block> DYED_BRICK_WALLS = createTag("dyed_brick_walls");



        private static TagKey<Block> createCommonTag(String path) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(BuildersZenith.MOD_ID, name));
        }
    }
}