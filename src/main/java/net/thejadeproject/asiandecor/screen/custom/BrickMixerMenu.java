package net.thejadeproject.asiandecor.screen.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.entity.BrickMixerBlockEntity;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.recipe.BrickMixerRecipe;
import net.thejadeproject.asiandecor.recipe.ModRecipes;
import net.thejadeproject.asiandecor.screen.ModMenuTypes;

import java.util.List;

public class BrickMixerMenu extends AbstractContainerMenu {
    // Slot indices
    public static final int SLOT_BASE = 0;
    public static final int SLOT_BRICK_DYE = 1;
    public static final int SLOT_MORTAR_DYE = 2;
    public static final int SLOT_RESULT = 3;

    // Slot positions from Loom
    public static final int BASE_SLOT_X = 13;        // Banner slot position
    public static final int BASE_SLOT_Y = 26;
    public static final int BRICK_DYE_SLOT_X = 33;   // Dye slot position
    public static final int BRICK_DYE_SLOT_Y = 26;
    public static final int MORTAR_DYE_SLOT_X = 23;  // Pattern slot position
    public static final int MORTAR_DYE_SLOT_Y = 45;
    public static final int RESULT_SLOT_X = 143;     // Carpenter result position
    public static final int RESULT_SLOT_Y = 33;

    public static final int INV_SLOT_START = 4;
    public static final int INV_SLOT_END = 31;
    public static final int USE_ROW_SLOT_START = 31;
    public static final int USE_ROW_SLOT_END = 40;

    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;
    private List<BrickMixerRecipe> recipes = List.of();
    private ItemStack inputBase = ItemStack.EMPTY;
    private long lastSoundTime;
    private final Container container;
    private final ContainerData data;
    private Slot baseSlot;
    private Slot brickDyeSlot;
    private Slot mortarDyeSlot;
    private Slot resultSlot;

    Runnable slotUpdateListener = () -> {};

    public BrickMixerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public BrickMixerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        this(containerId, playerInventory, new SimpleContainer(4), new SimpleContainerData(1), access);
    }

    public BrickMixerMenu(int containerId, Inventory playerInventory, BrickMixerBlockEntity blockEntity, ContainerData data) {
        this(containerId, playerInventory, blockEntity, data, ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()));
    }

    public BrickMixerMenu(int containerId, Inventory playerInventory, Container container, ContainerData data, ContainerLevelAccess access) {
        super(ModMenuTypes.BRICK_MIXER.get(), containerId);
        this.access = access;
        this.level = playerInventory.player.level();
        this.container = container;
        this.data = data;

        // Base block slot - Loom banner position (13, 26)
        this.baseSlot = this.addSlot(new Slot(container, SLOT_BASE, BASE_SLOT_X, BASE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isValidBaseBlock(stack);
            }

            @Override
            public void setChanged() {
                super.setChanged();
                BrickMixerMenu.this.slotUpdateListener.run();
            }
        });

        // Brick dye slot - Loom dye position (33, 26)
        this.brickDyeSlot = this.addSlot(new Slot(container, SLOT_BRICK_DYE, BRICK_DYE_SLOT_X, BRICK_DYE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                BrickMixerMenu.this.slotUpdateListener.run();
            }
        });

        // Mortar dye slot - Loom pattern position (23, 45)
        this.mortarDyeSlot = this.addSlot(new Slot(container, SLOT_MORTAR_DYE, MORTAR_DYE_SLOT_X, MORTAR_DYE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                BrickMixerMenu.this.slotUpdateListener.run();
            }
        });

        // Result slot - Carpenter result position (143, 33)
        this.resultSlot = this.addSlot(new Slot(container, SLOT_RESULT, RESULT_SLOT_X, RESULT_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                BrickMixerMenu.this.access.execute((level, pos) -> {
                    long l = level.getGameTime();
                    if (BrickMixerMenu.this.lastSoundTime != l) {
                        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        BrickMixerMenu.this.lastSoundTime = l;
                    }
                });

                // Consume 8 base items
                BrickMixerMenu.this.baseSlot.remove(8);

                // Consume 1 dye from each slot if present
                if (!BrickMixerMenu.this.brickDyeSlot.getItem().isEmpty()) {
                    BrickMixerMenu.this.brickDyeSlot.remove(1);
                }
                if (!BrickMixerMenu.this.mortarDyeSlot.getItem().isEmpty()) {
                    BrickMixerMenu.this.mortarDyeSlot.remove(1);
                }

                super.onTake(player, stack);
            }
        });

        // Player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);

        if (data.getCount() > 0) {
            this.selectedRecipeIndex.set(data.get(0));
        }
    }

    private boolean isValidBaseBlock(ItemStack stack) {
        return !stack.isEmpty();
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<BrickMixerRecipe> getRecipes() {
        return this.recipes;
    }

    public boolean hasInputItem() {
        return !this.baseSlot.getItem().isEmpty();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.BRICK_MIXER.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidRecipeIndex(id)) {
            this.selectedRecipeIndex.set(id);
            if (this.data != null && this.data.getCount() > 0) {
                this.data.set(0, id);
            }
            this.setupResultSlot();
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidRecipeIndex(int index) {
        return index >= 0 && index < this.recipes.size();
    }

    @Override
    public void slotsChanged(Container container) {
        ItemStack baseStack = this.baseSlot.getItem();
        if (!baseStack.is(this.inputBase.getItem())) {
            this.inputBase = baseStack.copy();
            this.setupRecipeList(container, baseStack);
        }
        this.slotUpdateListener.run();
    }

    private void setupRecipeList(Container container, ItemStack stack) {
        this.recipes = List.of();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);

        if (!stack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipes.BRICK_MIXER_TYPE.get())
                    .stream()
                    .filter(recipe -> recipe.value().matches(new SingleRecipeInput(stack), this.level))
                    .map(recipe -> recipe.value())
                    .toList();
        }

        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            BrickMixerRecipe recipe = this.recipes.get(this.selectedRecipeIndex.get());

            // Check if we have enough base items (8)
            if (this.baseSlot.getItem().getCount() < recipe.getBaseCount()) {
                this.resultSlot.set(ItemStack.EMPTY);
                this.broadcastChanges();
                return;
            }

            ItemStack result = recipe.assemble(new SingleRecipeInput(this.baseSlot.getItem()), this.level.registryAccess());

            DyeColor brickColor = getDyeColorFromSlot(brickDyeSlot);
            DyeColor mortarColor = getDyeColorFromSlot(mortarDyeSlot);

            if (brickColor != null || mortarColor != null) {
                DyeColor finalBrickColor = brickColor != null ? brickColor : DyeColor.WHITE;
                DyeColor finalMortarColor = mortarColor != null ? mortarColor : DyeColor.LIGHT_GRAY;

                DyedBrickData data = new DyedBrickData(finalBrickColor, finalMortarColor);
                result.set(ModDataComponents.BRICK_DATA.get(), data);
            }

            this.resultSlot.set(result);
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    private DyeColor getDyeColorFromSlot(Slot slot) {
        ItemStack stack = slot.getItem();
        if (stack.getItem() instanceof DyeItem dyeItem) {
            return dyeItem.getDyeColor();
        }
        return null;
    }

    @Override
    public MenuType<?> getType() {
        return ModMenuTypes.BRICK_MIXER.get();
    }

    public Slot getBaseSlot() {
        return this.baseSlot;
    }

    public Slot getBrickDyeSlot() {
        return this.brickDyeSlot;
    }

    public Slot getMortarDyeSlot() {
        return this.mortarDyeSlot;
    }

    public Slot getResultSlot() {
        return this.resultSlot;
    }

    public void registerUpdateListener(Runnable listener) {
        this.slotUpdateListener = listener;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == SLOT_RESULT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= INV_SLOT_START && index < USE_ROW_SLOT_END) {
                if (itemstack1.getItem() instanceof DyeItem) {
                    if (!this.moveItemStackTo(itemstack1, SLOT_BRICK_DYE, SLOT_BRICK_DYE + 1, false)) {
                        if (!this.moveItemStackTo(itemstack1, SLOT_MORTAR_DYE, SLOT_MORTAR_DYE + 1, false)) {
                            if (index < INV_SLOT_END) {
                                if (!this.moveItemStackTo(itemstack1, INV_SLOT_END, USE_ROW_SLOT_END, false)) {
                                    return ItemStack.EMPTY;
                                }
                            } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                } else if (!this.moveItemStackTo(itemstack1, SLOT_BASE, SLOT_BASE + 1, false)) {
                    if (index < INV_SLOT_END) {
                        if (!this.moveItemStackTo(itemstack1, INV_SLOT_END, USE_ROW_SLOT_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> this.clearContainer(player, new SimpleContainer(
                this.baseSlot.getItem(),
                this.brickDyeSlot.getItem(),
                this.mortarDyeSlot.getItem()
        )));
    }
}