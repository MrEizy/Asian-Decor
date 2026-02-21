// TapeMeasureRenderer.java - Simplified without text rendering
package net.thejadeproject.asiandecor.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
import net.thejadeproject.asiandecor.items.buildersgadgets.TapeMeasureItem;

import java.awt.Color;

@EventBusSubscriber(value = Dist.CLIENT)
public class TapeMeasureRenderer {

    private static final Color PREVIEW_COLOR = new Color(255, 255, 0, 255);
    private static final Color FINALIZED_COLOR = new Color(0, 255, 0, 255);
    private static final Color HIGHLIGHT_COLOR = new Color(0, 200, 255, 128);

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

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
        Vec3 camera = event.getCamera().getPosition();

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

        VertexConsumer lineBuilder = bufferSource.getBuffer(RenderType.lines());

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        LevelRenderer.renderLineBox(poseStack, lineBuilder, box, r, g, b, a);
    }
}