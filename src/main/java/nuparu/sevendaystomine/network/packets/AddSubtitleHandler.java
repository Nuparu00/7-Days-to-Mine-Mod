package nuparu.sevendaystomine.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.EntityHuman;
import nuparu.sevendaystomine.util.dialogue.Subtitle;
import nuparu.sevendaystomine.util.dialogue.SubtitleHelper;

public class AddSubtitleHandler implements IMessageHandler<AddSubtitleMessage, IMessage> {

	@Override
	public IMessage onMessage(AddSubtitleMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				int entityID = message.getEntityID();
				double duration = message.getDuration();
				String dialogue = message.getDialogue();

				EntityPlayer player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
				World world = player.world;

				Entity entity = world.getEntityByID(entityID);
				if (entity == null || !(entity instanceof EntityHuman))
					return;

				EntityHuman human = (EntityHuman) entity;

				SubtitleHelper.INSTANCE.addSubtitleToQueue(new Subtitle(human, dialogue, duration));
			}
		});
		return null;
	}

}
