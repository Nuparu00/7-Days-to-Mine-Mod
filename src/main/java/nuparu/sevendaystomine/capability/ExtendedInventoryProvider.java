package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ExtendedInventoryProvider implements ICapabilitySerializable<NBTBase> {
	@CapabilityInject(IItemHandlerExtended.class)
	public static final Capability<IItemHandlerExtended> EXTENDED_INV_CAP = null;
	private IItemHandlerExtended instance = EXTENDED_INV_CAP.getDefaultInstance();

	public ExtendedInventoryProvider(int slots) {
		this.instance = new ExtendedInventory(slots);
	}
	
	public ExtendedInventoryProvider(int slots, String name) {
		this.instance = new ExtendedInventory(slots,name);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)

	{

		return capability == EXTENDED_INV_CAP;

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)

	{

		return capability == EXTENDED_INV_CAP ? EXTENDED_INV_CAP.<T>cast(this.instance) : null;

	}

	@Override
	public NBTBase serializeNBT()

	{

		return EXTENDED_INV_CAP.getStorage().writeNBT(EXTENDED_INV_CAP, this.instance, null);

	}

	@Override
	public void deserializeNBT(NBTBase nbt)

	{

		EXTENDED_INV_CAP.getStorage().readNBT(EXTENDED_INV_CAP, this.instance, null, nbt);

	}

}