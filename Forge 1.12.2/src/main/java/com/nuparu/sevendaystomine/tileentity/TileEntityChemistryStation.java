package com.nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockChemistryStation;
import com.nuparu.sevendaystomine.inventory.ContainerChemistryStation;
import com.nuparu.sevendaystomine.item.crafting.chemistry.ChemistryRecipeManager;
import com.nuparu.sevendaystomine.item.crafting.chemistry.IChemistryRecipe;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityChemistryStation extends TileEntityLockableLoot implements ITickable, ISidedInventory {

	public enum EnumSlots {
		INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT, FUEL_SLOT
	}

	private static final int[] slotsTop = new int[] { EnumSlots.INPUT_SLOT.ordinal(), EnumSlots.INPUT_SLOT2.ordinal(),
			EnumSlots.INPUT_SLOT3.ordinal(), EnumSlots.INPUT_SLOT4.ordinal() };
	private static final int[] slotsBottom = new int[] { EnumSlots.OUTPUT_SLOT.ordinal() };
	private static final int[] slotsSides = new int[] { EnumSlots.FUEL_SLOT.ordinal() };

	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
	private int burnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;
	private String customName;

	private IChemistryRecipe currentRecipe = null;

	public TileEntityChemistryStation() {

	}

	@Override
	public void update() {
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}
		if (!this.world.isRemote) {
			ItemStack itemstack = this.inventory.get(EnumSlots.FUEL_SLOT.ordinal());
			if (this.isBurning() || hasFuel() && !isInputEmpty()) {
				if (!this.isBurning() && this.canSmelt()) {
					this.burnTime = getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.burnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.inventory.set(EnumSlots.FUEL_SLOT.ordinal(), item1);
							}
						}
						this.totalCookTime = this.getCookTime(null);
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(null);
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}
		}

		if (flag1) {
			this.markDirty();
		}

		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		markDirty();

	}

	private boolean canSmelt() {
		if (!getOutputSlot().isEmpty() && getOutputSlot().getCount() > this.getInventoryStackLimit())
			return false;
		if (isInputEmpty() || !hasFuel())
			return false;
		for (IChemistryRecipe recipe : ChemistryRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				ItemStack output = recipe.getOutput(this);
				if (!getOutputSlot().isEmpty() && !ItemStack.areItemsEqual(getOutputSlot(), output)) {
					continue;
				}
				if (getOutputSlot().getCount() + output.getCount() <= this.getInventoryStackLimit()) {
					currentRecipe = recipe;
					return true;
				}
			}
		}
		return false;
	}

	public void smeltItem() {

		IChemistryRecipe recipeToUse = null;
		for (IChemistryRecipe recipe : ChemistryRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				recipeToUse = recipe;
				break;
			}
		}
		if (recipeToUse != null) {

			ItemStack currentOutput = getOutputSlot();
			if (currentOutput.isEmpty()) {
				setInventorySlotContents(EnumSlots.OUTPUT_SLOT.ordinal(), recipeToUse.getOutput(this));

			} else {
				if (ItemStack.areItemsEqual(currentOutput, recipeToUse.getOutput(this)) && currentOutput.getCount()
						+ recipeToUse.getOutput(this).getCount() <= getInventoryStackLimit()) {

					currentOutput.grow(recipeToUse.getOutput(this).getCount());
					setInventorySlotContents(EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
				}
			}
			consumeInput(recipeToUse);

		}
	}

	public void consumeInput(IChemistryRecipe recipe) {
		recipe.consumeInput(this);
	}

	public boolean isInputEmpty() {
		return (getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()).isEmpty()
				&& getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()).isEmpty()
				&& getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()).isEmpty()
				&& getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()).isEmpty());
	}

	public boolean hasFuel() {
		return !getStackInSlot(EnumSlots.FUEL_SLOT.ordinal()).isEmpty();
	}

	public ItemStack getOutputSlot() {
		return getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal());
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);
		this.burnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentItemBurnTime = getItemBurnTime(this.inventory.get(1));

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short) this.burnTime);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
		ItemStackHelper.saveAllItems(compound, this.inventory);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		}

		return compound;
	}

	public int getCookTime(ItemStack stack) {
		return 600;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	public List<ItemStack> getActiveInventory() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()));
		list.add(getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()));
		list.add(getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()));
		list.add(getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()));
		return list;
	}

	public ItemStack[][] getActiveInventoryAsArray() {
		ItemStack[][] array = new ItemStack[2][2];

		array[0][0] = getStackInSlot(EnumSlots.INPUT_SLOT.ordinal());
		array[0][1] = getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal());
		array[1][0] = getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal());
		array[1][1] = getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal());

		return array;
	}

	public IChemistryRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.inventory) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.inventory.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.inventory.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return (index != EnumSlots.OUTPUT_SLOT.ordinal());
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.burnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.chemistry";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerChemistryStation(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return SevenDaysToMine.MODID + ":chemistry";
	}

	private IBlockState getState() {
		return world.getBlockState(pos);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}

		}

		return true;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
			if (burnTime >= 0)
				return burnTime;
			Item item = stack.getItem();

			if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) {
				return 150;
			} else if (item == Item.getItemFromBlock(Blocks.WOOL)) {
				return 100;
			} else if (item == Item.getItemFromBlock(Blocks.CARPET)) {
				return 67;
			} else if (item == Item.getItemFromBlock(Blocks.LADDER)) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) {
				return 100;
			} else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
				return 16000;
			} else if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) {
				return 200;
			} else if (item == Items.STICK) {
				return 100;
			} else if (item != Items.BOW && item != Items.FISHING_ROD) {
				if (item == Items.SIGN) {
					return 200;
				} else if (item == Items.COAL) {
					return 1600;
				} else if (item == Items.LAVA_BUCKET) {
					return 20000;
				} else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL) {
					if (item == Items.BLAZE_ROD) {
						return 2400;
					} else if (item instanceof ItemDoor && item != Items.IRON_DOOR) {
						return 200;
					} else {
						return item instanceof ItemBoat ? 400 : 0;
					}
				} else {
					return 100;
				}
			} else {
				return 300;
			}
		}
	}

	public static boolean isItemFuel(ItemStack stack) {
		return getItemBurnTime(stack) > 0;
	}

}
