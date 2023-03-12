package nuparu.sevendaystomine.network.messages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.util.photo.PhotoCatcherServer;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.function.Supplier;

public class PhotoToServerMessage {

	private byte[] bytes;
	private int index;
	private int parts;
	private String id;

	public PhotoToServerMessage() {

	}

	public PhotoToServerMessage(byte[] bytes, int index, int parts, String id) {
		this.bytes = bytes;
		this.index = index;
		this.parts = parts;
		this.id = id;
	}

	public static void encode(PhotoToServerMessage msg, FriendlyByteBuf buf) {
		CompoundTag nbt = new CompoundTag();
		nbt.putByteArray("image", msg.bytes);
		nbt.putInt("index", msg.index);
		nbt.putInt("parts", msg.parts);
		nbt.putString("id", msg.id);
		buf.writeNbt(nbt);
		
	}

	public static PhotoToServerMessage decode(FriendlyByteBuf buf) {
		CompoundTag nbt = buf.readNbt();
		byte[] bytes = nbt.getByteArray("image");
		int index = nbt.getInt("index");
		int parts = nbt.getInt("parts");
		String id = nbt.getString("id");
		
		return new PhotoToServerMessage(bytes,index,parts,id);
	}

	public static class Handler {

		public static void handle(PhotoToServerMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				if (!ServerConfig.allowPhotos.get()) return;
				ServerPlayer player = ctx.get().getSender();

				File file = PhotoCatcherServer.addBytesToMap(msg.bytes, msg.id, msg.parts,
						msg.index, player.getName().getString().toLowerCase());

				if (file == null)
					return;

				player.level.playSound(null, player.blockPosition(), ModSounds.CAMERA_TAKE.get(), SoundSource.PLAYERS, 0.3F,
						1.0F / (player.level.random.nextFloat() * 0.4F + 1.2F) + 0.5F);
				ItemStack stack = new ItemStack(ModItems.PHOTO.get());
				stack.getOrCreateTag().putString("path", FilenameUtils.getName(file.getPath()));
				if (!player.getInventory().add(stack)) {
					player.drop(stack, false);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
