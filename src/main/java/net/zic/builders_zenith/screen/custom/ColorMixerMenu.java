package net.zic.builders_zenith.screen.custom;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.entity.ColorMixerBlockEntity;
import net.zic.builders_zenith.screen.ModMenuTypes;

public class ColorMixerMenu extends AbstractContainerMenu {
    public static final int SLOT_BASE = 0;
    public static final int SLOT_PRIMARY_DYE = 1;
    public static final int SLOT_SECONDARY_DYE = 2;
    public static final int SLOT_RESULT = 3;

    public static final int BASE_SLOT_X = 13;
    public static final int BASE_SLOT_Y = 26;
    public static final int PRIMARY_DYE_SLOT_X = 33;
    public static final int PRIMARY_DYE_SLOT_Y = 26;
    public static final int SECONDARY_DYE_SLOT_X = 23;
    public static final int SECONDARY_DYE_SLOT_Y = 45;
    public static final int RESULT_SLOT_X = 143;
    public static final int RESULT_SLOT_Y = 33;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    public final ColorMixerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public ColorMixerMenu(int containerId, Inventory inv, ColorMixerBlockEntity entity, ContainerData data) {
        super(ModMenuTypes.COLOR_MIXER.get(), containerId);
        this.blockEntity = entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerHotbar(inv);
        addPlayerInventory(inv);

        // Base slot - only bricks or dyed bricks
        this.addSlot(new SlotItemHandler(entity.itemHandler, SLOT_BASE, BASE_SLOT_X, BASE_SLOT_Y));

        // Primary dye slot - only dyes
        this.addSlot(new SlotItemHandler(entity.itemHandler, SLOT_PRIMARY_DYE, PRIMARY_DYE_SLOT_X, PRIMARY_DYE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }
        });

        // Secondary dye slot - only dyes
        this.addSlot(new SlotItemHandler(entity.itemHandler, SLOT_SECONDARY_DYE, SECONDARY_DYE_SLOT_X, SECONDARY_DYE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }
        });

        // Result slot - CANNOT place items in, but CAN ALWAYS pickup (even while processing)
        this.addSlot(new SlotItemHandler(entity.itemHandler, SLOT_RESULT, RESULT_SLOT_X, RESULT_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // Cannot insert items
            }

            @Override
            public boolean mayPickup(Player player) {
                return true; // ALWAYS allow pickup, even while crafting
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                // Optional: Reset progress when output is taken
                // blockEntity.resetProgress();
            }
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.COLOR_MIXER.get());
    }

    public boolean isProcessing() {
        return this.data.get(2) != 0;
    }

    public int getProgress() {
        return this.data.get(0);
    }

    public int getTotalProgress() {
        return this.data.get(1);
    }

    public float getProgressPercent() {
        int total = getTotalProgress();
        if (total <= 0) return 0.0f;
        return (float) getProgress() / (float) total;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if clicking the result slot (slot index 3 in TE inventory)
        if (index == TE_INVENTORY_FIRST_SLOT_INDEX + SLOT_RESULT) {
            // Move from result slot to player inventory (ALLOWED even while processing)
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }

            sourceSlot.onQuickCraft(sourceStack, copyOfSourceStack);
        } else if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // Player inventory to TE
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // TE to player inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}