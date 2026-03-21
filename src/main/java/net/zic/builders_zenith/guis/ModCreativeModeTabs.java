package net.zic.builders_zenith.guis;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.items.ModItems;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BuildersZenith.MOD_ID);

    public static final Supplier<CreativeModeTab> ASIAN_DECOR_ITEMS = CREATIVE_MODE_TAB.register("builders_zenith_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WHITE_BLOCK_POUCH.get()))
                    .title(Component.translatable("creativetab.builders_zenith.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.WHITE_BLOCK_POUCH);
                        output.accept(ModItems.BLUEPRINT);
                        output.accept(ModItems.TAPE_MEASURE);
                        output.accept(ModItems.TROWEL);
                        output.accept(ModItems.HANDHELD_FILLER);
                    }).build());

    public static final Supplier<CreativeModeTab> ASIAN_DECOR_WOOD = CREATIVE_MODE_TAB.register("builders_zenith_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CARPENTER.get()))
                    .title(Component.translatable("creativetab.builders_zenith.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.CARPENTER);
                        output.accept(ModBlocks.COLOR_MIXER);
                        output.accept(ModBlocks.SHAPE_MAKER);
                        //output.accept(ModBlocks.WINGED_TABLE);




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