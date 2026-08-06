package net.zic.builders_zenith.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.zic.builders_zenith.blocks.ModBlocks;

import java.util.List;

public class CarpenterRecipes implements Recipe<RecipeInput> {
    private final String group;
    private final Ingredient ingredient;
    private final ItemStackTemplate result;
    private final int ingredientCount;

    // === 4-arg constructor — used by CODEC and STREAM_CODEC ===
    // result is an ItemStackTemplate rather than an ItemStack: constructing a real ItemStack
    // requires the item's data components to be "bound" (Holder.Reference#components()), which
    // is not guaranteed during recipe datagen. ItemStackTemplate carries the same item/count/
    // component-patch information without that requirement, and is turned into a real ItemStack
    // via #create(HolderLookup.Provider) once one is actually needed (see assemble()).
    public CarpenterRecipes(String group, Ingredient ingredient, ItemStackTemplate result, int ingredientCount) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.ingredientCount = ingredientCount;
    }

    // === 5-arg datagen helper — delegates to the 4-arg one ===
    public CarpenterRecipes(String group, Ingredient ingredient, ItemLike resultItem, int resultCount, int ingredientCount) {
        this(group, ingredient, new ItemStackTemplate(resultItem.asItem(), resultCount), ingredientCount);
    }

    // === MapCodec: 4 fields → 4-arg constructor ===
    public static final MapCodec<CarpenterRecipes> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(CarpenterRecipes::group),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CarpenterRecipes::getIngredient),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CarpenterRecipes::getResult),
            Codec.INT.fieldOf("ingredient_count").orElse(1).forGetter(CarpenterRecipes::getIngredientCount)
    ).apply(inst, CarpenterRecipes::new));

    // === StreamCodec: 4 fields → 4-arg constructor ===
    public static final StreamCodec<RegistryFriendlyByteBuf, CarpenterRecipes> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CarpenterRecipes::group,
            Ingredient.CONTENTS_STREAM_CODEC, CarpenterRecipes::getIngredient,
            ItemStackTemplate.STREAM_CODEC, CarpenterRecipes::getResult,
            ByteBufCodecs.VAR_INT, CarpenterRecipes::getIngredientCount,
            CarpenterRecipes::new
    );

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput) {
        return null;
    }


    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return this.group;
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return ModRecipes.CARPENTER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return ModRecipes.CARPENTER_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public List<RecipeDisplay> display() {
        // width=1, height=1: this isn't a real shaped crafting recipe, ShapedCraftingRecipeDisplay
        // is just being reused to show a single ingredient -> single result in the recipe book/JEI.
        return List.of(new ShapedCraftingRecipeDisplay(
                1, 1,
                List.of(this.ingredient.display()),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(ModBlocks.CARPENTER.get().asItem())
        ));
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    public ItemStackTemplate getResult() {
        return this.result;
    }
}