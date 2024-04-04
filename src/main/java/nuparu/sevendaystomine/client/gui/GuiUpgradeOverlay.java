package nuparu.sevendaystomine.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemUpgrader;

@SideOnly(Side.CLIENT)
public class GuiUpgradeOverlay extends Gui {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void eventHandler(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE) {
			
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer entity = mc.player;
			ItemStack stack = entity.inventory.getCurrentItem();
			if (stack != null && stack.getItem() instanceof ItemUpgrader) {
				RayTraceResult objectPosition = entity.rayTrace(5, 1);
				if (objectPosition != null) {
					BlockPos blockPos = objectPosition.getBlockPos();
					if (blockPos != null && stack.getTagCompound() != null && stack.getTagCompound().hasKey("Percent", Constants.NBT.TAG_FLOAT)) {
						if (stack.getTagCompound().getFloat("Percent") != 0) {
						//IBlockState state = world.getBlockState(blockPos);
						//if (state.getBlock() instanceof IUpgradeable || VanillaManager.getVanillaUpgrade(state) != null) {
							
								
								ScaledResolution resolution = event.getResolution();
								int posX = (resolution.getScaledWidth() / 2);
								int posY = (resolution.getScaledHeight() / 2);
								float percent = stack.getTagCompound().getFloat("Percent");
								float percentAbs = Math.abs(percent);
								
								GL11.glPushMatrix();
								GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
								GL11.glDisable(GL11.GL_LIGHTING);
								GL11.glEnable(GL11.GL_BLEND);
								mc.renderEngine.bindTexture(GuiPlayerUI.UI_TEX);
								drawTexturedModalRect(posX - 50, posY + 2, 0, 178, (int) (percentAbs * 100f), 7);
								drawTexturedModalRect(posX - 51, posY - 32, percent < 0 ? 102 : 0, 136, 102, 42);
								drawCenteredString(mc.fontRenderer, (int) (percentAbs * 100f) + "%", posX, posY + 10,
										0xffffff);
								GL11.glPopMatrix();

							//}
						}

					}

				}
			}

		}
	}
}
