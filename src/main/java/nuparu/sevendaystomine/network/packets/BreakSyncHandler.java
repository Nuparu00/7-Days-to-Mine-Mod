package nuparu.sevendaystomine.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.block.repair.BreakSavedData;

public class BreakSyncHandler implements IMessageHandler<BreakSyncMessage, BreakSyncMessage> {
	@SideOnly(Side.CLIENT)
	public BreakSyncMessage onMessage(BreakSyncMessage message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				handleClient(message.getNBT());
			}
		});

		return null;
	}

	@SideOnly(Side.CLIENT)
	public void handleClient(NBTTagCompound nbt) {
		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.world;
		BreakSavedData data = BreakSavedData.get(world);
		if (data != null && nbt != null) {
			data.readFromNBT(nbt);
		}
	}
}