package net.thejadeproject.asiandecor.client.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.component.PouchContents;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlockPouchItem;
import net.thejadeproject.asiandecor.network.PouchScrollPacket;

@EventBusSubscriber(modid = AsianDecor.MOD_ID, value = Dist.CLIENT)
public class PouchInputHandler {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || !player.isShiftKeyDown()) return;

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof BlockPouchItem) {
                event.setCanceled(true);

                double scrollDelta = event.getScrollDeltaY();
                if (scrollDelta == 0) return;

                int direction = scrollDelta > 0 ? -1 : 1;

                PacketDistributor.sendToServer(new PouchScrollPacket(hand == InteractionHand.MAIN_HAND, direction));

                PouchContents contents = stack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
                PouchContents newContents = contents.cycleSelection(direction);
                stack.set(ModDataComponents.POUCH_CONTENTS.get(), newContents);

                break;
            }
        }
    }
}
