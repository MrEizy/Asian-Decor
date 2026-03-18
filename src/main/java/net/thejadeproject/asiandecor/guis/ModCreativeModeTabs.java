package net.thejadeproject.asiandecor.guis;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.asiandecor.AsianDecor;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.blocks.custom.DyedBrickType;
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
                        output.accept(ModItems.HANDHELD_FILLER);
                    }).build());

    public static final Supplier<CreativeModeTab> ASIAN_DECOR_WOOD = CREATIVE_MODE_TAB.register("asiandecor_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CARPENTER.get()))
                    .title(Component.translatable("creativetab.asiandecor.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.CARPENTER);
                        output.accept(ModBlocks.SHAPE_MAKER);
                        output.accept(ModBlocks.WINGED_TABLE);



                        for (DyedBrickType type : DyedBrickType.values()) {
                            output.accept(ModBlocks.DYED_BRICKS.get(type));
                            output.accept(ModBlocks.DYED_BRICK_STAIRS.get(type));
                            output.accept(ModBlocks.DYED_BRICK_SLABS.get(type));
                            output.accept(ModBlocks.DYED_BRICK_WALLS.get(type));
                        }
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}