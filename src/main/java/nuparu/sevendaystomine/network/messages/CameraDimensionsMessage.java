package nuparu.sevendaystomine.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.item.AnalogCameraItem;

import java.util.function.Supplier;

public class CameraDimensionsMessage {

	double deltaWidth;
	double deltaHeight;
	double deltaZoom;

	public CameraDimensionsMessage() {

	}

	public CameraDimensionsMessage(double deltaWidth, double deltaHeight, double deltaZoom) {
		this.deltaWidth = deltaWidth;
		this.deltaHeight = deltaHeight;
		this.deltaZoom = deltaZoom;
	}

	public static void encode(CameraDimensionsMessage msg, FriendlyByteBuf buf) {
		buf.writeDouble(msg.deltaWidth);
		buf.writeDouble(msg.deltaHeight);
		buf.writeDouble(msg.deltaZoom);
	}

	public static CameraDimensionsMessage decode(FriendlyByteBuf buf) {
		return new CameraDimensionsMessage(buf.readDouble(),buf.readDouble(),buf.readDouble());
	}
	
	public static class Handler {

		public static void handle(CameraDimensionsMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				ServerPlayer player = ctx.get().getSender();
				ItemStack stack = player.getMainHandItem();
				if (stack.isEmpty())
					return;

				if (!(stack.getItem() instanceof AnalogCameraItem))
					return;

				AnalogCameraItem.setWidth(
						Mth.clamp(msg.deltaWidth + AnalogCameraItem.getWidth(stack, player), 0.25, 1), stack,
						player);
				AnalogCameraItem.setHeight(
						Mth.clamp(msg.deltaHeight + AnalogCameraItem.getHeight(stack, player), 0.25, 1),
						stack, player);
				AnalogCameraItem.setZoom(
						MathUtils.roundToNDecimal(
								Mth.clamp(msg.deltaZoom + AnalogCameraItem.getZoom(stack, player), 1, 4), 1),
						stack, player);
			});
		}
	}
}
