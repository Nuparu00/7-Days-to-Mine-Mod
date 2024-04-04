package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.tileentity.TileEntityKeySafe;

public class KeySafeUpdateHandler implements IMessageHandler<KeySafeUpdateMessage, KeySafeUpdateMessage> {

	public KeySafeUpdateHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public KeySafeUpdateMessage onMessage(KeySafeUpdateMessage message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		World world = player.world;
		
		BlockPos pos = BlockPos.fromLong(message.pos);
		float angle = message.angle;
		float force = message.force;
		
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity != null && tileEntity instanceof TileEntityKeySafe) {
			TileEntityKeySafe safe = (TileEntityKeySafe)tileEntity;
			safe.setAngle(angle);
			safe.setForce(force);
		}
		return null;
	}

}
