package nuparu.sevendaystomine.network.packets;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class KillProcessHandler implements IMessageHandler<KillProcessMessage, IMessage> {

	@Override
	public IMessage onMessage(KillProcessMessage message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;

		BlockPos pos = message.getPos();
		UUID id = message.getUUID();

		TileEntity TE = world.getTileEntity(pos);

		if (TE != null && TE instanceof TileEntityComputer) {
			TileEntityComputer computerTE = (TileEntityComputer) TE;
			if (computerTE.getMonitorTE() != null && computerTE.getMonitorTE().getLookingPlayers().contains(player)) {
				TickingProcess process = computerTE.getProcessByUUID(id);
				if (process != null) {
					computerTE.killProcess(process);
				}
			}
		}

		return null;
	}

}
