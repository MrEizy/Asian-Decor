package net.thejadeproject.asiandecor.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.asiandecor.AsianDecor;

public class ModNetwork {
    public static void register(final IEventBus eventBus) {
        eventBus.addListener(ModNetwork::registerPackets);
    }

    private static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AsianDecor.MOD_ID)
                .versioned("1.0")
                .optional();

        //Server
        registrar.playToServer(
                PouchScrollPacket.TYPE,
                PouchScrollPacket.STREAM_CODEC,
                PouchScrollPacket::handle
        );
        registrar.playToServer(
                BlueprintRotatePacket.TYPE,
                BlueprintRotatePacket.STREAM_CODEC,
                BlueprintRotatePacket::handle
        );



        //Client
        registrar.playToClient(
                BlueprintSetPreviewPacket.TYPE,
                BlueprintSetPreviewPacket.STREAM_CODEC,
                BlueprintSetPreviewPacket::handle
        );

        registrar.playToClient(
                BlueprintClearPreviewPacket.TYPE,
                BlueprintClearPreviewPacket.STREAM_CODEC,
                BlueprintClearPreviewPacket::handle
        );

    }
}