package net.zic.builders_zenith;

import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.entity.ModBlockEntities;
import net.zic.builders_zenith.component.ModDataComponents;
import net.zic.builders_zenith.guis.ModCreativeModeTabs;
import net.zic.builders_zenith.items.ModItems;
import net.zic.builders_zenith.network.ModNetwork;
import net.zic.builders_zenith.recipe.ModRecipes;
import net.zic.builders_zenith.screen.ModMenuTypes;
import net.zic.builders_zenith.screen.custom.ColorMixerScreen;
import net.zic.builders_zenith.screen.custom.CarpenterScreen;
import net.zic.builders_zenith.screen.custom.PouchScreen;
import net.zic.builders_zenith.screen.custom.ShapeMakerScreen;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BuildersZenith.MOD_ID)
public class BuildersZenith {
    public static final String MOD_ID = "builders_zenith";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public BuildersZenith(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModNetwork.register(modEventBus);
        ModDataComponents.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);



        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {






        }
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.CARPENTER.get(), CarpenterScreen::new);
            event.register(ModMenuTypes.POUCH_MENU.get(), PouchScreen::new);
            event.register(ModMenuTypes.SHAPE_MAKER.get(), ShapeMakerScreen::new);
            event.register(ModMenuTypes.COLOR_MIXER.get(), ColorMixerScreen::new);
        }


        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
            // Register color handlers for all dyed brick FULL BLOCKS
            ModBlocks.DYED_BRICKS.forEach((type, blockDeferred) -> {
                event.register((state, level, pos, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get());
            });

            // Register color handlers for all dyed brick SLABS
            ModBlocks.DYED_BRICK_SLABS.forEach((type, blockDeferred) -> {
                event.register((state, level, pos, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get());
            });

            // Register color handlers for all dyed brick VERTICAL SLABS
            ModBlocks.DYED_BRICK_VERTICAL_SLABS.forEach((type, blockDeferred) -> {
                event.register((state, level, pos, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get());
            });

            // Register color handlers for all dyed brick STAIRS
            ModBlocks.DYED_BRICK_STAIRS.forEach((type, blockDeferred) -> {
                event.register((state, level, pos, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get());
            });

            // Register color handlers for all dyed brick WALLS
            ModBlocks.DYED_BRICK_WALLS.forEach((type, blockDeferred) -> {
                event.register((state, level, pos, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get());
            });
        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            // Full blocks
            ModBlocks.DYED_BRICKS.forEach((type, blockDeferred) -> {
                event.register((stack, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get().asItem());
            });

            // Slabs
            ModBlocks.DYED_BRICK_SLABS.forEach((type, blockDeferred) -> {
                event.register((stack, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get().asItem());
            });

            // Vertical Slabs
            ModBlocks.DYED_BRICK_VERTICAL_SLABS.forEach((type, blockDeferred) -> {
                event.register((stack, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get().asItem());
            });

            // Stairs
            ModBlocks.DYED_BRICK_STAIRS.forEach((type, blockDeferred) -> {
                event.register((stack, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get().asItem());
            });

            // Walls
            ModBlocks.DYED_BRICK_WALLS.forEach((type, blockDeferred) -> {
                event.register((stack, tintIndex) -> {
                    DyeColor color = switch (tintIndex) {
                        case 0 -> type.getBrickColor();
                        case 1 -> type.getMortarColor();
                        default -> DyeColor.WHITE;
                    };
                    return color.getTextureDiffuseColor();
                }, blockDeferred.get().asItem());
            });
        }


    }
}
