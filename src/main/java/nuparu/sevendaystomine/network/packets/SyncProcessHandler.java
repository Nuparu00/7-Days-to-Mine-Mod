package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class SyncProcessHandler implements IMessageHandler<SyncProcessMessage, SyncProcessMessage> {

	@Override
	public SyncProcessMessage onMessage(SyncProcessMessage message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;

		BlockPos pos = message.getPos();
		NBTTagCompound nbt = message.getNBT();

		TileEntity TE = world.getTileEntity(pos);

		if (TE != null && TE instanceof TileEntityComputer) {
			TileEntityComputer computerTE = (TileEntityComputer) TE;
			if (computerTE.getMonitorTE() != null && computerTE.getMonitorTE().getLookingPlayers().contains(player)) {
				computerTE.startProcess(nbt,true);
			}
		}

		return null;
	}

}
