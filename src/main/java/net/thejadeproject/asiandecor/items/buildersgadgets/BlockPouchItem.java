package net.thejadeproject.asiandecor.items.buildersgadgets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
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
import net.thejadeproject.asiandecor.screen.custom.PouchMenu;

import java.util.List;

public class BlockPouchItem extends Item {
    public static final Component CONTAINER_TITLE = Component.translatable("container.asiandecor.block_pouch");

    public BlockPouchItem(Properties properties) {
        super(properties.component(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                openMenu(serverPlayer, stack, hand);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        ItemStack pouchStack = context.getItemInHand();
        PouchContents contents = pouchStack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
        ItemStack selectedStack = contents.getSelectedStack();

        if (selectedStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!(selectedStack.getItem() instanceof BlockItem blockItem)) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();

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

        InteractionResult result = tryPlaceBlock(placeContext, blockItem, pouchStack, contents, selectedStack, player);

        return result;
    }

    private InteractionResult tryPlaceBlock(BlockPlaceContext context, BlockItem blockItem,
                                            ItemStack pouchStack, PouchContents contents,
                                            ItemStack selectedStack, Player player) {
        Level level = context.getLevel();
        BlockPos placePos = context.getClickedPos();

        if (!level.getBlockState(placePos).canBeReplaced(context)) {
            placePos = placePos.relative(context.getClickedFace());
        }

        if (!level.mayInteract(player, placePos) || !player.mayUseItemAt(placePos, context.getClickedFace(), pouchStack)) {
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

        if (!player.isCreative()) {
            ItemStack newSelected = selectedStack.copy();
            newSelected.shrink(1);

            PouchContents newContents = contents.withSlot(contents.selectedSlot(), newSelected);
            pouchStack.set(ModDataComponents.POUCH_CONTENTS.get(), newContents);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void openMenu(ServerPlayer player, ItemStack stack, InteractionHand hand) {
        player.openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, p) -> new PouchMenu(containerId, playerInventory, stack, hand),
                CONTAINER_TITLE
        ));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        PouchContents contents = stack.getOrDefault(ModDataComponents.POUCH_CONTENTS.get(), PouchContents.EMPTY);
        ItemStack selected = contents.getSelectedStack();

        if (!selected.isEmpty()) {
            tooltipComponents.add(Component.translatable("tooltip.asiandecor.block_pouch.selected",
                    selected.getHoverName()).withStyle(net.minecraft.ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("tooltip.asiandecor.block_pouch.count",
                    selected.getCount()).withStyle(net.minecraft.ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.asiandecor.block_pouch.empty")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        }

        int filled = contents.getNonEmptySlotCount();
        tooltipComponents.add(Component.translatable("tooltip.asiandecor.block_pouch.slots",
                filled, PouchContents.MAX_SLOTS).withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }
}