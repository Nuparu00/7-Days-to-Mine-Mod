package nuparu.sevendaystomine.inventory.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import nuparu.sevendaystomine.entity.EntityAirdrop;

public class AirdropInventoryHandler extends ItemStackHandler {

	protected EntityAirdrop airdrop;

	public AirdropInventoryHandler(EntityAirdrop airdrop) {
		super();
		this.airdrop = airdrop;
	}

	public AirdropInventoryHandler(int size, EntityAirdrop airdrop) {
		super(size);
		this.airdrop = airdrop;
	}

	public AirdropInventoryHandler(NonNullList<ItemStack> stacks, EntityAirdrop airdrop) {
		super(stacks);
		this.airdrop = airdrop;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (this.airdrop != null) {
			this.airdrop.onInventoryChanged(this);
		}
	}
}
