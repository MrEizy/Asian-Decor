package net.thejadeproject.asiandecor.blocks.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AsianDecor.MOD_ID);


    public static final Supplier<BlockEntityType<ShapeMakerBlockEntity>> SHAPE_MAKER =
            BLOCK_ENTITIES.register("shape_maker",
                    () -> BlockEntityType.Builder.of(
                            ShapeMakerBlockEntity::new,
                            ModBlocks.SHAPE_MAKER.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<BrickMixerBlockEntity>> BRICK_MIXER =
            BLOCK_ENTITIES.register("brick_mixer", () -> BlockEntityType.Builder.of(
                    BrickMixerBlockEntity::new,
                    ModBlocks.BRICK_MIXER.get()
            ).build(null));




    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
