package net.thejadeproject.asiandecor.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.items.blockitem.DyedBrickBlockItem;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlockPouchItem;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlueprintItem;
import net.thejadeproject.asiandecor.items.buildersgadgets.TapeMeasureItem;
import net.thejadeproject.asiandecor.items.buildersgadgets.TrowelItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AsianDecor.MOD_ID);

    public static final DeferredItem<Item> WHITE_BLOCK_POUCH = ITEMS.register("white_block_pouch",
            () -> new BlockPouchItem(new Item.Properties()));


    public static final DeferredItem<Item> BLUEPRINT = ITEMS.register("blueprint",
            () -> new BlueprintItem(new Item.Properties()));

    public static final DeferredItem<Item> TAPE_MEASURE = ITEMS.register("tape_measure",
            () -> new TapeMeasureItem(new Item.Properties().stacksTo(1).durability(64)));


    public static final DeferredItem<Item> TROWEL = ITEMS.register("trowel",
            () -> new TrowelItem(new Item.Properties().stacksTo(1).durability(64)));

    public static final DeferredItem<Item> DYED_BRICK = ITEMS.register("dyed_brick",
            () -> new DyedBrickBlockItem(ModBlocks.DYED_BRICK.get(), new Item.Properties()));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
