package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.electricity.network.INetwork;

public class SendPacketHandler implements IMessageHandler<SendPacketMessage, SendPacketMessage> {

	public SendPacketHandler() {

	}

	@Override
	public SendPacketMessage onMessage(SendPacketMessage message, MessageContext ctx) {

		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;
		
		BlockPos fromPos = message.from;
		BlockPos toPos = message.to;
		
		TileEntity fromTE = world.getTileEntity(fromPos);
		if(fromTE == null || !(fromTE instanceof INetwork)) return null;
		TileEntity toTE = world.getTileEntity(toPos);
		if(toTE == null || !(toTE instanceof INetwork)) return null;

		if(player.getDistanceSq((double) fromPos.getX() + 0.5D, (double) fromPos.getY() + 0.5D,
					(double) fromPos.getZ() + 0.5D) > 64.0D) {
			return null;
		}

		INetwork from = (INetwork)fromTE;
		INetwork to = (INetwork)toTE;
		if(!from.isConnectedTo(to)) return null;
		to.sendPacket(message.packet,from,player);
		
		return null;
	}

}
