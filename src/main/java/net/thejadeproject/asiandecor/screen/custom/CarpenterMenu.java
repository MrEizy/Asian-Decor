package net.thejadeproject.asiandecor.screen.custom;
import com.google.common.collect.Lists;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.recipe.CarpenterRecipes;
import net.thejadeproject.asiandecor.recipe.ModRecipes;
import net.thejadeproject.asiandecor.screen.ModMenuTypes;

import java.util.List;

public class CarpenterMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;

    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex;
    private final Level level;
    private List<RecipeHolder<CarpenterRecipes>> recipes = Lists.newArrayList();
    private ItemStack input = ItemStack.EMPTY;
    private long lastSoundTime;

    final Slot inputSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener = () -> {};
    public final Container container;
    final ResultContainer resultContainer;

    public CarpenterMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public CarpenterMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.CARPENTER.get(), containerId);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.access = access;
        this.level = playerInventory.player.level();

        this.container = new SimpleContainer(1) {
            @Override
            public void setChanged() {
                super.setChanged();
                CarpenterMenu.this.slotsChanged(this);
                CarpenterMenu.this.slotUpdateListener.run();
            }
        };

        this.resultContainer = new ResultContainer();

        // Input slot (left side)
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));

        // Result slot (right side) - cannot place items in
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                CarpenterMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());

                // Consume ingredients based on recipe requirement
                RecipeHolder<CarpenterRecipes> recipe = CarpenterMenu.this.recipes.get(CarpenterMenu.this.selectedRecipeIndex.get());
                int required = recipe.value().getIngredientCount();
                ItemStack inputStack = CarpenterMenu.this.inputSlot.getItem();
                inputStack.shrink(required);

                if (inputStack.isEmpty()) {
                    CarpenterMenu.this.inputSlot.set(ItemStack.EMPTY);
                }

                CarpenterMenu.this.setupResultSlot();

                access.execute((level, pos) -> {
                    long time = level.getGameTime();
                    if (CarpenterMenu.this.lastSoundTime != time) {
                        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                                SoundSource.BLOCKS, 1.0F, 1.0F);
                        CarpenterMenu.this.lastSoundTime = time;
                    }
                });

                super.onTake(player, stack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(CarpenterMenu.this.inputSlot.getItem());
            }
        });

        // Player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Hotbar
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<RecipeHolder<CarpenterRecipes>> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.CARPENTER.get()); // Update with your block
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidRecipeIndex(id)) {
            this.selectedRecipeIndex.set(id);
            this.setupResultSlot();
        }
        return true;
    }

    private boolean isValidRecipeIndex(int recipeIndex) {
        return recipeIndex >= 0 && recipeIndex < this.recipes.size();
    }

    @Override
    public void slotsChanged(Container inventory) {
        ItemStack itemstack = this.inputSlot.getItem();
        if (!itemstack.is(this.input.getItem())) {
            this.input = itemstack.copy();
            this.setupRecipeList(inventory, itemstack);
        }
    }

    private static SingleRecipeInput createRecipeInput(Container container) {
        return new SingleRecipeInput(container.getItem(0));
    }

    private void setupRecipeList(Container container, ItemStack stack) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);

        if (!stack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(
                    ModRecipes.CARPENTER_TYPE.get(),
                    createRecipeInput(container),
                    this.level
            );
        }
    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            RecipeHolder<CarpenterRecipes> recipeHolder = this.recipes.get(this.selectedRecipeIndex.get());
            CarpenterRecipes recipe = recipeHolder.value();

            // Check if we have enough ingredients
            ItemStack inputStack = this.inputSlot.getItem();
            if (inputStack.getCount() >= recipe.getIngredientCount()) {
                ItemStack result = recipe.assemble(createRecipeInput(this.container), this.level.registryAccess());
                if (result.isItemEnabled(this.level.enabledFeatures())) {
                    this.resultContainer.setRecipeUsed(recipeHolder);
                    this.resultSlot.set(result);
                } else {
                    this.resultSlot.set(ItemStack.EMPTY);
                }
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    @Override
    public MenuType<?> getType() {
        return ModMenuTypes.CARPENTER.get();
    }

    public void registerUpdateListener(Runnable listener) {
        this.slotUpdateListener = listener;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();

            if (index == RESULT_SLOT) {
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index == INPUT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(
                    ModRecipes.CARPENTER_TYPE.get(),
                    new SingleRecipeInput(itemstack1),
                    this.level).isPresent()) {
                if (!this.moveItemStackTo(itemstack1, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
                if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END
                    && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level, pos) -> {
            this.clearContainer(player, this.container);
        });
    }
}