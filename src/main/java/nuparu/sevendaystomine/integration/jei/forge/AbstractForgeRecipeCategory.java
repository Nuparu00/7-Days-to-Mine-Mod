package nuparu.sevendaystomine.integration.jei.forge;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.item.crafting.forge.ForgeRecipeMaterial;
import nuparu.sevendaystomine.world.item.crafting.forge.IForgeRecipe;

public abstract class AbstractForgeRecipeCategory<T extends IForgeRecipe> implements IRecipeCategory<T> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/container/forge.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawableAnimated flame;

    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public AbstractForgeRecipeCategory(IGuiHelper helper){
        background = helper.createDrawable(TEXTURE, 42, 4, 130, 79);
        icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.FORGE.get()));
        title = Component.translatable("container.forge");
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

    protected void drawExperience(T recipe, PoseStack matrixStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(matrixStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawCookTime(T recipe, PoseStack matrixStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(matrixStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        flame.draw(matrixStack, 46, 42);
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(matrixStack, 77, 38);
        drawExperience(recipe, matrixStack, 0);
        drawCookTime(recipe, matrixStack, 65);
    }
}
