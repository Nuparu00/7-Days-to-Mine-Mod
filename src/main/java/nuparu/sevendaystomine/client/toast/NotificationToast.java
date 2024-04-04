package nuparu.sevendaystomine.client.toast;

import java.util.List;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.sound.SoundHelper;

@SideOnly(Side.CLIENT)
public class NotificationToast implements IToast {

	private boolean hasPlayedSound = false;
	private ItemStack stack;
	private ITextComponent title;
	private ITextComponent desc;

	public NotificationToast(ItemStack stack, ITextComponent title, ITextComponent desc) {
		this.stack = stack;
		this.title = title;
		this.desc = desc;
	}

	public IToast.Visibility draw(GuiToast toastGui, long delta) {
		toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

		if (!stack.isEmpty()) {
			List<String> list = toastGui.getMinecraft().fontRenderer
					.listFormattedStringToWidth(title.getFormattedText(), 125);

			if (list.size() == 1) {
				toastGui.getMinecraft().fontRenderer.drawString(title.getFormattedText(), 30, 7, 16776960 | -16777216);
				toastGui.getMinecraft().fontRenderer.drawString(desc.getFormattedText(), 30, 18, -1);
			} else {
				int j = 1500;
				float f = 300.0F;

				if (delta < 1500L) {
					int k = MathHelper
							.floor(MathHelper.clamp((float) (1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24
							| 67108864;
					toastGui.getMinecraft().fontRenderer.drawString(I18n.format("unlocked.toast"), 30, 11,
							16776960 | k);
				} else {
					int i1 = MathHelper
							.floor(MathHelper.clamp((float) (delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24
							| 67108864;
					int l = 16 - list.size() * toastGui.getMinecraft().fontRenderer.FONT_HEIGHT / 2;

					for (String s : list) {
						toastGui.getMinecraft().fontRenderer.drawString(s, 30, l, 16777215 | i1);
						l += toastGui.getMinecraft().fontRenderer.FONT_HEIGHT;
					}
				}
			}

			if (!this.hasPlayedSound && delta > 0L) {
				this.hasPlayedSound = true;

				toastGui.getMinecraft().getSoundHandler()
						.playSound(PositionedSoundRecord.getRecord(SoundHelper.RECIPE_UNLOCKED, 1.0F, 1.0F));

			}

			RenderHelper.enableGUIStandardItemLighting();
			toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase) null, stack, 8, 8);
			return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
		} else {
			return IToast.Visibility.HIDE;
		}
	}
}