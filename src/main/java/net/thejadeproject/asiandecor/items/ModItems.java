package net.thejadeproject.asiandecor.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlockPouchItem;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlueprintItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AsianDecor.MOD_ID);

    public static final DeferredItem<Item> WHITE_BLOCK_POUCH = ITEMS.register("white_block_pouch",
            () -> new BlockPouchItem(new Item.Properties()));


    public static final DeferredItem<Item> BLUEPRINT = ITEMS.register("blueprint",
            () -> new BlueprintItem(new Item.Properties()));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
