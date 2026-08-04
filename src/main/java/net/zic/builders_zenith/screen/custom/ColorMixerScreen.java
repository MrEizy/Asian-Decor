package net.zic.builders_zenith.screen.custom;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.zic.builders_zenith.BuildersZenith;

import java.util.List;

public class ColorMixerScreen extends AbstractContainerScreen<ColorMixerMenu> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(BuildersZenith.MOD_ID, "textures/gui/container/color_mixer.png");

    private static final Identifier PROGRESS_SPRITE =
            Identifier.fromNamespaceAndPath(BuildersZenith.MOD_ID, "textures/gui/sprites/color_mixer/mixer_progress.png");

    private static final int ARROW_X = 92;
    private static final int ARROW_Y = 33;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 17;

    private static final int ARROW_TEXTURE_X = 190;
    private static final int ARROW_TEXTURE_Y = 11;

    public ColorMixerScreen(ColorMixerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        // Render background
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);

        // Render progress arrow
        int progress = this.menu.getProgress();
        int arrowWidth = (int) ((progress / 100.0) * ARROW_WIDTH);

        if (arrowWidth > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    this.leftPos + ARROW_X,
                    this.topPos + ARROW_Y,
                    (float) ARROW_TEXTURE_X,
                    (float) ARROW_TEXTURE_Y,
                    arrowWidth,
                    ARROW_HEIGHT,
                    256, 256);
        }

        // Draw empty arrow background
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos + ARROW_X,
                this.topPos + ARROW_Y,
                (float) (ARROW_TEXTURE_X + ARROW_WIDTH),
                (float) ARROW_TEXTURE_Y,
                ARROW_WIDTH,
                ARROW_HEIGHT,
                256, 256);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);

        int arrowScreenX = this.leftPos + ARROW_X;
        int arrowScreenY = this.topPos + ARROW_Y;

        if (mouseX >= arrowScreenX && mouseX < arrowScreenX + ARROW_WIDTH &&
                mouseY >= arrowScreenY && mouseY < arrowScreenY + ARROW_HEIGHT) {

            if (this.menu.isProcessing()) {
                int percent = (int) (this.menu.getProgressPercent() * 100);
                List<FormattedCharSequence> lines = List.of(
                        Component.literal(percent + "%").getVisualOrderText()
                );
                graphics.setTooltipForNextFrame(this.font, lines, mouseX, mouseY);
            }
        }
    }
}