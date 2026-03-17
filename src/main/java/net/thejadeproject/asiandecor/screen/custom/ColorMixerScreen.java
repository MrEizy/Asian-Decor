package net.thejadeproject.asiandecor.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.thejadeproject.asiandecor.AsianDecor;

public class ColorMixerScreen extends AbstractContainerScreen<ColorMixerMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "textures/gui/container/color_mixer.png");

    // Progress sprite location (points to assets/asiandecor/textures/gui/sprites/color_mixer/mixer_progress.png)
    private static final ResourceLocation PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath(AsianDecor.MOD_ID, "textures/gui/sprites/color_mixer/mixer_progress.png");

    private static final int ARROW_X = 92;      // Screen X position of arrow
    private static final int ARROW_Y = 33;      // Screen Y position of arrow
    private static final int ARROW_WIDTH = 24;  // Arrow width
    private static final int ARROW_HEIGHT = 17; // Arrow height

    private static final int ARROW_TEXTURE_X = 190;   // X in PNG where arrow starts
    private static final int ARROW_TEXTURE_Y = 11;     // Y in PNG where arrow starts

    public ColorMixerScreen(ColorMixerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Render background
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // Render progress arrow
        int progress = this.menu.getProgress(); // 0-100 or whatever scale
        int arrowWidth = (int) ((progress / 100.0) * ARROW_WIDTH);

        if (arrowWidth > 0) {
            // Draw the filled portion of the arrow
            graphics.blit(TEXTURE,
                    this.leftPos + ARROW_X,           // Screen X
                    this.topPos + ARROW_Y,            // Screen Y
                    ARROW_TEXTURE_X,                   // Texture X (filled arrow)
                    ARROW_TEXTURE_Y,                   // Texture Y
                    arrowWidth,                        // Width to draw (progress-based)
                    ARROW_HEIGHT,                      // Full height
                    256, 256);                         // Texture file size
        }

        // Optionally draw empty arrow background first if your texture has one
        graphics.blit(TEXTURE, this.leftPos + ARROW_X, this.topPos + ARROW_Y,
             ARROW_TEXTURE_X + ARROW_WIDTH, ARROW_TEXTURE_Y, ARROW_WIDTH, ARROW_HEIGHT, 256, 256);
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