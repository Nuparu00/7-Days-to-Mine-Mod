package nuparu.sevendaystomine.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientEventHandler;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.BiOptional;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.world.item.AnalogCameraItem;
import nuparu.sevendaystomine.world.item.GunItem;

public class GunOverlay extends Gui implements IGuiOverlay {
    public static final ResourceLocation OVERLAYS_TEXTURE = new ResourceLocation(SevenDaysToMine.MODID, "textures/gui/overlays.png");

    public GunOverlay(Minecraft minecraft) {
        super(minecraft,minecraft.getItemRenderer());
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {

        if (!minecraft.options.hideGui && minecraft.getCameraEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            BiOptional<ItemStack, ItemStack> guns = PlayerUtils.gunsInHands(player);
            guns.ifLeftPresent(gun -> renderMainhandAmmo(gun, guiGraphics));
            guns.ifRightPresent(gun -> renderOffhandAmmo(gun, guiGraphics));
        }
    }

    private void renderMainhandAmmo(ItemStack stack, GuiGraphics guiGraphics){
        HumanoidArm hand = minecraft.options.mainHand().get();
        renderAmmo(stack, hand, guiGraphics);
    }

    private void renderOffhandAmmo(ItemStack stack, GuiGraphics guiGraphics){
        HumanoidArm hand = minecraft.options.mainHand().get().getOpposite();
        renderAmmo(stack, hand, guiGraphics);
    }

    private void renderAmmo(ItemStack stack, HumanoidArm hand, GuiGraphics guiGraphics){
        int ammo = GunItem.getAmmo(stack);
        int capacity = GunItem.getCapacity(stack);

        Component component = Component.translatable("tooltip.sevendaystomine.ammo",ammo, capacity);
        int y = 5;
        int x = hand == HumanoidArm.LEFT ? 5 : minecraft.getWindow().getGuiScaledWidth() - 5 - minecraft.font.width(component);
        guiGraphics.drawString(minecraft.font, component, x, y, 0xffffff);
    }

    private void bind(ResourceLocation res) {
        RenderSystem.setShaderTexture(0, res);
    }
}
