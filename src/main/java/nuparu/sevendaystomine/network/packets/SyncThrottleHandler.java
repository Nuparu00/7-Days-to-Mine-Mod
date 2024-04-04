package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.entity.EntityMinibike;

public class SyncThrottleHandler implements IMessageHandler<SyncThrottleMessage, IMessage> {

	public SyncThrottleHandler() {
	}

	@Override
	public IMessage onMessage(SyncThrottleMessage message, MessageContext ctx) {
		
		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;
		
		int id = message.id;
		float throttle = message.throttle;
		
		Entity entity = world.getEntityByID(id);
		
		if(entity == null) return null;
		
		if(!(entity instanceof EntityMinibike)) return null;
		
		EntityMinibike minibike = (EntityMinibike)entity;
		if(minibike.getControllingPassenger() != player) return null;
		
		//minibike.setThrottle(throttle);
		
		return null;
	}

}
