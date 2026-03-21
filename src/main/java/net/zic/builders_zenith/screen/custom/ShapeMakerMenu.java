package net.zic.builders_zenith.screen.custom;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zic.builders_zenith.blocks.entity.ShapeMakerBlockEntity;
import net.zic.builders_zenith.network.ShapeMakerTogglePreviewPacket;
import net.zic.builders_zenith.network.ShapeMakerUpdatePacket;
import net.zic.builders_zenith.screen.ModMenuTypes;

public class ShapeMakerMenu extends AbstractContainerMenu {

    private final ShapeMakerBlockEntity blockEntity;
    private final ContainerData data;

    // ADJUSTMENT: Lower hotbar by 12 pixels (from 198 to 210)
    private static final int HOTBAR_Y = 204;
    // ADJUSTMENT: Lower player inventory by 12 pixels (from 140 to 152)
    private static final int PLAYER_INV_Y = 146;
    // ADJUSTMENT: Lower chest inventory row by 12 pixels (from 108 to 120)
    private static final int CHEST_ROW_Y = 114;

    public ShapeMakerMenu(int containerId, Inventory playerInventory, ShapeMakerBlockEntity be) {
        this(containerId, playerInventory, be, new SimpleContainerData(8));
    }

    public ShapeMakerMenu(int containerId, Inventory playerInventory, ShapeMakerBlockEntity be, ContainerData data) {
        super(ModMenuTypes.SHAPE_MAKER.get(), containerId);
        this.blockEntity = be;
        this.data = data;

        // CHEST INVENTORY - Lowered by 12 pixels
        if (be != null) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(be, col, 8 + col * 18, CHEST_ROW_Y) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem;
                    }
                });
            }
        }

        // PLAYER INVENTORY - Lowered by 12 pixels
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, PLAYER_INV_Y + row * 18));
            }
        }

        // HOTBAR - Lowered by 12 pixels
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, HOTBAR_Y));
        }

        this.addDataSlots(data);

        if (be != null) {
            syncDataFromBE();
        }
    }

    public void syncDataFromBE() {
        if (blockEntity == null) return;
        data.set(0, blockEntity.getSelectedShape().ordinal());
        data.set(1, blockEntity.getXOffset());
        data.set(2, blockEntity.getYOffset());
        data.set(3, blockEntity.getZOffset());
        data.set(4, blockEntity.getRadius());
        data.set(5, blockEntity.getThickness());
        data.set(6, blockEntity.isPreviewEnabled() ? 1 : 0);
        data.set(7, blockEntity.getRedstoneMode().ordinal());
    }

    public void updateShapeType(ShapeMakerBlockEntity.ShapeType type) {
        data.set(0, type.ordinal());
        sendUpdateToServer();
        broadcastChanges();
    }

    public void updateXOffset(int x) {
        data.set(1, x);
        sendUpdateToServer();
        broadcastChanges();
    }

    public void updateYOffset(int y) {
        data.set(2, y);
        sendUpdateToServer();
        broadcastChanges();
    }

    public void updateZOffset(int z) {
        data.set(3, z);
        sendUpdateToServer();
        broadcastChanges();
    }

    public void updateRadius(int radius) {
        data.set(4, radius);
        sendUpdateToServer();
        broadcastChanges();
    }

    public void updateThickness(int thickness) {
        data.set(5, thickness);
        sendUpdateToServer();
        broadcastChanges();
    }

    public void updatePreview(boolean enabled) {
        data.set(6, enabled ? 1 : 0);
        if (blockEntity != null) {
            PacketDistributor.sendToServer(new ShapeMakerTogglePreviewPacket(
                    blockEntity.getBlockPos(), enabled));
        }
        broadcastChanges();
    }

    public void updateRedstoneMode(ShapeMakerBlockEntity.RedstoneMode mode) {
        data.set(7, mode.ordinal());
        sendUpdateToServer();
        broadcastChanges();
    }

    private void sendUpdateToServer() {
        if (blockEntity == null) return;
        PacketDistributor.sendToServer(new ShapeMakerUpdatePacket(
                blockEntity.getBlockPos(),
                data.get(0), data.get(1), data.get(2), data.get(3),
                data.get(4), data.get(5), data.get(6) == 1, data.get(7)));
    }

    public ShapeMakerBlockEntity getBlockEntity() { return blockEntity; }
    public ShapeMakerBlockEntity.ShapeType getSelectedShape() {
        return ShapeMakerBlockEntity.ShapeType.values()[data.get(0)];
    }
    public int getXOffset() { return data.get(1); }
    public int getYOffset() { return data.get(2); }
    public int getZOffset() { return data.get(3); }
    public int getRadius() { return data.get(4); }
    public int getThickness() { return data.get(5); }
    public boolean isPreviewEnabled() { return data.get(6) == 1; }
    public ShapeMakerBlockEntity.RedstoneMode getRedstoneMode() {
        return ShapeMakerBlockEntity.RedstoneMode.values()[data.get(7)];
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // If from block inventory (slots 0-8)
            if (index < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!itemstack1.isEmpty() &&
                    itemstack1.getItem() instanceof net.minecraft.world.item.BlockItem) {
                if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                    if (index >= 9 && index < 36) {
                        if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 36) {
                        if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
            else if (index >= 9 && index < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36) {
                if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
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
        if (blockEntity == null) return false;
        return blockEntity.getBlockPos().distToCenterSqr(player.position()) < 64;
    }
}