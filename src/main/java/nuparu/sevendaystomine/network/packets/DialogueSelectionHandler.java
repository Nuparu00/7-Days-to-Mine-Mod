package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.entity.EntityHuman;

public class DialogueSelectionHandler implements IMessageHandler<DialogueSelectionMessage, IMessage> {

	@Override
	public IMessage onMessage(DialogueSelectionMessage message, MessageContext ctx) {
		String dialogue = message.getDialogue();
		int entityID = message.getEntityID();

		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;
		Entity entity = world.getEntityByID(entityID);
		if (entity == null || !(entity instanceof EntityHuman))
			return null;

		EntityHuman human = (EntityHuman) entity;
		if (human.getDistance(player) > 6) {
			return null;
		}
		human.onDialogue(dialogue, player);

		return null;
	}

}
