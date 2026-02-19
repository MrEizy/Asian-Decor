package net.thejadeproject.asiandecor.blocks.custom.furniture.tables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WingedTableBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<TablePart> PART = EnumProperty.create("part", TablePart.class);

    protected static final VoxelShape CENTER_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);
    protected static final VoxelShape LEFT_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);
    protected static final VoxelShape RIGHT_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);

    public WingedTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, TablePart.CENTER));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        Direction leftDir = getLeftDirection(facing);
        Direction rightDir = getRightDirection(facing);

        BlockPos leftPos = pos.relative(leftDir);
        BlockPos rightPos = pos.relative(rightDir);

        if (!level.getBlockState(leftPos).canBeReplaced(context) ||
                !level.getBlockState(rightPos).canBeReplaced(context)) {
            return null;
        }

        return this.defaultBlockState().setValue(FACING, facing);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);

            Direction leftDir = getLeftDirection(facing);
            Direction rightDir = getRightDirection(facing);

            BlockPos leftPos = pos.relative(rightDir);
            BlockPos rightPos = pos.relative(leftDir);

            level.setBlock(leftPos, state.setValue(PART, TablePart.LEFT), 3);
            level.setBlock(rightPos, state.setValue(PART, TablePart.RIGHT), 3);
        }
    }

    private Direction getLeftDirection(Direction facing) {
        return facing.getCounterClockWise();
    }

    private Direction getRightDirection(Direction facing) {
        return facing.getClockWise();
    }

    /**
     * Get all three positions (center, left, right) for a table structure
     */
    private TablePositions getAllPositions(BlockPos pos, BlockState state) {
        TablePart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        Direction leftDir = getLeftDirection(facing);
        Direction rightDir = getRightDirection(facing);

        BlockPos centerPos;
        BlockPos leftPos;
        BlockPos rightPos;

        switch (part) {
            case CENTER -> {
                centerPos = pos;
                leftPos = pos.relative(rightDir);  // LEFT is at rightDir from center
                rightPos = pos.relative(leftDir);   // RIGHT is at leftDir from center
            }
            case LEFT -> {
                leftPos = pos;
                centerPos = pos.relative(leftDir);  // Center is leftDir from LEFT
                rightPos = centerPos.relative(leftDir);  // RIGHT is leftDir from center
            }
            case RIGHT -> {
                rightPos = pos;
                centerPos = pos.relative(rightDir);  // Center is rightDir from RIGHT
                leftPos = centerPos.relative(rightDir);  // LEFT is rightDir from center
            }
            default -> throw new IllegalStateException("Unexpected part: " + part);
        }

        return new TablePositions(centerPos, leftPos, rightPos);
    }

    private record TablePositions(BlockPos center, BlockPos left, BlockPos right) {}

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
                                  LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {

        TablePart part = state.getValue(PART);
        Direction tableFacing = state.getValue(FACING);

        Direction leftDir = getLeftDirection(tableFacing);
        Direction rightDir = getRightDirection(tableFacing);

        // Only check for removal if the update is from a direction we care about
        if (part == TablePart.CENTER) {
            if (facing == rightDir && !isValidTablePart(facingState, tableFacing, TablePart.LEFT)) {
                return Blocks.AIR.defaultBlockState();
            }
            if (facing == leftDir && !isValidTablePart(facingState, tableFacing, TablePart.RIGHT)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        else if (part == TablePart.LEFT) {
            if (facing == leftDir && !isValidTablePart(facingState, tableFacing, TablePart.CENTER)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        else if (part == TablePart.RIGHT) {
            if (facing == rightDir && !isValidTablePart(facingState, tableFacing, TablePart.CENTER)) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    private boolean isValidTablePart(BlockState state, Direction expectedFacing, TablePart expectedPart) {
        if (!(state.getBlock() instanceof WingedTableBlock)) {
            return false;
        }
        return state.getValue(FACING) == expectedFacing && state.getValue(PART) == expectedPart;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            removeOtherParts(level, pos, state);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * Removes the other two parts of the table when any part is broken.
     * The part being broken by the player will be handled by vanilla (and your loot table).
     */
    private void removeOtherParts(Level level, BlockPos brokenPos, BlockState brokenState) {
        TablePart brokenPart = brokenState.getValue(PART);
        TablePositions positions = getAllPositions(brokenPos, brokenState);

        // Remove the two parts that weren't broken
        switch (brokenPart) {
            case CENTER -> {
                removeBlockSilent(level, positions.left);
                removeBlockSilent(level, positions.right);
            }
            case LEFT -> {
                removeBlockSilent(level, positions.center);
                removeBlockSilent(level, positions.right);
            }
            case RIGHT -> {
                removeBlockSilent(level, positions.center);
                removeBlockSilent(level, positions.left);
            }
        }
    }

    /**
     * Silently removes a block by setting it to AIR without drops or updates.
     * Checks if it's actually a WingedTableBlock before removing.
     */
    private void removeBlockSilent(Level level, BlockPos pos) {
        // Don't remove if it's already air or the same block being processed
        if (!level.hasChunkAt(pos)) return;

        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof WingedTableBlock) {
            // Set to air with flags: 2 (send to clients) + 16 (skip drops and updates)
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2 | 16);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(PART)) {
            case CENTER -> CENTER_SHAPE;
            case LEFT -> LEFT_SHAPE;
            case RIGHT -> RIGHT_SHAPE;
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction facing = state.getValue(FACING);
        Direction mirroredFacing = mirror.getRotation(facing).rotate(facing);

        BlockState rotated = state.setValue(FACING, mirroredFacing);

        TablePart part = state.getValue(PART);
        if (mirror != Mirror.NONE) {
            rotated = rotated.setValue(PART, part == TablePart.LEFT ? TablePart.RIGHT :
                    (part == TablePart.RIGHT ? TablePart.LEFT : TablePart.CENTER));
        }

        return rotated;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    public enum TablePart implements net.minecraft.util.StringRepresentable {
        LEFT("left"),
        CENTER("center"),
        RIGHT("right");

        private final String name;

        TablePart(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}