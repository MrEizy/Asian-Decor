// TapeMeasureRenderer.java
package net.zic.builders_zenith.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.zic.builders_zenith.items.buildersgadgets.TapeMeasureItem;

import java.awt.Color;

@EventBusSubscriber(value = Dist.CLIENT)
public class TapeMeasureRenderer {

    private static final Color PREVIEW_COLOR = new Color(255, 255, 0, 255);
    private static final Color FINALIZED_COLOR = new Color(0, 255, 0, 255);
    private static final Color HIGHLIGHT_COLOR = new Color(0, 200, 255, 128);

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        ItemStack tapeMeasure = mainHand.getItem() instanceof TapeMeasureItem ? mainHand :
                (offHand.getItem() instanceof TapeMeasureItem ? offHand : null);

        if (tapeMeasure == null) return;

        TapeMeasureItem item = (TapeMeasureItem) tapeMeasure.getItem();
        BlockPos pos1 = item.getPos1(tapeMeasure);
        BlockPos pos2 = item.getPos2(tapeMeasure);

        if (pos1 == null) return;

        PoseStack poseStack = event.getPoseStack();
        Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        if (pos2 != null) {
            AABB bounds = TapeMeasureItem.createBounds(pos1, pos2);
            renderBox(poseStack, bufferSource, bounds, FINALIZED_COLOR, true);
        } else {
            BlockPos targetPos = getTargetBlockPos(mc);
            if (targetPos != null) {
                AABB previewBounds = TapeMeasureItem.createBounds(pos1, targetPos);
                renderBox(poseStack, bufferSource, previewBounds, PREVIEW_COLOR, false);

                AABB targetBox = new AABB(targetPos);
                renderBox(poseStack, bufferSource, targetBox, HIGHLIGHT_COLOR, false);
            } else {
                AABB singleBlock = new AABB(pos1);
                renderBox(poseStack, bufferSource, singleBlock, PREVIEW_COLOR, false);
            }
        }

        poseStack.popPose();

        bufferSource.endBatch();
    }

    private static BlockPos getTargetBlockPos(Minecraft mc) {
        HitResult hit = mc.hitResult;
        if (hit instanceof BlockHitResult blockHit && hit.getType() != HitResult.Type.MISS) {
            return blockHit.getBlockPos();
        }
        return null;
    }

    private static void renderBox(PoseStack poseStack, MultiBufferSource bufferSource,
                                  AABB box, Color color, boolean isFinalized) {

        // Use the built-in LINES render type for standard depth-tested outlines.
        // Switch to ModRenderTypes.linesNoDepth() if you want them visible through blocks.
        VertexConsumer lineBuilder = bufferSource.getBuffer(RenderTypes.LINES);

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        renderLineBox(poseStack, lineBuilder, box, r, g, b, a);
    }

    /** Draws the 12 edges of an AABB manually. Replaces the removed LevelRenderer.renderLineBox. */
    private static void renderLineBox(PoseStack poseStack, VertexConsumer consumer,
                                      AABB box, float r, float g, float b, float a) {
        PoseStack.Pose pose = poseStack.last();

        double minX = box.minX;
        double minY = box.minY;
        double minZ = box.minZ;
        double maxX = box.maxX;
        double maxY = box.maxY;
        double maxZ = box.maxZ;

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