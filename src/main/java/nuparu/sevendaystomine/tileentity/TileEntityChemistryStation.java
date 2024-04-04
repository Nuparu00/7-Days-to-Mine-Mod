package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeManager;
import nuparu.sevendaystomine.crafting.chemistry.IChemistryRecipe;
import nuparu.sevendaystomine.inventory.ContainerChemistryStation;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import nuparu.sevendaystomine.util.Utils;

public class TileEntityChemistryStation extends TileEntity implements IContainerCallbacks, ILootContainer, ITickable {

	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.chemistry");
	
	public enum EnumSlots {
		INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT, FUEL_SLOT
	}

	private int burnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	private IChemistryRecipe currentRecipe = null;
	
	private final ItemHandlerNameable HANDLER_INPUT = new ItemHandlerNameable(4, DEFAULT_NAME);
	private final ItemHandlerNameable HANDLER_OUTPUT = new ItemHandlerNameable(1, DEFAULT_NAME);
	private final ItemHandlerNameable HANDLER_FUEL = new ItemHandlerNameable(1, DEFAULT_NAME){
		@Override
	    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	    {
	        return !stack.isEmpty();
	    }
	};

	public TileEntityChemistryStation() {

	}

	@Override
	public void update() {
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}
		if (!this.world.isRemote) {
			ItemStack itemstack = this.getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal());
			if (this.isBurning() || hasFuel() && !isInputEmpty()) {
				if (!this.isBurning() && this.canSmelt()) {
					this.burnTime = Utils.getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.burnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.getInventory().setStackInSlot(EnumSlots.FUEL_SLOT.ordinal(), item1);
							}
						}
					}
					this.totalCookTime = this.getCookTime(null);
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
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.notifyBlockUpdate(pos, getState(), getState(), 3);
			world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
			markDirty();
		}



	}
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}
	
	private boolean canSmelt() {
		if (!getOutputSlot().isEmpty() && getOutputSlot().getCount() > 64)
			return false;
		if (isInputEmpty() || !hasFuel())
			return false;
		for (IChemistryRecipe recipe : ChemistryRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				ItemStack output = recipe.getOutput(this);
				if (!getOutputSlot().isEmpty() && !ItemStack.areItemsEqual(getOutputSlot(), output)) {
					continue;
				}
				if (getOutputSlot().getCount() + output.getCount() <= 64) {
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
				getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), recipeToUse.getOutput(this));

			} else {
				if (ItemStack.areItemsEqual(currentOutput, recipeToUse.getOutput(this)) && currentOutput.getCount()
						+ recipeToUse.getOutput(this).getCount() <= 64) {

					currentOutput.grow(recipeToUse.getOutput(this).getCount());
					getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
				}
			}
			consumeInput(recipeToUse);

		}
	}

	public void consumeInput(IChemistryRecipe recipe) {
		recipe.consumeInput(this);
	}

	public boolean isInputEmpty() {
		return (getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()).isEmpty());
	}

	public boolean hasFuel() {
		return !getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal()).isEmpty();
	}

	public ItemStack getOutputSlot() {
		return getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal());
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.burnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentItemBurnTime = Utils.getItemBurnTime(this.getInventory().getStackInSlot(1));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short) this.burnTime);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);

		return compound;
	}

	public int getCookTime(ItemStack stack) {
		return 600;
	}

	public List<ItemStack> getActiveInventory() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()));
		return list;
	}

	public ItemStack[][] getActiveInventoryAsArray() {
		ItemStack[][] array = new ItemStack[2][2];

		array[0][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal());
		array[0][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal());
		array[1][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal());
		array[1][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal());

		return array;
	}

	public IChemistryRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(TileEntityChemistryStation inventory) {
		return inventory.getField(0) > 0;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	public static boolean isItemFuel(ItemStack stack) {
		return Utils.getItemBurnTime(stack) > 0;
	}

	public CombinedInvWrapper getInventory() {
		return new CombinedInvWrapper(this.HANDLER_INPUT, this.HANDLER_OUTPUT, this.HANDLER_FUEL);
	}
	
	public NonNullList<ItemStack> getDrops() {
		return Utils.dropItemHandlerContents(getInventory(), getWorld().rand);
	}

	public void setDisplayName(String displayName) {
		HANDLER_INPUT.setDisplayName(new TextComponentString(displayName));
	}
	
	public ITextComponent getDisplayName() {
		return HANDLER_INPUT.getDisplayName();
	}

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

	public int getFieldCount() {
		return 4;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			switch(facing) {
			case UP : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(HANDLER_INPUT);
			case DOWN : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(HANDLER_OUTPUT);
			case NORTH :
			case WEST :
			case SOUTH :
			case EAST : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(HANDLER_FUEL);
			}
		}

		return null;
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
	}
	
	public ContainerChemistryStation createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerChemistryStation(playerInventoryWrapper, getInventory(), this, player);
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

}
