package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.NBTTagCompound;
import nuparu.sevendaystomine.world.gen.city.City;

public class ExtendedChunk implements IExtendedChunk {

	private City city;

	@Override
	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("city")) {
			if (this.city == null) {
				this.city = new City();
			}
			this.city.readNBT((NBTTagCompound) nbt.getTag("city"));
		} else {
			this.city = null;
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		if (city != null) {
			nbt.setTag("city", city.writeNBT(new NBTTagCompound()));
		}
		return nbt;
	}

	@Override
	public void copy(IExtendedChunk iep) {
		iep.setCity(getCity());
	}

	@Override
	public void onDataChanged() {

	}

	@Override
	public boolean hasCity() {
		return this.city != null;
	}

	@Override
	public void setCity(City city) {
		this.city = city;
		onDataChanged();
	}

	@Override
	public City getCity() {
		return this.city;
	}

}
