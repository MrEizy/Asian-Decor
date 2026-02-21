package net.thejadeproject.asiandecor.guis;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.items.ModItems;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AsianDecor.MOD_ID);

    public static final Supplier<CreativeModeTab> ASIAN_DECOR_ITEMS = CREATIVE_MODE_TAB.register("asiandecor_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WHITE_BLOCK_POUCH.get()))
                    .title(Component.translatable("creativetab.asiandecor.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.WHITE_BLOCK_POUCH);
                        output.accept(ModItems.BLUEPRINT);
                        output.accept(ModItems.TAPE_MEASURE);
                        output.accept(ModItems.TROWEL);
                    }).build());

    public static final Supplier<CreativeModeTab> ASIAN_DECOR_WOOD = CREATIVE_MODE_TAB.register("asiandecor_wood_decor",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CARPENTER.get()))
                    .title(Component.translatable("creativetab.asiandecor.wood"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.CARPENTER);
                        output.accept(ModBlocks.WINGED_TABLE);
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
