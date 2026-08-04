package net.zic.builders_zenith.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.component.BlueprintData;
import net.zic.builders_zenith.component.ModDataComponents;
import net.zic.builders_zenith.items.buildersgadgets.BlueprintItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = BuildersZenith.MOD_ID, value = Dist.CLIENT)
public class BlueprintPreviewRenderer {

    public static class PreviewData {
        public final BlockPos anchorPos;
        public BlueprintData data;
        public final long timestamp;

        public PreviewData(BlockPos anchorPos, BlueprintData data) {
            this.anchorPos = anchorPos;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        public void updateData(BlueprintData newData) {
            this.data = newData;
        }
    }

    private static final Map<UUID, PreviewData> activePreviews = new HashMap<>();
    private static final Map<UUID, BlueprintData> selectionData = new HashMap<>();

    public static void setPreview(UUID playerId, BlockPos anchorPos, BlueprintData data) {
        activePreviews.put(playerId, new PreviewData(anchorPos, data));
    }

    public static void updatePreviewData(UUID playerId, BlueprintData data) {
        PreviewData preview = activePreviews.get(playerId);
        if (preview != null) {
            preview.updateData(data);
        }
    }

    public static void clearPreview(UUID playerId) {
        activePreviews.remove(playerId);
    }

    public static void setSelection(UUID playerId, BlueprintData data) {
        if (data.hasBothPositions() && !data.hasData()) {
            selectionData.put(playerId, data);
        } else {
            selectionData.remove(playerId);
        }
    }

    public static void clearSelection(UUID playerId) {
        selectionData.remove(playerId);
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        UUID playerId = mc.player.getUUID();

        ItemStack mainHand = mc.player.getMainHandItem();
        ItemStack offHand = mc.player.getOffhandItem();
        boolean holdingBlueprint = mainHand.getItem() instanceof BlueprintItem ||
                offHand.getItem() instanceof BlueprintItem;

        if (!holdingBlueprint) {
            activePreviews.remove(playerId);
            selectionData.remove(playerId);
            return;
        }

        BlueprintData heldData = getHeldBlueprintData(mc.player);
        if (heldData != null && heldData.hasData()) {
            updatePreviewData(playerId, heldData);
            setSelection(playerId, heldData);
        }

        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getLevelRenderState().cameraRenderState.pos;

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        // Render selection box (orange)
        BlueprintData selData = selectionData.get(playerId);
        if (selData != null && selData.hasBothPositions() && !selData.hasData()) {
            renderBoundingBox(poseStack, bufferSource, selData.getMinPos(), selData.getMaxPos(), 1.0f, 0.6f, 0.0f, 1.0f);
        }

        // Render preview
        PreviewData preview = activePreviews.get(playerId);
        if (preview != null && preview.data.hasData()) {
            BlockPos dims = preview.data.getRotatedDimensions();
            BlockPos min = preview.anchorPos;
            BlockPos max = preview.anchorPos.offset(dims.getX() - 1, dims.getY() - 1, dims.getZ() - 1);

            renderBoundingBox(poseStack, bufferSource, min, max, 0.0f, 1.0f, 1.0f, 1.0f);
            renderGhostBlocks(poseStack, bufferSource, preview.anchorPos, preview.data);
        }

        poseStack.popPose();

        bufferSource.endBatch();
    }

    private static BlueprintData getHeldBlueprintData(net.minecraft.world.entity.player.Player player) {
        ItemStack main = player.getMainHandItem();
        if (main.getItem() instanceof BlueprintItem) {
            return main.getOrDefault(ModDataComponents.BLUEPRINT_DATA.get(), BlueprintData.EMPTY);
        }
        ItemStack off = player.getOffhandItem();
        if (off.getItem() instanceof BlueprintItem) {
            return off.getOrDefault(ModDataComponents.BLUEPRINT_DATA.get(), BlueprintData.EMPTY);
        }
        return null;
    }

    /** Draws the 12 edges of a bounding box. */
    private static void renderBoundingBox(PoseStack poseStack, MultiBufferSource bufferSource,
                                          BlockPos min, BlockPos max,
                                          float r, float g, float b, float a) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.LINES);
        PoseStack.Pose pose = poseStack.last();

        double minX = min.getX() - 0.005;
        double minY = min.getY() - 0.005;
        double minZ = min.getZ() - 0.005;
        double maxX = max.getX() + 1.005;
        double maxY = max.getY() + 1.005;
        double maxZ = max.getZ() + 1.005;

        // Bottom face
        line(consumer, pose, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        line(consumer, pose, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        line(consumer, pose, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        line(consumer, pose, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        // Top face
        line(consumer, pose, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(consumer, pose, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        line(consumer, pose, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        line(consumer, pose, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        // Vertical edges
        line(consumer, pose, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        line(consumer, pose, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(consumer, pose, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        line(consumer, pose, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
    }

    /** Renders a wireframe box for every block in the blueprint. */
    private static void renderGhostBlocks(PoseStack poseStack, MultiBufferSource bufferSource,
                                          BlockPos anchor, BlueprintData data) {
        for (BlueprintData.BlockEntry entry : data.blocks()) {
            BlockPos rotatedPos = data.rotatePos(entry.x(), entry.y(), entry.z());
            BlockPos pos = anchor.offset(rotatedPos.getX(), rotatedPos.getY(), rotatedPos.getZ());

            // Slightly smaller box so individual block edges don't completely overlap
            renderSmallBox(poseStack, bufferSource, pos, 0.0f, 0.8f, 1.0f, 0.5f);
        }
    }

    /** Draws a wireframe slightly inset from the full block. */
    private static void renderSmallBox(PoseStack poseStack, MultiBufferSource bufferSource,
                                       BlockPos pos, float r, float g, float b, float a) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.LINES);
        PoseStack.Pose pose = poseStack.last();

        double minX = pos.getX() + 0.02;
        double minY = pos.getY() + 0.02;
        double minZ = pos.getZ() + 0.02;
        double maxX = pos.getX() + 0.98;
        double maxY = pos.getY() + 0.98;
        double maxZ = pos.getZ() + 0.98;

        // Bottom face
        line(consumer, pose, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        line(consumer, pose, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        line(consumer, pose, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        line(consumer, pose, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        // Top face
        line(consumer, pose, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(consumer, pose, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        line(consumer, pose, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        line(consumer, pose, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        // Vertical edges
        line(consumer, pose, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        line(consumer, pose, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(consumer, pose, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        line(consumer, pose, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
    }

    private static void line(VertexConsumer consumer, PoseStack.Pose pose,
                             double x0, double y0, double z0,
                             double x1, double y1, double z1,
                             float r, float g, float b, float a) {
        float nx = (float) (x1 - x0);
        float ny = (float) (y1 - y0);
        float nz = (float) (z1 - z0);
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len > 0) {
            nx /= len;
            ny /= len;
            nz /= len;
        }

        consumer.addVertex(pose, (float) x0, (float) y0, (float) z0)
                .setColor(r, g, b, a)
                .setNormal(pose, nx, ny, nz)
                .setLineWidth(2.0f);
        consumer.addVertex(pose, (float) x1, (float) y1, (float) z1)
                .setColor(r, g, b, a)
                .setNormal(pose, nx, ny, nz)
                .setLineWidth(2.0f);
    }
}