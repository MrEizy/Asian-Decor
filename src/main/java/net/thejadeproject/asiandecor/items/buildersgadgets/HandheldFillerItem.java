package net.thejadeproject.asiandecor.items.buildersgadgets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.thejadeproject.asiandecor.Config;
import net.thejadeproject.asiandecor.component.HandheldFillerData;
import net.thejadeproject.asiandecor.component.ModDataComponents;

import java.util.*;

public class HandheldFillerItem extends Item {

    private static final Map<UUID, BlockPos> startPositions = new HashMap<>();
    // Queue of pending placements: Player UUID -> List of positions to place
    private static final Map<UUID, Queue<BlockPlacement>> pendingPlacements = new HashMap<>();
    // Track tick counters for each player
    private static final Map<UUID, Integer> placementTicks = new HashMap<>();

    private static final int PLACEMENT_DELAY = 1; // 10 ticks = 0.5 seconds

    private record BlockPlacement(BlockPos pos, Block block, UUID playerId, int originalCharge) {}

    public HandheldFillerItem(Properties properties) {
        super(properties);
        // Register server tick handler
        NeoForge.EVENT_BUS.addListener(this::onServerTick);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());

        tooltipComponents.add(Component.literal("Charge: " + data.charge() + " / " + HandheldFillerData.MAX_CHARGE));

        data.copiedBlock().ifPresent(block -> {
            tooltipComponents.add(Component.literal("Stored: " + block.getName().getString()));
        });

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Check for charging first
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
                    player.displayClientMessage(Component.literal("Charged! Current: " + newCharge), true);
                }
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
        }

        // Shift right-click: Copy block
        if (player.isShiftKeyDown()) {
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

            if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                Block block = level.getBlockState(pos).getBlock();

                if (!level.isClientSide()) {
                    HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());
                    stack.set(ModDataComponents.HANDHELD_FILLER_DATA.get(), data.withCopiedBlock(block));
                    player.displayClientMessage(Component.literal("Copied: " + block.getName().getString()), true);
                }

                level.playSound(player, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (player == null) return InteractionResult.PASS;

        // Check for charging first
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
                    player.displayClientMessage(Component.literal("Charged! Current: " + newCharge), true);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        // Start filling mode - store position and start using item
        HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());

        if (data.charge() > 0 && data.copiedBlock().isPresent()) {
            if (!level.isClientSide()) {
                startPositions.put(player.getUUID(), pos);
            }
            player.startUsingItem(context.getHand());
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (!(livingEntity instanceof Player player)) return;
        if (level.isClientSide()) return;

        BlockPos startPos = startPositions.remove(player.getUUID());
        if (startPos == null) return;

        // Get end position - what player is looking at now
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != BlockHitResult.Type.BLOCK) return;

        BlockPos endPos = hitResult.getBlockPos();

        HandheldFillerData data = stack.getOrDefault(ModDataComponents.HANDHELD_FILLER_DATA.get(), new HandheldFillerData());

        if (data.charge() <= 0 || data.copiedBlock().isEmpty()) return;

        Block targetBlock = data.copiedBlock().get();

        // Calculate all positions between start and end (inclusive)
        List<BlockPos> positions = getPositionsBetween(startPos, endPos);

        // Filter to only positions that can be replaced and that player has blocks for
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
            // Deduct all charge upfront
            int totalCost = placements.size();
            int newCharge = data.charge() - totalCost;
            stack.set(ModDataComponents.HANDHELD_FILLER_DATA.get(),
                    new HandheldFillerData(newCharge, data.copiedBlock()));

            // Store pending placements
            pendingPlacements.put(player.getUUID(), placements);
            placementTicks.put(player.getUUID(), 0);

            player.displayClientMessage(Component.literal("Placing " + placements.size() + " blocks..."), true);
        }
    }

    private void onServerTick(ServerTickEvent.Post event) {
        // Process pending placements
        Iterator<Map.Entry<UUID, Queue<BlockPlacement>>> iterator = pendingPlacements.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Queue<BlockPlacement>> entry = iterator.next();
            UUID playerId = entry.getKey();
            Queue<BlockPlacement> queue = entry.getValue();

            // Increment tick counter
            int ticks = placementTicks.getOrDefault(playerId, 0) + 1;
            placementTicks.put(playerId, ticks);

            // Only place every PLACEMENT_DELAY ticks
            if (ticks < PLACEMENT_DELAY) continue;

            // Reset tick counter
            placementTicks.put(playerId, 0);

            // Get next placement
            BlockPlacement placement = queue.poll();
            if (placement == null) {
                // Queue empty, clean up
                iterator.remove();
                placementTicks.remove(playerId);
                continue;
            }

            // Find player
            Player player = null;
            for (var level : event.getServer().getAllLevels()) {
                player = level.getPlayerByUUID(playerId);
                if (player != null) break;
            }

            if (player == null) {
                // Player offline, refund remaining blocks and clean up
                iterator.remove();
                placementTicks.remove(playerId);
                continue;
            }

            // Check if player still has the block
            if (!hasBlockInInventory(player, placement.block())) {
                // Skip this block, player ran out
                continue;
            }

            // Place the block
            Level level = player.level();
            BlockState placeState = placement.block().defaultBlockState();

            if (level.getBlockState(placement.pos()).canBeReplaced()) {
                level.setBlock(placement.pos(), placeState, 3);
                consumeBlockFromInventory(player, placement.block());
                level.playSound(null, placement.pos(), placeState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.5F, 1.0F);
            }

            // If queue is now empty, clean up
            if (queue.isEmpty()) {
                iterator.remove();
                placementTicks.remove(playerId);
                player.displayClientMessage(Component.literal("Done!"), true);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
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
        for (ItemStack invStack : player.getInventory().items) {
            if (ItemStack.isSameItem(invStack, blockStack)) {
                return true;
            }
        }
        return false;
    }

    private void consumeBlockFromInventory(Player player, Block block) {
        ItemStack blockStack = new ItemStack(block);
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack invStack = player.getInventory().items.get(i);
            if (ItemStack.isSameItem(invStack, blockStack)) {
                invStack.shrink(1);
                if (invStack.isEmpty()) {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                }
                return;
            }
        }
    }
}