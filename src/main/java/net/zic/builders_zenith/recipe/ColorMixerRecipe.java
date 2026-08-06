package net.zic.builders_zenith.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;

public class ColorMixerRecipe implements Recipe<SingleRecipeInput> {
    private final String group;
    private final NonNullList<Ingredient> ingredients;
    // Stored as Item + count rather than ItemStack: an ItemStack cannot be
    // constructed until item components are bound (e.g. during datagen), but
    // the Item itself and a plain int are safe at any point in the lifecycle.
    private final Item resultItem;
    private final int resultCount;
    private final int processingTime;

    public ColorMixerRecipe(String group, NonNullList<Ingredient> ingredients, Item resultItem, int resultCount,
                            int processingTime) {
        this.group = group;
        this.ingredients = ingredients;
        this.resultItem = resultItem;
        this.resultCount = resultCount;
        this.processingTime = processingTime;
    }

    /**
     * Convenience overload for call sites that already have a fully-built
     * ItemStack (e.g. runtime code, or existing callers you don't want to
     * touch). Do NOT use this from datagen — building the ItemStack passed
     * in here is exactly what crashes before components are bound.
     */
    public ColorMixerRecipe(String group, NonNullList<Ingredient> ingredients, ItemStack result,
                            int processingTime) {
        this(group, ingredients, result.getItem(), result.getCount(), processingTime);
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return !ingredients.isEmpty() && ingredients.get(0).test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput singleRecipeInput) {
        return new ItemStack(resultItem, resultCount);
    }

    public boolean canCraft(ItemStack baseStack, DyeColor primaryDye, DyeColor secondaryDye) {
        if (ingredients.isEmpty() || !ingredients.get(0).test(baseStack)) {
            return false;
        }

        if (baseStack.getCount() < getBaseCount()) {
            return false;
        }

        if (ingredients.size() > 1) {
            if (primaryDye == null) return false;
            ItemStack primaryDyeStack = new ItemStack(getDyeItem(primaryDye));
            if (!ingredients.get(1).test(primaryDyeStack)) {
                return false;
            }
        }

        if (ingredients.size() > 2) {
            if (secondaryDye == null) return false;
            ItemStack secondaryDyeStack = new ItemStack(getDyeItem(secondaryDye));
            if (!ingredients.get(2).test(secondaryDyeStack)) {
                return false;
            }
        }

        return true;
    }

    public int getBaseCount() {
        return 8;
    }

    public ItemStack assembleWithDyes(DyeColor primaryDye, DyeColor secondaryDye) {
        if (primaryDye != null && secondaryDye != null) {
            DyedBrickType resultType = DyedBrickType.fromColors(primaryDye, secondaryDye);

            if (group.contains("slab")) {
                return new ItemStack(ModBlocks.DYED_BRICK_SLABS.get(resultType).get(), 8);
            } else if (group.contains("stairs")) {
                return new ItemStack(ModBlocks.DYED_BRICK_STAIRS.get(resultType).get(), 4);
            } else if (group.contains("wall")) {
                return new ItemStack(ModBlocks.DYED_BRICK_WALLS.get(resultType).get(), 6);
            } else if (group.contains("vertical_slab")) {
                return new ItemStack(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(resultType).get(), 8);
            } else {
                return new ItemStack(ModBlocks.DYED_BRICKS.get(resultType).get(), 8);
            }
        }
        return new ItemStack(resultItem, resultCount);
    }

    public boolean isVanillaRecipe() {
        return group != null && group.contains("vanilla");
    }

    public boolean isRecolorRecipe() {
        return group != null && group.contains("recolor");
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return ModRecipes.COLOR_MIXER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return ModRecipes.COLOR_MIXER_TYPE.get();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public String getGroup() { return group; }
    public int getProcessingTime() { return processingTime; }
    public Item getResultItem() { return resultItem; }
    public int getResultCount() { return resultCount; }

    public static net.minecraft.world.item.Item getDyeItem(DyeColor color) {
        return switch (color) {
            case WHITE -> Items.WHITE_DYE;
            case ORANGE -> Items.ORANGE_DYE;
            case MAGENTA -> Items.MAGENTA_DYE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
            case YELLOW -> Items.YELLOW_DYE;
            case LIME -> Items.LIME_DYE;
            case PINK -> Items.PINK_DYE;
            case GRAY -> Items.GRAY_DYE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
            case CYAN -> Items.CYAN_DYE;
            case PURPLE -> Items.PURPLE_DYE;
            case BLUE -> Items.BLUE_DYE;
            case BROWN -> Items.BROWN_DYE;
            case GREEN -> Items.GREEN_DYE;
            case RED -> Items.RED_DYE;
            case BLACK -> Items.BLACK_DYE;
        };
    }

    // === NEW: Static codecs, no inner Serializer class ===

    public static final MapCodec<ColorMixerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(ColorMixerRecipe::getGroup),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> java.util.List.copyOf(r.ingredients)),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("result").forGetter(ColorMixerRecipe::getResultItem),
            Codec.INT.optionalFieldOf("count", 1).forGetter(ColorMixerRecipe::getResultCount),
            Codec.INT.optionalFieldOf("processing_time", 100).forGetter(ColorMixerRecipe::getProcessingTime)
    ).apply(instance, (group, ingredients, resultItem, resultCount, processingTime) ->
            new ColorMixerRecipe(group, NonNullList.copyOf(ingredients), resultItem, resultCount, processingTime)));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColorMixerRecipe> STREAM_CODEC = StreamCodec.of(
            ColorMixerRecipe::toNetwork, ColorMixerRecipe::fromNetwork
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, Item> ITEM_STREAM_CODEC =
            ByteBufCodecs.registry(Registries.ITEM);

    private static void toNetwork(RegistryFriendlyByteBuf buffer, ColorMixerRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeVarInt(recipe.ingredients.size());
        for (Ingredient ing : recipe.ingredients) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ing);
        }
        ITEM_STREAM_CODEC.encode(buffer, recipe.resultItem);
        buffer.writeVarInt(recipe.resultCount);
        buffer.writeInt(recipe.processingTime);
    }

    private static ColorMixerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        int ingredientCount = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int i = 0; i < ingredientCount; i++) {
            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        }
        Item resultItem = ITEM_STREAM_CODEC.decode(buffer);
        int resultCount = buffer.readVarInt();
        int processingTime = buffer.readInt();
        return new ColorMixerRecipe(group, ingredients, resultItem, resultCount, processingTime);
    }
}