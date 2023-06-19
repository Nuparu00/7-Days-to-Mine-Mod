package nuparu.sevendaystomine.network.messages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.photo.PhotoCatcherClient;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class PhotoToClientMessage{

	private byte[] bytes;
	private int index;
	private int parts;
	private String name;

	public PhotoToClientMessage() {

	}

	public PhotoToClientMessage(byte[] bytes, int index, int parts, String name) {
		this.bytes = bytes;
		this.index = index;
		this.parts = parts;
		this.name = name;
	}

	public static void encode(PhotoToClientMessage msg, FriendlyByteBuf buf) {
		CompoundTag nbt = new CompoundTag();
		nbt.putByteArray("image", msg.bytes);
		nbt.putInt("index", msg.index);
		nbt.putInt("parts", msg.parts);
		nbt.putString("id", msg.name);
		buf.writeNbt(nbt);
		
	}

	public static PhotoToClientMessage decode(FriendlyByteBuf buf) {
		CompoundTag nbt = buf.readNbt();
		byte[] bytes = nbt.getByteArray("image");
		int index = nbt.getInt("index");
		int parts = nbt.getInt("parts");
		String id = nbt.getString("id");
		
		return new PhotoToClientMessage(bytes,index,parts,id);
	}

	public static class Handler {

		public static void handle(PhotoToClientMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				if (!ServerConfig.allowPhotos.get()) return;
				File file;
				try {
					file = PhotoCatcherClient.addBytesToMap(msg.bytes, msg.parts, msg.index,
							msg.name);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

				if (file == null)
					return;
				ResourcesHelper.INSTANCE.addFile(file, msg.name);
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
