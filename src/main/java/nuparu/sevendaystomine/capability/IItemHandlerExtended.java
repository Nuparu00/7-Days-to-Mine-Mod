package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.items.IItemHandler;

public interface IItemHandlerExtended extends IItemHandler,IWorldNameable{
	public void copy(IItemHandler from);
    public NBTTagCompound serializeNBT();
    public void deserializeNBT(NBTTagCompound nbt);
}
