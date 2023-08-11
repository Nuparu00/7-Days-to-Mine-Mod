package nuparu.sevendaystomine.client.gui.components.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.init.ModSounds;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class NotificationToast implements Toast {
   private final ItemStack stack;
   private final Component title;
   private final Component description;
   private boolean playedSound;

   private static final DisplayInfo displayinfo = new DisplayInfo(ItemStack.EMPTY,Component.empty(),Component.empty(),null,FrameType.GOAL,true,false, false) ;

   public NotificationToast(ItemStack stack, Component title, Component description) {
      this.stack = stack;
      this.title = title;
      this.description = description;
   }

   public Toast.Visibility render(GuiGraphics pGuiGraphics, ToastComponent pToastComponent, long pTimeSinceLastVisible) {
      pGuiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
      if (displayinfo != null) {
         List<FormattedCharSequence> list = pToastComponent.getMinecraft().font.split(displayinfo.getTitle(), 125);
         int i = displayinfo.getFrame() == FrameType.CHALLENGE ? 16746751 : 16776960;
         if (list.size() == 1) {
            pGuiGraphics.drawString(pToastComponent.getMinecraft().font, displayinfo.getFrame().getDisplayName(), 30, 7, i | -16777216, false);
            pGuiGraphics.drawString(pToastComponent.getMinecraft().font, list.get(0), 30, 18, -1, false);
         } else {
            int j = 1500;
            float f = 300.0F;
            if (pTimeSinceLastVisible < 1500L) {
               int k = Mth.floor(Mth.clamp((float)(1500L - pTimeSinceLastVisible) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
               pGuiGraphics.drawString(pToastComponent.getMinecraft().font, displayinfo.getFrame().getDisplayName(), 30, 11, i | k, false);
            } else {
               int i1 = Mth.floor(Mth.clamp((float)(pTimeSinceLastVisible - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
               int l = this.height() / 2 - list.size() * 9 / 2;

               for(FormattedCharSequence formattedcharsequence : list) {
                  pGuiGraphics.drawString(pToastComponent.getMinecraft().font, formattedcharsequence, 30, l, 16777215 | i1, false);
                  l += 9;
               }
            }
         }

         if (!this.playedSound && pTimeSinceLastVisible > 0L) {
            this.playedSound = true;
            if (displayinfo.getFrame() == FrameType.CHALLENGE) {
               pToastComponent.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));
            }
         }

         pGuiGraphics.renderFakeItem(displayinfo.getIcon(), 8, 8);
         return (double)pTimeSinceLastVisible >= 5000.0D * pToastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
      } else {
         return Toast.Visibility.HIDE;
      }
   }
}