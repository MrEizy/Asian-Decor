package net.zic.builders_zenith.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;
import net.zic.builders_zenith.recipe.ModRecipes;
import net.zic.builders_zenith.screen.custom.ColorMixerMenu;

import java.util.Optional;

public class ColorMixerBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_BASE = 0;
    public static final int SLOT_PRIMARY_DYE = 1;
    public static final int SLOT_SECONDARY_DYE = 2;
    public static final int SLOT_RESULT = 3;
    public static final int CONTAINER_SIZE = 4;

    // Replaces ItemStackHandler — SimpleContainer is vanilla-stable and works with standard Slot
    private final SimpleContainer container = new SimpleContainer(CONTAINER_SIZE) {
        @Override
        public void setChanged() {
            super.setChanged();
            ColorMixerBlockEntity.this.setChanged();
            Level lvl = ColorMixerBlockEntity.this.level;
            if (lvl != null && !lvl.isClientSide()) {
                lvl.sendBlockUpdated(
                        ColorMixerBlockEntity.this.getBlockPos(),
                        ColorMixerBlockEntity.this.getBlockState(),
                        ColorMixerBlockEntity.this.getBlockState(),
                        3
                );
            }
        }
    };

    /** Expose the container so ColorMixerMenu can create standard Slot instances. */
    public SimpleContainer getContainer() {
        return container;
    }

    /** Compatibility helpers that mirror the old ItemStackHandler API. */
    public ItemStack getStackInSlot(int slot) {
        return container.getItem(slot);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        container.setItem(slot, stack);
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (simulate) {
            ItemStack current = container.getItem(slot);
            if (current.isEmpty()) return ItemStack.EMPTY;
            int toExtract = Math.min(amount, current.getCount());
            ItemStack result = current.copy();
            result.setCount(toExtract);
            return result;
        }
        return container.removeItem(slot, amount);
    }

    public int getSlots() {
        return container.getContainerSize();
    }

    private int progress = 0;
    private int totalProgress = 0;
    private boolean isProcessing = false;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> totalProgress;
                case 2 -> isProcessing ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> totalProgress = value;
                case 2 -> isProcessing = value != 0;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public ColorMixerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COLOR_MIXER.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        if (isResultSlotFull()) {
            if (isProcessing || progress > 0) {
                progress = 0;
                isProcessing = false;
                setChanged();
            }
            return;
        }

        Optional<ColorMixerRecipe> recipeOpt = getCurrentRecipe();

        if (recipeOpt.isPresent()) {
            ColorMixerRecipe recipe = recipeOpt.get();
            DyeColor primary = getPrimaryDyeColor();
            DyeColor secondary = getSecondaryDyeColor();

            if (recipe.canCraft(getStackInSlot(SLOT_BASE), primary, secondary)) {
                if (!canFitResult(recipe, primary, secondary)) {
                    if (isProcessing || progress > 0) {
                        progress = 0;
                        isProcessing = false;
                        setChanged();
                    }
                    return;
                }

                if (!isProcessing) {
                    isProcessing = true;
                    totalProgress = recipe.getProcessingTime();
                    progress = 0;
                    setChanged();
                }

                progress++;
                setChanged();

                if (progress >= totalProgress) {
                    craftItem(recipe, primary, secondary);
                    progress = 0;
                    isProcessing = false;
                    setChanged();
                }
            } else {
                if (isProcessing || progress > 0) {
                    progress = 0;
                    isProcessing = false;
                    setChanged();
                }
            }
        } else {
            if (isProcessing || progress > 0) {
                progress = 0;
                isProcessing = false;
                setChanged();
            }
        }
    }

    private boolean isResultSlotFull() {
        ItemStack resultStack = getStackInSlot(SLOT_RESULT);
        if (resultStack.isEmpty()) {
            return false;
        }
        return resultStack.getCount() >= resultStack.getMaxStackSize();
    }

    private boolean canFitResult(ColorMixerRecipe recipe, DyeColor primary, DyeColor secondary) {
        ItemStack result = recipe.assembleWithDyes(primary, secondary);
        ItemStack currentResult = getStackInSlot(SLOT_RESULT);

        if (currentResult.isEmpty()) {
            return true;
        }

        if (isSameItemAndComponents(currentResult, result)) {
            int newCount = currentResult.getCount() + result.getCount();
            return newCount <= currentResult.getMaxStackSize();
        }

        return false;
    }

    /**
     * 1.21.2+: Level#getRecipeManager was replaced by #recipeAccess (RecipeAccess on Level,
     * RecipeManager on ServerLevel). Recipes are now looked up through RecipeManager#recipeMap
     * rather than iterating getAllRecipesFor by hand — the map already pre-filters candidates
     * via Recipe#matches (which for this recipe only checks the base ingredient), so we just
     * need to layer the full canCraft(base, primary, secondary) dye check on top.
     */
    private Optional<ColorMixerRecipe> getCurrentRecipe() {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return Optional.empty();
        }

        ItemStack baseStack = getStackInSlot(SLOT_BASE);
        if (baseStack.isEmpty()) {
            return Optional.empty();
        }

        DyeColor primary = getPrimaryDyeColor();
        DyeColor secondary = getSecondaryDyeColor();
        SingleRecipeInput input = new SingleRecipeInput(baseStack);

        return serverLevel.recipeAccess()
                .recipeMap()
                .getRecipesFor(ModRecipes.COLOR_MIXER_TYPE.get(), input, serverLevel)
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.canCraft(baseStack, primary, secondary))
                .findFirst();
    }

    private void craftItem(ColorMixerRecipe recipe, DyeColor primary, DyeColor secondary) {
        extractItem(SLOT_BASE, recipe.getBaseCount(), false);

        if (!getStackInSlot(SLOT_PRIMARY_DYE).isEmpty()) {
            extractItem(SLOT_PRIMARY_DYE, 1, false);
        }
        if (!getStackInSlot(SLOT_SECONDARY_DYE).isEmpty()) {
            extractItem(SLOT_SECONDARY_DYE, 1, false);
        }

        ItemStack result = recipe.assembleWithDyes(primary, secondary);
        ItemStack currentResult = getStackInSlot(SLOT_RESULT);

        if (currentResult.isEmpty()) {
            setStackInSlot(SLOT_RESULT, result);
        } else if (isSameItemAndComponents(currentResult, result)) {
            if (currentResult.getCount() + result.getCount() <= currentResult.getMaxStackSize()) {
                currentResult.grow(result.getCount());
                setStackInSlot(SLOT_RESULT, currentResult);
            }
        }

        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.builders_zenith.color_mixer");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ColorMixerMenu(containerId, inventory, this, this.dataAccess);
    }

    public void drops() {
        SimpleContainer temp = new SimpleContainer(container.getContainerSize());
        for (int i = 0; i < container.getContainerSize(); i++) {
            temp.setItem(i, container.getItem(i));
        }
        Containers.dropContents(this.level, this.getBlockPos(), temp);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        saveItems(output);
        output.putInt("Progress", this.progress);
        output.putInt("TotalProgress", this.totalProgress);
        output.putBoolean("IsProcessing", this.isProcessing);
    }

    private void saveItems(ValueOutput output) {
        NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.set(i, container.getItem(i));
        }
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        loadItems(input);
        this.progress = input.getIntOr("Progress", 0);
        this.totalProgress = input.getIntOr("TotalProgress", 0);
        this.isProcessing = input.getBooleanOr("IsProcessing", false);
    }

    private void loadItems(ValueInput input) {
        NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
        for (int i = 0; i < items.size(); i++) {
            container.setItem(i, items.get(i));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    public ContainerData getDataAccess() {
        return this.dataAccess;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getTotalProgress() {
        return this.totalProgress;
    }

    public boolean isProcessing() {
        return this.isProcessing;
    }

    /**
     * 26.1+: DyeItem no longer carries a DyeColor (constructor + #getDyeColor were removed).
     * Dye color is now the DataComponents.DYE component on the stack — present on vanilla
     * dyes and on any modded item that opts in via Item.Properties#component(DataComponents.DYE, ...).
     */
    public DyeColor getPrimaryDyeColor() {
        return getStackInSlot(SLOT_PRIMARY_DYE).get(DataComponents.DYE);
    }

    public DyeColor getSecondaryDyeColor() {
        return getStackInSlot(SLOT_SECONDARY_DYE).get(DataComponents.DYE);
    }

    /** 1.21.4 replacement for ItemStack.isSameItemSameComponents */
    private static boolean isSameItemAndComponents(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        if (!ItemStack.isSameItem(a, b)) return false;
        ItemStack a1 = a.copy();
        a1.setCount(1);
        ItemStack b1 = b.copy();
        b1.setCount(1);
        return ItemStack.matches(a1, b1);
    }
}