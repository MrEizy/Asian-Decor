package net.thejadeproject.asiandecor.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.thejadeproject.asiandecor.AsianDecor;

public class ColorMixerScreen extends AbstractContainerScreen<ColorMixerMenu> {
    private static final ResourceLocation BG_LOCATION =
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "textures/gui/container/color_mixer.png");

    // Progress sprite location (points to assets/asiandecor/textures/gui/sprites/color_mixer/mixer_progress.png)
    private static final ResourceLocation PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "textures/gui/sprites/color_mixer/mixer_progress.png");

    // Arrow position in GUI (where the empty arrow background is)
    private static final int ARROW_X = 93;
    private static final int ARROW_Y = 33;
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 16;

    public ColorMixerScreen(ColorMixerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
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

        // Background texture (contains the empty arrow at 93, 33)
        guiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render progress arrow fill using sprite
        float progress = this.menu.getProgressPercent();
        if (progress > 0) {
            int fillWidth = (int) (ARROW_WIDTH * progress);
            if (fillWidth > 0) {
                // Draw partial sprite - only the filled portion
                // Parameters: sprite, spriteWidth, spriteHeight, spriteX, spriteY, screenX, screenY, width, height
                guiGraphics.blitSprite(
                        PROGRESS_SPRITE,
                        ARROW_WIDTH, ARROW_HEIGHT,  // sprite texture size
                        0, 0,                        // sprite x, y (start at top-left of sprite)
                        i + ARROW_X, j + ARROW_Y,    // screen position
                        fillWidth, ARROW_HEIGHT      // width to render (clipped by progress), full height
                );
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        // Tooltip for progress arrow showing percentage
        int arrowScreenX = this.leftPos + ARROW_X;
        int arrowScreenY = this.topPos + ARROW_Y;

        if (x >= arrowScreenX && x < arrowScreenX + ARROW_WIDTH &&
                y >= arrowScreenY && y < arrowScreenY + ARROW_HEIGHT) {

            if (this.menu.isProcessing()) {
                int percent = (int) (this.menu.getProgressPercent() * 100);
                guiGraphics.renderTooltip(this.font,
                        Component.literal(percent + "%"), x, y);
            }
        }
    }
}