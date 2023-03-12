package nuparu.sevendaystomine.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class PhotoRequestMessage {

	private String path;

	public PhotoRequestMessage() {

	}

	public PhotoRequestMessage(String path) {
		this.path = path;
	}

	public static void encode(PhotoRequestMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.path);
	}

	public static PhotoRequestMessage decode(FriendlyByteBuf buf) {
		return new PhotoRequestMessage(buf.readUtf());
	}

	public static class Handler {

		public static void handle(PhotoRequestMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ServerPlayer player = ctx.get().getSender();
				ServerLevel world = player.getLevel();
				File file = new File(
						DimensionType.getStorageFolder(world.dimension(),
								world.getServer().getWorldPath(LevelResource.ROOT).toFile().toPath()).toFile(),
						"/resources/photos/" + msg.path);
				if (file.exists() && !file.isDirectory()) {
					BufferedImage buffered = null;
					try {
						buffered = ImageIO.read(file);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					if (buffered == null)
						return;
					sendFile(buffered, player, msg.path);
				}
			});
			ctx.get().setPacketHandled(true);
		}

		public static void sendFile(BufferedImage img, ServerPlayer player, String name) {
			byte[] bytes = Utils.getBytes(img);
			List<byte[]> chunks = Utils.divideArray(bytes, 1000000);
			int parts = chunks.size();
			for (int i = 0; i < parts; i++) {
				PacketManager.sendTo(PacketManager.photoToClient, new PhotoToClientMessage(chunks.get(i), i, parts, name), player);
			}
		}
	}

}
