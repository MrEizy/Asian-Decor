package net.thejadeproject.asiandecor.recipe;

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
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;

public class ColorMixerRecipe implements Recipe<SingleRecipeInput> {
    private final String group;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int processingTime;
    private final DyedBrickData componentData;

    public ColorMixerRecipe(String group, NonNullList<Ingredient> ingredients, ItemStack result,
                            int processingTime, DyedBrickData componentData) {
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
        this.processingTime = processingTime;
        this.componentData = componentData;
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

        if (ingredients.size() > 1) {
            if (primaryDye == null) return false;
            if (!matchesDyeIngredient(ingredients.get(1), primaryDye)) {
                return false;
            }
        }

        if (ingredients.size() > 2) {
            if (secondaryDye == null) return false;
            if (!matchesDyeIngredient(ingredients.get(2), secondaryDye)) {
                return false;
            }
        }

        return true;
    }

    private boolean matchesDyeIngredient(Ingredient ingredient, DyeColor dyeColor) {
        ItemStack dyeStack = new ItemStack(getDyeItem(dyeColor));
        return ingredient.test(dyeStack);
    }

    private static net.minecraft.world.item.Item getDyeItem(DyeColor color) {
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

    public int getBaseCount() {
        return 8;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public ItemStack assembleWithDyes(DyeColor primaryDye, DyeColor secondaryDye) {
        ItemStack result = this.result.copy();

        DyeColor brickColor;
        DyeColor mortarColor;

        if (componentData != null) {
            brickColor = componentData.brickColor();
            mortarColor = componentData.mortarColor();
        } else {
            brickColor = primaryDye != null ? primaryDye : DyeColor.WHITE;
            mortarColor = secondaryDye != null ? secondaryDye : brickColor;
        }

        DyedBrickData data = new DyedBrickData(brickColor, mortarColor);
        result.set(ModDataComponents.BRICK_DATA.get(), data);

        return result;
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
    public DyedBrickData getComponentData() { return componentData; }

    public static class Serializer implements RecipeSerializer<ColorMixerRecipe> {
        public static final MapCodec<ColorMixerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ColorMixerRecipe::getGroup),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(r -> java.util.List.copyOf(r.ingredients)),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                Codec.INT.optionalFieldOf("processing_time", 100).forGetter(ColorMixerRecipe::getProcessingTime),
                DyedBrickData.CODEC.optionalFieldOf("component", (DyedBrickData)null).forGetter(r -> r.componentData)
        ).apply(instance, (group, ingredients, result, processingTime, componentData) ->
                new ColorMixerRecipe(group, NonNullList.copyOf(ingredients), result, processingTime, componentData)));

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
            buffer.writeBoolean(recipe.componentData != null);
            if (recipe.componentData != null) {
                DyedBrickData.STREAM_CODEC.encode(buffer, recipe.componentData);
            }
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
            DyedBrickData componentData = null;
            if (buffer.readBoolean()) {
                componentData = DyedBrickData.STREAM_CODEC.decode(buffer);
            }
            return new ColorMixerRecipe(group, ingredients, result, processingTime, componentData);
        }
    }
}