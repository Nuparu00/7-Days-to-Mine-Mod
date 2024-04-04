package nuparu.sevendaystomine.inventory.container;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.block.BlockArmchair;
import nuparu.sevendaystomine.entity.EntityCar;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.SlotCarSeat;
import nuparu.sevendaystomine.inventory.SlotMinibike;

public class ContainerCar extends Container {

	protected EntityCar car;
	protected InventoryPlayer playerInventory;
	protected ItemStackHandler inventory;

	public boolean addedChest = false;
	public ArrayList<Slot> chestSlots = new ArrayList<Slot>();

	public ContainerCar(InventoryPlayer playerInventory, EntityCar car) {
		this.playerInventory = playerInventory;
		this.car = car;

		inventory = car.getInventory();

		this.addSlotToContainer(new SlotMinibike(inventory, 0, 8, 8, ModItems.MINIBIKE_HANDLES));
		this.addSlotToContainer(new SlotMinibike(inventory, 1, 8, 26, Item.getItemFromBlock(ModBlocks.WHEELS)));
		this.addSlotToContainer(new SlotCarSeat(inventory, 2, 8, 44));

		this.addSlotToContainer(new SlotMinibike(inventory, 3, 152, 8, ModItems.CAR_BATTERY));
		this.addSlotToContainer(new SlotMinibike(inventory, 4, 152, 26, ModItems.SMALL_ENGINE));
		this.addSlotToContainer(new SlotMinibike(inventory, 5, 152, 44, Item.getItemFromBlock(ModBlocks.WHEELS)));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
		bindChest();
	}

	public void bindChest() {
		addedChest = true;
		int l = 0;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {

				Slot slot = new SlotItemHandler(inventory, 6 + l, 180 + j * 18, 14 + i * 18);
				this.chestSlots.add(slot);
				this.addSlotToContainer(slot);
				l++;
			}
		}
	}

	public void unbindChest() {

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.car.getDistance(playerIn) <= 4;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			Item item = itemstack.getItem();
			
			if (index < 6) {
				if (!this.mergeItemStack(itemstack1, 6, 40, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
				if (!car.world.isRemote) {
					car.updateInventory();
				}
			} else {
				boolean flag = false;
				if (item == ModItems.SMALL_ENGINE) {
					if (this.mergeItemStack(itemstack1, 0, 6, true)) {
						slot.onSlotChange(itemstack1, itemstack);
						flag = true;
					}
				} else if (item == ModItems.MINIBIKE_HANDLES) {
					if (this.mergeItemStack(itemstack1, 0, 6, true)) {
						slot.onSlotChange(itemstack1, itemstack);
						flag = true;
					}
				} else if ((itemstack.getItem() instanceof ItemBlock && ((ItemBlock)itemstack.getItem()).getBlock() instanceof BlockArmchair)) {
					if (this.mergeItemStack(itemstack1, 0, 6, true)) {
						slot.onSlotChange(itemstack1, itemstack);
						flag = true;
					}
				} else if (item == ModItems.CAR_BATTERY) {
					if (this.mergeItemStack(itemstack1, 0, 6, true)) {
						slot.onSlotChange(itemstack1, itemstack);
						flag = true;
					}
				} else if (item == Item.getItemFromBlock(ModBlocks.WHEELS)) {
					if (this.mergeItemStack(itemstack1, 0, 6, true)) {
						slot.onSlotChange(itemstack1, itemstack);
						flag = true;
					}
				}
				if (!flag) {
					if (index <= 32) {
						if (!this.mergeItemStack(itemstack1, 33, 41, true)) {
							return ItemStack.EMPTY;
						}
						slot.onSlotChange(itemstack1, itemstack);
					} else if (index <= 41) {
						if (!this.mergeItemStack(itemstack1, 7, 32, true)) {
							return ItemStack.EMPTY;
						}
						slot.onSlotChange(itemstack1, itemstack);
					}
				}
			}

		}
		return ItemStack.EMPTY;
	}

}
