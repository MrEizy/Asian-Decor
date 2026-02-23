package net.thejadeproject.asiandecor.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class DyedBrickBlock extends Block {
    public static final EnumProperty<DyeColor> BRICK_COLOR = EnumProperty.create("brick_color", DyeColor.class);
    public static final EnumProperty<DyeColor> MORTAR_COLOR = EnumProperty.create("mortar_color", DyeColor.class);

    public DyedBrickBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BRICK_COLOR, DyeColor.WHITE)
                .setValue(MORTAR_COLOR, DyeColor.LIGHT_GRAY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder); // Important!
        builder.add(BRICK_COLOR, MORTAR_COLOR);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }
}
