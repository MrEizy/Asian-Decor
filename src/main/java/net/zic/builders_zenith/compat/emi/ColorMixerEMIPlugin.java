// ColorMixerEMIPlugin.java
package net.zic.builders_zenith.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.zic.builders_zenith.BuildersZenith;
import net.zic.builders_zenith.blocks.ModBlocks;
import net.zic.builders_zenith.blocks.custom.DyedBrickType;
import net.zic.builders_zenith.recipe.ColorMixerRecipe;
import net.zic.builders_zenith.recipe.ModRecipes;

import java.util.Arrays;
import java.util.List;

import static net.zic.builders_zenith.recipe.ColorMixerRecipe.getDyeItem;

@EmiEntrypoint
public class ColorMixerEMIPlugin implements EmiPlugin {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            BuildersZenith.MOD_ID, "textures/gui/jemri/color_mixing.png");

    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(BuildersZenith.MOD_ID, "color_mixer"),
            EmiStack.of(ModBlocks.COLOR_MIXER.get())
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.COLOR_MIXER.get()));

        RecipeManager manager = registry.getRecipeManager();
        List<RecipeHolder<ColorMixerRecipe>> recipes = manager.getAllRecipesFor(
                ModRecipes.COLOR_MIXER_TYPE.get()
        );

        for (RecipeHolder<ColorMixerRecipe> holder : recipes) {
            registry.addRecipe(new ColorMixerEmiRecipe(holder));
        }
    }

    public static class ColorMixerEmiRecipe implements EmiRecipe {
        private final ColorMixerRecipe recipe;
        private final ResourceLocation id;

        public ColorMixerEmiRecipe(RecipeHolder<ColorMixerRecipe> holder) {
            this.recipe = holder.value();
            this.id = holder.id();
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return CATEGORY;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(
                    EmiIngredient.of(recipe.getIngredients().get(0)),
                    EmiIngredient.of(recipe.getIngredients().get(1)),
                    EmiIngredient.of(recipe.getIngredients().get(2))
            );
        }

        @Override
        public List<EmiStack> getOutputs() {
            String group = recipe.getGroup();
            boolean isVerticalSlab = group.contains("vertical_slab");
            boolean isSlab = !isVerticalSlab && group.contains("slab");
            boolean isStairs = group.contains("stair");
            boolean isWall = group.contains("wall");

            int outputCount = isStairs ? 4 : (isWall ? 6 : 8);

            java.util.function.Function<DyedBrickType, EmiStack> mapper;

            if (isSlab) {
                mapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_SLABS.get(type).get()).setAmount(outputCount);
            } else if (isStairs) {
                mapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_STAIRS.get(type).get()).setAmount(outputCount);
            } else if (isWall) {
                mapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_WALLS.get(type).get()).setAmount(outputCount);
            } else if (isVerticalSlab) {
                mapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type).get()).setAmount(outputCount);
            } else {
                mapper = type -> EmiStack.of(ModBlocks.DYED_BRICKS.get(type).get()).setAmount(outputCount);
            }

            return Arrays.stream(DyedBrickType.values())
                    .map(mapper)
                    .toList();
        }

        @Override
        public int getDisplayWidth() {
            return 121;
        }

        @Override
        public int getDisplayHeight() {
            return 57;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            widgets.addTexture(new dev.emi.emi.api.render.EmiTexture(
                    BACKGROUND_TEXTURE, 0, 0, 121, 57), 0, 0);

            // Dyes with count of 2 each
            List<EmiStack> allDyes = Arrays.stream(DyeColor.values())
                    .map(color -> EmiStack.of(getDyeItem(color)).setAmount(2))
                    .toList();

            String group = recipe.getGroup();
            boolean isVerticalSlab = group.contains("vertical_slab");
            boolean isSlab = !isVerticalSlab && group.contains("slab");
            boolean isStairs = group.contains("stair");
            boolean isWall = group.contains("wall");

            java.util.function.Function<DyedBrickType, EmiStack> stackMapper;
            EmiStack vanillaInput;
            int baseCount = 8;
            int outputCount = isStairs ? 4 : (isWall ? 6 : 8);

            if (isSlab) {
                stackMapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_SLABS.get(type).get());
                vanillaInput = EmiStack.of(Items.BRICK_SLAB).setAmount(baseCount);
            } else if (isStairs) {
                stackMapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_STAIRS.get(type).get());
                vanillaInput = EmiStack.of(Items.BRICK_STAIRS).setAmount(baseCount);
            } else if (isWall) {
                stackMapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_WALLS.get(type).get());
                vanillaInput = EmiStack.of(Items.BRICK_WALL).setAmount(baseCount);
            } else if (isVerticalSlab) {
                stackMapper = type -> EmiStack.of(ModBlocks.DYED_BRICK_VERTICAL_SLABS.get(type).get());
                vanillaInput = EmiStack.of(ModBlocks.BRICK_VERTICAL_SLAB.get()).setAmount(baseCount);
            } else {
                stackMapper = type -> EmiStack.of(ModBlocks.DYED_BRICKS.get(type).get());
                vanillaInput = EmiStack.of(Items.BRICKS).setAmount(baseCount);
            }

            if (recipe.isVanillaRecipe()) {
                widgets.addSlot(vanillaInput, 8, 13);
                widgets.addText(Component.literal(
                        isSlab ? "Vanilla Slabs + Dyes" :
                                isStairs ? "Vanilla Stairs + Dyes" :
                                        isWall ? "Vanilla Walls + Dyes" :
                                                isVerticalSlab ? "Vanilla Vertical Slabs + Dyes" :
                                                        "Vanilla Bricks + Dyes"
                ), 10, 5, 0xAAAAAA, false);
            } else {
                List<EmiStack> allDyed = Arrays.stream(DyedBrickType.values())
                        .map(type -> stackMapper.apply(type).setAmount(baseCount))
                        .toList();
                widgets.addSlot(EmiIngredient.of(allDyed), 8, 13);
                widgets.addText(Component.literal(
                        isSlab ? "Dyed Slabs + Dyes" :
                                isStairs ? "Dyed Stairs + Dyes" :
                                        isWall ? "Dyed Walls + Dyes" :
                                                isVerticalSlab ? "Dyed Vertical Slabs + Dyes" :
                                                        "Any Dyed Brick + Dyes"
                ), 10, 5, 0xAAAAAA, false);
            }

            widgets.addSlot(EmiIngredient.of(allDyes), 28, 13);
            widgets.addSlot(EmiIngredient.of(allDyes), 18, 32);

            List<EmiStack> allOutputs = Arrays.stream(DyedBrickType.values())
                    .map(type -> stackMapper.apply(type).setAmount(outputCount))
                    .toList();
            widgets.addSlot(EmiIngredient.of(allOutputs), 103, 20).recipeContext(this);

            int processingTime = recipe.getProcessingTime();
            if (processingTime > 0) {
                String timeText = (processingTime / 20) + "s";
                int textWidth = Minecraft.getInstance().font.width(timeText);
                int xPos = (getDisplayWidth() - textWidth) / 2;
                widgets.addText(Component.literal(timeText), xPos, 47, 0xFFFFFF, false);
            }
        }
    }
}