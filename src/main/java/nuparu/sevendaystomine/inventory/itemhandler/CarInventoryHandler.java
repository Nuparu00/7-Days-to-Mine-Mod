package nuparu.sevendaystomine.inventory.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import nuparu.sevendaystomine.entity.EntityCar;

public class CarInventoryHandler extends ItemStackHandler {

	protected EntityCar car;

	public CarInventoryHandler(EntityCar car) {
		super();
		this.car = car;
	}

	public CarInventoryHandler(int size, EntityCar car) {
		super(size);
		this.car = car;
	}

	public CarInventoryHandler(NonNullList<ItemStack> stacks, EntityCar car) {
		super(stacks);
		this.car = car;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (this.car != null) {
			this.car.onInventoryChanged(this);
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}
}
