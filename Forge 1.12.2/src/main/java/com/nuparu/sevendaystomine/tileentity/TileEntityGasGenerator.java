package com.nuparu.sevendaystomine.tileentity;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.init.ModFluids;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityGasGenerator extends TileEntityGeneratorBase{


	protected FluidTank tank = new FluidTank(ModFluids.GASOLINE, 0, 4000);

	protected static final int MAX_VOLUME = 4000;

	protected int timer;
	protected boolean isEmpty = true;


	public TileEntityGasGenerator() {
		super();
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
			flag = true;
		}
		if (temperature > 0) {
			temperature -= (0.00001 * ((2 * cold) + air));
		}

		if (temperature > 1) {
			this.world.createExplosion((Entity) null, pos.getX(), pos.getY(), pos.getZ(), 2, true);
		}
		if (tank.getFluidAmount() < MAX_VOLUME) {
			ItemStack stack = getStackInSlot(0);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item instanceof UniversalBucket) {
					UniversalBucket bucket = (UniversalBucket) item;
					FluidStack fluidStack = bucket.getFluid(stack);
					if (fluidStack.getFluid() == ModFluids.GASOLINE) {
						if (fluidStack.amount <= MAX_VOLUME - tank.getFluidAmount()) {
							tank.fill(fluidStack, true);
							this.setInventorySlotContents(0, new ItemStack(Items.BUCKET));
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


	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.generator.gas";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return SevenDaysToMine.MODID + ":generator.gas";
	}


	public float getFluidPercentage() {
		return (float) getTank().getFluidAmount() / (float) getTank().getCapacity();
	}

	public int getFluidGuiHeight(int maxHeight) {
		return (int) Math.ceil(getFluidPercentage() * (float) maxHeight);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) getTank();
		return super.getCapability(capability, facing);
	}

	public FluidTank getTank() {
		return tank;
	}

	public void setTank(FluidTank tank) {
		this.tank = tank;
	}
}
