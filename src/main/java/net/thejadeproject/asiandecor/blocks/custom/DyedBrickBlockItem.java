package net.thejadeproject.asiandecor.blocks.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;

import java.util.List;

public class DyedBrickBlockItem extends BlockItem {
    private final DyedBrickType type;

    public DyedBrickBlockItem(Block block, Properties properties, DyedBrickType type) {
        super(block, properties);
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        // Mortar color line with actual color
        MutableComponent mortarText = Component.literal("Mortar: ")
                .withStyle(ChatFormatting.GRAY);
        MutableComponent mortarColorName = Component.literal(formatColorName(type.getMortarColor()))
                .withStyle(getColorFormatting(type.getMortarColor()));
        tooltipComponents.add(mortarText.append(mortarColorName));

        // Brick color line with actual color
        MutableComponent brickText = Component.literal("Brick: ")
                .withStyle(ChatFormatting.GRAY);
        MutableComponent brickColorName = Component.literal(formatColorName(type.getBrickColor()))
                .withStyle(getColorFormatting(type.getBrickColor()));
        tooltipComponents.add(brickText.append(brickColorName));
    }

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

    private ChatFormatting getColorFormatting(net.minecraft.world.item.DyeColor color) {
        return switch (color) {
            case WHITE -> ChatFormatting.WHITE;
            case ORANGE -> ChatFormatting.GOLD;
            case MAGENTA -> ChatFormatting.LIGHT_PURPLE;
            case LIGHT_BLUE -> ChatFormatting.AQUA;
            case YELLOW -> ChatFormatting.YELLOW;
            case LIME -> ChatFormatting.GREEN;
            case PINK -> ChatFormatting.LIGHT_PURPLE;
            case GRAY -> ChatFormatting.DARK_GRAY;
            case LIGHT_GRAY -> ChatFormatting.GRAY;
            case CYAN -> ChatFormatting.DARK_AQUA;
            case PURPLE -> ChatFormatting.DARK_PURPLE;
            case BLUE -> ChatFormatting.BLUE;
            case BROWN -> ChatFormatting.GOLD; // Closest to brown
            case GREEN -> ChatFormatting.DARK_GREEN;
            case RED -> ChatFormatting.RED;
            case BLACK -> ChatFormatting.DARK_GRAY;
        };
    }
}
