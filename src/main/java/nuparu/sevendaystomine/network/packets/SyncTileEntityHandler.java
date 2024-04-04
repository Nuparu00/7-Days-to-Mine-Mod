package nuparu.sevendaystomine.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SyncTileEntityHandler implements IMessageHandler<SyncTileEntityMessage, SyncTileEntityMessage> {

	public SyncTileEntityHandler() {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public SyncTileEntityMessage onMessage(SyncTileEntityMessage message, MessageContext ctx) {
		BlockPos pos = message.pos;
		NBTTagCompound nbt = message.data;
		World world = Minecraft.getMinecraft().world;
		if(world == null) {
			return null;
		}
		TileEntity te = world.getTileEntity(pos);
		if(te == null) {
			return null;
		}
		te.readFromNBT(nbt);
		
		return null;
	}

}
