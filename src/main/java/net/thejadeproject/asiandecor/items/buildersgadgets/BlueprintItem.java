package net.thejadeproject.asiandecor.items.buildersgadgets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.asiandecor.component.BlueprintData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.network.BlueprintClearPreviewPacket;
import net.thejadeproject.asiandecor.network.BlueprintSetPreviewPacket;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlueprintItem extends Item {

    public BlueprintItem(Properties properties) {
        super(properties.component(ModDataComponents.BLUEPRINT_DATA.get(), BlueprintData.EMPTY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        BlueprintData data = stack.getOrDefault(ModDataComponents.BLUEPRINT_DATA.get(), BlueprintData.EMPTY);

        if (player == null) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            return handleShiftClick(level, player, stack, data, clickedPos);
        }

        return handleRightClick(level, player, stack, data, clickedPos);
    }

    private InteractionResult handleShiftClick(Level level, Player player, ItemStack stack,
                                               BlueprintData data, BlockPos clickedPos) {
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            if (!data.pos1().isPresent()) {
                stack.set(ModDataComponents.BLUEPRINT_DATA.get(), data.withPos1(clickedPos));
                player.displayClientMessage(
                        Component.translatable("message.asiandecor.blueprint.pos1_set",
                                clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()),
                        true
                );
                player.playNotifySound(SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.PLAYERS, 1.0f, 1.0f);

            } else if (!data.pos2().isPresent()) {
                BlueprintData withPos2 = data.withPos2(clickedPos);

                if (!withPos2.isValidSize()) {
                    BlockPos dims = withPos2.getDimensions();
                    player.displayClientMessage(
                            Component.translatable("message.asiandecor.blueprint.too_large",
                                    dims.getX(), dims.getY(), dims.getZ(), BlueprintData.MAX_SIZE),
                            true
                    );
                    return InteractionResult.FAIL;
                }

                stack.set(ModDataComponents.BLUEPRINT_DATA.get(), withPos2);
                BlockPos dims = withPos2.getDimensions();
                player.displayClientMessage(
                        Component.translatable("message.asiandecor.blueprint.pos2_set",
                                clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(),
                                dims.getX(), dims.getY(), dims.getZ()),
                        true
                );
                player.playNotifySound(SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.PLAYERS, 1.0f, 1.2f);

            } else {
                cutBlueprint(serverPlayer, stack, data);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void cutBlueprint(ServerPlayer player, ItemStack stack, BlueprintData data) {
        BlockPos min = data.getMinPos();
        BlockPos max = data.getMaxPos();
        ServerLevel level = player.serverLevel();

        List<BlueprintData.BlockEntry> blocks = new ArrayList<>();

        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && !state.is(Blocks.BEDROCK)) {
                blocks.add(new BlueprintData.BlockEntry(
                        pos.getX() - min.getX(),
                        pos.getY() - min.getY(),
                        pos.getZ() - min.getZ(),
                        state
                ));

                level.removeBlock(pos, false);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }

        BlockPos dims = data.getDimensions();
        BlueprintData saved = data.withBlocks(blocks, dims.getX(), dims.getY(), dims.getZ(), true);
        stack.set(ModDataComponents.BLUEPRINT_DATA.get(), saved);

        player.displayClientMessage(
                Component.translatable("message.asiandecor.blueprint.cut",
                        dims.getX(), dims.getY(), dims.getZ(), blocks.size()),
                true
        );
        player.playNotifySound(SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    private InteractionResult handleRightClick(Level level, Player player, ItemStack stack,
                                               BlueprintData data, BlockPos clickedPos) {
        if (!data.hasData()) {
            if (data.hasBothPositions()) {
                player.displayClientMessage(
                        Component.translatable("message.asiandecor.blueprint.ready_to_cut"),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.translatable("message.asiandecor.blueprint.empty"),
                        true
                );
            }
            return InteractionResult.FAIL;
        }

        if (level.isClientSide) return InteractionResult.SUCCESS;

        ServerPlayer serverPlayer = (ServerPlayer) player;
        BlockPos anchor = clickedPos.above();

        // Get current preview position for this player
        BlockPos currentPreview = BlueprintSetPreviewPacket.getPlayerPreviewPos(player.getUUID());

        if (currentPreview != null && currentPreview.equals(anchor)) {
            // Place and CLEAR
            placeBlueprint((ServerLevel) level, anchor, data, player, stack);
            BlueprintClearPreviewPacket.sendToPlayer(serverPlayer);
            stack.set(ModDataComponents.BLUEPRINT_DATA.get(), BlueprintData.EMPTY);
            return InteractionResult.sidedSuccess(false);
        } else {
            // Show/update preview
            BlueprintSetPreviewPacket.sendToPlayer(serverPlayer, anchor, data);
            player.displayClientMessage(
                    Component.translatable("message.asiandecor.blueprint.preview",
                            data.sizeX(), data.sizeY(), data.sizeZ(),
                            data.getRotationName()),
                    true
            );
            return InteractionResult.sidedSuccess(false);
        }
    }

    private void placeBlueprint(ServerLevel level, BlockPos origin, BlueprintData data,
                                Player player, ItemStack stack) {
        int placed = 0;
        int failed = 0;

        for (BlueprintData.BlockEntry entry : data.blocks()) {
            BlockPos rotatedPos = data.rotatePos(entry.x(), entry.y(), entry.z());
            BlockPos targetPos = origin.offset(rotatedPos.getX(), rotatedPos.getY(), rotatedPos.getZ());
            BlockState rotatedState = data.rotateBlockState(entry.state());

            BlockState existing = level.getBlockState(targetPos);
            if (!existing.isAir() && !existing.canBeReplaced()) {
                failed++;
                continue;
            }

            if (level.setBlock(targetPos, rotatedState, 3)) {
                placed++;
            } else {
                failed++;
            }
        }

        player.displayClientMessage(
                Component.translatable("message.asiandecor.blueprint.placed_cleared", placed, failed),
                true
        );
        level.playSound(null, origin, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0f, 0.8f);

        if (!player.isCreative()) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        BlueprintData data = stack.getOrDefault(ModDataComponents.BLUEPRINT_DATA.get(), BlueprintData.EMPTY);

        if (data.hasData()) {
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.saved_size",
                    data.sizeX(), data.sizeY(), data.sizeZ()).withStyle(net.minecraft.ChatFormatting.GREEN));
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.block_count",
                    data.blocks().size()).withStyle(net.minecraft.ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.rotation",
                    data.getRotationName()).withStyle(net.minecraft.ChatFormatting.AQUA));
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.facing",
                    data.getFacingName()).withStyle(net.minecraft.ChatFormatting.YELLOW));
            if (data.cutMode()) {
                tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.cut_mode")
                        .withStyle(net.minecraft.ChatFormatting.RED));
            }

        } else if (data.hasBothPositions()) {
            BlockPos dims = data.getDimensions();
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.area_selected",
                    dims.getX(), dims.getY(), dims.getZ()).withStyle(net.minecraft.ChatFormatting.YELLOW));
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.shift_to_cut")
                    .withStyle(net.minecraft.ChatFormatting.GREEN));

        } else if (data.pos1().isPresent()) {
            BlockPos p1 = data.pos1().get();
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.pos1",
                    p1.getX(), p1.getY(), p1.getZ()).withStyle(net.minecraft.ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.set_pos2")
                    .withStyle(net.minecraft.ChatFormatting.YELLOW));

        } else {
            tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.no_area")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        }

        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.usage.select")
                .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.usage.cut")
                .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.usage.preview")
                .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.usage.rotate")
                .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.asiandecor.blueprint.usage.rotate_vertical")
                .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
    }
}