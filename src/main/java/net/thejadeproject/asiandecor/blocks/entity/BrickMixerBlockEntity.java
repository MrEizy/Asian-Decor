package net.thejadeproject.asiandecor.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.asiandecor.screen.custom.BrickMixerMenu;

public class BrickMixerBlockEntity extends BaseContainerBlockEntity {
    public static final int SLOT_BASE = 0;
    public static final int SLOT_BRICK_DYE = 1;
    public static final int SLOT_MORTAR_DYE = 2;
    public static final int SLOT_RESULT = 3;
    public static final int CONTAINER_SIZE = 4;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private int selectedRecipeIndex = -1;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? selectedRecipeIndex : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                selectedRecipeIndex = value;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public BrickMixerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRICK_MIXER.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.asiandecor.brick_mixer");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, stack);
        this.items.set(index, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if (index < 3 && !flag) {
            this.selectedRecipeIndex = -1;
            this.setChanged();
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        // This ensures the menu gets notified when items change
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        // Use the constructor that matches what we defined in BrickMixerMenu
        return new BrickMixerMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("SelectedRecipeIndex", this.selectedRecipeIndex);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.selectedRecipeIndex = tag.getInt("SelectedRecipeIndex");
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex;
    }

    public void setSelectedRecipeIndex(int index) {
        this.selectedRecipeIndex = index;
        this.setChanged();
    }

    public ContainerData getDataAccess() {
        return this.dataAccess;
    }

    // Helper method to get dye colors from slots - Fixed to use instanceof check
    public DyeColor getBrickDyeColor() {
        ItemStack dyeStack = this.items.get(SLOT_BRICK_DYE);
        return getDyeColorFromItem(dyeStack);
    }

    public DyeColor getMortarDyeColor() {
        ItemStack dyeStack = this.items.get(SLOT_MORTAR_DYE);
        return getDyeColorFromItem(dyeStack);
    }

    private DyeColor getDyeColorFromItem(ItemStack stack) {
        if (stack.isEmpty()) return null;

        // Check if item is a DyeItem
        if (stack.getItem() instanceof DyeItem dyeItem) {
            return dyeItem.getDyeColor();
        }
        return null;
    }
}