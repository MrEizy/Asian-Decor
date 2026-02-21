package net.thejadeproject.asiandecor.client.handler;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlockPouchItem;
import net.thejadeproject.asiandecor.items.buildersgadgets.TrowelItem;

@EventBusSubscriber(modid = AsianDecor.MOD_ID)
public class PouchLinkHandler {

    @SubscribeEvent
    public static void onItemStackedOnOther(ItemStackedOnOtherEvent event) {
        ItemStack carriedItem = event.getCarriedItem();
        ItemStack stackedOnItem = event.getStackedOnItem();

        // Check if carrying trowel and clicking on pouch
        if (carriedItem.getItem() instanceof TrowelItem &&
                stackedOnItem.getItem() instanceof BlockPouchItem) {

            // Link the pouch to the trowel
            TrowelItem.setLinkedPouch(carriedItem, stackedOnItem.copy());

            Player player = event.getPlayer();
            player.displayClientMessage(Component.translatable(
                    "message.asiandecor.trowel.pouch_linked"), true);

            event.setCanceled(true);
        }
    }
}
