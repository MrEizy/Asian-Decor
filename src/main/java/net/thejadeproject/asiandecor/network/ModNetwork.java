package net.thejadeproject.asiandecor.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.entity.ShapeMakerBlockEntity;

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
        registrar.playToServer(
                PouchRadialSelectPacket.TYPE,
                PouchRadialSelectPacket.STREAM_CODEC,
                PouchRadialSelectPacket::handle
        );

        registrar.playToServer(
                TrowelTogglePacket.TYPE,
                TrowelTogglePacket.STREAM_CODEC,
                TrowelTogglePacket::handle
        );

        // Shape Maker packets
        registrar.playToServer(
                ShapeMakerUpdatePacket.TYPE,
                ShapeMakerUpdatePacket.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var level = context.player().level();
                        var be = level.getBlockEntity(payload.pos());
                        if (be instanceof ShapeMakerBlockEntity shapeMaker) {
                            try {
                                shapeMaker.setSelectedShape(ShapeMakerBlockEntity.ShapeType.values()[payload.shapeOrdinal()]);
                            } catch (ArrayIndexOutOfBoundsException e) {}
                            shapeMaker.setXOffset(payload.xOffset());
                            shapeMaker.setYOffset(payload.yOffset());
                            shapeMaker.setZOffset(payload.zOffset());
                            shapeMaker.setRadius(payload.radius());
                            shapeMaker.setThickness(payload.thickness());
                            shapeMaker.setPreviewEnabled(payload.previewEnabled());
                            try {
                                shapeMaker.setRedstoneMode(ShapeMakerBlockEntity.RedstoneMode.values()[payload.redstoneModeOrdinal()]);
                            } catch (ArrayIndexOutOfBoundsException e) {}
                        }
                    });
                }
        );

        registrar.playToServer(
                ShapeMakerTogglePreviewPacket.TYPE,
                ShapeMakerTogglePreviewPacket.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var level = context.player().level();
                        var be = level.getBlockEntity(payload.pos());
                        if (be instanceof ShapeMakerBlockEntity shapeMaker) {
                            shapeMaker.setPreviewEnabled(payload.enabled());
                        }
                    });
                }
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