package nuparu.sevendaystomine.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PlayerCapabilitySyncHandler implements IMessageHandler<PlayerCapabilitySyncMessage, IMessage> {

	@Override
     @SideOnly(Side.CLIENT)
	public IMessage onMessage(PlayerCapabilitySyncMessage message, MessageContext ctx) {

          Minecraft mc = Minecraft.getMinecraft();
          IExtendedPlayer extendedPlayer = message.getExtendedPlayer();
          int playerID = message.getPlayerID();
          if(mc.world != null && mc.player != null){
               Entity entity = mc.world.getEntityByID(playerID);
               if(entity != null && entity instanceof EntityPlayer){
                    EntityPlayer player = (EntityPlayer)entity;
                    IExtendedPlayer localExtendedPlayer = CapabilityHelper.getExtendedPlayer(player);
                    localExtendedPlayer.copy(extendedPlayer);
               }
          }
		return null;
	}
}