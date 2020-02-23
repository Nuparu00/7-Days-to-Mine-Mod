package com.nuparu.sevendaystomine.network.packets;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityHuman;
import com.nuparu.sevendaystomine.util.dialogue.Subtitle;
import com.nuparu.sevendaystomine.util.dialogue.SubtitleHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AddSubtitleHandler implements IMessageHandler<AddSubtitleMessage, IMessage> {

	@Override
	public IMessage onMessage(AddSubtitleMessage message, MessageContext ctx) {
		int entityID = message.getEntityID();
		double duration = message.getDuration();
		String dialogue = message.getDialogue();

		EntityPlayer player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
		World world = player.world;

		Entity entity = world.getEntityByID(entityID);
		if (entity == null || !(entity instanceof EntityHuman))
			return null;

		EntityHuman human = (EntityHuman) entity;

		SubtitleHelper.INSTANCE.addSubtitleToQueue(new Subtitle(human, dialogue, duration));

		return null;
	}

}
