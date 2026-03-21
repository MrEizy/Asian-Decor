package net.zic.builders_zenith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = BuildersZenith.MOD_ID)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<List<? extends String>> HANDHELD_FILLER_CHARGE_ITEMS = BUILDER
            .push("HandheldFiller")
            .comment("Items that can charge the Handheld Filler",
                    "Format: [\"modid:item_id,charge_value\", \"modid:item_id,charge_value\"]",
                    "Example: [\"minecraft:gunpowder,10\", \"minecraft:blaze_powder,50\"]")
            .defineList("chargeItems",
                    List.of("minecraft:gunpowder,10", "minecraft:blaze_powder,50"),
                    Config::validateChargeItemEntry);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static Map<Item, Integer> handheldFillerChargeItems = new HashMap<>();

    private static boolean validateChargeItemEntry(final Object obj)
    {
        if (!(obj instanceof String str)) return false;
        String[] parts = str.split(",");
        if (parts.length != 2) return false;
        try {
            ResourceLocation.parse(parts[0]);
            Integer.parseInt(parts[1].trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        // Parse handheld filler charge items
        handheldFillerChargeItems = new HashMap<>();
        for (String entry : HANDHELD_FILLER_CHARGE_ITEMS.get()) {
            String[] parts = entry.split(",");
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(parts[0]));
            int value = Integer.parseInt(parts[1].trim());
            handheldFillerChargeItems.put(item, value);
        }
    }
}