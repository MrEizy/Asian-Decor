package net.thejadeproject.asiandecor.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;

import java.util.function.Supplier;

import static net.minecraft.util.datafix.fixes.References.DATA_COMPONENTS;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AsianDecor.MOD_ID);

    public static final Supplier<DataComponentType<PouchContents>> POUCH_CONTENTS = REGISTRAR.registerComponentType(
            "pouch_contents",
            builder -> builder
                    .persistent(PouchContents.CODEC)
                    .networkSynchronized(PouchContents.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<BlueprintData>> BLUEPRINT_DATA = REGISTRAR.registerComponentType(
            "blueprint_data",
            builder -> builder
                    .persistent(BlueprintData.CODEC)
                    .networkSynchronized(BlueprintData.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<TapeMeasureDataComponent>> TAPE_MEASURE_DATA =
            REGISTRAR.register("tape_measure_data", () ->
                    DataComponentType.<TapeMeasureDataComponent>builder()
                            .persistent(TapeMeasureDataComponent.CODEC)
                            .build()
            );

    public static final Supplier<DataComponentType<TrowelDataComponent>> TROWEL_DATA =
            REGISTRAR.register("trowel_data", () ->
                    DataComponentType.<TrowelDataComponent>builder()
                            .persistent(TrowelDataComponent.CODEC)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyedBrickData>> BRICK_DATA =
            REGISTRAR.register("brick_data", () ->
                    DataComponentType.<DyedBrickData>builder()
                            .persistent(DyedBrickData.CODEC)
                            .networkSynchronized(DyedBrickData.STREAM_CODEC)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ColorMixerData>> COLOR_MIXER_DATA =
            REGISTRAR.register("color_mixer_data", () ->
                    DataComponentType.<ColorMixerData>builder()
                            .persistent(ColorMixerData.CODEC)
                            .networkSynchronized(ColorMixerData.STREAM_CODEC)
                            .build()
            );


    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }
}