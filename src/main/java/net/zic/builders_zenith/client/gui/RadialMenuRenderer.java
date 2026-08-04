package net.zic.builders_zenith.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.zic.builders_zenith.BuildersZenith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = BuildersZenith.MOD_ID, value = Dist.CLIENT)
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

    private static final float BASE_R = 0.15f;
    private static final float BASE_G = 0.15f;
    private static final float BASE_B = 0.15f;
    private static final float BASE_A = 0.85f;

    private static final float HIGHLIGHT_R = 0.2f;
    private static final float HIGHLIGHT_G = 0.8f;
    private static final float HIGHLIGHT_B = 1.0f;
    private static final float HIGHLIGHT_A = 0.9f;

    /** Same pipeline GuiGraphicsExtractor.fill() uses — solid color, no texture. */
    private static final RenderType GUI_COLOR = RenderType.create(
            "builders_zenith_gui_color",
            RenderSetup.builder(RenderPipelines.GUI).createRenderSetup()
    );

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

        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        updateHoveredSection(centerX, centerY);

        renderBackgroundDim(graphics, screenWidth, screenHeight);

        // ── Custom geometry via BufferSource ─────────────────────────────────
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        PoseStack poseStack = new PoseStack(); // identity — screen coords are absolute
        PoseStack.Pose pose = poseStack.last();

        for (int i = 0; i < sections.size(); i++) {
            renderSection(pose, bufferSource, centerX, centerY, i, sections.get(i));
        }

        renderDividers(pose, bufferSource, centerX, centerY);
        renderSelector(pose, bufferSource, centerX, centerY);

        bufferSource.endBatch();
        // ─────────────────────────────────────────────────────────────────────

        renderCenter(graphics, centerX, centerY);

        for (int i = 0; i < sections.size(); i++) {
            renderItemIcon(graphics, centerX, centerY, i, sections.get(i));
        }
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

    private static void renderBackgroundDim(GuiGraphicsExtractor graphics, int width, int height) {
        int alpha = (int) (100 * openProgress);
        graphics.fill(0, 0, width, height, (alpha << 24));
    }

    /** Draws each section as a strip of quads (inner→outer→outer→inner). */
    private static void renderSection(PoseStack.Pose pose, MultiBufferSource bufferSource,
                                      float centerX, float centerY, int index, RadialSection section) {
        boolean hovered = index == hoveredIndex;
        float innerR = INNER_RADIUS * openProgress;
        float outerR = OUTER_RADIUS * openProgress;

        float r = hovered ? HIGHLIGHT_R : BASE_R;
        float g = hovered ? HIGHLIGHT_G : BASE_G;
        float b = hovered ? HIGHLIGHT_B : BASE_B;
        float a = hovered ? 0.95f : BASE_A;

        VertexConsumer consumer = bufferSource.getBuffer(GUI_COLOR);

        int segments = 24;
        float startRad = (float) Math.toRadians(section.startAngle);
        float endRad = (float) Math.toRadians(section.endAngle);

        for (int i = 0; i < segments; i++) {
            float t0 = i / (float) segments;
            float t1 = (i + 1) / (float) segments;
            float angle0 = startRad + (endRad - startRad) * t0;
            float angle1 = startRad + (endRad - startRad) * t1;

            float innerX0 = centerX + (float) Math.cos(angle0) * innerR;
            float innerY0 = centerY + (float) Math.sin(angle0) * innerR;
            float outerX0 = centerX + (float) Math.cos(angle0) * outerR;
            float outerY0 = centerY + (float) Math.sin(angle0) * outerR;

            float innerX1 = centerX + (float) Math.cos(angle1) * innerR;
            float innerY1 = centerY + (float) Math.sin(angle1) * innerR;
            float outerX1 = centerX + (float) Math.cos(angle1) * outerR;
            float outerY1 = centerY + (float) Math.sin(angle1) * outerR;

            consumer.addVertex(pose, innerX0, innerY0, 0).setColor(r, g, b, a);
            consumer.addVertex(pose, outerX0, outerY0, 0).setColor(r, g, b, a);
            consumer.addVertex(pose, outerX1, outerY1, 0).setColor(r, g, b, a);
            consumer.addVertex(pose, innerX1, innerY1, 0).setColor(r, g, b, a);
        }

        // White border when hovered — thin quads along the outer arc
        if (hovered) {
            float br = 1f, bg = 1f, bb = 1f, ba = openProgress;
            float thick = 0.8f;

            for (int i = 0; i < segments; i++) {
                float t0 = i / (float) segments;
                float t1 = (i + 1) / (float) segments;
                float angle0 = startRad + (endRad - startRad) * t0;
                float angle1 = startRad + (endRad - startRad) * t1;

                float x0 = centerX + (float) Math.cos(angle0) * outerR;
                float y0 = centerY + (float) Math.sin(angle0) * outerR;
                float x1 = centerX + (float) Math.cos(angle1) * outerR;
                float y1 = centerY + (float) Math.sin(angle1) * outerR;

                float nx = -(y1 - y0);
                float ny = (x1 - x0);
                float len = (float) Math.sqrt(nx * nx + ny * ny);
                if (len > 0) {
                    nx = nx / len * thick;
                    ny = ny / len * thick;
                }

                consumer.addVertex(pose, x0 + nx, y0 + ny, 0).setColor(br, bg, bb, ba);
                consumer.addVertex(pose, x0 - nx, y0 - ny, 0).setColor(br, bg, bb, ba);
                consumer.addVertex(pose, x1 - nx, y1 - ny, 0).setColor(br, bg, bb, ba);
                consumer.addVertex(pose, x1 + nx, y1 + ny, 0).setColor(br, bg, bb, ba);
            }
        }
    }

    private static void renderDividers(PoseStack.Pose pose, MultiBufferSource bufferSource,
                                       float centerX, float centerY) {
        if (sections.size() <= 1) return;

        VertexConsumer consumer = bufferSource.getBuffer(GUI_COLOR);
        float innerR = (INNER_RADIUS - 2) * openProgress;
        float outerR = (OUTER_RADIUS + 2) * openProgress;
        float thick = 0.5f;

        for (RadialSection section : sections) {
            float angle = (float) Math.toRadians(section.startAngle);
            float x1 = centerX + (float) Math.cos(angle) * innerR;
            float y1 = centerY + (float) Math.sin(angle) * innerR;
            float x2 = centerX + (float) Math.cos(angle) * outerR;
            float y2 = centerY + (float) Math.sin(angle) * outerR;

            float dx = x2 - x1;
            float dy = y2 - y1;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            float nx = -dy / len * thick;
            float ny = dx / len * thick;

            float r = 0.1f, g = 0.1f, b = 0.1f, a = openProgress * 0.5f;

            consumer.addVertex(pose, x1 + nx, y1 + ny, 0).setColor(r, g, b, a);
            consumer.addVertex(pose, x1 - nx, y1 - ny, 0).setColor(r, g, b, a);
            consumer.addVertex(pose, x2 - nx, y2 - ny, 0).setColor(r, g, b, a);
            consumer.addVertex(pose, x2 + nx, y2 + ny, 0).setColor(r, g, b, a);
        }
    }

    private static void renderCenter(GuiGraphicsExtractor graphics, float centerX, float centerY) {
        float radius = INNER_RADIUS * openProgress * 0.95f;
        int x1 = (int) (centerX - radius);
        int y1 = (int) (centerY - radius);
        int x2 = (int) (centerX + radius);
        int y2 = (int) (centerY + radius);
        int color = ((int) (BASE_A * openProgress * 255) << 24)
                | ((int) (BASE_R * 255) << 16)
                | ((int) (BASE_G * 255) << 8)
                | ((int) (BASE_B * 255));
        graphics.fill(x1, y1, x2, y2, color);

        if (hoveredIndex >= 0 && hoveredIndex < sections.size()) {
            RadialSection section = sections.get(hoveredIndex);
            String name = section.stack.getHoverName().getString();
            int textWidth = Minecraft.getInstance().font.width(name);
            graphics.text(Minecraft.getInstance().font, name,
                    (int) centerX - textWidth / 2, (int) centerY - 4, 0xFFFFFF, true);
        }
    }

    private static void renderItemIcon(GuiGraphicsExtractor graphics, float centerX, float centerY,
                                       int index, RadialSection section) {
        float midAngle = (section.startAngle + section.endAngle) / 2f;
        float midRad = (float) Math.toRadians(midAngle);

        float itemRadius = (INNER_RADIUS + OUTER_RADIUS) / 2f * openProgress - 2f;

        int itemX = (int) (centerX + (float) Math.cos(midRad) * itemRadius) - 6;
        int itemY = (int) (centerY + (float) Math.sin(midRad) * itemRadius) - 8;

        graphics.pose().pushMatrix();
        graphics.pose().translate(itemX + 6, itemY + 6);
        graphics.pose().scale(0.7f, 0.7f);
        graphics.pose().translate(-(itemX + 6), -(itemY + 6));

        graphics.item(section.stack, itemX, itemY);

        graphics.pose().popMatrix();

        if (section.count > 1) {
            float countRadius = itemRadius + 18f;
            String countStr = String.valueOf(section.count);
            int textWidth = Minecraft.getInstance().font.width(countStr);
            int countX = (int) (centerX + (float) Math.cos(midRad) * countRadius) - textWidth / 2;
            int countY = (int) (centerY + (float) Math.sin(midRad) * countRadius) - 4;

            graphics.text(Minecraft.getInstance().font,
                    countStr,
                    countX, countY, 0xFFFFFF, true);
        }
    }

    private static void renderSelector(PoseStack.Pose pose, MultiBufferSource bufferSource,
                                       float centerX, float centerY) {
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

        VertexConsumer consumer = bufferSource.getBuffer(GUI_COLOR);

        float size = 8f * openProgress;
        boolean hasHover = hoveredIndex >= 0;
        float r = hasHover ? 0.0f : 1.0f;
        float g = hasHover ? 0.9f : 1.0f;
        float b = hasHover ? 1.0f : 1.0f;

        float innerX = selectorX - (float) Math.cos(angle) * size * 1.5f;
        float innerY = selectorY - (float) Math.sin(angle) * size * 1.5f;

        float perpX = -(float) Math.sin(angle) * size;
        float perpY = (float) Math.cos(angle) * size;

        float tipX = selectorX + (float) Math.cos(angle) * size * 0.5f;
        float tipY = selectorY + (float) Math.sin(angle) * size * 0.5f;

        // Arrow body as a single quad (4 vertices)
        consumer.addVertex(pose, innerX, innerY, 0).setColor(r, g, b, openProgress);
        consumer.addVertex(pose, selectorX + perpX, selectorY + perpY, 0).setColor(r * 0.7f, g * 0.7f, b * 0.7f, openProgress);
        consumer.addVertex(pose, tipX, tipY, 0).setColor(r, g, b, openProgress);
        consumer.addVertex(pose, selectorX - perpX, selectorY - perpY, 0).setColor(r * 0.7f, g * 0.7f, b * 0.7f, openProgress);

        // Outline
        float lr = 1f, lg = 1f, lb = 1f, la = openProgress;
        drawLine(pose, bufferSource, innerX, innerY, selectorX + perpX, selectorY + perpY, lr, lg, lb, la);
        drawLine(pose, bufferSource, selectorX + perpX, selectorY + perpY, tipX, tipY, lr, lg, lb, la);
        drawLine(pose, bufferSource, tipX, tipY, selectorX - perpX, selectorY - perpY, lr, lg, lb, la);
        drawLine(pose, bufferSource, selectorX - perpX, selectorY - perpY, innerX, innerY, lr, lg, lb, la);
    }

    private static void drawLine(PoseStack.Pose pose, MultiBufferSource bufferSource,
                                 float x0, float y0, float x1, float y1,
                                 float r, float g, float b, float a) {
        float dx = x1 - x0;
        float dy = y1 - y0;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len < 0.001f) return;
        float nx = -dy / len * 0.5f;
        float ny = dx / len * 0.5f;

        VertexConsumer consumer = bufferSource.getBuffer(GUI_COLOR);
        consumer.addVertex(pose, x0 + nx, y0 + ny, 0).setColor(r, g, b, a);
        consumer.addVertex(pose, x0 - nx, y0 - ny, 0).setColor(r, g, b, a);
        consumer.addVertex(pose, x1 - nx, y1 - ny, 0).setColor(r, g, b, a);
        consumer.addVertex(pose, x1 + nx, y1 + ny, 0).setColor(r, g, b, a);
    }
}