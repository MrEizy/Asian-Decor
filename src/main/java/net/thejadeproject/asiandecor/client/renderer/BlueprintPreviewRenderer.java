package net.thejadeproject.asiandecor.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.component.BlueprintData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.items.buildersgadgets.BlueprintItem;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = AsianDecor.MOD_ID, value = Dist.CLIENT)
public class BlueprintPreviewRenderer {

    public static class PreviewData {
        public final BlockPos anchorPos;
        public final BlueprintData data;
        public final long timestamp;

        public PreviewData(BlockPos anchorPos, BlueprintData data) {
            this.anchorPos = anchorPos;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
    }

    private static final Map<UUID, PreviewData> activePreviews = new HashMap<>();
    private static final Map<UUID, BlueprintData> selectionData = new HashMap<>();

    public static void setPreview(UUID playerId, BlockPos anchorPos, BlueprintData data) {
        activePreviews.put(playerId, new PreviewData(anchorPos, data));
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
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        UUID playerId = mc.player.getUUID();

        // Check if holding blueprint
        ItemStack mainHand = mc.player.getMainHandItem();
        ItemStack offHand = mc.player.getOffhandItem();
        boolean holdingBlueprint = mainHand.getItem() instanceof BlueprintItem || offHand.getItem() instanceof BlueprintItem;

        if (!holdingBlueprint) {
            activePreviews.remove(playerId);
            selectionData.remove(playerId);
            return;
        }

        // Update selection data from held item
        BlueprintData heldData = getBlueprintData(mc.player);
        if (heldData != null) {
            setSelection(playerId, heldData);
        }

        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // Render selection bounding box (orange) - BEFORE cutting
        BlueprintData selData = selectionData.get(playerId);
        if (selData != null && selData.hasBothPositions() && !selData.hasData()) {
            renderBoundingBox(poseStack, selData.getMinPos(), selData.getMaxPos(), 1.0f, 0.6f, 0.0f, 1.0f);
        }

        // Render preview (cyan box + ghost blocks)
        PreviewData preview = activePreviews.get(playerId);
        if (preview != null && preview.data.hasData()) {
            BlockPos dims = preview.data.getRotatedDimensions();
            BlockPos min = preview.anchorPos;
            BlockPos max = preview.anchorPos.offset(dims.getX() - 1, dims.getY() - 1, dims.getZ() - 1);

            renderBoundingBox(poseStack, min, max, 0.0f, 1.0f, 1.0f, 1.0f);
            renderGhostBlocks(poseStack, preview.anchorPos, preview.data);
        }

        poseStack.popPose();
    }

    private static BlueprintData getBlueprintData(net.minecraft.world.entity.player.Player player) {
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

    private static void renderBoundingBox(PoseStack poseStack, BlockPos min, BlockPos max, float r, float g, float b, float a) {
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

        Matrix4f matrix = poseStack.last().pose();

        double minX = min.getX() - 0.005;
        double minY = min.getY() - 0.005;
        double minZ = min.getZ() - 0.005;
        double maxX = max.getX() + 1.005;
        double maxY = max.getY() + 1.005;
        double maxZ = max.getZ() + 1.005;

        // Bottom face
        line(builder, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        line(builder, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        line(builder, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        line(builder, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        // Top face
        line(builder, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(builder, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        line(builder, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        line(builder, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        // Vertical edges
        line(builder, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        line(builder, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(builder, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        line(builder, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);

        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.enableCull();
    }

    private static void line(BufferBuilder builder, Matrix4f matrix,
                             double x1, double y1, double z1,
                             double x2, double y2, double z2,
                             float r, float g, float b, float a) {
        float nx = (float)(x2 - x1);
        float ny = (float)(y2 - y1);
        float nz = (float)(z2 - z1);
        float len = (float)Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len > 0) {
            nx /= len;
            ny /= len;
            nz /= len;
        }

        builder.addVertex(matrix, (float)x1, (float)y1, (float)z1).setColor(r, g, b, a).setNormal(nx, ny, nz);
        builder.addVertex(matrix, (float)x2, (float)y2, (float)z2).setColor(r, g, b, a).setNormal(nx, ny, nz);
    }

    private static void renderGhostBlocks(PoseStack poseStack, BlockPos anchor, BlueprintData data) {
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);

        poseStack.pushPose();

        for (BlueprintData.BlockEntry entry : data.blocks()) {
            // Rotate position for preview
            BlockPos rotatedPos = data.rotatePos(entry.x(), entry.y(), entry.z());
            BlockPos pos = anchor.offset(rotatedPos.getX(), rotatedPos.getY(), rotatedPos.getZ());
            BlockState state = entry.state();

            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

            // Rotate block state for preview
            BlockState rotatedState = data.rotateBlockState(state);

            blockRenderer.renderSingleBlock(
                    rotatedState,
                    poseStack,
                    bufferSource,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY
            );

            poseStack.popPose();
        }

        bufferSource.endBatch();
        poseStack.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

}