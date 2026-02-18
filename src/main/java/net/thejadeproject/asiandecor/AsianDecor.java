package net.thejadeproject.asiandecor;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.entity.ModBlockEntities;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.guis.ModCreativeModeTabs;
import net.thejadeproject.asiandecor.items.ModItems;
import net.thejadeproject.asiandecor.network.ModNetwork;
import net.thejadeproject.asiandecor.recipe.ModRecipes;
import net.thejadeproject.asiandecor.screen.ModMenuTypes;
import net.thejadeproject.asiandecor.screen.custom.CarpenterScreen;
import net.thejadeproject.asiandecor.screen.custom.PouchScreen;
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
@Mod(AsianDecor.MOD_ID)
public class AsianDecor {
    public static final String MOD_ID = "asiandecor";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public AsianDecor(IEventBus modEventBus, ModContainer modContainer) {
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
        }
    }
}
