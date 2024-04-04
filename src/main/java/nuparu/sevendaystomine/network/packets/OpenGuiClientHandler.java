package nuparu.sevendaystomine.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class OpenGuiClientHandler implements IMessageHandler<OpenGuiClientMessage, OpenGuiClientMessage> {

	public OpenGuiClientHandler() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public OpenGuiClientMessage onMessage(OpenGuiClientMessage message, MessageContext ctx) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player == null) {
			return null;
		}
		player.openGui(SevenDaysToMine.instance, message.id, player.world, message.x, message.y, message.z);
		return null;
	}

}
