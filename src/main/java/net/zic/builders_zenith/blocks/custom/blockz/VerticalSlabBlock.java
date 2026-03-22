package net.zic.builders_zenith.blocks.custom.blockz;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalSlabBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty DOUBLE = BooleanProperty.create("double");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 0, 16, 16, 8);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 8, 16, 16, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 0, 0, 8, 16, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(8, 0, 0, 16, 16, 16);
    private static final VoxelShape DOUBLE_SHAPE = Shapes.block();

    public VerticalSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(DOUBLE, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DOUBLE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState existingState = context.getLevel().getBlockState(pos);
        Direction clickedFace = context.getClickedFace();
        ItemStack heldItem = context.getItemInHand();

        if (existingState.getBlock() instanceof VerticalSlabBlock existingBlock
                && !existingState.getValue(DOUBLE)) {
            Direction existingFacing = existingState.getValue(FACING);

            if (clickedFace == existingFacing.getOpposite() && isSameSlabType(heldItem, existingBlock)) {
                return existingState.setValue(DOUBLE, true).setValue(WATERLOGGED, false);
            }
        }

        Direction facing;

        if (clickedFace == Direction.UP || clickedFace == Direction.DOWN) {
            facing = getFacingFromHitPosition(context);
        }
        else {
            facing = clickedFace;
        }

        FluidState fluidState = context.getLevel().getFluidState(pos);

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(DOUBLE, false)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }


    private Direction getFacingFromHitPosition(BlockPlaceContext context) {
        Direction playerFacing = context.getHorizontalDirection();

        double hitX = context.getClickLocation().x() - context.getClickedPos().getX();
        double hitZ = context.getClickLocation().z() - context.getClickedPos().getZ();

        if (playerFacing.getAxis() == Direction.Axis.Z) {
            double relativePos = (playerFacing == Direction.SOUTH) ? hitZ : (1 - hitZ);

            if (relativePos > 0.5) {
                return playerFacing;
            } else {
                return playerFacing.getOpposite();
            }
        } else {
            double relativePos = (playerFacing == Direction.EAST) ? hitX : (1 - hitX);

            if (relativePos > 0.5) {
                return playerFacing;
            } else {
                return playerFacing.getOpposite();
            }
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (state.getValue(DOUBLE)) {
            return false;
        }

        Direction existingFacing = state.getValue(FACING);
        Direction clickedFace = context.getClickedFace();
        Block existingBlock = state.getBlock();
        ItemStack heldItem = context.getItemInHand();

        return clickedFace == existingFacing.getOpposite()
                && isSameSlabType(heldItem, existingBlock);
    }

    private boolean isSameSlabType(ItemStack heldItem, Block existingBlock) {
        if (!(heldItem.getItem() instanceof BlockItem blockItem)) {
            return false;
        }
        Block heldBlock = blockItem.getBlock();
        return heldBlock == existingBlock;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(DOUBLE)) {
            return DOUBLE_SHAPE;
        }
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}