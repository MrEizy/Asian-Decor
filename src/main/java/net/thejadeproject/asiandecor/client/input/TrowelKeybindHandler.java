package net.thejadeproject.asiandecor.client.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.items.buildersgadgets.TrowelItem;
import net.thejadeproject.asiandecor.network.TrowelTogglePacket;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = AsianDecor.MOD_ID, value = Dist.CLIENT)
public class TrowelKeybindHandler {

    private static boolean wasKeyPressed = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getKey() != GLFW.GLFW_KEY_V) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        boolean isPressed = event.getAction() == GLFW.GLFW_PRESS;

        if (isPressed && !wasKeyPressed) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof TrowelItem) {
                    // Send packet to server
                    PacketDistributor.sendToServer(new TrowelTogglePacket(hand == InteractionHand.MAIN_HAND));

                    // Update client immediately for responsiveness
                    TrowelItem.Mode newMode = TrowelItem.toggleMode(stack);

                    // Show message to player
                    player.displayClientMessage(Component.translatable(
                            "message.asiandecor.trowel.mode_changed",
                            Component.translatable("tooltip.asiandecor.trowel.mode." + newMode.getName())
                    ), true);

                    break;
                }
            }
        }

        wasKeyPressed = isPressed;
    }
}