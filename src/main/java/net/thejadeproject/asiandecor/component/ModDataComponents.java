package net.thejadeproject.asiandecor.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AsianDecor.MOD_ID);

    public static final Supplier<DataComponentType<PouchContents>> POUCH_CONTENTS = REGISTRAR.registerComponentType(
            "pouch_contents",
            builder -> builder
                    .persistent(PouchContents.CODEC)
                    .networkSynchronized(PouchContents.STREAM_CODEC)
    );

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }
}