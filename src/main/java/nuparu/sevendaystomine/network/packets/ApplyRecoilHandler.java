package nuparu.sevendaystomine.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.events.RenderEventHandler;

public class ApplyRecoilHandler implements IMessageHandler<ApplyRecoilMessage, ApplyRecoilMessage> {
	@SideOnly(Side.CLIENT)
	public ApplyRecoilMessage onMessage(ApplyRecoilMessage message, MessageContext ctx) {
		SevenDaysToMine.proxy.addRecoil(message.recoil, Minecraft.getMinecraft().player);
		if (message.flash) {
			if (message.main) {
				RenderEventHandler.mainMuzzleFlash = 5;
				RenderEventHandler.mainMuzzleFlashAngle=Minecraft.getMinecraft().world.rand.nextDouble()*360;
			} else {
				RenderEventHandler.sideMuzzleFlash = 5;
				RenderEventHandler.sideMuzzleFlashAngle=Minecraft.getMinecraft().world.rand.nextDouble()*360;
			}
		}
		return null;
	}

}