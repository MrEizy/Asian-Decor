package net.thejadeproject.asiandecor.items.blockitem;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickBlock;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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

    // Helper method to format enum name to readable string (RED -> Red, LIGHT_BLUE -> Light Blue)
    private String formatColorName(net.minecraft.world.item.DyeColor color) {
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
        BlockState state = super.getPlacementState(context);
        if (state == null) return null;

        ItemStack stack = context.getItemInHand();
        DyedBrickData data = stack.getOrDefault(ModDataComponents.BRICK_DATA.get(),
                new DyedBrickData(net.minecraft.world.item.DyeColor.WHITE, net.minecraft.world.item.DyeColor.LIGHT_GRAY));

        return state
                .setValue(DyedBrickBlock.BRICK_COLOR, data.brickColor())
                .setValue(DyedBrickBlock.MORTAR_COLOR, data.mortarColor());
    }
}