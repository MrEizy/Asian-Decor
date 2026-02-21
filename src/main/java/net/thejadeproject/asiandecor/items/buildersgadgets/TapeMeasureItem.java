// TapeMeasureItem.java - Updated with tooltip
package net.thejadeproject.asiandecor.items.buildersgadgets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.component.TapeMeasureDataComponent;

import javax.annotation.Nullable;
import java.util.List;

public class TapeMeasureItem extends Item {

    public TapeMeasureItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        TapeMeasureDataComponent data = stack.get(ModDataComponents.TAPE_MEASURE_DATA.get());

        if (data == null || !data.hasSelection()) {
            tooltipComponents.add(Component.translatable("tooltip.asiandecor.tape_measure.no_selection"));
        } else {
            BlockPos pos1 = data.pos1().orElse(null);
            BlockPos pos2 = data.pos2().orElse(null);

            if (pos1 != null) {
                tooltipComponents.add(Component.translatable("tooltip.asiandecor.tape_measure.pos1_set",
                        pos1.getX(), pos1.getY(), pos1.getZ()));
            }

            if (pos2 != null) {
                tooltipComponents.add(Component.translatable("tooltip.asiandecor.tape_measure.pos2_set",
                        pos2.getX(), pos2.getY(), pos2.getZ()));

                // Show dimensions if finalized
                if (data.finalized()) {
                    int length = Math.abs(pos2.getX() - pos1.getX()) + 1;
                    int height = Math.abs(pos2.getY() - pos1.getY()) + 1;
                    int width = Math.abs(pos2.getZ() - pos1.getZ()) + 1;

                    tooltipComponents.add(Component.empty());
                    tooltipComponents.add(Component.translatable("tooltip.asiandecor.tape_measure.dimensions",
                            length, width, height));
                }
            } else {
                tooltipComponents.add(Component.translatable("tooltip.asiandecor.tape_measure.select_pos2"));
            }

            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.asiandecor.tape_measure.clear"));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.FAIL;

        // Shift + Right Click = Clear
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                clearSelection(stack);
                player.displayClientMessage(Component.translatable("message.asiandecor.tape_measure.cleared"), true);
                level.playSound(null, player.blockPosition(), SoundEvents.ITEM_FRAME_REMOVE_ITEM,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // Normal Right Click
        if (!level.isClientSide) {
            TapeMeasureDataComponent data = stack.getOrDefault(ModDataComponents.TAPE_MEASURE_DATA.get(),
                    TapeMeasureDataComponent.EMPTY);

            if (!data.hasSelection()) {
                // Set Pos1
                setPos1(stack, clickedPos);
                player.displayClientMessage(Component.translatable("message.asiandecor.tape_measure.pos1_set",
                        clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()), true);
                level.playSound(null, clickedPos, SoundEvents.STONE_BUTTON_CLICK_ON,
                        SoundSource.BLOCKS, 1.0F, 2.0F);
            } else if (!data.finalized()) {
                // Set Pos2 and finalize
                BlockPos pos1 = data.pos1().orElse(null);
                if (pos1 != null) {
                    setPos2(stack, clickedPos);
                    finalizeMeasurement(stack, pos1, clickedPos, level, player);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void setPos1(ItemStack stack, BlockPos pos) {
        TapeMeasureDataComponent newData = TapeMeasureDataComponent.EMPTY.withPos1(pos);
        stack.set(ModDataComponents.TAPE_MEASURE_DATA.get(), newData);
    }

    private void setPos2(ItemStack stack, BlockPos pos) {
        TapeMeasureDataComponent currentData = stack.getOrDefault(ModDataComponents.TAPE_MEASURE_DATA.get(),
                TapeMeasureDataComponent.EMPTY);
        TapeMeasureDataComponent newData = currentData.withPos2(pos);
        stack.set(ModDataComponents.TAPE_MEASURE_DATA.get(), newData);
    }

    @Nullable
    public BlockPos getPos1(ItemStack stack) {
        TapeMeasureDataComponent data = stack.get(ModDataComponents.TAPE_MEASURE_DATA.get());
        return data != null ? data.pos1().orElse(null) : null;
    }

    @Nullable
    public BlockPos getPos2(ItemStack stack) {
        TapeMeasureDataComponent data = stack.get(ModDataComponents.TAPE_MEASURE_DATA.get());
        return data != null ? data.pos2().orElse(null) : null;
    }

    public boolean hasSelection(ItemStack stack) {
        TapeMeasureDataComponent data = stack.get(ModDataComponents.TAPE_MEASURE_DATA.get());
        return data != null && data.hasSelection();
    }

    public boolean isFinalized(ItemStack stack) {
        TapeMeasureDataComponent data = stack.get(ModDataComponents.TAPE_MEASURE_DATA.get());
        return data != null && data.finalized();
    }

    private void finalizeMeasurement(ItemStack stack, BlockPos pos1, BlockPos pos2,
                                     Level level, Player player) {
        TapeMeasureDataComponent data = stack.get(ModDataComponents.TAPE_MEASURE_DATA.get());
        if (data == null) return;

        Vec3 center = data.center().orElse(Vec3.ZERO);

        // Calculate dimensions
        int length = Math.abs(pos2.getX() - pos1.getX()) + 1;
        int height = Math.abs(pos2.getY() - pos1.getY()) + 1;
        int width = Math.abs(pos2.getZ() - pos1.getZ()) + 1;

        AABB bounds = createBounds(pos1, pos2);
        int totalBlocks = (int) (bounds.getXsize() * bounds.getYsize() * bounds.getZsize());

        // Send translatable action bar message
        Component message = Component.translatable("message.asiandecor.tape_measure.measured",
                length, width, height, totalBlocks);

        player.displayClientMessage(message, true);

        level.playSound(null, pos2, SoundEvents.PLAYER_LEVELUP,
                SoundSource.BLOCKS, 0.5F, 2.0F);
    }

    private void clearSelection(ItemStack stack) {
        stack.remove(ModDataComponents.TAPE_MEASURE_DATA.get());
    }

    public static AABB createBounds(BlockPos pos1, BlockPos pos2) {
        return new AABB(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ()),
                Math.max(pos1.getX(), pos2.getX()) + 1,
                Math.max(pos1.getY(), pos2.getY()) + 1,
                Math.max(pos1.getZ(), pos2.getZ()) + 1
        );
    }

    @Nullable
    public Vec3 getCenter(ItemStack stack) {
        TapeMeasureDataComponent data = stack.get(ModDataComponents.TAPE_MEASURE_DATA.get());
        return data != null ? data.center().orElse(null) : null;
    }
}