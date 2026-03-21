package net.zic.builders_zenith.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.component.HandheldFillerData;
import net.zic.builders_zenith.component.ModDataComponents;
import net.zic.builders_zenith.items.buildersgadgets.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BuildersZenith.MOD_ID);

    public static final DeferredItem<Item> WHITE_BLOCK_POUCH = ITEMS.register("white_block_pouch",
            () -> new BlockPouchItem(new Item.Properties()));


    public static final DeferredItem<Item> BLUEPRINT = ITEMS.register("blueprint",
            () -> new BlueprintItem(new Item.Properties()));

    public static final DeferredItem<Item> TAPE_MEASURE = ITEMS.register("tape_measure",
            () -> new TapeMeasureItem(new Item.Properties().stacksTo(1).durability(64)));


    public static final DeferredItem<Item> TROWEL = ITEMS.register("trowel",
            () -> new TrowelItem(new Item.Properties().stacksTo(1).durability(64)));



    public static final DeferredItem<Item> HANDHELD_FILLER = ITEMS.register("handheld_filler",
            () -> new HandheldFillerItem(new Item.Properties()
                    .stacksTo(1)
                    .component(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData())));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
