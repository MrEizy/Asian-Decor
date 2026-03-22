package net.zic.builders_zenith.guis;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.items.ModItems;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BuildersZenith.MOD_ID);

    public static final Supplier<CreativeModeTab> BUILDERS_ZENITH_ITEMS = CREATIVE_MODE_TAB.register("builders_zenith_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WHITE_BLOCK_POUCH.get()))
                    .title(Component.translatable("creativetab.builders_zenith.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.WHITE_BLOCK_POUCH);
                        output.accept(ModItems.BLUEPRINT);
                        output.accept(ModItems.TAPE_MEASURE);
                        output.accept(ModItems.TROWEL);
                        output.accept(ModItems.HANDHELD_FILLER);
                    }).build());

    public static final Supplier<CreativeModeTab> BUILDERS_ZENITH_BLOCKS = CREATIVE_MODE_TAB.register("builders_zenith_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CARPENTER.get()))
                    .title(Component.translatable("creativetab.builders_zenith.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.CARPENTER);
                        output.accept(ModBlocks.COLOR_MIXER);
                        output.accept(ModBlocks.SHAPE_MAKER);
                        //output.accept(ModBlocks.WINGED_TABLE);




                        for (DyedBrickType type : DyedBrickType.values()) {
                            output.accept(ModBlocks.DYED_BRICKS.get(type));
                            output.accept(ModBlocks.DYED_BRICK_STAIRS.get(type));
                            output.accept(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type));
                            output.accept(ModBlocks.DYED_BRICK_SLABS.get(type));
                            output.accept(ModBlocks.DYED_BRICK_WALLS.get(type));
                        }
                    }).build());

    @SubscribeEvent
    public static void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // Wood vertical slabs
            event.insertBefore(Items.OAK_SLAB.getDefaultInstance(), ModBlocks.OAK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.MUD_BRICK_SLAB.getDefaultInstance(), ModBlocks.MUD_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SPRUCE_SLAB.getDefaultInstance(), ModBlocks.SPRUCE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.BIRCH_SLAB.getDefaultInstance(), ModBlocks.BIRCH_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.JUNGLE_SLAB.getDefaultInstance(), ModBlocks.JUNGLE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.ACACIA_SLAB.getDefaultInstance(), ModBlocks.ACACIA_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.DARK_OAK_SLAB.getDefaultInstance(), ModBlocks.DARK_OAK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.MANGROVE_SLAB.getDefaultInstance(), ModBlocks.MANGROVE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.CHERRY_SLAB.getDefaultInstance(), ModBlocks.CHERRY_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.BAMBOO_SLAB.getDefaultInstance(), ModBlocks.BAMBOO_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.CRIMSON_SLAB.getDefaultInstance(), ModBlocks.CRIMSON_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.WARPED_SLAB.getDefaultInstance(), ModBlocks.WARPED_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            // Stone vertical slabs
            event.insertBefore(Items.STONE_SLAB.getDefaultInstance(), ModBlocks.STONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.COBBLESTONE_SLAB.getDefaultInstance(), ModBlocks.COBBLESTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.MOSSY_COBBLESTONE_SLAB.getDefaultInstance(), ModBlocks.MOSSY_COBBLESTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SMOOTH_STONE_SLAB.getDefaultInstance(), ModBlocks.SMOOTH_STONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.STONE_BRICK_SLAB.getDefaultInstance(), ModBlocks.STONE_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.MOSSY_STONE_BRICK_SLAB.getDefaultInstance(), ModBlocks.MOSSY_STONE_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GRANITE_SLAB.getDefaultInstance(), ModBlocks.GRANITE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.POLISHED_GRANITE_SLAB.getDefaultInstance(), ModBlocks.POLISHED_GRANITE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.DIORITE_SLAB.getDefaultInstance(), ModBlocks.DIORITE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.POLISHED_DIORITE_SLAB.getDefaultInstance(), ModBlocks.POLISHED_DIORITE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.ANDESITE_SLAB.getDefaultInstance(), ModBlocks.ANDESITE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.POLISHED_ANDESITE_SLAB.getDefaultInstance(), ModBlocks.POLISHED_ANDESITE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.COBBLED_DEEPSLATE_SLAB.getDefaultInstance(), ModBlocks.COBBLED_DEEPSLATE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.POLISHED_DEEPSLATE_SLAB.getDefaultInstance(), ModBlocks.POLISHED_DEEPSLATE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.DEEPSLATE_BRICK_SLAB.getDefaultInstance(), ModBlocks.DEEPSLATE_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.DEEPSLATE_TILE_SLAB.getDefaultInstance(), ModBlocks.DEEPSLATE_TILE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.TUFF_SLAB.getDefaultInstance(), ModBlocks.TUFF_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.POLISHED_TUFF_SLAB.getDefaultInstance(), ModBlocks.POLISHED_TUFF_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.TUFF_BRICK_SLAB.getDefaultInstance(), ModBlocks.TUFF_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.BRICK_SLAB.getDefaultInstance(), ModBlocks.BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SANDSTONE_SLAB.getDefaultInstance(), ModBlocks.SANDSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SMOOTH_SANDSTONE_SLAB.getDefaultInstance(), ModBlocks.SMOOTH_SANDSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.CUT_STANDSTONE_SLAB.getDefaultInstance(), ModBlocks.CUT_SANDSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.RED_SANDSTONE_SLAB.getDefaultInstance(), ModBlocks.RED_SANDSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SMOOTH_RED_SANDSTONE_SLAB.getDefaultInstance(), ModBlocks.SMOOTH_RED_SANDSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.CUT_RED_SANDSTONE_SLAB.getDefaultInstance(), ModBlocks.CUT_RED_SANDSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.PRISMARINE_SLAB.getDefaultInstance(), ModBlocks.PRISMARINE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.PRISMARINE_BRICK_SLAB.getDefaultInstance(), ModBlocks.PRISMARINE_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.DARK_PRISMARINE_SLAB.getDefaultInstance(), ModBlocks.DARK_PRISMARINE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.NETHER_BRICK_SLAB.getDefaultInstance(), ModBlocks.NETHER_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.RED_NETHER_BRICK_SLAB.getDefaultInstance(), ModBlocks.RED_NETHER_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.BLACKSTONE_SLAB.getDefaultInstance(), ModBlocks.BLACKSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.POLISHED_BLACKSTONE_SLAB.getDefaultInstance(), ModBlocks.POLISHED_BLACKSTONE_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.POLISHED_BLACKSTONE_BRICK_SLAB.getDefaultInstance(), ModBlocks.POLISHED_BLACKSTONE_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.END_STONE_BRICK_SLAB.getDefaultInstance(), ModBlocks.END_STONE_BRICK_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.PURPUR_SLAB.getDefaultInstance(), ModBlocks.PURPUR_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.QUARTZ_SLAB.getDefaultInstance(), ModBlocks.QUARTZ_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SMOOTH_QUARTZ_SLAB.getDefaultInstance(), ModBlocks.SMOOTH_QUARTZ_VERTICAL_SLAB.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);

        eventBus.addListener(ModCreativeModeTabs::buildCreativeModeTabContents);
    }


}