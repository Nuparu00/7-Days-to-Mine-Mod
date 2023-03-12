package nuparu.sevendaystomine.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.function.Supplier;

public class SchedulePhotoMessage{
	
	public SchedulePhotoMessage() {
		
	}
	
	public static void encode(SchedulePhotoMessage msg, FriendlyByteBuf buf) {
	}

	public static SchedulePhotoMessage decode(FriendlyByteBuf buf) {
		return new SchedulePhotoMessage();
	}
	
	public static class Handler {

		public static void handle(SchedulePhotoMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				SevenDaysToMine.proxy.schedulePhoto();
			});
		}
	}
}
