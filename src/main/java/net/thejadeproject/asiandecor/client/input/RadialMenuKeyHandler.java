package net.thejadeproject.asiandecor.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.client.ModKeybinds;
import net.thejadeproject.asiandecor.client.gui.RadialMenuRenderer;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.component.PouchContents;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlockPouchItem;
import net.thejadeproject.asiandecor.network.PouchRadialSelectPacket;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AsianDecor.MOD_ID, value = Dist.CLIENT)
public class RadialMenuKeyHandler {



    private static boolean wasKeyDown = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean isKeyDown = ModKeybinds.RADIAL_MENU_KEY.isDown();

        ItemStack pouch = null;
        InteractionHand hand = null;

        for (InteractionHand h : InteractionHand.values()) {
            ItemStack stack = mc.player.getItemInHand(h);
            if (stack.getItem() instanceof BlockPouchItem) {
                pouch = stack;
                hand = h;
                break;
            }
        }

        if (pouch == null) {
            if (RadialMenuRenderer.isMenuOpen()) {
                RadialMenuRenderer.closeMenu();
            }
            wasKeyDown = isKeyDown;
            return;
        }

        if (isKeyDown && !wasKeyDown) {
            PouchContents contents = pouch.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
            List<ItemStack> items = contents.toItemList();

            RadialMenuRenderer.openMenu(items);
            mc.mouseHandler.releaseMouse();
        }

        if (!isKeyDown && wasKeyDown) {
            if (RadialMenuRenderer.isMenuOpen()) {
                int selectedSlot = RadialMenuRenderer.getSelectedSlot();

                if (selectedSlot >= 0) {
                    // Send packet to select this slot
                    PacketDistributor.sendToServer(new PouchRadialSelectPacket(
                            hand == InteractionHand.MAIN_HAND, selectedSlot));

                    // Update client immediately
                    PouchContents contents = pouch.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
                    pouch.set(ModDataComponents.POUCH_CONTENTS.get(), contents.withSelectedSlot(selectedSlot));
                }

                RadialMenuRenderer.closeMenu();
            }

            // Don't reset selection immediately - let it persist for this tick
            // Reset will happen on next open
            mc.mouseHandler.grabMouse();
        }

        wasKeyDown = isKeyDown;

        if (!RadialMenuRenderer.isMenuOpen() && !wasKeyDown) {
            RadialMenuRenderer.resetSelection();
        }

    }

    private static int getSlotIndexFromSelection(ItemStack pouch, int selectionIndex) {
        PouchContents contents = pouch.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);

        Map<String, Integer> firstSlotOfGroup = new HashMap<>();
        List<String> groupOrder = new ArrayList<>();

        // Iterate through entries directly
        for (PouchContents.SlotEntry entry : contents.entries()) {
            int slot = entry.slot();
            ItemStack stack = entry.stack();
            if (stack.isEmpty()) continue;

            String key = stack.getItem().toString() + stack.getDamageValue();
            if (!firstSlotOfGroup.containsKey(key)) {
                firstSlotOfGroup.put(key, slot);
                groupOrder.add(key);
            }
        }

        if (selectionIndex < groupOrder.size()) {
            String key = groupOrder.get(selectionIndex);
            return firstSlotOfGroup.get(key);
        }

        return -1;
    }
}