package nuparu.sevendaystomine.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientEventHandler;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.world.item.AnalogCameraItem;

public class CameraOverlay extends Gui implements IGuiOverlay {
    public static final ResourceLocation OVERLAYS_TEXTURE = new ResourceLocation(SevenDaysToMine.MODID, "textures/gui/overlays.png");

    public CameraOverlay(Minecraft minecraft) {
        super(minecraft,minecraft.getItemRenderer());
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {

        if (!minecraft.options.hideGui && minecraft.getCameraEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty() && stack.getItem() == ModItems.ANALOG_CAMERA.get()) {
                if (ClientEventHandler.takingPhoto)
                    return;
                double dW = 1 - AnalogCameraItem.getWidth(stack, player);
                double dH = 1 - AnalogCameraItem.getHeight(stack, player);

                int xMin = (int) (0 + width * dW / 2);
                int yMin = (int) (0 + height * dH / 2);
                int xMax = (int) (width - 32 - width * dW / 2);
                int yMax = (int) (height - 32 - height * dH / 2);

                guiGraphics.pose().pushPose();
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                guiGraphics.blit(ThirstBarOverlay.OVERLAYS_TEXTURE, xMin, yMin, 0, 102, 32, 32);
                guiGraphics.blit(ThirstBarOverlay.OVERLAYS_TEXTURE, xMax, yMin, 34, 102, 32, 32);

                guiGraphics.blit(ThirstBarOverlay.OVERLAYS_TEXTURE, xMin, yMax, 0, 135, 32, 32);
                guiGraphics.blit(ThirstBarOverlay.OVERLAYS_TEXTURE, xMax, yMax, 34, 135, 32, 32);

                guiGraphics.drawString(minecraft.font, AnalogCameraItem.getZoom(stack, player) + "x", xMin + 5,
                        yMax + 25 - minecraft.font.lineHeight, 0xffffff);
                RenderSystem.disableBlend();
                guiGraphics.pose().popPose();

            }
        }
    }

    private void bind(ResourceLocation res) {
        RenderSystem.setShaderTexture(0, res);
    }
}
