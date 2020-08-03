package com.nuparu.sevendaystomine.inventory.itemhandler;

import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.entity.EntityMinibike;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

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
