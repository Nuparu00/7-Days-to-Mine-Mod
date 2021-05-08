package com.nuparu.sevendaystomine.network.packets;

import java.io.File;
import java.io.IOException;

import com.nuparu.sevendaystomine.client.util.ResourcesHelper;
import com.nuparu.sevendaystomine.util.photo.PhotoCatcherClient;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PhotoToClientHandler implements IMessageHandler<PhotoToClientMessage, IMessage> {

	@Override
	public IMessage onMessage(PhotoToClientMessage message, MessageContext ctx) {
		File file = PhotoCatcherClient.addBytesToMap(message.getBytes(), message.getParts(), message.getIndex(),
				message.getName());

		if (file == null)
			return null;

		ResourcesHelper.INSTANCE.addFile(file, message.getName());

		return null;
	}

}
