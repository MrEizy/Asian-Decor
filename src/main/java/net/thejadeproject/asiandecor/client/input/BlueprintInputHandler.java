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
import net.thejadeproject.asiandecor.items.buildersgadgets.BlueprintItem;
import net.thejadeproject.asiandecor.network.BlueprintRotatePacket;

@EventBusSubscriber(modid = AsianDecor.MOD_ID, value = Dist.CLIENT)
public class BlueprintInputHandler {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || !player.isShiftKeyDown()) return;

        // Check both hands for blueprint
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof BlueprintItem) {
                // Only rotate if blueprint has data
                var data = stack.getOrDefault(ModDataComponents.BLUEPRINT_DATA.get(),
                        net.thejadeproject.asiandecor.component.BlueprintData.EMPTY);

                if (!data.hasData()) continue;

                // Cancel the event so we don't scroll hotbar
                event.setCanceled(true);

                double scrollDelta = event.getScrollDeltaY();
                if (scrollDelta == 0) return;

                int direction = scrollDelta > 0 ? 1 : -1; // Scroll up = rotate right, down = rotate left

                // Send packet to server
                PacketDistributor.sendToServer(new BlueprintRotatePacket(hand == InteractionHand.MAIN_HAND, direction));

                // Update client side immediately for responsiveness
                int newRotation = (data.rotation() + direction + 4) % 4;
                var rotated = data.withRotation(newRotation);
                stack.set(ModDataComponents.BLUEPRINT_DATA.get(), rotated);

                break;
            }
        }
    }
}