package net.zic.builders_zenith.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.entity.ShapeMakerBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShapeMakerScreen extends AbstractContainerScreen<ShapeMakerMenu> {

    // Vanilla generic_54 texture
    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(BuildersZenith.MOD_ID, "textures/gui/container/shape_maker.png");

    // GUI dimensions from vanilla generic_54
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 228; // 6 rows chest (114) + 3 rows player (54) + spacing

    // Color scheme
    private static final int COLOR_PANEL_BG = 0xCC2C3E50;      // Semi-transparent dark blue
    private static final int COLOR_PANEL_BORDER = 0xFF3498DB; // Bright blue accent
    private static final int COLOR_TEXT = 0xFFECF0F1;         // Off-white
    private static final int COLOR_TEXT_DIM = 0xFFBDC3C7;     // Gray
    private static final int COLOR_SUCCESS = 0xFF2ECC71;      // Green
    private static final int COLOR_WARNING = 0xFFF39C12;      // Orange
    private static final int COLOR_DANGER = 0xFFE74C3C;      // Red
    private static final int COLOR_SLIDER_TRACK = 0xFF1A252F;
    private static final int COLOR_SLIDER_FILL = 0xFF3498DB;

    // ADJUSTMENT: Lower scroll and config part by 8 pixels
    private static final int PANEL_Y_OFFSET = 4;

    // ADJUSTMENT: Lower hotbar by 12 pixels (from 198 to 210)
    private static final int HOTBAR_Y = 204;

    // ADJUSTMENT: Lower player inventory by 12 pixels (from 140 to 152)
    private static final int PLAYER_INV_Y = 146;

    // ADJUSTMENT: Lower inventory text by 17 pixels (calculated based on new positions)

    private ShapeScrollPanel scrollPanel;
    private DraggableSlider xSlider, ySlider, zSlider, radiusSlider, thicknessSlider;
    private IconButton previewButton;
    private IconButton redstoneButton;

    // For slider persistence - track if we've received initial data
    private boolean slidersInitialized = false;

    public ShapeMakerScreen(ShapeMakerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        // ADJUSTMENT: Lower inventory label by 17 pixels (from -94 to -77 relative to bottom)
        this.inventoryLabelY = this.imageHeight - 93;
    }

    @Override
    protected void init() {
        super.init();

        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;

        // IMPORTANT: Always sync from BE first
        menu.syncDataFromBE();

        // Get current values
        int currentX = menu.getXOffset();
        int currentY = menu.getYOffset();
        int currentZ = menu.getZOffset();
        int currentRadius = menu.getRadius();
        int currentThickness = menu.getThickness();
        boolean currentPreview = menu.isPreviewEnabled();
        ShapeMakerBlockEntity.ShapeType currentShape = menu.getSelectedShape();
        ShapeMakerBlockEntity.RedstoneMode currentMode = menu.getRedstoneMode();

        // ============================================
        // LEFT PANEL: Shape Selector (covers chest rows 0-4)
        // ADJUSTMENT: Lowered by PANEL_Y_OFFSET (8 pixels)
        // ============================================
        scrollPanel = new ShapeScrollPanel(
                minecraft,
                75,  // width (fits in left chest area)
                85,  // height (covers 5 rows: 18-104)
                guiTop + 18 + PANEL_Y_OFFSET,  // top aligned with chest + offset
                guiLeft + 8,  // left aligned with chest slots
                this::onShapeSelected
        );
        scrollPanel.setSelected(currentShape);
        addRenderableWidget(scrollPanel);

        // ============================================
        // RIGHT PANEL: Configuration (covers chest rows 0-4, right side)
        // ADJUSTMENT: Lowered by PANEL_Y_OFFSET (8 pixels)
        // ============================================
        int panelX = guiLeft + 88;
        int panelY = guiTop + 18 + PANEL_Y_OFFSET;  // ADJUSTED
        int sliderWidth = 78;

        // Compact sliders with proper labels
        xSlider = new DraggableSlider(panelX, panelY, sliderWidth, 14,
                "X", -50, 50, currentX, this::updateXOffset);
        addRenderableWidget(xSlider);

        ySlider = new DraggableSlider(panelX, panelY + 18, sliderWidth, 14,
                "Y", 0, 100, currentY, this::updateYOffset);
        addRenderableWidget(ySlider);

        zSlider = new DraggableSlider(panelX, panelY + 36, sliderWidth, 14,
                "Z", -50, 50, currentZ, this::updateZOffset);
        addRenderableWidget(zSlider);

        radiusSlider = new DraggableSlider(panelX, panelY + 54, sliderWidth, 14,
                "Radius", 1, 50, currentRadius, this::updateRadius);
        addRenderableWidget(radiusSlider);

        thicknessSlider = new DraggableSlider(panelX, panelY + 72, sliderWidth, 14,
                "Thick", 1, 20, currentThickness, this::updateThickness);
        addRenderableWidget(thicknessSlider);

        // ============================================
        // TOP RIGHT: Control buttons (small, in chest header area)
        // ============================================
        int btnSize = 14;
        int btnY = guiTop + 4;

        // Redstone mode button
        redstoneButton = new IconButton(
                guiLeft + imageWidth - 36, btnY, btnSize, btnSize,
                getRedstoneIcon(currentMode),
                getRedstoneColor(currentMode),
                btn -> cycleRedstoneMode((IconButton) btn)
        );
        redstoneButton.setTooltip(Tooltip.create(Component.literal("Redstone: " + currentMode.getDisplayName())));
        addRenderableWidget(redstoneButton);

        // Preview button
        previewButton = new IconButton(
                guiLeft + imageWidth - 20, btnY, btnSize, btnSize,
                currentPreview ? "👁" : "✕",
                currentPreview ? COLOR_SUCCESS : COLOR_DANGER,
                btn -> togglePreview((IconButton) btn)
        );
        previewButton.setTooltip(Tooltip.create(Component.literal(currentPreview ? "Preview ON" : "Preview OFF")));
        addRenderableWidget(previewButton);

        slidersInitialized = true;
    }

    private String getRedstoneIcon(ShapeMakerBlockEntity.RedstoneMode mode) {
        return switch (mode) {
            case OFF -> "✕";
            case PULSE -> "⚡";
            case CONSTANT -> "∞";
        };
    }

    private int getRedstoneColor(ShapeMakerBlockEntity.RedstoneMode mode) {
        return switch (mode) {
            case OFF -> COLOR_DANGER;
            case PULSE -> COLOR_WARNING;
            case CONSTANT -> COLOR_SUCCESS;
        };
    }

    private void updateXOffset(int value) { if (slidersInitialized) menu.updateXOffset(value); }
    private void updateYOffset(int value) { if (slidersInitialized) menu.updateYOffset(value); }
    private void updateZOffset(int value) { if (slidersInitialized) menu.updateZOffset(value); }
    private void updateRadius(int value) { if (slidersInitialized) menu.updateRadius(value); }
    private void updateThickness(int value) { if (slidersInitialized) menu.updateThickness(value); }

    private void onShapeSelected(ShapeMakerBlockEntity.ShapeType shape) {
        menu.updateShapeType(shape);
    }

    private void togglePreview(IconButton button) {
        boolean newState = !menu.isPreviewEnabled();
        menu.updatePreview(newState);
        button.setIcon(newState ? "👁" : "✕");
        button.setColor(newState ? COLOR_SUCCESS : COLOR_DANGER);
        button.setTooltip(Tooltip.create(Component.literal(newState ? "Preview ON" : "Preview OFF")));
    }

    private void cycleRedstoneMode(IconButton button) {
        ShapeMakerBlockEntity.RedstoneMode current = menu.getRedstoneMode();
        ShapeMakerBlockEntity.RedstoneMode next = switch (current) {
            case OFF -> ShapeMakerBlockEntity.RedstoneMode.PULSE;
            case PULSE -> ShapeMakerBlockEntity.RedstoneMode.CONSTANT;
            case CONSTANT -> ShapeMakerBlockEntity.RedstoneMode.OFF;
        };
        menu.updateRedstoneMode(next);
        button.setIcon(getRedstoneIcon(next));
        button.setColor(getRedstoneColor(next));
        button.setTooltip(Tooltip.create(Component.literal("Redstone: " + next.getDisplayName())));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Draw vanilla generic_54 background
        graphics.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);

        // ADJUSTMENT: Draw semi-transparent panels over upper chest area (lowered by 8 pixels)
        // Left panel for shape selector
        graphics.fill(x + 7, y + 17 + PANEL_Y_OFFSET, x + 85, y + 104 + PANEL_Y_OFFSET, COLOR_PANEL_BG);
        graphics.renderOutline(x + 7, y + 17 + PANEL_Y_OFFSET, 78, 87, COLOR_PANEL_BORDER);

        // Right panel for sliders
        graphics.fill(x + 87, y + 17 + PANEL_Y_OFFSET, x + 169, y + 104 + PANEL_Y_OFFSET, COLOR_PANEL_BG);
        graphics.renderOutline(x + 87, y + 17 + PANEL_Y_OFFSET, 82, 87, COLOR_PANEL_BORDER);

        // Title (replace "Chest" with "Shape Maker")
        graphics.drawString(font, "Shape Maker", x + 8, y + 6, COLOR_TEXT, false);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // ADJUSTMENT: Inventory label Y position is now set in constructor (lowered by 17 pixels)
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        // Update button states from server data
        boolean currentPreview = menu.isPreviewEnabled();
        previewButton.setIcon(currentPreview ? "👁" : "✕");
        previewButton.setColor(currentPreview ? COLOR_SUCCESS : COLOR_DANGER);

        ShapeMakerBlockEntity.RedstoneMode currentMode = menu.getRedstoneMode();
        redstoneButton.setIcon(getRedstoneIcon(currentMode));
        redstoneButton.setColor(getRedstoneColor(currentMode));

        // Sync sliders if they drift (only if not currently being dragged)
        if (slidersInitialized && !xSlider.isDragging && !ySlider.isDragging &&
                !zSlider.isDragging && !radiusSlider.isDragging && !thicknessSlider.isDragging) {

            // Only update if significantly different to avoid fighting
            if (Math.abs(xSlider.getIntValue() - menu.getXOffset()) > 0) {
                xSlider.setValue(menu.getXOffset());
            }
            if (Math.abs(ySlider.getIntValue() - menu.getYOffset()) > 0) {
                ySlider.setValue(menu.getYOffset());
            }
            if (Math.abs(zSlider.getIntValue() - menu.getZOffset()) > 0) {
                zSlider.setValue(menu.getZOffset());
            }
            if (Math.abs(radiusSlider.getIntValue() - menu.getRadius()) > 0) {
                radiusSlider.setValue(menu.getRadius());
            }
            if (Math.abs(thicknessSlider.getIntValue() - menu.getThickness()) > 0) {
                thicknessSlider.setValue(menu.getThickness());
            }
        }
    }

    // ============================================
    // FIXED DRAGGABLE SLIDER
    // ============================================
    private static class DraggableSlider extends net.minecraft.client.gui.components.AbstractSliderButton {
        private final String label;
        private final int min, max;
        private final Consumer<Integer> onChange;
        private int currentValue;
        private final int actualWidth;
        public boolean isDragging = false;

        public DraggableSlider(int x, int y, int width, int height, String label,
                               int min, int max, int current, Consumer<Integer> onChange) {
            super(x, y, width, height, Component.empty(), (current - min) / (double)(max - min));
            this.actualWidth = width;
            this.label = label;
            this.min = min;
            this.max = max;
            this.onChange = onChange;
            this.currentValue = current;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int newValue = getIntValue();
            if (newValue != currentValue) {
                currentValue = newValue;
                onChange.accept(newValue);
            }
        }

        @Override
        protected void applyValue() {
            int newValue = getIntValue();
            if (newValue != currentValue) {
                currentValue = newValue;
                onChange.accept(newValue);
            }
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            String labelText = label + ": " + getIntValue();
            graphics.drawString(net.minecraft.client.Minecraft.getInstance().font,
                    labelText, getX(), getY() - 2, COLOR_TEXT, false);

            graphics.fill(getX(), getY() + 10, getX() + actualWidth, getY() + 14, COLOR_SLIDER_TRACK);

            int fillWidth = (int)(this.value * (actualWidth - 4)) + 2;
            graphics.fill(getX() + 2, getY() + 10, getX() + fillWidth, getY() + 14, COLOR_SLIDER_FILL);

            int handleX = getX() + fillWidth;
            graphics.fill(handleX - 2, getY() + 8, handleX + 2, getY() + 16, 0xFFFFFFFF);
        }

        public int getIntValue() {
            return min + (int)Math.round(this.value * (max - min));
        }

        public void setValue(int value) {
            int clamped = net.minecraft.util.Mth.clamp(value, min, max);
            this.value = (clamped - min) / (double)(max - min);
            this.currentValue = clamped;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.active && this.visible && button == 0) {
                if (isMouseOver(mouseX, mouseY)) {
                    double relativeX = mouseX - (this.getX() + 2);
                    this.value = net.minecraft.util.Mth.clamp(relativeX / (this.actualWidth - 4), 0.0, 1.0);
                    applyValue();
                    isDragging = true;
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            if (isDragging && button == 0) {
                double relativeX = mouseX - (this.getX() + 2);
                this.value = net.minecraft.util.Mth.clamp(relativeX / (this.actualWidth - 4), 0.0, 1.0);
                updateMessage();
                return true;
            }
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (isDragging && button == 0) {
                isDragging = false;
                applyValue();
            }
            return super.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= getX() && mouseX <= getX() + actualWidth &&
                    mouseY >= getY() - 2 && mouseY <= getY() + 16;
        }
    }

    // ============================================
    // ICON BUTTON
    // ============================================
    private static class IconButton extends Button {
        private String icon;
        private int color;
        private final OnPress onPressAction;

        public IconButton(int x, int y, int width, int height, String icon, int color, OnPress onPress) {
            super(x, y, width, height, Component.empty(), btn -> {}, DEFAULT_NARRATION);
            this.icon = icon;
            this.color = color;
            this.onPressAction = onPress;
        }

        public void setIcon(String icon) { this.icon = icon; }
        public void setColor(int color) { this.color = color; }

        @Override
        public void onPress() { onPressAction.onPress(this); }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            int bgColor = isHovered() ? 0xFF5D6D7E : 0xFF34495E;
            graphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);
            graphics.renderOutline(getX(), getY(), width, height, color);

            net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
            int iconWidth = font.width(icon);
            graphics.drawString(font, icon,
                    getX() + (width - iconWidth) / 2,
                    getY() + (height - 8) / 2,
                    COLOR_TEXT, false);
        }
    }

    // ============================================
    // SHAPE SCROLL PANEL
    // ============================================
    private static class ShapeScrollPanel extends net.minecraft.client.gui.components.AbstractWidget {
        private final Consumer<ShapeMakerBlockEntity.ShapeType> onSelect;
        private final List<ShapeEntry> entries = new ArrayList<>();
        private double scrollAmount;
        private ShapeMakerBlockEntity.ShapeType selected;
        private final int left, top, right, bottom;

        public ShapeScrollPanel(net.minecraft.client.Minecraft mc, int width, int height,
                                int top, int left, Consumer<ShapeMakerBlockEntity.ShapeType> onSelect) {
            super(left, top, width, height, Component.empty());
            this.left = left;
            this.top = top;
            this.right = left + width;
            this.bottom = top + height;
            this.onSelect = onSelect;

            int y = 2;
            for (ShapeMakerBlockEntity.ShapeType shape : ShapeMakerBlockEntity.ShapeType.values()) {
                entries.add(new ShapeEntry(shape, y));
                y += 14;
            }
        }

        public void setSelected(ShapeMakerBlockEntity.ShapeType shape) {
            this.selected = shape;
            if (shape != null) {
                int targetY = entries.stream()
                        .filter(e -> e.shape == shape)
                        .findFirst()
                        .map(e -> e.y)
                        .orElse(0);

                int contentHeight = entries.size() * 14;
                int maxScroll = Math.max(0, contentHeight - height + 4);
                scrollAmount = net.minecraft.util.Mth.clamp(targetY - height / 2, 0, maxScroll);
            }
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.enableScissor(left, top, right, bottom);

            int yOffset = top - (int)scrollAmount;
            for (ShapeEntry entry : entries) {
                int entryY = yOffset + entry.y;
                if (entryY + 12 > top && entryY < bottom) {
                    renderEntry(graphics, entry, entryY, mouseX, mouseY);
                }
            }

            graphics.disableScissor();

            int contentHeight = entries.size() * 14;
            if (contentHeight > height) {
                int scrollbarHeight = Math.max(8, (int)((float)height / contentHeight * height));
                int scrollbarY = top + (int)(scrollAmount / (contentHeight - height) * (height - scrollbarHeight));
                graphics.fill(right - 3, top, right - 1, bottom, 0xFF1A252F);
                graphics.fill(right - 3, scrollbarY, right - 1, scrollbarY + scrollbarHeight, COLOR_PANEL_BORDER);
            }
        }

        private void renderEntry(GuiGraphics graphics, ShapeEntry entry, int y, int mouseX, int mouseY) {
            boolean isSelected = entry.shape == selected;
            boolean isHovered = mouseX >= left && mouseX <= right - 4 &&
                    mouseY >= y && mouseY <= y + 12;

            int bgColor = isSelected ? COLOR_PANEL_BORDER : (isHovered ? 0xFF34495E : 0x00000000);
            if (isSelected || isHovered) {
                graphics.fill(left + 2, y, right - 4, y + 12, bgColor);
            }

            String text = (isSelected ? "> " : "  ") + entry.shape.getDisplayName();
            int textColor = isSelected ? COLOR_TEXT : COLOR_TEXT_DIM;
            graphics.drawString(net.minecraft.client.Minecraft.getInstance().font,
                    text, left + 4, y + 2, textColor, false);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY)) return false;

            int yOffset = top - (int)scrollAmount;
            for (ShapeEntry entry : entries) {
                int entryY = yOffset + entry.y;
                if (mouseY >= entryY && mouseY <= entryY + 12) {
                    selected = entry.shape;
                    onSelect.accept(entry.shape);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
            if (!isMouseOver(mouseX, mouseY)) return false;

            int contentHeight = entries.size() * 14;
            int maxScroll = Math.max(0, contentHeight - height + 4);
            scrollAmount = net.minecraft.util.Mth.clamp(scrollAmount - scrollY * 8, 0, maxScroll);
            return true;
        }

        @Override
        protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput output) {}

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
        }

        private record ShapeEntry(ShapeMakerBlockEntity.ShapeType shape, int y) {}
    }
}