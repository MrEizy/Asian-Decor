package net.thejadeproject.asiandecor.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AsianDecor.MOD_ID);

    /*public static final DeferredItem<Item> RAW_JADE = ITEMS.register("raw_jade",
            () -> new Item(new Item.Properties()));*/




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
