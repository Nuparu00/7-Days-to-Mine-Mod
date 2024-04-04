package nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.dialogue.Subtitle;
import nuparu.sevendaystomine.util.dialogue.SubtitleHelper;

@SideOnly(Side.CLIENT)
public class GuiSubtitles {

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@SideOnly(Side.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
		
		if (event.getType() == ElementType.EXPERIENCE && !event.isCancelable()) {

			SubtitleHelper helper = SubtitleHelper.INSTANCE;
			if (helper.getCurrentSubtitle() == null) {
				if (!helper.isAnythingInQueue()) {
					return;
				}
				helper.setCurrentSubtitle(getSubtitleFromQueue());
			} else if (helper.getCurrentSubtitle().showTime >= helper.getCurrentSubtitle().getDuration()) {
				GuiIngameForge.renderArmor = true;
				if (!helper.isAnythingInQueue()) {
					return;
				}
				helper.setCurrentSubtitle(getSubtitleFromQueue());
			}

			Minecraft mc = Minecraft.getMinecraft();
			Subtitle subtitle = helper.getCurrentSubtitle();
			if (subtitle == null)
				return;
			ScaledResolution resolution = event.getResolution();

			int dialogueBottom = Math.round(resolution.getScaledHeight() - 40 - (resolution.getScaledHeight() / 10f));
			int scale = resolution.getScaleFactor();
			int x = resolution.getScaledWidth() / 2;
			int yBase = dialogueBottom;
			yBase += MathUtils.clamp((int) Math.round((resolution.getScaledHeight() - dialogueBottom - 50) / 2d), 5,
					resolution.getScaledHeight() - dialogueBottom);

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			if (subtitle.getSender() != null) {
				RenderUtils.drawCenteredString(subtitle.getSender().getDisplayName().getFormattedText(), x, yBase, GuiDialogue.STYLING_COLOR,
						true);
			}
			RenderUtils.drawCenteredString(
					TextFormatting.ITALIC + SevenDaysToMine.proxy.localize(subtitle.getDialogue() + ".response"), x,
					yBase + 10, 0xffffff, true);
			GL11.glPopMatrix();

			ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, mc.ingameGUI, 0, "field_92017_k");

			subtitle.showTime+=event.getPartialTicks();
			GuiIngameForge.renderArmor = false;
		}
	}

	public static Subtitle getSubtitleFromQueue() {
		try {
			return SubtitleHelper.INSTANCE.getSubtitleFromQueue();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
