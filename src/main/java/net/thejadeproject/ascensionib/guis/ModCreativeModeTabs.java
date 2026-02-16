package net.thejadeproject.ascensionib.guis;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascensionib.AscensionIB;
import net.thejadeproject.ascensionib.blocks.ModBlocks;
import net.thejadeproject.ascensionib.items.ModItems;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AscensionIB.MOD_ID);

    public static final Supplier<CreativeModeTab> ASCENSION_ITEMS_TAB = CREATIVE_MODE_TAB.register("ascensionitb_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.JADE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AscensionIB.MOD_ID, "ascensionitb_blocks_tab"))
                    .title(Component.translatable("creativetab.ascension.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RAW_JADE);
                        output.accept(ModItems.JADE);
                        output.accept(ModItems.REGENERATION_PILL);
                    }).build());

    public static final Supplier<CreativeModeTab> ASCENSION_BLOCKS_TAB = CREATIVE_MODE_TAB.register("ascensionitb_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.JADE_ORE.get()))
                    .title(Component.translatable("creativetab.ascensionitb.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.JADE_ORE);
                        output.accept(ModBlocks.JADE_BLOCK);
                        output.accept(ModBlocks.PILL_CAULDRON_HUMAN_LOW);


                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
