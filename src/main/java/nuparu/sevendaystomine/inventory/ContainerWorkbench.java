package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityWorkbench;

public class ContainerWorkbench extends Container {
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 5, 5);
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	private World worldObj;
	/** Position of the workbench */
	private BlockPos pos;
	public EntityPlayer player;

	public ContainerWorkbench(EntityPlayer player, IItemHandlerNameable playerInventory, IItemHandlerNameable combinedInvWrapper,
			TileEntityWorkbench workbench, BlockPos posIn) {
		this.worldObj = workbench.getWorld();
		this.player = player;
		this.pos = posIn;
		craftMatrix = new InventoryCrafting(this, 5, 5);
		craftResult = new InventoryCraftResult();
		this.addSlotToContainer(new SlotWorkbenchCrafting(player, this.craftMatrix, this.craftResult, 0, 134, 44));

		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 5, 8 + j * 18, 7 + i * 18));
			}
		}

		bindPlayerInventory(playerInventory);
		this.onCraftMatrixChanged(this.craftMatrix);

	}

	protected void bindPlayerInventory(IItemHandlerNameable playerInventory) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new SlotItemHandler(playerInventory, k, 8 + k * 18, 164));
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.slotChangedCraftingGrid(this.worldObj, this.player, this.craftMatrix, this.craftResult);
	}

	@Override
	protected void slotChangedCraftingGrid(World p_192389_1_, EntityPlayer p_192389_2_, InventoryCrafting p_192389_3_,
			InventoryCraftResult p_192389_4_) {
		if (!p_192389_1_.isRemote) {
			boolean flag = false;
			for (int i = 0; i < p_192389_3_.getSizeInventory(); i++) {
				if (!p_192389_3_.getStackInSlot(i).isEmpty()) {
					flag = true;
					break;
				}
			}

			EntityPlayerMP entityplayermp = (EntityPlayerMP) p_192389_2_;
			ItemStack itemstack = ItemStack.EMPTY;
			//if (flag) {
				IRecipe irecipe = CraftingManager.findMatchingRecipe(p_192389_3_, p_192389_1_);

				if (irecipe != null
						&& (irecipe.isDynamic() || !p_192389_1_.getGameRules().getBoolean("doLimitedCrafting")
								|| entityplayermp.getRecipeBook().isUnlocked(irecipe))) {
					p_192389_4_.setRecipeUsed(irecipe);
					itemstack = irecipe.getCraftingResult(p_192389_3_);
				}
			//}

			p_192389_4_.setInventorySlotContents(0, itemstack);
			entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, itemstack));
		}
	}

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		if (!this.worldObj.isRemote) {
			this.clearContainer(playerIn, this.worldObj, this.craftMatrix);
		}
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		if (this.worldObj.getBlockState(this.pos).getBlock() != ModBlocks.WORKBENCH) {
			return false;
		} else {
			return playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this
	 * moves the stack between the player inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, this.worldObj, playerIn);

				if (!this.mergeItemStack(itemstack1, 26, 62, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 26 && index < 53) {
				if (!this.mergeItemStack(itemstack1, 53, 62, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 53 && index < 62) {
				if (!this.mergeItemStack(itemstack1, 26, 53, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 26, 62, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	/**
	 * Called to determine if the current slot is valid for the stack merging
	 * (double-click) code. The stack passed in is null for the initial slot that
	 * was double-clicked.
	 */
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}
}