package net.thejadeproject.asiandecor.items.blockitem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickBlock;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;

import java.util.List;

public class DyedBrickBlockItem extends BlockItem {

    public DyedBrickBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        DyedBrickData data = stack.get(ModDataComponents.BRICK_DATA.get());

        if (data != null) {
            tooltipComponents.add(Component.literal("Brick Tint: " + formatColorName(data.brickColor()))
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
            tooltipComponents.add(Component.literal("Mortar Tint: " + formatColorName(data.mortarColor()))
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.literal("Brick Tint: White")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
            tooltipComponents.add(Component.literal("Mortar Tint: Light Gray")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        }
    }

    private String formatColorName(DyeColor color) {
        String name = color.getName();
        String[] parts = name.split("_");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1).toLowerCase());
        }

        return result.toString();
    }

    @Override
    public BlockState getPlacementState(BlockPlaceContext context) {
        // Get base state from the block itself, not super
        BlockState state = this.getBlock().getStateForPlacement(context);
        if (state == null) return null;

        ItemStack stack = context.getItemInHand();
        DyedBrickData data = stack.get(ModDataComponents.BRICK_DATA.get());

        if (data == null) {
            data = new DyedBrickData(DyeColor.WHITE, DyeColor.LIGHT_GRAY);
        }


        BlockState result = state
                .setValue(DyedBrickBlock.BRICK_COLOR, data.brickColor())
                .setValue(DyedBrickBlock.MORTAR_COLOR, data.mortarColor());

        return result;
    }
}