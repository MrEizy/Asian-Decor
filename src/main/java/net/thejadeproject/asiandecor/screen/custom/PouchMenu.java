package net.thejadeproject.asiandecor.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.component.PouchContents;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlockPouchItem;
import net.thejadeproject.asiandecor.screen.ModMenuTypes;

import java.util.ArrayList;
import java.util.List;

public class PouchMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 27;
    private final ItemStack pouchStack;
    private final InteractionHand hand;
    private final Inventory playerInventory;
    private final ItemStackHandler itemHandler;

    public PouchMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory,
                getPouchFromInventory(playerInventory),
                getHandFromInventory(playerInventory));
    }

    private static ItemStack getPouchFromInventory(Inventory inv) {
        ItemStack main = inv.getSelected();
        return main.getItem() instanceof BlockPouchItem ? main : inv.player.getOffhandItem();
    }

    private static InteractionHand getHandFromInventory(Inventory inv) {
        return inv.getSelected().getItem() instanceof BlockPouchItem ?
                InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public PouchMenu(int containerId, Inventory playerInventory, ItemStack pouchStack, InteractionHand hand) {
        super(ModMenuTypes.POUCH_MENU.get(), containerId);
        this.pouchStack = pouchStack;
        this.hand = hand;
        this.playerInventory = playerInventory;

        PouchContents contents = pouchStack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);

        this.itemHandler = new ItemStackHandler(CONTAINER_SIZE) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof net.minecraft.world.item.BlockItem;
            }
        };

        List<ItemStack> items = contents.toItemList();
        for (int i = 0; i < CONTAINER_SIZE; i++) {
            this.itemHandler.setStackInSlot(i, items.get(i).copy());
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = row * 9 + col;
                int x = 8 + col * 18;
                int y = 18 + row * 18;
                this.addSlot(new SlotItemHandler(this.itemHandler, slotIndex, x, y));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 86 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            final int slotIndex = col;
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 144) {
                @Override
                public boolean mayPickup(Player player) {
                    if (slotIndex == playerInventory.selected && hand == InteractionHand.MAIN_HAND) {
                        return false;
                    }
                    return super.mayPickup(player);
                }
            });
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        saveContents();
    }

    private void saveContents() {
        if (playerInventory.player.level().isClientSide) return;

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < CONTAINER_SIZE; i++) {
            items.add(this.itemHandler.getStackInSlot(i).copy());
        }

        PouchContents oldContents = pouchStack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
        PouchContents newContents = PouchContents.fromItemList(items, oldContents.selectedSlot());
        pouchStack.set(ModDataComponents.POUCH_CONTENTS.get(), newContents);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < CONTAINER_SIZE) {
                if (!this.moveItemStackTo(itemstack1, CONTAINER_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!(itemstack1.getItem() instanceof net.minecraft.world.item.BlockItem)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, 0, CONTAINER_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        ItemStack current = hand == InteractionHand.MAIN_HAND ?
                player.getMainHandItem() : player.getOffhandItem();
        return current == pouchStack || (ItemStack.isSameItem(current, pouchStack) &&
                ItemStack.matches(current, pouchStack));
    }

    public ItemStack getPouchStack() {
        return pouchStack;
    }

    public int getSelectedSlot() {
        PouchContents contents = pouchStack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
        return contents.selectedSlot();
    }
}