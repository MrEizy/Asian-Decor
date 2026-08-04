package net.zic.builders_zenith.screen.custom;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class PouchScreen extends AbstractContainerScreen<PouchMenu> {
    private static final Identifier CONTAINER_TEXTURE =
            Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final Identifier HOTBAR_SELECTOR =
            Identifier.withDefaultNamespace("textures/gui/sprites/hud/hotbar_selection.png");

    public PouchScreen(PouchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 168);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int x = this.leftPos;
        int y = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, x, y, 0.0F, 0.0F,
                this.imageWidth, 71, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, x, y + 71, 0.0F, 126.0F,
                this.imageWidth, 96, 256, 256);

        int selectedSlot = menu.getSelectedSlot();
        if (selectedSlot >= 0 && selectedSlot < 27) {
            int slotX = x + 8 + (selectedSlot % 9) * 18 - 1;
            int slotY = y + 18 + (selectedSlot / 9) * 18 - 1;

            graphics.blit(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTOR, slotX - 3, slotY - 3,
                    0.0F, 0.0F, 24, 24, 24, 24);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.text(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 4210752, false);
    }
}