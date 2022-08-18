package nuparu.sevendaystomine.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.ItemUtils;

public class UpgradeOverlay extends Gui implements IGuiOverlay {
    public static final ResourceLocation OVERLAYS_TEXTURE = new ResourceLocation(SevenDaysToMine.MODID, "textures/gui/overlays.png");

    public UpgradeOverlay(Minecraft minecraft) {
        super(minecraft,minecraft.getItemRenderer());
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {

        if (!minecraft.options.hideGui) {
            Player player = (Player) this.minecraft.getCameraEntity();
            ItemStack stack = player.getMainHandItem();
            if (ItemUtils.getUpgradeAmount(stack) <= 0) return;
            if (!stack.getOrCreateTag().contains("7D2M_UpgradeProgress", Tag.TAG_DOUBLE)) return;
            double progress = stack.getOrCreateTag().getDouble("7D2M_UpgradeProgress");

            bind(OVERLAYS_TEXTURE);
            RenderSystem.enableBlend();
            int x = width / 2;
            int y = height / 2;

            blit(poseStack, x - 50, y + 2, 0, 94, (int) (Math.abs(progress) * 100f), 7);
            blit(poseStack, x - 51, y - 32, progress < 0 ? 102 : 0, 52, 102, 42);
            drawCenteredString(poseStack, minecraft.font, (int) (Math.abs(progress) * 100f) + "%", x, y + 10,
                    0xffffff);

            RenderSystem.disableBlend();
        }
    }

    private void bind(ResourceLocation res) {
        RenderSystem.setShaderTexture(0, res);
    }
}
