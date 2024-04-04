package nuparu.sevendaystomine.electricity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

/*
 * Not used right now. Will be used in the future
 */

@Deprecated
public class EnergyManager extends EnergyStorage implements INBTSerializable<NBTTagCompound> {


	public EnergyManager(int capacity) {
		super(capacity);
	}

	public EnergyManager(int capacity, int maxTransfer) {
		super(maxTransfer, maxTransfer);
	}

	public EnergyManager(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public EnergyManager(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("energy", energy);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		energy = nbt.getInteger("energy");
	}
	
	public long getEnergy() {
		return energy;
	}

}
