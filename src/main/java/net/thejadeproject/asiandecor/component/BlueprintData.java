package net.thejadeproject.asiandecor.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
        int rotation // 0=none, 1=90, 2=180, 3=270 degrees clockwise
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
                    Codec.INT.fieldOf("rotation").forGetter(BlueprintData::rotation)
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
            int rotation = ByteBufCodecs.INT.decode(buf);
            return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, pos1, pos2, cutMode, rotation);
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
            ByteBufCodecs.INT.encode(buf, data.rotation);
        }
    };

    public static final BlueprintData EMPTY = new BlueprintData(new ArrayList<>(), 0, 0, 0, false, Optional.empty(), Optional.empty(), false, 0);

    public BlueprintData withPos1(BlockPos pos) {
        return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, Optional.of(pos), pos2, cutMode, rotation);
    }

    public BlueprintData withPos2(BlockPos pos) {
        return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, pos1, Optional.of(pos), cutMode, rotation);
    }

    public BlueprintData withBlocks(List<BlockEntry> newBlocks, int sx, int sy, int sz, boolean cut) {
        return new BlueprintData(newBlocks, sx, sy, sz, true, Optional.empty(), Optional.empty(), cut, 0);
    }

    public BlueprintData withRotation(int newRotation) {
        return new BlueprintData(blocks, sizeX, sizeY, sizeZ, hasData, pos1, pos2, cutMode, newRotation & 3); // Clamp 0-3
    }

    public Rotation getRotation() {
        return switch (rotation) {
            case 1 -> Rotation.CLOCKWISE_90;
            case 2 -> Rotation.CLOCKWISE_180;
            case 3 -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    public String getRotationName() {
        return switch (rotation) {
            case 1 -> "90°";
            case 2 -> "180°";
            case 3 -> "270°";
            default -> "0°";
        };
    }

    // Rotate a relative position based on current rotation
    public BlockPos rotatePos(int x, int y, int z) {
        return switch (rotation) {
            case 1 -> new BlockPos(-z, y, x); // 90 clockwise
            case 2 -> new BlockPos(-x, y, -z); // 180
            case 3 -> new BlockPos(z, y, -x); // 270 clockwise
            default -> new BlockPos(x, y, z); // 0
        };
    }

    // Rotate block state based on rotation
    public BlockState rotateBlockState(BlockState state) {
        Rotation rot = getRotation();

        // Try to rotate the block state
        state = state.rotate(rot);

        return state;
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
        return switch (rotation) {
            case 1, 3 -> new BlockPos(dims.getZ(), dims.getY(), dims.getX()); // Swapped X/Z
            default -> dims;
        };
    }
}