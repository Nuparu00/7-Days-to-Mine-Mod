package nuparu.sevendaystomine.inventory.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import nuparu.sevendaystomine.entity.EntityMinibike;

public class MinibikeInventoryHandler extends ItemStackHandler {

	protected EntityMinibike minibike;

	public MinibikeInventoryHandler(EntityMinibike minibike) {
		super();
		this.minibike = minibike;
	}

	public MinibikeInventoryHandler(int size, EntityMinibike minibike) {
		super(size);
		this.minibike = minibike;
	}

	public MinibikeInventoryHandler(NonNullList<ItemStack> stacks, EntityMinibike minibike) {
		super(stacks);
		this.minibike = minibike;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (this.minibike != null) {
			this.minibike.onInventoryChanged(this);
		}
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}
}
