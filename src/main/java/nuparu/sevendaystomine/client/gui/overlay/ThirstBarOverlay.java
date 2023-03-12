package nuparu.sevendaystomine.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEffects;

public class ThirstBarOverlay extends Gui implements IGuiOverlay {
    public static final ResourceLocation OVERLAYS_TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/overlays.png");

    public ThirstBarOverlay(Minecraft minecraft) {
        super(minecraft,minecraft.getItemRenderer());
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if(!ServerConfig.thirst.get()) return;
        boolean isMounted = minecraft.player.getVehicle() instanceof LivingEntity;
        if (!isMounted && !minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
            bind(OVERLAYS_TEXTURE);
            Player player = (Player) this.minecraft.getCameraEntity();
            RenderSystem.enableBlend();
            int left = width / 2 + 91;
            int top = height - gui.rightHeight;
            gui.rightHeight += 10;

            FoodData stats = minecraft.player.getFoodData();
            IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
            int level = extendedPlayer == null ?  0  : extendedPlayer.getWaterLevel();

            for (int i = 0; i < 10; ++i) {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int icon = 16;
                byte background = 0;

                if (minecraft.player.hasEffect(ModEffects.DYSENTERY.get())) {
                    icon += 27;
                    background = 3;
                }

                if (extendedPlayer != null && extendedPlayer.getSaturationLevel() <= 0.0F && minecraft.gui.getGuiTicks() % (level * 3 + 1) == 0) {
                    y = top + (random.nextInt(3) - 1);
                }

                blit(poseStack, x, y, background * 9, 0, 9, 9);

                if (idx < level)
                    blit(poseStack, x, y, icon - 7, 0, 9, 9);
                else if (idx == level)
                    blit(poseStack, x, y, icon + 2, 0, 9, 9);
            }
            RenderSystem.disableBlend();
        }
    }

    private void bind(ResourceLocation res)
    {
        RenderSystem.setShaderTexture(0, res);
    }
}
