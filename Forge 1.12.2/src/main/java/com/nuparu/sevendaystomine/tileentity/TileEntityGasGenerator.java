package com.nuparu.sevendaystomine.tileentity;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.init.ModFluids;
import com.nuparu.sevendaystomine.inventory.ContainerGenerator;
import com.nuparu.sevendaystomine.inventory.container.ContainerBatteryStation;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileEntityGasGenerator extends TileEntityGeneratorBase{
	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.generator.gas");

	protected FluidTank tank = new FluidTank(ModFluids.GASOLINE, 0, 4000);

	protected static final int MAX_VOLUME = 4000;

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
			flag = true;
		}
		if (temperature > 0) {
			temperature -= (0.00001 * ((2 * cold) + air));
		}

		if (temperature > 1) {
			this.world.createExplosion((Entity) null, pos.getX(), pos.getY(), pos.getZ(), 2, true);
		}
		if (tank.getFluidAmount() < MAX_VOLUME) {
			ItemStack stack = this.inventory.getStackInSlot(0);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item instanceof UniversalBucket) {
					UniversalBucket bucket = (UniversalBucket) item;
					FluidStack fluidStack = bucket.getFluid(stack);
					if (fluidStack.getFluid() == ModFluids.GASOLINE) {
						if (fluidStack.amount <= MAX_VOLUME - tank.getFluidAmount()) {
							tank.fill(fluidStack, true);
							this.inventory.setStackInSlot(0, new ItemStack(Items.BUCKET));
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
}