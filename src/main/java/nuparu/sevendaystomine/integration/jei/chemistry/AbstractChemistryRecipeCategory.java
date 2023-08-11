package nuparu.sevendaystomine.integration.jei.chemistry;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.item.crafting.chemistry.IChemistryRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class AbstractChemistryRecipeCategory<T extends IChemistryRecipe> implements IRecipeCategory<T> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/container/chemistry_station.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawableAnimated flame;

    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public AbstractChemistryRecipeCategory(IGuiHelper helper){
        background = helper.createDrawable(TEXTURE, 42, 4, 130, 79);
        icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.CHEMISTRY_STATION.get()));
        title = Component.translatable("container.chemistry_station");
        flame = helper.drawableBuilder(TEXTURE, 176, 0, 14, 14)
                .buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(TEXTURE, 176, 14, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    protected IDrawableAnimated getArrow(T recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = 600;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    protected void drawExperience(T recipe, GuiGraphics guiGraphics, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            guiGraphics.drawString(fontRenderer, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawCookTime(T recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                int index = i*2+j;
                builder.addSlot(RecipeIngredientRole.INPUT, 41 + j * 18 - 5, 12 + i * 18 - 5).addItemStacks(index < ingredients.size() ? Arrays.stream(ingredients.get(index).getItems()).collect(Collectors.toList()) : new ArrayList<>());
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 38).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        flame.draw(guiGraphics, 46, 42);
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(guiGraphics, 77, 38);
        drawExperience(recipe, guiGraphics, 0);
        drawCookTime(recipe, guiGraphics, 65);
    }
}
