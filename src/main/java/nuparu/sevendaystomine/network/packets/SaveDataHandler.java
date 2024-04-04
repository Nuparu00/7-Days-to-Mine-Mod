package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.item.ItemCircuit;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class SaveDataHandler implements IMessageHandler<SaveDataMessage, SaveDataMessage> {

	public SaveDataHandler() {

	}

	@Override
	public SaveDataMessage onMessage(SaveDataMessage message, MessageContext ctx) {
		
		EntityPlayer player = ctx.getServerHandler().player;
		World world = player.world;
		
		BlockPos pos = message.pos;
		
		
		if(player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
					(double) pos.getZ() + 0.5D) > 64.0D) {
			return null;
		}
		
		String data = message.data;
		if (world == null) {
			return null;
		}

		TileEntity te = world.getTileEntity(pos);
		if (te == null || !(te instanceof TileEntityComputer)) {
			return null;
		}

		TileEntityComputer computer = (TileEntityComputer) te;
		if (!computer.isCompleted() || !computer.isOn() || computer.isLocked()) {
			return null;
		}

		ItemStack stack = computer.getStackInSlot(6);
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemCircuit)) {
			return null;
		}

		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setString("data", data);
		return null;
	}

}
