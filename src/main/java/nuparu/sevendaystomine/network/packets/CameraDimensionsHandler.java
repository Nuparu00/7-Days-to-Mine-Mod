package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.item.ItemAnalogCamera;
import nuparu.sevendaystomine.util.MathUtils;

public class CameraDimensionsHandler implements IMessageHandler<CameraDimensionsMessage, CameraDimensionsMessage> {

	@Override
	public CameraDimensionsMessage onMessage(CameraDimensionsMessage message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		player.getServer().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				ItemStack stack = player.getHeldItemMainhand();
				if (stack.isEmpty())
					return;

				if (!(stack.getItem() instanceof ItemAnalogCamera))
					return;

				ItemAnalogCamera.setWidth(
						MathHelper.clamp(message.deltaWidth + ItemAnalogCamera.getWidth(stack, player), 0.25, 1), stack,
						player);
				ItemAnalogCamera.setHeight(
						MathHelper.clamp(message.deltaHeight + ItemAnalogCamera.getHeight(stack, player), 0.25, 1),
						stack, player);
				ItemAnalogCamera.setZoom(
						MathUtils.roundToNDecimal(
								MathHelper.clamp(message.deltaZoom + ItemAnalogCamera.getZoom(stack, player), 1, 4), 1),
						stack, player);
			}
		});
		return null;
	}
}
