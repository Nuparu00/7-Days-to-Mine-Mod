package com.nuparu.sevendaystomine.tileentity;

import java.util.List;

import com.nuparu.sevendaystomine.inventory.container.ContainerSeparator;
import com.nuparu.sevendaystomine.inventory.container.ContainerSmall;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import com.nuparu.sevendaystomine.item.crafting.separator.SeparatorRecipeManager;
import com.nuparu.sevendaystomine.item.crafting.separator.ISeparatorRecipe;
import com.nuparu.sevendaystomine.tileentity.TileEntityChemistryStation.EnumSlots;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileEntitySeparator extends TileEntityItemHandler<ItemHandlerNameable> implements ITickable {

	private static final int INVENTORY_SIZE = 3;
	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.separator");

	private int cookTime;
	private int totalCookTime;

	public ISeparatorRecipe currentRecipe = null;

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
	}

	@Override
	public ContainerSeparator createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerSeparator(playerInventoryWrapper, inventory, player, this);
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {

	}

	@Override
	public void onContainerClosed(EntityPlayer player) {

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
	}

	public void setDisplayName(String displayName) {
		inventory.setDisplayName(new TextComponentString(displayName));
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		boolean flag1 = false;

			if (!getInput().isEmpty()) {
				if (this.canSmelt()) {
					++this.cookTime;
					if(this.totalCookTime == 0) {
						this.totalCookTime = this.getCookTime(null);
					}
					else if (this.cookTime >= this.totalCookTime) {
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

		if (flag1) {
			this.markDirty();
		}

		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		markDirty();
	}

	private boolean canSmelt() {
		ISeparatorRecipe recipeToUse = null;
		for (ISeparatorRecipe recipe : SeparatorRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				recipeToUse = recipe;
				break;
			}
		}
		if(recipeToUse == null)
			return false;
		if (getInput().isEmpty())
			return false;
		if (!getOutputLeft().isEmpty() && getOutputLeft().getCount() >= 64)
			return false;
		if (!getOutputRight().isEmpty() && getOutputRight().getCount() >= 64)
			return false;
		for (ISeparatorRecipe recipe : SeparatorRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				List<ItemStack> output = recipe.getOutputs(this);
				if (!getOutputLeft().isEmpty() && !ItemStack.areItemsEqual(getOutputLeft(), output.get(0))) {
					continue;
				}
				if (!getOutputRight().isEmpty() && !ItemStack.areItemsEqual(getOutputRight(), output.get(1))) {
					continue;
				}
				if (getOutputLeft().getCount() + output.get(0).getCount() > 64) {
					continue;
				}
				if (getOutputRight().getCount() + output.get(1).getCount() > 64) {
					continue;
				}
				
				currentRecipe = recipe;
				return true;
			}
		}
		return false;
	}

	public void smeltItem() {

		ISeparatorRecipe recipeToUse = null;
		for (ISeparatorRecipe recipe : SeparatorRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				recipeToUse = recipe;
				break;
			}
		}
		if (recipeToUse != null) {

			ItemStack currentOutput = getOutputLeft();
			if (currentOutput.isEmpty()) {
				inventory.setStackInSlot(1, recipeToUse.getOutputs(this).get(0).copy());

			} else {
				if (ItemStack.areItemsEqual(currentOutput, recipeToUse.getOutputs(this).get(0)) && currentOutput.getCount()
						+ recipeToUse.getOutputs(this).get(0).getCount() <= 64) {
					currentOutput.grow(recipeToUse.getOutputs(this).get(0).getCount());
					inventory.setStackInSlot(1, currentOutput);
				}
			}
			
			currentOutput = getOutputRight();
			if (currentOutput.isEmpty()) {
				inventory.setStackInSlot(2, recipeToUse.getOutputs(this).get(1).copy());

			} else {
				if (ItemStack.areItemsEqual(currentOutput, recipeToUse.getOutputs(this).get(1)) && currentOutput.getCount()
						+ recipeToUse.getOutputs(this).get(1).getCount() <= 64) {

					currentOutput.grow(recipeToUse.getOutputs(this).get(1).getCount());
					inventory.setStackInSlot(2, currentOutput);
				}
			}
			consumeInput(recipeToUse);

		}
	}

	public void consumeInput(ISeparatorRecipe recipe) {
		recipe.consumeInput(this);
	}

	public boolean isBurning() {
		return true;
	}
	
	public int getCookTime(ItemStack stack) {
		return 300;
	}
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}
	
	public ItemStack getInput() {
		return inventory.getStackInSlot(0);
	}
	
	public ItemStack getOutputLeft() {
		return inventory.getStackInSlot(1);
	}
	
	public ItemStack getOutputRight() {
		return inventory.getStackInSlot(2);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
		return compound;
	}
	
	public int getCookTime() {
		return cookTime;
	}
	
	public int getTotalCookTime() {
		return totalCookTime;
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net,
			net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 1);
	}

}
