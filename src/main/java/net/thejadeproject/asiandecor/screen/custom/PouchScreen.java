package net.thejadeproject.asiandecor.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class PouchScreen extends AbstractContainerScreen<PouchMenu> {
    private static final ResourceLocation CONTAINER_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final ResourceLocation HOTBAR_SELECTOR =
            ResourceLocation.withDefaultNamespace("textures/gui/sprites/hud/hotbar_selection.png");

    public PouchScreen(PouchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 168; // Taller for 27 slots
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(CONTAINER_TEXTURE, x, y, 0, 0, this.imageWidth, 71);
        guiGraphics.blit(CONTAINER_TEXTURE, x, y + 71, 0, 126, this.imageWidth, 96);

        int selectedSlot = menu.getSelectedSlot();
        if (selectedSlot >= 0 && selectedSlot < 27) {
            int slotX = x + 8 + (selectedSlot % 9) * 18 - 1;
            int slotY = y + 18 + (selectedSlot / 9) * 18 - 1;

            guiGraphics.blit(HOTBAR_SELECTOR, slotX - 3, slotY - 3, 0, 0, 24, 24, 24, 24);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 4210752, false);
    }
}