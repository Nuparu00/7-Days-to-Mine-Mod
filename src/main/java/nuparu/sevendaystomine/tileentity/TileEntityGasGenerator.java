package nuparu.sevendaystomine.tileentity;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModFluids;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.container.ContainerGenerator;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import nuparu.sevendaystomine.util.MathUtils;

public class TileEntityGasGenerator extends TileEntityGeneratorBase{
	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.generator.gas");

	protected FluidTank tank = new FluidTank(ModFluids.GASOLINE, 0, 4000);


	protected int timer;
	protected boolean isEmpty = true;


	public TileEntityGasGenerator() {
		super();
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(1, DEFAULT_NAME);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		timer = compound.getInteger("timer");
		isEmpty = compound.getBoolean("isEmpty");
		tank.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
	
		compound.setBoolean("isEmpty", isEmpty);
		compound.setInteger("timer", timer);
		if (tank != null) {
			tank.writeToNBT(compound);
		}
		return compound;
	}

	@Override
	public void update() {

		boolean flag = false;

		int lava = countAdjacentMats(Material.LAVA);
		int air = countAdjacentBlocks(Blocks.AIR);
		int water = countAdjacentMats(Material.WATER);
		int ice = countAdjacentMats(Material.ICE);
		int packedIce = countAdjacentMats(Material.PACKED_ICE);
		int snow = countAdjacentMats(Material.SNOW);
		int fire = countAdjacentMats(Material.FIRE);
		
		int hot = fire + lava * 2;
		int cold = snow + packedIce * 2 + ice * 2 + water;
		temperatureLimit = (float) (0.35 + (hot * 0.65) - (cold * 0.65));

		if (this.isBurning && tank.getFluid() != null && tank.getFluidAmount() > 0) {

			tank.drain(1, true);
			if (this.voltage < this.capacity) {
				this.storePower(getPowerPerUpdate());
			}
			if (temperature < temperatureLimit) {

				temperature += (0.0002 * hot);
			}
			if(!world.isRemote && ++soundCounter >= 90) {
				world.playSound(null, pos, SoundHelper.MINIBIKE_IDLE, SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.95f, 1.05f), MathUtils.getFloatInRange(0.95f, 1.05f));
				soundCounter = 0;
			}

			flag = true;
		}
		if (temperature > 0) {
			temperature -= (0.00001 * ((2 * cold) + air));
		}

		if (temperature > 1) {
			this.world.createExplosion((Entity) null, pos.getX(), pos.getY(), pos.getZ(), 2, true);
		}
		if (tank.getFluidAmount() < tank.getCapacity()) {
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item instanceof UniversalBucket) {
					UniversalBucket bucket = (UniversalBucket) item;
					FluidStack fluidStack = bucket.getFluid(stack);
					if (fluidStack.getFluid() == ModFluids.GASOLINE) {
						if (fluidStack.amount <= tank.getCapacity() - tank.getFluidAmount()) {
							tank.fill(fluidStack, true);
							this.inventory.setStackInSlot(0, new ItemStack(Items.BUCKET));
						}
					}
				}
				else if (item == ModItems.GAS_CANISTER) {
					if (250 <= tank.getCapacity() - tank.getFluidAmount()) {
						tank.fill(new FluidStack(ModFluids.GASOLINE,250), true);
						stack.shrink(1);
						if(stack.getCount() <= 0) {
							this.inventory.setStackInSlot(0, ItemStack.EMPTY);
						}
					}
				}
			}
		}

		if (this.isBurning && tank.getFluidAmount() <= 0) {
			isBurning = false;
			tank.setFluid(new FluidStack(ModFluids.GASOLINE, 0));
		}
		if (temperature < 0) {
			temperature = 0;
		}

		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(world);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection),connection);
				this.voltage -= l;
				if (l != 0) {
					flag = true;
				}
			} else {
				iterator.remove();
				flag = true;
			}
		}

		iterator = inputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getFrom(world);
			if (voltage == null) {
				iterator.remove();
				flag = true;
			}
		}

		if (flag) {
			this.markDirty();
		}
	}


	public float getFluidPercentage() {
		return (float) getTank().getFluidAmount() / (float) getTank().getCapacity();
	}

	public int getFluidGuiHeight(int maxHeight) {
		return (int) Math.ceil(getFluidPercentage() * (float) maxHeight);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) getTank();
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this);
		}
		return super.getCapability(capability, facing);
	}

	public FluidTank getTank() {
		return tank;
	}

	public void setTank(FluidTank tank) {
		this.tank = tank;
	}
	
	public boolean isBurning() {
		return this.burnTime > 0;
	}
	
	@Override
	public ContainerGenerator createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory, playerInventory);

		return new ContainerGenerator(playerInventoryWrapper, inventory,player, this);
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {
		
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}
	
	@Override
	public boolean disconnect(IVoltage voltage) {
		for(ElectricConnection input : getInputs()) {
			if(input.getFrom().equals(voltage.getPos())) {
				this.inputs.remove(input);
				markDirty();
				world.markBlockRangeForRenderUpdate(pos, pos);
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
				world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
				return true;
			}
		}
		
		for(ElectricConnection output : getOutputs()) {
			if(output.getTo().equals(voltage.getPos())) {
				this.outputs.remove(output);
				markDirty();
				world.markBlockRangeForRenderUpdate(pos, pos);
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
				world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
				return true;
			}
		}
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
}
