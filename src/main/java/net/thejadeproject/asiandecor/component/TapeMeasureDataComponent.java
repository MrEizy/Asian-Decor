package net.thejadeproject.asiandecor.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record TapeMeasureDataComponent(
        Optional<BlockPos> pos1,
        Optional<BlockPos> pos2,
        Optional<Vec3> center,
        boolean finalized
) {
    public static final Codec<TapeMeasureDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.optionalFieldOf("pos1").forGetter(TapeMeasureDataComponent::pos1),
                    BlockPos.CODEC.optionalFieldOf("pos2").forGetter(TapeMeasureDataComponent::pos2),
                    Vec3.CODEC.optionalFieldOf("center").forGetter(TapeMeasureDataComponent::center),
                    Codec.BOOL.fieldOf("finalized").orElse(false).forGetter(TapeMeasureDataComponent::finalized)
            ).apply(instance, TapeMeasureDataComponent::new)
    );

    public static final TapeMeasureDataComponent EMPTY = new TapeMeasureDataComponent(
            Optional.empty(), Optional.empty(), Optional.empty(), false
    );

    public boolean hasSelection() {
        return pos1.isPresent();
    }

    public TapeMeasureDataComponent withPos1(BlockPos pos) {
        return new TapeMeasureDataComponent(Optional.of(pos), Optional.empty(), Optional.empty(), false);
    }

    public TapeMeasureDataComponent withPos2(BlockPos pos) {
        if (pos1.isEmpty()) return this;

        AABB bounds = createBounds(pos1.get(), pos);
        Vec3 newCenter = bounds.getCenter();

        return new TapeMeasureDataComponent(pos1, Optional.of(pos), Optional.of(newCenter), true);
    }

    public TapeMeasureDataComponent cleared() {
        return EMPTY;
    }

    private static net.minecraft.world.phys.AABB createBounds(BlockPos pos1, BlockPos pos2) {
        return new net.minecraft.world.phys.AABB(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ()),
                Math.max(pos1.getX(), pos2.getX()) + 1,
                Math.max(pos1.getY(), pos2.getY()) + 1,
                Math.max(pos1.getZ(), pos2.getZ()) + 1
        );
    }
}