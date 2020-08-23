package com.nuparu.sevendaystomine.network.packets;

import java.util.UUID;

import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;
import com.nuparu.sevendaystomine.util.computer.process.TickingProcess;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
