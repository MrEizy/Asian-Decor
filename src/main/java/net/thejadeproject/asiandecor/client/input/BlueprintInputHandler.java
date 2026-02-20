package net.thejadeproject.asiandecor.client.input;

import com.mojang.blaze3d.platform.InputConstants;
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
import net.thejadeproject.asiandecor.component.BlueprintData;
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

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof BlueprintItem) {
                var data = stack.getOrDefault(ModDataComponents.BLUEPRINT_DATA.get(),
                        net.thejadeproject.asiandecor.component.BlueprintData.EMPTY);

                if (!data.hasData()) continue;

                event.setCanceled(true);

                double scrollDelta = event.getScrollDeltaY();
                if (scrollDelta == 0) return;

                int direction = scrollDelta > 0 ? 1 : -1;

                // Check Ctrl using GLFW
                long window = mc.getWindow().getWindow();
                boolean ctrlDown = InputConstants.isKeyDown(window, InputConstants.KEY_LCONTROL) ||
                        InputConstants.isKeyDown(window, InputConstants.KEY_RCONTROL);

                int dirY = ctrlDown ? 0 : direction;
                int dirX = ctrlDown ? direction : 0;

                // Send to server
                PacketDistributor.sendToServer(new BlueprintRotatePacket(
                        hand == InteractionHand.MAIN_HAND, dirY, dirX));

                // Update client immediately for instant preview feedback
                BlueprintData rotated = data;
                if (dirY != 0) {
                    rotated = rotated.withRotationY((data.rotationY() + dirY + 4) % 4);
                }
                if (dirX != 0) {
                    rotated = rotated.withRotationX((data.rotationX() + dirX + 4) % 4);
                }
                stack.set(ModDataComponents.BLUEPRINT_DATA.get(), rotated);

                // Show message
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable(
                                "message.asiandecor.blueprint.rotated",
                                rotated.getRotationName(),
                                rotated.getFacingName()
                        ),
                        true
                );

                break;
            }
        }
    }
}