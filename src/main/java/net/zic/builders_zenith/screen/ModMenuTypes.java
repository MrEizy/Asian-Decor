package net.zic.builders_zenith.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.entity.ColorMixerBlockEntity;
import net.zic.builders_zenith.screen.custom.ColorMixerMenu;
import net.zic.builders_zenith.screen.custom.CarpenterMenu;
import net.zic.builders_zenith.screen.custom.PouchMenu;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, BuildersZenith.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CarpenterMenu>> CARPENTER =
            MENUS.register("carpenter", () -> IMenuTypeExtension.create((windowId, inv, data) -> {
                if (data != null) {
                    net.minecraft.core.BlockPos pos = data.readBlockPos();
                    return new CarpenterMenu(windowId, inv,
                            net.minecraft.world.inventory.ContainerLevelAccess.create(inv.player.level(), pos));
                }
                return new CarpenterMenu(windowId, inv);
            }));

    public static final Supplier<MenuType<ColorMixerMenu>> COLOR_MIXER =
            MENUS.register("color_mixer", () -> IMenuTypeExtension.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity be = inv.player.level().getBlockEntity(pos);
                if (be instanceof ColorMixerBlockEntity mixer) {
                    return new ColorMixerMenu(windowId, inv, mixer, mixer.getDataAccess());
                }
                return null; // Return null if BE not found - menu won't open
            }));


    public static final Supplier<MenuType<PouchMenu>> POUCH_MENU = MENUS.register("pouch_menu",
            () -> IMenuTypeExtension.create(PouchMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}