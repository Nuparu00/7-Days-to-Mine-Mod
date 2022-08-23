package nuparu.sevendaystomine.client.gui.components.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
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

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class NotificationToast implements Toast {
   private final ItemStack stack;
   private final Component title;
   private final Component description;
   private boolean playedSound;

   public NotificationToast(ItemStack stack, Component title, Component description) {
      this.stack = stack;
      this.title = title;
      this.description = description;
   }

   public Visibility render(PoseStack p_94800_, ToastComponent p_94801_, long p_94802_) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, TEXTURE);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      p_94801_.blit(p_94800_, 0, 0, 0, 0, this.width(), this.height());
         List<FormattedCharSequence> list = p_94801_.getMinecraft().font.split(description, 125);
         int i = 16776960;
         if (list.size() == 1) {
            p_94801_.getMinecraft().font.draw(p_94800_, title, 30.0F, 7.0F, i | -16777216);
            p_94801_.getMinecraft().font.draw(p_94800_, list.get(0), 30.0F, 18.0F, -1);
         } else {
            int j = 1500;
            float f = 300.0F;
            if (p_94802_ < 1500L) {
               int k = Mth.floor(Mth.clamp((float)(1500L - p_94802_) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
               p_94801_.getMinecraft().font.draw(p_94800_, title, 30.0F, 11.0F, i | k);
            } else {
               int i1 = Mth.floor(Mth.clamp((float)(p_94802_ - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
               int l = this.height() / 2 - list.size() * 9 / 2;

               for(FormattedCharSequence formattedcharsequence : list) {
                  p_94801_.getMinecraft().font.draw(p_94800_, formattedcharsequence, 30.0F, (float)l, 16777215 | i1);
                  l += 9;
               }
            }
         }

         if (!this.playedSound && p_94802_ > 0L) {
            this.playedSound = true;
               p_94801_.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.RECIPE_UNLOCKED.get(), 1.0F, 1.0F));
         }

         p_94801_.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(stack, 8, 8);
         return p_94802_ >= 5000L ? Visibility.HIDE : Visibility.SHOW;
      }
}