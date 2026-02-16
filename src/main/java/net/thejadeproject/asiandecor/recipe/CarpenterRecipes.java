package net.thejadeproject.asiandecor.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class CarpenterRecipes implements Recipe<SingleRecipeInput> {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int ingredientCount;
    private final String group;

    public CarpenterRecipes(String group, Ingredient ingredient, ItemStack result, int ingredientCount) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.ingredientCount = ingredientCount;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.ingredient.test(input.item()) && input.item().getCount() >= this.ingredientCount;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
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
        return ModRecipes.CARPENTER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CARPENTER_TYPE.get();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public ItemStack getResult() {
        return result;
    }

    public String getGroup() {
        return group;
    }

    public static class Serializer implements RecipeSerializer<CarpenterRecipes> {
        public static final MapCodec<CarpenterRecipes> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(CarpenterRecipes::getGroup),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CarpenterRecipes::getIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(CarpenterRecipes::getResult),
                Codec.INT.optionalFieldOf("ingredient_count", 1).forGetter(CarpenterRecipes::getIngredientCount)
        ).apply(instance, CarpenterRecipes::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CarpenterRecipes> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<CarpenterRecipes> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CarpenterRecipes> streamCodec() {
            return STREAM_CODEC;
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, CarpenterRecipes recipe) {
            buffer.writeUtf(recipe.getGroup());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getIngredient());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResult());
            buffer.writeInt(recipe.getIngredientCount());
        }

        private static CarpenterRecipes fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int count = buffer.readInt();
            return new CarpenterRecipes(group, ingredient, result, count);
        }
    }
}