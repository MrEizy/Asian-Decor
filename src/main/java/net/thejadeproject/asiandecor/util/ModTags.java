package net.thejadeproject.asiandecor.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.thejadeproject.asiandecor.AsianDecor;


public class ModTags {

    public static class Items {


        //public static final TagKey<Item> BLACK_IRON_RAW = createCommonTag("raw_materials/black_iron");
        //public static final TagKey<Item> WOOLABLE = createTag("crafting/woolable");

        public static final TagKey<Item> DYED_BRICKS = createTag("dyed_bricks");
        public static final TagKey<Item> DYES = createCommonTag("dyes");



        private static TagKey<Item> createCommonTag(String path) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, name));
        }
    }

    public static class Blocks {
        //public static final TagKey<Block> DESTRUCTIBLE_BLOCKS = createTag("blocks_destruction");
        //public static final TagKey<Block> STORAGE_BLOCKS_BLACK_IRON = createCommonTag("storage_blocks/black_iron");

        public static final TagKey<Block> DYED_BRICK_BLOCKS = createTag("dyed_brick_blocks");



        private static TagKey<Block> createCommonTag(String path) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, name));
        }
    }
}