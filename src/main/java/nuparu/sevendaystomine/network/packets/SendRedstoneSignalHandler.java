package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class SendRedstoneSignalHandler implements IMessageHandler<SendRedstoneSignalMessage, SendRedstoneSignalMessage> {

	public SendRedstoneSignalHandler() {

	}

	@Override
	public SendRedstoneSignalMessage onMessage(SendRedstoneSignalMessage message, MessageContext ctx) {

		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;
		
		BlockPos pos = message.pos;
		
		TileEntity te = world.getTileEntity(pos);
		if(te == null || !(te instanceof TileEntityComputer)) return null;

		if(player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
					(double) pos.getZ() + 0.5D) > 64.0D) {
			return null;
		}

		TileEntityComputer computer = (TileEntityComputer)te;
		computer.updateRedstoneSignal(message.facing, message.strength);
		
		
		return null;
	}

}
