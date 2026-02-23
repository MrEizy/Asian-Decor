package net.thejadeproject.asiandecor.screen.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.recipe.BrickMixerRecipe;

import java.util.List;

public class BrickMixerScreen extends AbstractContainerScreen<BrickMixerMenu> {
    // Custom GUI texture (background only)
    private static final ResourceLocation BG_LOCATION =
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "textures/gui/container/brick_mixer.png");

    // All sprites from vanilla stonecutter (same as Carpenter)
    private static final ResourceLocation SCROLLER_SPRITE =
            ResourceLocation.withDefaultNamespace("container/stonecutter/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE =
            ResourceLocation.withDefaultNamespace("container/stonecutter/scroller_disabled");
    private static final ResourceLocation RECIPE_SELECTED_SPRITE =
            ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_selected");
    private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE =
            ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_highlighted");
    private static final ResourceLocation RECIPE_SPRITE =
            ResourceLocation.withDefaultNamespace("container/stonecutter/recipe");

    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 52;
    private static final int RECIPES_Y = 14;

    // Scroller Y offset - moved down 1 pixel from 12 to 13 (1 pixel up from original 15)
    private static final int SCROLLER_Y_OFFSET = 13;
    // Click area Y offset - moved down 1 pixel from 6 to 7 (1 pixel up from original 9)
    private static final int SCROLLER_CLICK_Y_OFFSET = 7;
    // Drag area Y start - moved down 1 pixel from 11 to 12 (1 pixel up from original 14)
    private static final int SCROLLER_DRAG_Y_START = 12;

    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public BrickMixerScreen(BrickMixerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        menu.registerUpdateListener(this::containerChanged);
        --this.titleLabelY;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;

        // Background texture
        guiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Scrollbar with enabled/disabled states (moved down 1 pixel from previous)
        int k = (int)(41.0F * this.scrollOffs);
        ResourceLocation scrollerSprite = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        guiGraphics.blitSprite(scrollerSprite, i + 119, j + SCROLLER_Y_OFFSET + k, 12, 15);

        // Recipe buttons area
        int l = this.leftPos + RECIPES_X;
        int i1 = this.topPos + RECIPES_Y;
        int j1 = this.startIndex + 12;

        this.renderButtons(guiGraphics, mouseX, mouseY, l, i1, j1);
        this.renderRecipes(guiGraphics, l, i1, j1);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        if (this.displayRecipes) {
            int i = this.leftPos + RECIPES_X;
            int j = this.topPos + RECIPES_Y;
            int k = this.startIndex + 12;
            List<BrickMixerRecipe> list = this.menu.getRecipes();

            for (int l = this.startIndex; l < k && l < this.menu.getRecipes().size(); ++l) {
                int i1 = l - this.startIndex;
                int j1 = i + i1 % 4 * 16;
                int k1 = j + i1 / 4 * 18 + 2;

                if (x >= j1 && x < j1 + 16 && y >= k1 && y < k1 + 18) {
                    ItemStack result = list.get(l).getResultItem(this.minecraft.level.registryAccess());

                    // Preview with current dye colors
                    ItemStack previewStack = result.copy();
                    DyeColor brickColor = getBrickDyeColor();
                    DyeColor mortarColor = getMortarDyeColor();

                    if (brickColor != null || mortarColor != null) {
                        DyedBrickData data = new DyedBrickData(
                                brickColor != null ? brickColor : DyeColor.WHITE,
                                mortarColor != null ? mortarColor : DyeColor.LIGHT_GRAY
                        );
                        previewStack.set(ModDataComponents.BRICK_DATA.get(), data);
                    }

                    guiGraphics.renderTooltip(this.font, previewStack, x, y);
                }
            }
        }
    }

    private DyeColor getBrickDyeColor() {
        if (this.menu.getBrickDyeSlot().hasItem()) {
            net.minecraft.world.item.ItemStack stack = this.menu.getBrickDyeSlot().getItem();
            if (stack.getItem() instanceof net.minecraft.world.item.DyeItem dyeItem) {
                return dyeItem.getDyeColor();
            }
        }
        return null;
    }

    private DyeColor getMortarDyeColor() {
        if (this.menu.getMortarDyeSlot().hasItem()) {
            net.minecraft.world.item.ItemStack stack = this.menu.getMortarDyeSlot().getItem();
            if (stack.getItem() instanceof net.minecraft.world.item.DyeItem dyeItem) {
                return dyeItem.getDyeColor();
            }
        }
        return null;
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int lastVisibleElementIndex) {
        for (int i = this.startIndex; i < lastVisibleElementIndex && i < this.menu.getRecipes().size(); ++i) {
            int j = i - this.startIndex;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int i1 = y + l * 18 + 2;

            // Use sprites for recipe buttons (same as Carpenter)
            ResourceLocation sprite;
            if (i == this.menu.getSelectedRecipeIndex()) {
                sprite = RECIPE_SELECTED_SPRITE;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                sprite = RECIPE_HIGHLIGHTED_SPRITE;
            } else {
                sprite = RECIPE_SPRITE;
            }

            guiGraphics.blitSprite(sprite, k, i1 - 1, 16, 18);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int startIndex) {
        List<BrickMixerRecipe> list = this.menu.getRecipes();

        for (int i = this.startIndex; i < startIndex && i < list.size(); ++i) {
            int j = i - this.startIndex;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int i1 = y + l * 18 + 2;

            ItemStack result = list.get(i).getResultItem(this.minecraft.level.registryAccess());

            // Apply current dye colors for preview
            ItemStack displayStack = result.copy();
            DyeColor brickColor = getBrickDyeColor();
            DyeColor mortarColor = getMortarDyeColor();

            if (brickColor != null || mortarColor != null) {
                DyedBrickData data = new DyedBrickData(
                        brickColor != null ? brickColor : DyeColor.WHITE,
                        mortarColor != null ? mortarColor : DyeColor.LIGHT_GRAY
                );
                displayStack.set(ModDataComponents.BRICK_DATA.get(), data);
            }

            guiGraphics.renderItem(displayStack, k, i1);
            guiGraphics.renderItemDecorations(this.font, displayStack, k, i1);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;

        if (this.displayRecipes) {
            int i = this.leftPos + RECIPES_X;
            int j = this.topPos + RECIPES_Y;
            int k = this.startIndex + 12;

            for (int l = this.startIndex; l < k; ++l) {
                int i1 = l - this.startIndex;
                double d0 = mouseX - (double)(i + i1 % 4 * 16);
                double d1 = mouseY - (double)(j + i1 / 4 * 18);

                if (d0 >= 0.0 && d1 >= 0.0 && d0 < 16.0 && d1 < 18.0
                        && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
                    return true;
                }
            }
        }

        // Check if clicking scrollbar (moved down 1 pixel)
        int i = this.leftPos + 119;
        int j = this.topPos + SCROLLER_CLICK_Y_OFFSET;
        if (mouseX >= i && mouseX < i + 12 && mouseY >= j && mouseY < j + 54) {
            this.scrolling = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            // Drag start moved down 1 pixel
            int i = this.topPos + SCROLLER_DRAG_Y_START;
            int j = i + 54;
            this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5) * 4;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float)scrollY / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
        }
        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getRecipes().size() > 12;
    }

    protected int getOffscreenRows() {
        return (this.menu.getRecipes().size() + 4 - 1) / 4 - 3;
    }

    private void containerChanged() {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }
}