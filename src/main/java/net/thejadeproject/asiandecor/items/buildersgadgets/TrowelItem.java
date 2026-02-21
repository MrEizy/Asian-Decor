package net.thejadeproject.asiandecor.items.buildersgadgets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.component.PouchContents;
import net.thejadeproject.asiandecor.component.TrowelDataComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrowelItem extends Item {

    private static final Random RANDOM = new Random();

    public enum Mode {
        HOTBAR(0, "hotbar"),
        INVENTORY(1, "inventory"),
        POUCH(2, "pouch");

        private final int id;
        private final String name;

        Mode(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }

        public static Mode byId(int id) {
            for (Mode mode : values()) {
                if (mode.id == id) return mode;
            }
            return HOTBAR;
        }

        public Mode next(boolean hasPouchLink) {
            int nextId = (this.id + 1) % 3;
            if (nextId == 2 && !hasPouchLink) {
                nextId = 0;
            }
            return byId(nextId);
        }
    }

    public TrowelItem(Properties properties) {
        super(properties.stacksTo(1).durability(128));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        TrowelDataComponent data = stack.getOrDefault(ModDataComponents.TROWEL_DATA.get(), TrowelDataComponent.EMPTY);
        Mode mode = data.getMode();
        boolean hasPouchLink = data.hasLinkedPouch();

        tooltipComponents.add(Component.translatable("tooltip.asiandecor.trowel.mode",
                Component.translatable("tooltip.asiandecor.trowel.mode." + mode.getName())));

        if (mode == Mode.POUCH && hasPouchLink) {
            ItemStack pouch = data.getLinkedPouch();
            if (!pouch.isEmpty()) {
                tooltipComponents.add(Component.translatable("tooltip.asiandecor.trowel.linked_pouch"));
                PouchContents contents = pouch.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
                int filled = contents.getNonEmptySlotCount();
                tooltipComponents.add(Component.translatable("tooltip.asiandecor.trowel.pouch_slots",
                        filled, PouchContents.MAX_SLOTS));
            }
        } else if (mode == Mode.POUCH && !hasPouchLink) {
            tooltipComponents.add(Component.translatable("tooltip.asiandecor.trowel.no_pouch_link"));
        }

        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("tooltip.asiandecor.trowel.usage"));
        tooltipComponents.add(Component.translatable("tooltip.asiandecor.trowel.toggle_key", "V"));

        if (!hasPouchLink) {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.asiandecor.trowel.link_instruction"));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        ItemStack trowelStack = context.getItemInHand();
        Level level = context.getLevel();

        if (level.isClientSide) return InteractionResult.SUCCESS;

        TrowelDataComponent data = trowelStack.getOrDefault(ModDataComponents.TROWEL_DATA.get(), TrowelDataComponent.EMPTY);
        Mode mode = data.getMode();
        List<ItemStack> availableBlocks = getAvailableBlocks(player, data, mode);

        if (availableBlocks.isEmpty()) {
            player.displayClientMessage(Component.translatable("message.asiandecor.trowel.no_blocks"), true);
            return InteractionResult.FAIL;
        }

        // Select random block
        ItemStack selectedStack = availableBlocks.get(RANDOM.nextInt(availableBlocks.size()));

        if (!(selectedStack.getItem() instanceof BlockItem blockItem)) {
            return InteractionResult.PASS;
        }

        // Try to place
        BlockPlaceContext placeContext = new BlockPlaceContext(
                level,
                player,
                context.getHand(),
                selectedStack,
                new BlockHitResult(
                        context.getClickLocation(),
                        context.getClickedFace(),
                        context.getClickedPos(),
                        context.isInside()
                )
        );

        return tryPlaceBlock(placeContext, blockItem, player, trowelStack, selectedStack, mode, data);
    }

    private List<ItemStack> getAvailableBlocks(Player player, TrowelDataComponent data, Mode mode) {
        List<ItemStack> blocks = new ArrayList<>();

        switch (mode) {
            case HOTBAR -> {
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                        blocks.add(stack);
                    }
                }
            }
            case INVENTORY -> {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                        blocks.add(stack);
                    }
                }
            }
            case POUCH -> {
                // Find actual pouch in inventory, not the stored copy
                ItemStack pouch = findActualPouch(player, data);
                if (!pouch.isEmpty()) {
                    PouchContents contents = pouch.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
                    List<ItemStack> items = contents.toItemList();
                    for (int i = 0; i < PouchContents.MAX_SLOTS && i < items.size(); i++) {
                        ItemStack stack = items.get(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                            blocks.add(stack);
                        }
                    }
                }
            }
        }

        return blocks;
    }

    private ItemStack findActualPouch(Player player, TrowelDataComponent data) {
        ItemStack linkedPouch = data.getLinkedPouch();
        if (linkedPouch.isEmpty()) return ItemStack.EMPTY;

        PouchContents linkedContents = linkedPouch.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);

        // Search player's inventory for the actual pouch
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack invStack = player.getInventory().getItem(i);
            if (invStack.getItem() instanceof BlockPouchItem) {
                PouchContents invContents = invStack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);

                // Match by comparing contents - same selected slot and similar contents
                if (invContents.selectedSlot() == linkedContents.selectedSlot()) {
                    // Additional check: compare if they have same items in same slots
                    boolean matches = true;
                    for (int slot = 0; slot < Math.min(PouchContents.MAX_SLOTS, 5); slot++) {
                        ItemStack linkedSlot = linkedContents.toItemList().get(slot);
                        ItemStack invSlot = invContents.toItemList().get(slot);
                        if (!ItemStack.matches(linkedSlot, invSlot)) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        return invStack;
                    }
                }
            }
        }

        // Return linked copy as fallback (won't consume properly but prevents crash)
        return linkedPouch;
    }

    private InteractionResult tryPlaceBlock(BlockPlaceContext context, BlockItem blockItem,
                                            Player player, ItemStack trowelStack,
                                            ItemStack sourceStack, Mode mode,
                                            TrowelDataComponent data) {
        Level level = context.getLevel();
        BlockPos placePos = context.getClickedPos();

        if (!level.getBlockState(placePos).canBeReplaced(context)) {
            placePos = placePos.relative(context.getClickedFace());
        }

        if (!level.mayInteract(player, placePos) || !player.mayUseItemAt(placePos, context.getClickedFace(), trowelStack)) {
            return InteractionResult.FAIL;
        }

        BlockState state = blockItem.getBlock().getStateForPlacement(context);
        if (state == null) return InteractionResult.FAIL;

        if (!level.setBlock(placePos, state, 11)) {
            return InteractionResult.FAIL;
        }

        BlockState placedState = level.getBlockState(placePos);
        SoundType soundType = placedState.getSoundType();
        level.playSound(player, placePos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

        // Consume item and damage trowel
        if (!player.isCreative()) {
            // Damage trowel by 1
            trowelStack.setDamageValue(trowelStack.getDamageValue() + 1);

            // Check if broken
            if (trowelStack.getDamageValue() >= trowelStack.getMaxDamage()) {
                trowelStack.shrink(1);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_BREAK,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            // Consume from source
            switch (mode) {
                case HOTBAR, INVENTORY -> sourceStack.shrink(1);
                case POUCH -> {
                    // Find the actual pouch in inventory (not the stored copy)
                    ItemStack actualPouch = findActualPouch(player, data);

                    if (!actualPouch.isEmpty()) {
                        PouchContents contents = actualPouch.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);

                        // Find the matching slot by item type
                        for (int i = 0; i < PouchContents.MAX_SLOTS; i++) {
                            ItemStack slotStack = contents.toItemList().get(i);
                            if (!slotStack.isEmpty() && slotStack.getItem() == sourceStack.getItem()) {
                                // Create new stack with decreased count
                                ItemStack newStack = slotStack.copy();
                                newStack.shrink(1);

                                // Update the ACTUAL pouch in inventory
                                PouchContents newContents = contents.withSlot(i, newStack);
                                actualPouch.set(ModDataComponents.POUCH_CONTENTS.get(), newContents);

                                // Also update the trowel's linked reference to match
                                trowelStack.set(ModDataComponents.TROWEL_DATA.get(), data.withLinkedPouch(actualPouch.copy()));
                                break;
                            }
                        }
                    }
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static Mode getMode(ItemStack stack) {
        TrowelDataComponent data = stack.get(ModDataComponents.TROWEL_DATA.get());
        return data != null ? data.getMode() : Mode.HOTBAR;
    }

    public static void setMode(ItemStack stack, Mode mode) {
        TrowelDataComponent current = stack.getOrDefault(ModDataComponents.TROWEL_DATA.get(), TrowelDataComponent.EMPTY);
        stack.set(ModDataComponents.TROWEL_DATA.get(), current.withMode(mode));
    }

    public static boolean hasLinkedPouch(ItemStack stack) {
        TrowelDataComponent data = stack.get(ModDataComponents.TROWEL_DATA.get());
        return data != null && data.hasLinkedPouch();
    }

    public static ItemStack getLinkedPouch(ItemStack stack) {
        TrowelDataComponent data = stack.get(ModDataComponents.TROWEL_DATA.get());
        return data != null ? data.getLinkedPouch() : ItemStack.EMPTY;
    }

    public static void setLinkedPouch(ItemStack trowelStack, ItemStack pouchStack) {
        TrowelDataComponent current = trowelStack.getOrDefault(ModDataComponents.TROWEL_DATA.get(), TrowelDataComponent.EMPTY);
        trowelStack.set(ModDataComponents.TROWEL_DATA.get(), current.withLinkedPouch(pouchStack));
    }

    public static void clearLinkedPouch(ItemStack stack) {
        TrowelDataComponent current = stack.getOrDefault(ModDataComponents.TROWEL_DATA.get(), TrowelDataComponent.EMPTY);
        stack.set(ModDataComponents.TROWEL_DATA.get(), current.clearLinkedPouch());
    }

    public static Mode toggleMode(ItemStack stack) {
        TrowelDataComponent data = stack.getOrDefault(ModDataComponents.TROWEL_DATA.get(), TrowelDataComponent.EMPTY);
        Mode current = data.getMode();
        boolean hasPouchLink = data.hasLinkedPouch();
        Mode next = current.next(hasPouchLink);
        stack.set(ModDataComponents.TROWEL_DATA.get(), data.withMode(next));
        return next;
    }
}