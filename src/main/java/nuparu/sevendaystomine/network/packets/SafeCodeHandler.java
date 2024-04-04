package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

public class SafeCodeHandler implements IMessageHandler<SafeCodeMessage, SafeCodeMessage> {

	public SafeCodeHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public SafeCodeMessage onMessage(SafeCodeMessage message, MessageContext ctx) {
		BlockPos pos = message.pos;
		int toAdd = message.toAdd;
		if (pos == null || toAdd == 0) {
			return null;
		}

		EntityPlayerMP player = ctx.getServerHandler().player;
		player.getServer().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				World world = player.world;
				if (world == null) {
					return;
				}
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TileEntityCodeSafe) {
					TileEntityCodeSafe safe = (TileEntityCodeSafe) te;
					int selectedCode = safe.getSelectedCode();

					int h = (selectedCode / 100) % 10;
					int d = (selectedCode / 10) % 10;
					int u = selectedCode % 10;

					int absToAdd = Math.abs(toAdd);
					if (absToAdd <= 10) {
						u += (toAdd / 10);
						if (u < 0) {
							u = 9;
						} else if (u > 9) {
							u = 0;
						}
					} else if (absToAdd <= 100) {
						d += (toAdd / 100);
						if (d < 0) {
							d = 9;
						} else if (d > 9) {
							d = 0;
						}
					} else if (absToAdd <= 1000) {
						h += (toAdd / 1000);
						if (h < 0) {
							h = 9;
						} else if (h > 9) {
							h = 0;
						}
					}

					String codeInString = new StringBuilder().append(h).append(d).append(u).toString();
					int codeInInt = Integer.parseInt(codeInString);
					safe.setSelectedCode(codeInInt, player);
					NBTTagCompound nbt = safe.writeToNBT(new NBTTagCompound());
					nbt.removeTag("CorrectCode");
					PacketManager.syncTileEntity.sendToAllTracking(new SyncTileEntityMessage(nbt, pos),
							new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 8));
				}
			}
		});

		return null;
	}

}
