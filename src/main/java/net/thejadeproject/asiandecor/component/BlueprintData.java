package net.thejadeproject.asiandecor.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record BlueprintData(
        List<BlockEntry> blocks,
        int sizeX, int sizeY, int sizeZ,
        boolean hasData,
        Optional<BlockPos> pos1,
        Optional<BlockPos> pos2,
        boolean cutMode,
        int rotationY, // 0-3 (horizontal rotation around Y axis)
        int rotationX  // 0-3 (vertical rotation around X axis - allows wall/ceiling placement)
) {
    public static final int MAX_SIZE = 16;

    public record BlockEntry(int x, int y, int z, BlockState state) {
        public static final Codec<BlockEntry> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("x").forGetter(BlockEntry::x),
                        Codec.INT.fieldOf("y").forGetter(BlockEntry::y),
                        Codec.INT.fieldOf("z").forGetter(BlockEntry::z),
                        BlockState.CODEC.fieldOf("state").forGetter(BlockEntry::state)
                ).apply(instance, BlockEntry::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, BlockEntry::x,
                ByteBufCodecs.INT, BlockEntry::y,
                ByteBufCodecs.INT, BlockEntry::z,
                ByteBufCodecs.fromCodec(BlockState.CODEC), BlockEntry::state,
                BlockEntry::new
        );
    }

    public static final Codec<BlueprintData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockEntry.CODEC.listOf().fieldOf("blocks").forGetter(BlueprintData::blocks),
                    Codec.INT.fieldOf("size_x").forGetter(BlueprintData::sizeX),
                    Codec.INT.fieldOf("size_y").forGetter(BlueprintData::sizeY),
                    Codec.INT.fieldOf("size_z").forGetter(BlueprintData::sizeZ),
                    Codec.BOOL.fieldOf("has_data").forGetter(BlueprintData::hasData),
                    BlockPos.CODEC.optionalFieldOf("pos1").forGetter(BlueprintData::pos1),
                    BlockPos.CODEC.optionalFieldOf("pos2").forGetter(BlueprintData::pos2),
                    Codec.BOOL.fieldOf("cut_mode").forGetter(BlueprintData::cutMode),
                    Codec.INT.fieldOf("rotation_y").forGetter(BlueprintData::rotationY),
                    Codec.INT.fieldOf("rotation_x").forGetter(BlueprintData::rotationX)
            ).apply(instance, BlueprintData::new)
    );

    public static final StreamCodec<ByteBuf, BlueprintData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public BlueprintData decode(ByteBuf buf) {
            List<BlockEntry> blocks = BlockEntry.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)).decode((RegistryFriendlyByteBuf) buf);
            int sizeX = ByteBufCodecs.INT.decode(buf);
            int sizeY = ByteBufCodecs.INT.decode(buf);
            int sizeZ = ByteBufCodecs.INT.decode(buf);
            boolean hasData = ByteBufCodecs.BOOL.decode(buf);
            Optional<BlockPos> pos1 = ByteBufCodecs.optional(BlockPos.STREAM_CODEC).decode((RegistryFriendlyByteBuf) buf);
            Optional<BlockPos> pos2 = ByteBufCodecs.optional(BlockPos.STREAM_CODEC).decode((RegistryFriendlyByteBuf) buf);
            boolean cutMode = ByteBufCodecs.BOOL.decode(buf);
            int rotationY = ByteBufCodecs.INT.decode(buf);
            int rotationX = ByteBufCodecs.INT.decode(buf);
            return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, pos1, pos2, cutMode, rotationY, rotationX);
        }

        @Override
        public void encode(ByteBuf buf, BlueprintData data) {
            BlockEntry.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)).encode((RegistryFriendlyByteBuf) buf, (ArrayList<BlockEntry>) data.blocks);
            ByteBufCodecs.INT.encode(buf, data.sizeX);
            ByteBufCodecs.INT.encode(buf, data.sizeY);
            ByteBufCodecs.INT.encode(buf, data.sizeZ);
            ByteBufCodecs.BOOL.encode(buf, data.hasData);
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC).encode((RegistryFriendlyByteBuf) buf, data.pos1);
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC).encode((RegistryFriendlyByteBuf) buf, data.pos2);
            ByteBufCodecs.BOOL.encode(buf, data.cutMode);
            ByteBufCodecs.INT.encode(buf, data.rotationY);
            ByteBufCodecs.INT.encode(buf, data.rotationX);
        }
    };

    public static final BlueprintData EMPTY = new BlueprintData(new ArrayList<>(), 0, 0, 0, false, Optional.empty(), Optional.empty(), false, 0, 0);

    public BlueprintData withPos1(BlockPos pos) {
        return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, Optional.of(pos), pos2, cutMode, rotationY, rotationX);
    }

    public BlueprintData withPos2(BlockPos pos) {
        return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, pos1, Optional.of(pos), cutMode, rotationY, rotationX);
    }

    public BlueprintData withBlocks(List<BlockEntry> newBlocks, int sx, int sy, int sz, boolean cut) {
        return new BlueprintData(newBlocks, sx, sy, sz, true, Optional.empty(), Optional.empty(), cut, 0, 0);
    }

    public BlueprintData withRotationY(int newRotation) {
        return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, pos1, pos2, cutMode, newRotation & 3, rotationX);
    }

    public BlueprintData withRotationX(int newRotation) {
        return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, pos1, pos2, cutMode, rotationY, newRotation & 3);
    }

    public Rotation getRotationY() {
        return switch (rotationY) {
            case 1 -> Rotation.CLOCKWISE_90;
            case 2 -> Rotation.CLOCKWISE_180;
            case 3 -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    public String getRotationName() {
        String y = switch (rotationY) {
            case 1 -> "90°";
            case 2 -> "180°";
            case 3 -> "270°";
            default -> "0°";
        };
        String x = switch (rotationX) {
            case 1 -> " (Wall)";
            case 2 -> " (Ceiling)";
            case 3 -> " (Wall Inv)";
            default -> "";
        };
        return y + x;
    }

    public String getFacingName() {
        return switch (rotationX) {
            case 0 -> "Floor";
            case 1 -> "Wall (South)";
            case 2 -> "Ceiling";
            case 3 -> "Wall (North)";
            default -> "Floor";
        };
    }

    // Full 3D rotation of position
    public BlockPos rotatePos(int x, int y, int z) {
        int rx = x, ry = y, rz = z;

        // Apply Y rotation first (horizontal)
        switch (rotationY) {
            case 1 -> { int t = rx; rx = -rz; rz = t; } // 90 clockwise
            case 2 -> { rx = -rx; rz = -rz; } // 180
            case 3 -> { int t = rx; rx = rz; rz = -t; } // 270 clockwise
        }

        // Apply X rotation (vertical - pitch)
        switch (rotationX) {
            case 1 -> { int t = ry; ry = -rz; rz = t; } // Forward 90 (wall)
            case 2 -> { ry = -ry; rz = -rz; } // 180 (ceiling)
            case 3 -> { int t = ry; ry = rz; rz = -t; } // Back 90 (wall other side)
        }

        return new BlockPos(rx, ry, rz);
    }

    // Rotate block state for all 6 directions
    public BlockState rotateBlockState(BlockState state) {
        // First apply Y rotation (horizontal)
        state = state.rotate(getRotationY());

        // Then apply X rotation for vertical placement
        // This is complex - we need to remap directions
        state = rotateVertical(state);

        return state;
    }

    private BlockState rotateVertical(BlockState state) {
        if (rotationX == 0) return state; // No vertical rotation

        // Handle directional properties
        for (Property<?> prop : state.getProperties()) {
            if (prop instanceof DirectionProperty dirProp) {
                Direction current = state.getValue(dirProp);
                Direction rotated = rotateDirectionVertical(current);
                if (dirProp.getPossibleValues().contains(rotated)) {
                    state = state.setValue(dirProp, rotated);
                }
            } else if (prop instanceof EnumProperty<?> enumProp) {
                // Handle other enum properties if needed
            }
        }

        return state;
    }

    private Direction rotateDirectionVertical(Direction dir) {
        return switch (rotationX) {
            case 1 -> switch (dir) { // Wall (pitched 90 forward)
                case UP -> Direction.SOUTH;
                case DOWN -> Direction.NORTH;
                case SOUTH -> Direction.DOWN;
                case NORTH -> Direction.UP;
                default -> dir;
            };
            case 2 -> switch (dir) { // Ceiling (180)
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                default -> dir;
            };
            case 3 -> switch (dir) { // Wall other side (pitched 90 back)
                case UP -> Direction.NORTH;
                case DOWN -> Direction.SOUTH;
                case SOUTH -> Direction.UP;
                case NORTH -> Direction.DOWN;
                default -> dir;
            };
            default -> dir;
        };
    }

    public boolean hasBothPositions() {
        return pos1.isPresent() && pos2.isPresent();
    }

    public BlockPos getMinPos() {
        if (!hasBothPositions()) return BlockPos.ZERO;
        BlockPos p1 = pos1.get();
        BlockPos p2 = pos2.get();
        return new BlockPos(
                Math.min(p1.getX(), p2.getX()),
                Math.min(p1.getY(), p2.getY()),
                Math.min(p1.getZ(), p2.getZ())
        );
    }

    public BlockPos getMaxPos() {
        if (!hasBothPositions()) return BlockPos.ZERO;
        BlockPos p1 = pos1.get();
        BlockPos p2 = pos2.get();
        return new BlockPos(
                Math.max(p1.getX(), p2.getX()),
                Math.max(p1.getY(), p2.getY()),
                Math.max(p1.getZ(), p2.getZ())
        );
    }

    public boolean isValidSize() {
        if (!hasBothPositions()) return false;
        BlockPos dims = getDimensions();
        return dims.getX() <= MAX_SIZE && dims.getY() <= MAX_SIZE && dims.getZ() <= MAX_SIZE;
    }

    public BlockPos getDimensions() {
        BlockPos min = getMinPos();
        BlockPos max = getMaxPos();
        return new BlockPos(
                max.getX() - min.getX() + 1,
                max.getY() - min.getY() + 1,
                max.getZ() - min.getZ() + 1
        );
    }

    // Get rotated dimensions for preview/placement
    public BlockPos getRotatedDimensions() {
        BlockPos dims = getDimensions();
        int rx = dims.getX(), ry = dims.getY(), rz = dims.getZ();

        // Apply Y rotation to dimensions
        switch (rotationY) {
            case 1, 3 -> { int t = rx; rx = rz; rz = t; }
        }

        // Apply X rotation to dimensions
        switch (rotationX) {
            case 1, 3 -> { int t = ry; ry = rz; rz = t; }
            case 2 -> { ry = dims.getY(); } // Ceiling keeps same
        }

        return new BlockPos(rx, ry, rz);
    }
}