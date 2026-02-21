package net.thejadeproject.asiandecor.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record PouchContents(List<SlotEntry> entries, int selectedSlot) {
    public static final int MAX_SLOTS = 27;

    public record SlotEntry(int slot, ItemStack stack) {
        public static final Codec<SlotEntry> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("slot").forGetter(SlotEntry::slot),
                        ItemStack.CODEC.fieldOf("stack").forGetter(SlotEntry::stack)
                ).apply(instance, SlotEntry::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, SlotEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                SlotEntry::slot,
                ItemStack.STREAM_CODEC,
                SlotEntry::stack,
                SlotEntry::new
        );
    }

    public static final Codec<PouchContents> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    SlotEntry.CODEC.listOf().fieldOf("entries").forGetter(PouchContents::entries),
                    Codec.INT.fieldOf("selected_slot").forGetter(PouchContents::selectedSlot)
            ).apply(instance, PouchContents::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PouchContents> STREAM_CODEC = StreamCodec.composite(
            SlotEntry.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            PouchContents::entries,
            ByteBufCodecs.INT,
            PouchContents::selectedSlot,
            PouchContents::new
    );

    public static final PouchContents EMPTY = new PouchContents(new ArrayList<>(), 0);

    public PouchContents {
        List<SlotEntry> filtered = new ArrayList<>();
        for (SlotEntry entry : entries) {
            if (entry.slot >= 0 && entry.slot < MAX_SLOTS && !entry.stack.isEmpty()) {
                filtered.add(entry);
            }
        }
        entries = List.copyOf(filtered);
        selectedSlot = Math.clamp(selectedSlot, 0, MAX_SLOTS - 1);
    }

    public List<ItemStack> toItemList() {
        List<ItemStack> list = new ArrayList<>(MAX_SLOTS);
        for (int i = 0; i < MAX_SLOTS; i++) {
            list.add(ItemStack.EMPTY);
        }
        for (SlotEntry entry : entries) {
            if (entry.slot >= 0 && entry.slot < MAX_SLOTS) {
                list.set(entry.slot, entry.stack.copy());
            }
        }
        return list;
    }

    public static PouchContents fromItemList(List<ItemStack> items, int selectedSlot) {
        List<SlotEntry> entries = new ArrayList<>();
        for (int i = 0; i < Math.min(items.size(), MAX_SLOTS); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                entries.add(new SlotEntry(i, stack.copy()));
            }
        }
        return new PouchContents(entries, selectedSlot);
    }

    public ItemStack getSelectedStack() {
        for (SlotEntry entry : entries) {
            if (entry.slot == selectedSlot) {
                return entry.stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int getNonEmptySlotCount() {
        return entries.size();
    }

    public PouchContents withSelectedSlot(int slot) {
        return new PouchContents(entries, slot);
    }

    public PouchContents withSlot(int slot, ItemStack stack) {
        List<SlotEntry> newEntries = new ArrayList<>();

        for (SlotEntry entry : entries) {
            if (entry.slot != slot) {
                newEntries.add(entry);
            }
        }

        if (!stack.isEmpty()) {
            newEntries.add(new SlotEntry(slot, stack.copy()));
        }

        return new PouchContents(newEntries, selectedSlot);
    }

    public PouchContents cycleSelection(int direction) {
        if (entries.size() <= 1) return this;

        List<Integer> occupiedSlots = entries.stream()
                .map(SlotEntry::slot)
                .sorted()
                .toList();

        if (occupiedSlots.isEmpty()) return this;

        int currentIndex = -1;
        for (int i = 0; i < occupiedSlots.size(); i++) {
            if (occupiedSlots.get(i) == selectedSlot) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) {
            return new PouchContents(entries, occupiedSlots.get(0));
        }

        int newIndex = (currentIndex + direction + occupiedSlots.size()) % occupiedSlots.size();
        return new PouchContents(entries, occupiedSlots.get(newIndex));
    }


}