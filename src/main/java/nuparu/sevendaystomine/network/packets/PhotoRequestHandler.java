package nuparu.sevendaystomine.network.packets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.util.Utils;

public class PhotoRequestHandler implements IMessageHandler<PhotoRequestMessage, IMessage> {

	@Override
	public IMessage onMessage(PhotoRequestMessage message, MessageContext ctx) {
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "/resources/photos/" + message.getPath());
		if (file.exists() && !file.isDirectory()) {
			BufferedImage buffered = null;
			try {
				buffered = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			if (buffered == null)
				return null;
			sendFile(buffered, ctx.getServerHandler().player,message.getPath());
		}
		return null;
	}

	public void sendFile(BufferedImage img, EntityPlayerMP player, String name) {
		byte[] bytes = Utils.getBytes(img);
		List<byte[]> chunks = Utils.divideArray(bytes, 1000000);
		int parts = chunks.size();
		for (int i = 0; i < parts; i++) {
			PacketManager.photoToClient.sendTo(new PhotoToClientMessage(chunks.get(i), i, parts, name), player);
		}
	}

}
