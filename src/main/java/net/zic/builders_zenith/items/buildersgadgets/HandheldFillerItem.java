package net.zic.builders_zenith.items.buildersgadgets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.zic.builders_zenith.Config;
import net.zic.builders_zenith.component.HandheldFillerData;
import net.zic.builders_zenith.component.ModDataComponents;

import java.util.*;
import java.util.function.Consumer;

public class HandheldFillerItem extends Item {

    private static final Map<UUID, BlockPos> startPositions = new HashMap<>();
    private static final Map<UUID, Queue<BlockPlacement>> pendingPlacements = new HashMap<>();
    private static final Map<UUID, Integer> placementTicks = new HashMap<>();

    private static final int PLACEMENT_DELAY = 1;

    private record BlockPlacement(BlockPos pos, Block block, UUID playerId, int originalCharge) {}

    public HandheldFillerItem(Properties properties) {
        super(properties);
        NeoForge.EVENT_BUS.addListener(this::onServerTick);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());

        tooltip.accept(Component.literal("Charge: " + data.charge() + " / " + HandheldFillerData.MAX_CHARGE));

        data.copiedBlock().ifPresent(block -> {
            tooltip.accept(Component.literal("Stored: " + block.getName().getString()));
        });
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherHandStack = player.getItemInHand(otherHand);

        if (!otherHandStack.isEmpty()) {
            Integer chargeValue = Config.handheldFillerChargeItems.get(otherHandStack.getItem());
            if (chargeValue != null) {
                if (!level.isClientSide()) {
                    HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());
                    int newCharge = Math.min(data.charge() + chargeValue, HandheldFillerData.MAX_CHARGE);
                    stack.set(ModDataComponents.HANDHELD_FILLER_DATA.get(), data.withCharge(newCharge));

                    otherHandStack.shrink(1);
                    level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                    player.sendOverlayMessage(Component.literal("Charged! Current: " + newCharge));
                }
                return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }
        }

        if (player.isShiftKeyDown()) {
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

            if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                Block block = level.getBlockState(pos).getBlock();

                if (!level.isClientSide()) {
                    HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());
                    stack.set(ModDataComponents.HANDHELD_FILLER_DATA.get(), data.withCopiedBlock(block));
                    player.sendOverlayMessage(Component.literal("Copied: " + block.getName().getString()));
                }

                level.playSound(player, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.PLAYERS, 1.0F, 1.0F);
                return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (player == null) return InteractionResult.PASS;

        InteractionHand otherHand = context.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherHandStack = player.getItemInHand(otherHand);

        if (!otherHandStack.isEmpty()) {
            Integer chargeValue = Config.handheldFillerChargeItems.get(otherHandStack.getItem());
            if (chargeValue != null) {
                if (!level.isClientSide()) {
                    HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());
                    int newCharge = Math.min(data.charge() + chargeValue, HandheldFillerData.MAX_CHARGE);
                    stack.set(ModDataComponents.HANDHELD_FILLER_DATA.get(), data.withCharge(newCharge));

                    otherHandStack.shrink(1);
                    level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                    player.sendOverlayMessage(Component.literal("Charged! Current: " + newCharge));
                }
                return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }
        }

        HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());

        if (data.charge() > 0 && data.copiedBlock().isPresent()) {
            if (!level.isClientSide()) {
                startPositions.put(player.getUUID(), pos);
            }
            player.startUsingItem(context.getHand());
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (!(livingEntity instanceof Player player)) return false;
        if (level.isClientSide()) return false;

        BlockPos startPos = startPositions.remove(player.getUUID());
        if (startPos == null) return false;

        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != BlockHitResult.Type.BLOCK) return false;

        BlockPos endPos = hitResult.getBlockPos();

        HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());

        if (data.charge() <= 0 || data.copiedBlock().isEmpty()) return false;

        Block targetBlock = data.copiedBlock().get();

        List<BlockPos> positions = getPositionsBetween(startPos, endPos);

        Queue<BlockPlacement> placements = new LinkedList<>();
        int availableCharge = data.charge();

        for (BlockPos targetPos : positions) {
            if (availableCharge <= 0) break;
            if (!hasBlockInInventory(player, targetBlock)) break;
            if (!level.getBlockState(targetPos).canBeReplaced()) continue;

            placements.add(new BlockPlacement(targetPos, targetBlock, player.getUUID(), data.charge()));
            availableCharge--;
        }

        if (!placements.isEmpty()) {
            int totalCost = placements.size();
            int newCharge = data.charge() - totalCost;
            stack.set(ModDataComponents.HANDHELD_FILLER_DATA.get(),
                    new HandheldFillerData(newCharge, data.copiedBlock()));

            pendingPlacements.put(player.getUUID(), placements);
            placementTicks.put(player.getUUID(), 0);

            player.sendOverlayMessage(Component.literal("Placing " + placements.size() + " blocks..."));
        }
        return false;
    }

    private void onServerTick(ServerTickEvent.Post event) {
        Iterator<Map.Entry<UUID, Queue<BlockPlacement>>> iterator = pendingPlacements.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Queue<BlockPlacement>> entry = iterator.next();
            UUID playerId = entry.getKey();
            Queue<BlockPlacement> queue = entry.getValue();

            int ticks = placementTicks.getOrDefault(playerId, 0) + 1;
            placementTicks.put(playerId, ticks);

            if (ticks < PLACEMENT_DELAY) continue;

            placementTicks.put(playerId, 0);

            BlockPlacement placement = queue.poll();
            if (placement == null) {
                iterator.remove();
                placementTicks.remove(playerId);
                continue;
            }

            Player player = event.getServer().getPlayerList().getPlayer(playerId);

            if (player == null) {
                iterator.remove();
                placementTicks.remove(playerId);
                continue;
            }

            if (!hasBlockInInventory(player, placement.block())) {
                continue;
            }

            Level level = player.level();
            BlockState placeState = placement.block().defaultBlockState();

            if (level.getBlockState(placement.pos()).canBeReplaced()) {
                level.setBlock(placement.pos(), placeState, 3);
                consumeBlockFromInventory(player, placement.block());
                level.playSound(null, placement.pos(), placeState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.5F, 1.0F);
            }

            if (queue.isEmpty()) {
                iterator.remove();
                placementTicks.remove(playerId);
                player.sendOverlayMessage(Component.literal("Done!"));
            }
        }
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    private List<BlockPos> getPositionsBetween(BlockPos start, BlockPos end) {
        List<BlockPos> positions = new ArrayList<>();

        int x1 = Math.min(start.getX(), end.getX());
        int x2 = Math.max(start.getX(), end.getX());
        int y1 = Math.min(start.getY(), end.getY());
        int y2 = Math.max(start.getY(), end.getY());
        int z1 = Math.min(start.getZ(), end.getZ());
        int z2 = Math.max(start.getZ(), end.getZ());

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    positions.add(new BlockPos(x, y, z));
                }
            }
        }

        return positions;
    }

    private boolean hasBlockInInventory(Player player, Block block) {
        ItemStack blockStack = new ItemStack(block);
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (ItemStack.isSameItem(inv.getItem(i), blockStack)) {
                return true;
            }
        }
        return false;
    }

    private void consumeBlockFromInventory(Player player, Block block) {
        ItemStack blockStack = new ItemStack(block);
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack invStack = inv.getItem(i);
            if (ItemStack.isSameItem(invStack, blockStack)) {
                invStack.shrink(1);
                if (invStack.isEmpty()) {
                    inv.setItem(i, ItemStack.EMPTY);
                }
                return;
            }
        }
    }
}