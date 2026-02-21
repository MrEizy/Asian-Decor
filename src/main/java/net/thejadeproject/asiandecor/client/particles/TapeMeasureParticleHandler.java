// Updated TapeMeasureParticleHandler.java
package net.thejadeproject.asiandecor.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.asiandecor.items.buildersgadgets.TapeMeasureItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(value = Dist.CLIENT)
public class TapeMeasureParticleHandler {

    private static final Map<UUID, Object> activeParticles = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide()) return;

        ItemStack mainHand = event.getEntity().getMainHandItem();
        ItemStack offHand = event.getEntity().getOffhandItem();
        ItemStack tapeMeasure = mainHand.getItem() instanceof TapeMeasureItem ? mainHand :
                (offHand.getItem() instanceof TapeMeasureItem ? offHand : null);

        UUID playerId = event.getEntity().getUUID();
        Minecraft mc = Minecraft.getInstance();

        if (tapeMeasure != null) {
            TapeMeasureItem item = (TapeMeasureItem) tapeMeasure.getItem();
            Vec3 center = item.getCenter(tapeMeasure);

            if (center != null && item.isFinalized(tapeMeasure)) {
                if (mc.level != null) {
                    mc.level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                            center.x, center.y, center.z, 0, 0.05, 0);
                    mc.level.addParticle(ParticleTypes.END_ROD,
                            center.x, center.y + 0.5, center.z, 0, 0.02, 0);
                }
            } else {
                activeParticles.remove(playerId);
            }
        } else {
            activeParticles.remove(playerId);
        }
    }
}