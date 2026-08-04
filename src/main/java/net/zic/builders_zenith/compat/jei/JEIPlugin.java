// JEIPlugin.java
package net.zic.builders_zenith.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.recipe.CarpenterRecipes;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;
import net.zic.builders_zenith.recipe.ModRecipes;
import net.zic.builders_zenith.screen.custom.CarpenterScreen;
import net.zic.builders_zenith.screen.custom.ColorMixerScreen;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    // Recipes synced from the server via OnDatapackSyncEvent/RecipesReceivedEvent below.
    // registerRecipes() runs at JEI startup, which can happen before Minecraft.getInstance().level
    // exists (and Level#getRecipeManager was removed anyway) - the synced RecipeMap sidesteps both.
    private static RecipeMap syncedRecipes = RecipeMap.EMPTY;

    public static final IRecipeType<RecipeHolder<CarpenterRecipes>> CARPENTER_RECIPE_TYPE =
            createRecipeHolderType("carpenter");

    public static final IRecipeType<RecipeHolder<ColorMixerRecipe>> COLOR_MIXER_RECIPE_TYPE =
            createRecipeHolderType("color_mixer");

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(BuildersZenith.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CarpenterRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ColorMixerRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(CARPENTER_RECIPE_TYPE, getRecipes(syncedRecipes, ModRecipes.CARPENTER_TYPE.get()));

        // Color Mixer - Only 2 recipes, but show all 256 combinations via JEI's recipe filling
        registration.addRecipes(COLOR_MIXER_RECIPE_TYPE, getRecipes(syncedRecipes, ModRecipes.COLOR_MIXER_TYPE.get()));
        // JEI will automatically expand tag ingredients to show all combinations!
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CARPENTER.get()),
                CARPENTER_RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.COLOR_MIXER.get()),
                COLOR_MIXER_RECIPE_TYPE
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                CarpenterScreen.class,
                79, 34, 24, 17,
                CARPENTER_RECIPE_TYPE
        );
        registration.addRecipeClickArea(
                ColorMixerScreen.class,
                101, 32, 24, 17,
                COLOR_MIXER_RECIPE_TYPE
        );
    }

    // From Occultism, under MIT License
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipes(RecipeMap recipeMap, RecipeType<T> type) {
        return (List) recipeMap.byType(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> IRecipeType<T> createRecipeHolderType(String path) {
        return (IRecipeType<T>) IRecipeType.create(Identifier.fromNamespaceAndPath(BuildersZenith.MOD_ID, path), RecipeHolder.class);
    }

    @EventBusSubscriber(modid = BuildersZenith.MOD_ID)
    public static class ServerRecipeSync {
        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
            event.sendRecipes(
                    ModRecipes.CARPENTER_TYPE.get(),
                    ModRecipes.COLOR_MIXER_TYPE.get()
            );
        }
    }

    @EventBusSubscriber(modid = BuildersZenith.MOD_ID, value = Dist.CLIENT)
    public static class ClientRecipeSync {
        @SubscribeEvent
        public static void onRecipeReceived(RecipesReceivedEvent event) {
            syncedRecipes = event.getRecipeMap();
        }
    }
}