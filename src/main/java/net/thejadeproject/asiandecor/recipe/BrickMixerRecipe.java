package net.thejadeproject.asiandecor.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.thejadeproject.asiandecor.component.DyedBrickData;
import net.thejadeproject.asiandecor.component.ModDataComponents;
import net.thejadeproject.asiandecor.items.ModItems;

public class BrickMixerRecipe implements Recipe<SingleRecipeInput> {
    private final String group;
    private final Ingredient baseIngredient;
    private final ItemStack result;
    private final int baseCount;
    private final DyeColor defaultBrickColor;
    private final DyeColor defaultMortarColor;

    public BrickMixerRecipe(String group, Ingredient baseIngredient, ItemStack result,
                            int baseCount, DyeColor defaultBrickColor, DyeColor defaultMortarColor) {
        this.group = group;
        this.baseIngredient = baseIngredient;
        this.result = result;
        this.baseCount = baseCount;
        this.defaultBrickColor = defaultBrickColor;
        this.defaultMortarColor = defaultMortarColor;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        // Use the ingredient to test - this respects the item registration properly
        boolean matchesIngredient = this.baseIngredient.test(input.item());
        boolean enoughCount = input.item().getCount() >= this.baseCount;

        return matchesIngredient && enoughCount;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    // Overloaded assemble that accepts dye colors
    public ItemStack assembleWithColors(SingleRecipeInput input, HolderLookup.Provider registries,
                                        DyeColor brickColor, DyeColor mortarColor) {
        ItemStack result = this.result.copy();

        if (result.has(ModDataComponents.BRICK_DATA.get())) {
            DyedBrickData data = new DyedBrickData(
                    brickColor != null ? brickColor : defaultBrickColor,
                    mortarColor != null ? mortarColor : defaultMortarColor
            );
            result.set(ModDataComponents.BRICK_DATA.get(), data);
        }

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
        return ModRecipes.BRICK_MIXER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BRICK_MIXER_TYPE.get();
    }

    public String getGroup() {
        return group;
    }

    public Ingredient getBaseIngredient() {
        return baseIngredient;
    }

    public int getBaseCount() {
        return baseCount;
    }

    public DyeColor getDefaultBrickColor() {
        return defaultBrickColor;
    }

    public DyeColor getDefaultMortarColor() {
        return defaultMortarColor;
    }

    public static class Serializer implements RecipeSerializer<BrickMixerRecipe> {
        public static final MapCodec<BrickMixerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(BrickMixerRecipe::getGroup),
                Ingredient.CODEC_NONEMPTY.fieldOf("base_ingredient").forGetter(BrickMixerRecipe::getBaseIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                Codec.INT.optionalFieldOf("base_count", 8).forGetter(BrickMixerRecipe::getBaseCount),
                DyeColor.CODEC.optionalFieldOf("default_brick_color", DyeColor.WHITE).forGetter(BrickMixerRecipe::getDefaultBrickColor),
                DyeColor.CODEC.optionalFieldOf("default_mortar_color", DyeColor.LIGHT_GRAY).forGetter(BrickMixerRecipe::getDefaultMortarColor)
        ).apply(instance, BrickMixerRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BrickMixerRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<BrickMixerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BrickMixerRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, BrickMixerRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getBaseIngredient());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.baseCount);
            buffer.writeEnum(recipe.defaultBrickColor);
            buffer.writeEnum(recipe.defaultMortarColor);
        }

        private static BrickMixerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int baseCount = buffer.readInt();
            DyeColor brickColor = buffer.readEnum(DyeColor.class);
            DyeColor mortarColor = buffer.readEnum(DyeColor.class);
            return new BrickMixerRecipe(group, ingredient, result, baseCount, brickColor, mortarColor);
        }
    }
}