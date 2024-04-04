package nuparu.sevendaystomine.tileentity;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.crafting.separator.ISeparatorRecipe;
import nuparu.sevendaystomine.crafting.separator.SeparatorRecipeManager;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.inventory.container.ContainerSeparator;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntitySeparator extends TileEntityItemHandler<ItemHandlerNameable> implements ITickable, IVoltage {

	private static final int INVENTORY_SIZE = 3;
	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.separator");
	private long voltage = 0;
	private long capacity = 200;
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
		if(this.voltage < this.getRequiredPower()) return false;
		
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
			this.voltage -= this.getRequiredPower();
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

	public void markForUpdate() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		this.markDirty();
	}
	
	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.CONSUMER;
	}
	
	@Override
	public int getMaximalInputs() {
		return 0;
	}

	@Override
	public int getMaximalOutputs() {
		return 0;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return null;
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return null;
	}

	@Override
	public long getOutput() {
		return 0;
	}

	@Override
	public long getMaxOutput() {
		return 0;
	}

	@Override
	public long getOutputForConnection(ElectricConnection connection) {
		return 0;
	}

	@Override
	public boolean tryToConnect(ElectricConnection connection) {
		return false;
	}

	@Override
	public boolean canConnect(ElectricConnection connection) {
		return false;
	}

	@Override
	public long getRequiredPower() {
		return 20;
	}

	@Override
	public long getCapacity() {
		return this.capacity;
	}

	@Override
	public long getVoltageStored() {
		return this.voltage;
	}

	@Override
	public void storePower(long power) {
		this.voltage += power;
		if (this.voltage > this.getCapacity()) {
			this.voltage = this.getCapacity();
		}
		if (this.voltage < 0) {
			this.voltage = 0;
		}
	}

	@Override
	public long tryToSendPower(long power, ElectricConnection connection) {
		long canBeAdded = capacity - voltage;
		long delta = Math.min(canBeAdded, power);
		long lost = 0;
		if (connection != null) {
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;

		return delta;
	}

	@Override
	public Vec3d getWireOffset() {
		return null;
	}

	@Override
	public boolean isPassive() {
		return true;
	}
	
	@Override
	public boolean disconnect(IVoltage voltage) {
		return false;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		long toAdd = Math.min(this.capacity-this.voltage, maxReceive);
		if(!simulate) {
			this.voltage+=toAdd;
		}
		return (int)toAdd;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		long toExtract = Math.min(this.voltage, maxExtract);
		if(!simulate) {
			this.voltage-=toExtract;
		}
		return (int)toExtract;
	}

	@Override
	public int getEnergyStored() {
		return (int) this.voltage;
	}

	@Override
	public int getMaxEnergyStored() {
		return (int) this.capacity;
	}

	@Override
	public boolean canExtract() {
		return this.capacity > 0;
	}

	@Override
	public boolean canReceive() {
		return this.voltage < this.capacity;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this);
		}

		return super.getCapability(capability, facing);
	}
	
}
