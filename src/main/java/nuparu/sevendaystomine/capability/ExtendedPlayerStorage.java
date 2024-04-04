package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ExtendedPlayerStorage implements IStorage<IExtendedPlayer> {

     @Override
     public NBTBase writeNBT(Capability<IExtendedPlayer> capability, IExtendedPlayer instance, EnumFacing side)

     {
          NBTTagCompound nbt = new NBTTagCompound();
          instance.writeNBT(nbt);
          return nbt;
     }

     @Override
     public void readNBT(Capability<IExtendedPlayer> capability, IExtendedPlayer instance, EnumFacing side, NBTBase nbt) {
          NBTTagCompound tag = (NBTTagCompound) nbt;
          instance.readNBT(tag);
     }
}