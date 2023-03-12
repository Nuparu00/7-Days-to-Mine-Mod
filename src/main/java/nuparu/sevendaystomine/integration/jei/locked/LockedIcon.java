package nuparu.sevendaystomine.integration.jei.locked;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.client.util.ClientUtils;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class LockedIcon {

    private final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/jei/locked_icon.png");

    public LockedIcon() {

    }

    public void draw(PoseStack matrixStack, int recipeWidth) {

        int width = 10;
        matrixStack.pushPose();
        ClientUtils.drawTexturedRect(matrixStack,TEXTURE,recipeWidth-width-9,0,0,0,width,width,width,width,1,0);
        matrixStack.popPose();
    }

    @Nullable
    public List<Component> getTooltipStrings(int mouseX, int mouseY, int recipeWidth, int recipeHeight, String recipe) {

        int width = 10;
        boolean inArea = Utils.isInArea(mouseX,mouseY,recipeWidth-width-9,0,width,width);
        ResourceLocation resourceLocation = new ResourceLocation(recipe);
        MutableComponent textComponent = Component.translatable("jei.tooltip.locked.recipe",Component.translatable("item." + resourceLocation.getNamespace()+"."+resourceLocation.getPath()));

        Player player = Minecraft.getInstance().player;
        if(player != null) {
            IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
            textComponent.setStyle(textComponent.getStyle().withColor(iep.hasRecipe(recipe) ? ChatFormatting.GREEN : ChatFormatting.RED));

        }
        if (inArea) {
            return Collections.singletonList(textComponent);
        }
        return null;
    }

}
