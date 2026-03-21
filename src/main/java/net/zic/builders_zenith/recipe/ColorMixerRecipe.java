package net.zic.builders_zenith.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;

public class ColorMixerRecipe implements Recipe<SingleRecipeInput> {
    private final String group;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int processingTime;

    public ColorMixerRecipe(String group, NonNullList<Ingredient> ingredients, ItemStack result,
                            int processingTime) {
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
        this.processingTime = processingTime;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return !ingredients.isEmpty() && ingredients.get(0).test(input.item());
    }

    public boolean canCraft(ItemStack baseStack, DyeColor primaryDye, DyeColor secondaryDye) {
        if (ingredients.isEmpty() || !ingredients.get(0).test(baseStack)) {
            return false;
        }

        if (baseStack.getCount() < getBaseCount()) {
            return false;
        }

        // Check primary dye slot accepts the dye (for tag-based ingredients)
        if (ingredients.size() > 1) {
            if (primaryDye == null) return false;
            ItemStack primaryDyeStack = new ItemStack(getDyeItem(primaryDye));
            if (!ingredients.get(1).test(primaryDyeStack)) {
                return false;
            }
        }

        // Check secondary dye slot accepts the dye
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

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    /**
     * Assemble result dynamically based on dye colors and recipe type
     */
    public ItemStack assembleWithDyes(DyeColor primaryDye, DyeColor secondaryDye) {
        if (primaryDye != null && secondaryDye != null) {
            DyedBrickType resultType = DyedBrickType.fromColors(primaryDye, secondaryDye);

            // Determine which block type to return based on recipe group
            if (group.contains("slab")) {
                return new ItemStack(ModBlocks.DYED_BRICK_SLABS.get(resultType).get(), 8);
            } else if (group.contains("stairs")) {
                return new ItemStack(ModBlocks.DYED_BRICK_STAIRS.get(resultType).get(), 4);
            } else if (group.contains("wall")) {
                return new ItemStack(ModBlocks.DYED_BRICK_WALLS.get(resultType).get(), 6);
            } else {
                // Default to full bricks
                return new ItemStack(ModBlocks.DYED_BRICKS.get(resultType).get(), 8);
            }
        }
        // Fallback to placeholder
        return this.result.copy();
    }

    /**
     * Check if this is the vanilla brick recipe
     */
    public boolean isVanillaRecipe() {
        return group != null && group.contains("vanilla");
    }

    /**
     * Check if this is the recolor recipe
     */
    public boolean isRecolorRecipe() {
        return group != null && group.contains("recolor");
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.COLOR_MIXER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.COLOR_MIXER_TYPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getGroup() { return group; }
    public int getProcessingTime() { return processingTime; }

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

    public static class Serializer implements RecipeSerializer<ColorMixerRecipe> {
        public static final MapCodec<ColorMixerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ColorMixerRecipe::getGroup),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(r -> java.util.List.copyOf(r.ingredients)),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                Codec.INT.optionalFieldOf("processing_time", 100).forGetter(ColorMixerRecipe::getProcessingTime)
        ).apply(instance, (group, ingredients, result, processingTime) ->
                new ColorMixerRecipe(group, NonNullList.copyOf(ingredients), result, processingTime)));

        public static final StreamCodec<RegistryFriendlyByteBuf, ColorMixerRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<ColorMixerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ColorMixerRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ColorMixerRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ing);
            }
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.processingTime);
        }

        private static ColorMixerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int i = 0; i < ingredientCount; i++) {
                ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            }
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int processingTime = buffer.readInt();
            return new ColorMixerRecipe(group, ingredients, result, processingTime);
        }
    }
}