package net.thejadeproject.asiandecor.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.asiandecor.items.buildersgadgets.TrowelItem;

import java.util.Optional;

public record TrowelDataComponent(
        int modeId,
        Optional<ItemStack> linkedPouch
) {
    public static final Codec<TrowelDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("mode_id").orElse(0).forGetter(TrowelDataComponent::modeId),
                    ItemStack.CODEC.optionalFieldOf("linked_pouch").forGetter(TrowelDataComponent::linkedPouch)
            ).apply(instance, TrowelDataComponent::new)
    );

    public static final TrowelDataComponent EMPTY = new TrowelDataComponent(0, Optional.empty());

    public TrowelItem.Mode getMode() {
        return TrowelItem.Mode.byId(modeId);
    }

    public boolean hasLinkedPouch() {
        return linkedPouch.isPresent() && !linkedPouch.get().isEmpty();
    }

    public ItemStack getLinkedPouch() {
        return linkedPouch.orElse(ItemStack.EMPTY);
    }

    public TrowelDataComponent withMode(TrowelItem.Mode mode) {
        return new TrowelDataComponent(mode.getId(), linkedPouch);
    }

    public TrowelDataComponent withLinkedPouch(ItemStack pouch) {
        return new TrowelDataComponent(modeId, Optional.of(pouch.copy()));
    }

    public TrowelDataComponent clearLinkedPouch() {
        return new TrowelDataComponent(modeId, Optional.empty());
    }
}