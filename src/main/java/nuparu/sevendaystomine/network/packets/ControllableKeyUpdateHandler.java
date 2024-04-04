package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.entity.IControllable;

public class ControllableKeyUpdateHandler implements IMessageHandler<ControllableKeyUpdateMessage, IMessage> {

	public ControllableKeyUpdateHandler() {
	}

	@Override
	public IMessage onMessage(ControllableKeyUpdateMessage message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		
		int key = message.getKey();
		byte state = message.getState();
		
		if(player == null) return null;
		
		Entity riding = player.getRidingEntity();
		if(riding == null) return null;
		
		if(!(riding instanceof IControllable)) return null;
		
		((IControllable)riding).handleKey(key, state);
		
		return null;
	}

}
