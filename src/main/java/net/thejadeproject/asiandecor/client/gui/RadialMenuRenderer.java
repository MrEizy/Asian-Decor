package net.thejadeproject.asiandecor.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.thejadeproject.asiandecor.AsianDecor;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AsianDecor.MOD_ID, value = Dist.CLIENT)
public class RadialMenuRenderer {

    public static class RadialSection {
        public final ItemStack stack;
        public final int count;
        public final float startAngle;
        public final float endAngle;
        public final int originalSlot;

        public RadialSection(ItemStack stack, int count, float startAngle, float endAngle, int originalSlot) {
            this.stack = stack;
            this.count = count;
            this.startAngle = startAngle;
            this.endAngle = endAngle;
            this.originalSlot = originalSlot;
        }
    }

    private static boolean menuOpen = false;
    private static float openProgress = 0f;
    private static List<RadialSection> sections = new ArrayList<>();
    private static int hoveredIndex = -1;
    private static int lastHoveredIndex = -1;

    private static final float INNER_RADIUS = 45f;
    private static final float OUTER_RADIUS = 100f;
    private static final float ANIMATION_SPEED = 0.25f;

    // Base colors (dark gray, matching inner circle)
    private static final float BASE_R = 0.15f;
    private static final float BASE_G = 0.15f;
    private static final float BASE_B = 0.15f;
    private static final float BASE_A = 0.85f;

    // Highlight colors (brighter cyan when hovered)
    private static final float HIGHLIGHT_R = 0.2f;
    private static final float HIGHLIGHT_G = 0.8f;
    private static final float HIGHLIGHT_B = 1.0f;
    private static final float HIGHLIGHT_A = 0.9f;

    public static void openMenu(List<ItemStack> items) {
        menuOpen = true;
        hoveredIndex = -1;
        lastHoveredIndex = -1;
        buildSections(items);
    }

    public static void closeMenu() {
        menuOpen = false;
    }

    public static boolean isMenuOpen() {
        return menuOpen;
    }

    public static int getSelectedSlot() {
        if (lastHoveredIndex >= 0 && lastHoveredIndex < sections.size()) {
            return sections.get(lastHoveredIndex).originalSlot;
        }
        return -1;
    }

    public static void resetSelection() {
        hoveredIndex = -1;
        lastHoveredIndex = -1;
        sections.clear();
        openProgress = 0f;
    }

    private static void buildSections(List<ItemStack> items) {
        sections.clear();

        Map<String, ItemStack> grouped = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        Map<String, Integer> firstSlot = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) continue;

            String key = stack.getItem().toString() + stack.getDamageValue();

            if (!firstSlot.containsKey(key)) {
                firstSlot.put(key, i);
                grouped.put(key, stack.copy());
            }
            counts.merge(key, stack.getCount(), Integer::sum);
        }

        List<Map.Entry<String, ItemStack>> uniqueItems = new ArrayList<>(grouped.entrySet());
        if (uniqueItems.isEmpty()) return;

        float anglePerSection = 360f / uniqueItems.size();

        for (int i = 0; i < uniqueItems.size(); i++) {
            String key = uniqueItems.get(i).getKey();
            ItemStack stack = uniqueItems.get(i).getValue();
            int count = counts.get(key);
            int slot = firstSlot.get(key);

            float startAngle = i * anglePerSection - 90f;
            float endAngle = (i + 1) * anglePerSection - 90f;

            sections.add(new RadialSection(stack, count, startAngle, endAngle, slot));
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (menuOpen && openProgress < 1f) {
            openProgress = Math.min(1f, openProgress + ANIMATION_SPEED);
        } else if (!menuOpen && openProgress > 0f) {
            openProgress = Math.max(0f, openProgress - ANIMATION_SPEED);
            if (openProgress <= 0f) return;
        } else if (openProgress <= 0f) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        updateHoveredSection(centerX, centerY);

        renderBackgroundDim(graphics, screenWidth, screenHeight);

        // Render sections with matching inner circle color
        for (int i = 0; i < sections.size(); i++) {
            renderSection(graphics, centerX, centerY, i, sections.get(i));
        }

        renderDividers(graphics, centerX, centerY);

        renderCenter(graphics, centerX, centerY);

        for (int i = 0; i < sections.size(); i++) {
            renderItemIcon(graphics, centerX, centerY, i, sections.get(i));
        }

        renderSelector(graphics, centerX, centerY);
    }

    private static void updateHoveredSection(float centerX, float centerY) {
        Minecraft mc = Minecraft.getInstance();

        double mouseX = mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();

        float dx = (float) mouseX - centerX;
        float dy = (float) mouseY - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        float innerR = INNER_RADIUS * openProgress;
        float outerR = OUTER_RADIUS * openProgress;

        if (distance < innerR || distance > outerR + 30) {
            return;
        }

        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        float adjustedAngle = angle + 90f;

        while (adjustedAngle < 0) adjustedAngle += 360f;
        while (adjustedAngle >= 360f) adjustedAngle -= 360f;

        hoveredIndex = -1;
        for (int i = 0; i < sections.size(); i++) {
            RadialSection section = sections.get(i);
            float start = normalizeAngle(section.startAngle + 90f);
            float end = normalizeAngle(section.endAngle + 90f);

            boolean inSection;
            if (start > end) {
                inSection = adjustedAngle >= start || adjustedAngle <= end;
            } else {
                inSection = adjustedAngle >= start && adjustedAngle <= end;
            }

            if (inSection) {
                hoveredIndex = i;
                lastHoveredIndex = i;
                break;
            }
        }
    }

    private static float normalizeAngle(float angle) {
        while (angle < 0) angle += 360f;
        while (angle >= 360f) angle -= 360f;
        return angle;
    }

    private static void renderBackgroundDim(GuiGraphics graphics, int width, int height) {
        int alpha = (int) (100 * openProgress);
        graphics.fill(0, 0, width, height, (alpha << 24));
    }

    private static void renderSection(GuiGraphics graphics, float centerX, float centerY,
                                      int index, RadialSection section) {
        boolean hovered = index == hoveredIndex;
        float innerR = INNER_RADIUS * openProgress;
        float outerR = OUTER_RADIUS * openProgress;

        // Use base color normally, highlight color when hovered
        float r = hovered ? HIGHLIGHT_R : BASE_R;
        float g = hovered ? HIGHLIGHT_G : BASE_G;
        float b = hovered ? HIGHLIGHT_B : BASE_B;
        // Use same solid alpha for both, no transparency difference
        float a = hovered ? 0.95f : BASE_A;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = graphics.pose().last().pose();

        int segments = 24;
        float startRad = (float) Math.toRadians(section.startAngle);
        float endRad = (float) Math.toRadians(section.endAngle);

        // Center with same color as inner circle
        builder.addVertex(matrix, centerX, centerY, 0).setColor(r, g, b, a);

        // Outer arc - same solid color
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            float angle = startRad + (endRad - startRad) * t;
            float x = centerX + (float) Math.cos(angle) * outerR;
            float y = centerY + (float) Math.sin(angle) * outerR;
            builder.addVertex(matrix, x, y, 0).setColor(r, g, b, a);
        }

        // Inner arc - same solid color
        for (int i = segments; i >= 0; i--) {
            float t = i / (float) segments;
            float angle = startRad + (endRad - startRad) * t;
            float x = centerX + (float) Math.cos(angle) * innerR;
            float y = centerY + (float) Math.sin(angle) * innerR;
            builder.addVertex(matrix, x, y, 0).setColor(r, g, b, a);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());

        // White border when hovered
        if (hovered) {
            builder = tesselator.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

            for (int i = 0; i <= segments; i++) {
                float t = i / (float) segments;
                float angle = startRad + (endRad - startRad) * t;
                float x = centerX + (float) Math.cos(angle) * outerR;
                float y = centerY + (float) Math.sin(angle) * outerR;
                builder.addVertex(matrix, x, y, 0).setColor(1f, 1f, 1f, openProgress);
            }

            BufferUploader.drawWithShader(builder.buildOrThrow());
        }
    }

    private static void renderDividers(GuiGraphics graphics, float centerX, float centerY) {
        if (sections.size() <= 1) return;

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = graphics.pose().last().pose();

        float innerR = (INNER_RADIUS - 2) * openProgress;
        float outerR = (OUTER_RADIUS + 2) * openProgress;

        for (RadialSection section : sections) {
            float angle = (float) Math.toRadians(section.startAngle);
            float x1 = centerX + (float) Math.cos(angle) * innerR;
            float y1 = centerY + (float) Math.sin(angle) * innerR;
            float x2 = centerX + (float) Math.cos(angle) * outerR;
            float y2 = centerY + (float) Math.sin(angle) * outerR;

            builder.addVertex(matrix, x1, y1, 0).setColor(0.1f, 0.1f, 0.1f, openProgress * 0.5f);
            builder.addVertex(matrix, x2, y2, 0).setColor(0.1f, 0.1f, 0.1f, openProgress * 0.5f);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }

    private static void renderCenter(GuiGraphics graphics, float centerX, float centerY) {
        float radius = INNER_RADIUS * openProgress * 0.95f;

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = graphics.pose().last().pose();

        // Match the base color of sections
        for (int i = 0; i <= 32; i++) {
            float angle = (float) (i * Math.PI * 2 / 32);
            float x = centerX + (float) Math.cos(angle) * radius;
            float y = centerY + (float) Math.sin(angle) * radius;
            builder.addVertex(matrix, x, y, 0).setColor(BASE_R, BASE_G, BASE_B, BASE_A * openProgress);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());

        // Center text
        if (hoveredIndex >= 0 && hoveredIndex < sections.size()) {
            RadialSection section = sections.get(hoveredIndex);
            String name = section.stack.getHoverName().getString();
            int textWidth = Minecraft.getInstance().font.width(name);
            graphics.drawString(Minecraft.getInstance().font, name,
                    (int) centerX - textWidth / 2, (int) centerY - 4, 0xFFFFFF, true);
        }
    }

    private static void renderItemIcon(GuiGraphics graphics, float centerX, float centerY,
                                       int index, RadialSection section) {
        float midAngle = (section.startAngle + section.endAngle) / 2f;
        float midRad = (float) Math.toRadians(midAngle);

        // Position for item
        float itemRadius = (INNER_RADIUS + OUTER_RADIUS) / 2f * openProgress - 2f;

        int itemX = (int) (centerX + (float) Math.cos(midRad) * itemRadius) - 6;
        int itemY = (int) (centerY + (float) Math.sin(midRad) * itemRadius) - 8;

        // Draw item smaller
        graphics.pose().pushPose();
        graphics.pose().translate(itemX + 6, itemY + 6, 0);
        graphics.pose().scale(0.7f, 0.7f, 0.7f);
        graphics.pose().translate(-(itemX + 6), -(itemY + 6), 0);

        graphics.renderItem(section.stack, itemX, itemY);

        graphics.pose().popPose();

        // Position for count (below the item)
        if (section.count > 1) {
            float countRadius = itemRadius + 18f;

            String countStr = String.valueOf(section.count);
            int textWidth = Minecraft.getInstance().font.width(countStr);
            int countX = (int) (centerX + (float) Math.cos(midRad) * countRadius) - textWidth / 2;
            int countY = (int) (centerY + (float) Math.sin(midRad) * countRadius) - 4;

            graphics.drawString(Minecraft.getInstance().font,
                    countStr,
                    countX, countY, 0xFFFFFF, true);
        }
    }

    private static void renderSelector(GuiGraphics graphics, float centerX, float centerY) {
        Minecraft mc = Minecraft.getInstance();
        double mouseX = mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();

        float dx = (float) mouseX - centerX;
        float dy = (float) mouseY - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 5f) return;

        float angle = (float) Math.atan2(dy, dx);
        float selectorR = (OUTER_RADIUS + 10) * openProgress;

        float selectorX = centerX + (float) Math.cos(angle) * selectorR;
        float selectorY = centerY + (float) Math.sin(angle) * selectorR;

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = graphics.pose().last().pose();

        float size = 8f * openProgress;

        // Color based on hover - cyan when hovering, white when not
        boolean hasHover = hoveredIndex >= 0;
        float r = hasHover ? 0.0f : 1.0f;
        float g = hasHover ? 0.9f : 1.0f;
        float b = hasHover ? 1.0f : 1.0f;

        float innerX = selectorX - (float) Math.cos(angle) * size * 1.5f;
        float innerY = selectorY - (float) Math.sin(angle) * size * 1.5f;

        builder.addVertex(matrix, innerX, innerY, 0).setColor(r, g, b, openProgress);

        float perpX = -(float) Math.sin(angle) * size;
        float perpY = (float) Math.cos(angle) * size;

        builder.addVertex(matrix, selectorX + perpX, selectorY + perpY, 0).setColor(r * 0.7f, g * 0.7f, b * 0.7f, openProgress);
        builder.addVertex(matrix, selectorX + (float) Math.cos(angle) * size * 0.5f,
                selectorY + (float) Math.sin(angle) * size * 0.5f, 0).setColor(r, g, b, openProgress);
        builder.addVertex(matrix, selectorX - perpX, selectorY - perpY, 0).setColor(r * 0.7f, g * 0.7f, b * 0.7f, openProgress);

        BufferUploader.drawWithShader(builder.buildOrThrow());

        // Outline
        builder = tesselator.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        builder.addVertex(matrix, innerX, innerY, 0).setColor(1f, 1f, 1f, openProgress);
        builder.addVertex(matrix, selectorX + perpX, selectorY + perpY, 0).setColor(1f, 1f, 1f, openProgress);
        builder.addVertex(matrix, selectorX + (float) Math.cos(angle) * size * 0.5f,
                selectorY + (float) Math.sin(angle) * size * 0.5f, 0).setColor(1f, 1f, 1f, openProgress);
        builder.addVertex(matrix, selectorX - perpX, selectorY - perpY, 0).setColor(1f, 1f, 1f, openProgress);
        builder.addVertex(matrix, innerX, innerY, 0).setColor(1f, 1f, 1f, openProgress);

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }
}