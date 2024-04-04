package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ExtendedChunkStorage implements IStorage<IExtendedChunk> {

	@Override
	public NBTBase writeNBT(Capability<IExtendedChunk> capability, IExtendedChunk instance, EnumFacing side)
	{
		NBTTagCompound nbt = instance.writeNBT(new NBTTagCompound());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IExtendedChunk> capability, IExtendedChunk instance, EnumFacing side,
			NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.readNBT(tag);
	}
}