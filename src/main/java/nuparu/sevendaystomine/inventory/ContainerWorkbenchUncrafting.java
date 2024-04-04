package nuparu.sevendaystomine.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.block.BlockChemistryStation;
import nuparu.sevendaystomine.block.BlockCombustionGenerator;
import nuparu.sevendaystomine.block.BlockGenerator;
import nuparu.sevendaystomine.block.BlockWorkbench;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.item.IQuality;
import nuparu.sevendaystomine.item.ItemBullet;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.tileentity.TileEntityWorkbench;
import nuparu.sevendaystomine.util.Utils;

public class ContainerWorkbenchUncrafting extends Container {
	public InventoryUncrafting craftMatrix = new InventoryUncrafting(this, 1, 1);
	public InventoryUncraftResult craftResult = new InventoryUncraftResult();
	private World worldObj;
	/** Position of the workbench */
	private BlockPos pos;
	public EntityPlayer player;
	public TileEntityWorkbench workbench;

	public ContainerWorkbenchUncrafting(EntityPlayer player, IItemHandlerNameable playerInventory,
			IItemHandlerNameable combinedInvWrapper, TileEntityWorkbench workbench, BlockPos posIn) {
		this.worldObj = workbench.getWorld();
		this.player = player;
		this.pos = posIn;
		this.workbench = workbench;
		craftMatrix = new InventoryUncrafting(this, 1, 1);
		craftResult = new InventoryUncraftResult();
		this.addSlotToContainer(new Slot(this.craftMatrix, 0, 26, 44));

		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				this.addSlotToContainer(new SlotWorkbenchUncrafting(player, this.craftMatrix, this.craftResult,
						j + i * 5, 80 + j * 18, 7 + i * 18, this));
			}
		}
		this.addSlotToContainer(new SlotWorkbenchScrap(combinedInvWrapper, 0, 26, 70, this));
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

	public void onScrapChanged(ItemStack scrap) {

		updateCraftingGrid(worldObj, player, craftMatrix, craftResult, scrap);
	}

	protected void slotChangedCraftingGrid(World world, EntityPlayer player, InventoryUncrafting input,
			InventoryUncraftResult output) {
		updateCraftingGrid(world, player, input, output, this.workbench.getInventory().getStackInSlot(0));
	}

	protected void updateCraftingGrid(World world, EntityPlayer player, InventoryUncrafting input,
			InventoryUncraftResult output, ItemStack scrap) {
		if (!world.isRemote) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
			if (scrap.isEmpty()) {
				for (int m = 0; m < 25; m++) {
					output.setInventorySlotContents(m, ItemStack.EMPTY);
					entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, m + 1, ItemStack.EMPTY));
				}
				return;
			}
			ItemStack itemstack = input.getStackInSlot(0);
			if(!isUncraftable(itemstack)) {
				for (int m = 0; m < 25; m++) {
					output.setInventorySlotContents(m, ItemStack.EMPTY);
					entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, m + 1, ItemStack.EMPTY));
				}
				return;
			}
			IRecipe irecipe = Utils.getRecipesForStack(itemstack, world);
			if (irecipe != null) {
				NonNullList<Ingredient> list = irecipe.getIngredients();
				int i = 3;
				int j = irecipe instanceof net.minecraftforge.common.crafting.IShapedRecipe
						? Math.max(3, ((net.minecraftforge.common.crafting.IShapedRecipe) irecipe).getRecipeHeight())
						: i;
				int k = irecipe instanceof net.minecraftforge.common.crafting.IShapedRecipe
						? ((net.minecraftforge.common.crafting.IShapedRecipe) irecipe).getRecipeWidth()
						: i;
				int l = 1;

				int qualityItems = 0;
				List<ItemStack> items = new ArrayList<ItemStack>();
				for (int m = 0; m < 25; m++) {
					ItemStack stack = ItemStack.EMPTY;
					if (list.size() > m) {
						Ingredient ingredient = list.get(m);
						if (ingredient.getMatchingStacks().length > 0) {
							stack = ingredient.getMatchingStacks()[0].copy();
							if (stack.getItem() instanceof IQuality) {
								qualityItems++;
							}
						}
					}
					items.add(stack);
				}

				int originalQuality = -1;
				if (itemstack.getItem() instanceof IQuality) {
					IQuality quality = (IQuality) itemstack.getItem();
					originalQuality = quality.getQuality(itemstack);
				}

				for (int m = 0; m < 25; m++) {
					ItemStack stack = items.get(m);
					if (originalQuality > 0 && stack.getItem() instanceof IQuality) {
						IQuality quality = (IQuality) stack.getItem();
						quality.setQuality(stack,
								Math.max(1, (int) (originalQuality * 0.75f - world.rand.nextInt(10))));
					}

					output.setInventorySlotContents(m, stack);
					entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, m + 1, stack));
				}

			}
		}
	}

	public static boolean isUncraftable(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		Item item = stack.getItem();
		if (item instanceof ItemGun) {
			return true;
		} else if (item instanceof ItemBlock) {
			ItemBlock itemBlock = (ItemBlock) item;
			Block block = itemBlock.getBlock();
			if (block instanceof BlockGenerator) {
				return true;
			}
			if (block instanceof BlockWorkbench) {
				return true;
			}
			if (block instanceof BlockCombustionGenerator) {
				return true;
			}
			if (block instanceof BlockChemistryStation) {
				return true;
			}
		} else if (item instanceof ItemBullet) {
			return true;
		} else if (item instanceof ItemFuelTool) {
			return true;
		}
		return false;
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

	@Override
	public void putStackInSlot(int slotID, ItemStack stack) {
		//
		this.getSlot(slotID).putStack(stack);
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		return super.slotClick(slotId, dragType, clickTypeIn, player);
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
				// itemstack1.getItem().onCreated(itemstack1, this.worldObj, playerIn);

				if (!this.mergeItemStack(itemstack1, 27, 63, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 27 && index < 63) {
				if (itemstack1.getItem() == ModItems.IRON_SCRAP) {
					if (!this.mergeItemStack(itemstack1, 26, 27, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}

			} else if (index == 26) {
				if (!this.mergeItemStack(itemstack1, 27, 63, true)) {
					return ItemStack.EMPTY;
				}
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