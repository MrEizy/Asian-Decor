package net.thejadeproject.asiandecor.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.recipe.ColorMixerRecipe;
import net.thejadeproject.asiandecor.recipe.ModRecipes;
import net.thejadeproject.asiandecor.screen.custom.ColorMixerMenu;

import java.util.Optional;

public class ColorMixerBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_BASE = 0;
    public static final int SLOT_PRIMARY_DYE = 1;
    public static final int SLOT_SECONDARY_DYE = 2;
    public static final int SLOT_RESULT = 3;
    public static final int CONTAINER_SIZE = 4;

    public final ItemStackHandler itemHandler = new ItemStackHandler(CONTAINER_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

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
        if (level.isClientSide) return;


        Optional<ColorMixerRecipe> recipeOpt = getCurrentRecipe();

        if (recipeOpt.isPresent()) {
            ColorMixerRecipe recipe = recipeOpt.get();
            DyeColor primary = getPrimaryDyeColor();
            DyeColor secondary = getSecondaryDyeColor();

            if (recipe.canCraft(itemHandler.getStackInSlot(SLOT_BASE), primary, secondary)) {
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

    private Optional<ColorMixerRecipe> getCurrentRecipe() {
        if (level == null) return Optional.empty();

        ItemStack baseStack = itemHandler.getStackInSlot(SLOT_BASE);
        DyeColor primary = getPrimaryDyeColor();
        DyeColor secondary = getSecondaryDyeColor();


        if (baseStack.isEmpty()) {
            return Optional.empty();
        }

        var recipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.COLOR_MIXER_TYPE.get());

        for (var holder : recipes) {
            ColorMixerRecipe recipe = holder.value();
            boolean baseMatches = !recipe.getIngredients().isEmpty() && recipe.getIngredients().get(0).test(baseStack);

            if (baseMatches && recipe.canCraft(baseStack, primary, secondary)) {
                return Optional.of(recipe);
            }
        }

        return Optional.empty();
    }

    private void craftItem(ColorMixerRecipe recipe, DyeColor primary, DyeColor secondary) {
        itemHandler.extractItem(SLOT_BASE, recipe.getBaseCount(), false);

        if (!itemHandler.getStackInSlot(SLOT_PRIMARY_DYE).isEmpty()) {
            itemHandler.extractItem(SLOT_PRIMARY_DYE, 1, false);
        }
        if (!itemHandler.getStackInSlot(SLOT_SECONDARY_DYE).isEmpty()) {
            itemHandler.extractItem(SLOT_SECONDARY_DYE, 1, false);
        }

        ItemStack result = recipe.assembleWithDyes(primary, secondary);

        ItemStack currentResult = itemHandler.getStackInSlot(SLOT_RESULT);
        if (currentResult.isEmpty()) {
            itemHandler.setStackInSlot(SLOT_RESULT, result);
        } else if (ItemStack.isSameItemSameComponents(currentResult, result)) {
            if (currentResult.getCount() + result.getCount() <= currentResult.getMaxStackSize()) {
                currentResult.grow(result.getCount());
                itemHandler.setStackInSlot(SLOT_RESULT, currentResult);
            }
        }

        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.asiandecor.color_mixer");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ColorMixerMenu(containerId, inventory, this, this.dataAccess);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("Progress", this.progress);
        tag.putInt("TotalProgress", this.totalProgress);
        tag.putBoolean("IsProcessing", this.isProcessing);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        this.progress = tag.getInt("Progress");
        this.totalProgress = tag.getInt("TotalProgress");
        this.isProcessing = tag.getBoolean("IsProcessing");
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

    public DyeColor getPrimaryDyeColor() {
        ItemStack dyeStack = itemHandler.getStackInSlot(SLOT_PRIMARY_DYE);
        if (dyeStack.getItem() instanceof DyeItem dyeItem) {
            return dyeItem.getDyeColor();
        }
        return null;
    }

    public DyeColor getSecondaryDyeColor() {
        ItemStack dyeStack = itemHandler.getStackInSlot(SLOT_SECONDARY_DYE);
        if (dyeStack.getItem() instanceof DyeItem dyeItem) {
            return dyeItem.getDyeColor();
        }
        return null;
    }
}