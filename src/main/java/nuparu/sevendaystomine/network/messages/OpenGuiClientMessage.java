package nuparu.sevendaystomine.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.function.Supplier;

public class OpenGuiClientMessage {

	int id;
	int x;
	int y;
	int z;

	public OpenGuiClientMessage() {

	}

	public OpenGuiClientMessage(int id, int x, int y, int z) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void encode(OpenGuiClientMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.id);
		buf.writeInt(msg.x);
		buf.writeInt(msg.y);
		buf.writeInt(msg.z);
	}

	public static OpenGuiClientMessage decode(FriendlyByteBuf buf) {
		return new OpenGuiClientMessage(buf.readInt(),buf.readInt(),buf.readInt(),buf.readInt());
	}
	
	public static class Handler {

		public static void handle(OpenGuiClientMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				Player player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
				if(player == null) {
					return;
				}
				SevenDaysToMine.proxy.openClientSideGui(msg.id,msg.x, msg.y, msg.z);
				//System.out.println("DO NO USE THIS");
				//NetworkHooks.openGui(player, containerSupplier);
            });
			ctx.get().setPacketHandled(true);
		}
	}
}
