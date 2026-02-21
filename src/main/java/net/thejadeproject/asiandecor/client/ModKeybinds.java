package net.thejadeproject.asiandecor.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT)
public class ModKeybinds {

    public static final KeyMapping MODE_TOGGLE = new KeyMapping(
            "key.asiandecor.mode_toggle",
            GLFW.GLFW_KEY_V,
            "key.categories.asiandecor"
    );


    public static final KeyMapping RADIAL_MENU_KEY = new KeyMapping(
            "key.asiandecor.radial_menu",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_G,
            "key.categories.asiandecor"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(MODE_TOGGLE);
        event.register(RADIAL_MENU_KEY);
    }
}