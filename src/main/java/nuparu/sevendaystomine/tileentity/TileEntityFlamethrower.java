package nuparu.sevendaystomine.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.entity.EntityFlame;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModFluids;
import nuparu.sevendaystomine.inventory.container.ContainerFlamethrower;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;

public class TileEntityFlamethrower extends TileEntityItemHandler<ItemHandlerNameable> implements ITickable {

	public static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.flamethrower");
	protected static final int MAX_VOLUME = 4000;
	
	protected FluidTank tank =  new FluidTank(ModFluids.GASOLINE, 0, Fluid.BUCKET_VOLUME);

	EnumFacing facing;

	public TileEntityFlamethrower() {

	}

	@Override
	public void onContainerOpened(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		if (facing == null) {
			facing = world.getBlockState(pos).getValue(BlockHorizontalBase.FACING);
			if (facing == null) {
				return;
			}
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
							this.markDirty();
							world.updateComparatorOutputLevel(pos, ModBlocks.FLAMETHOWER);
						}
					}
				}
			}
		}

		boolean powered = world.isBlockPowered(pos);
		if (powered && tank.getFluidAmount() > 0) {
			double x = pos.getX() + 0.5;
			double y = pos.getY() + 13 / 16d;
			double z = pos.getZ() + 0.5;

			switch (facing) {
			case NORTH:
				z -= 0.51;
				break;
			case SOUTH:
				z += 0.51;
				break;
			case WEST:
				x -= 0.51;
				break;
			case EAST:
				x += 0.51;
				break;
			}

			Vec3i vec = facing.getDirectionVec();
			float yaw = getYaw(vec) + 90;

			float pitch = 0f;
			for (int i = 0; i < 2; i++) {
				EntityFlame entity = new EntityFlame(world, x, y, z, yaw, pitch, 0.1f, 20f);
				if (!world.isRemote) {
					world.spawnEntity(entity);
				}
			}
			tank.drain(1, true);
			world.updateComparatorOutputLevel(pos, ModBlocks.FLAMETHOWER);
		}
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(1, DEFAULT_NAME);
	}

	@Override
	public Container createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerFlamethrower(playerInventoryWrapper, inventory, player, this);
	}

	public static float getYaw(Vec3i vec) {
		double deltaX = vec.getX();
		double deltaZ = vec.getZ();
		double yaw = 0;
		if (deltaX != 0) {
			if (deltaX < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(deltaZ / deltaX);
		} else if (deltaZ < 0) {
			yaw = Math.PI;
		}
		return (float) (-yaw * 180 / Math.PI - 90);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		tank.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (tank != null) {
			tank.writeToNBT(tag);
		}
		return tag;
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

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		return super.getCapability(capability, facing);
	}
	
	public FluidTank getTank() {
		return this.tank;
	}
	
	public int getFluidGuiHeight(int maxHeight) {
		return (int) Math.ceil(getFluidPercentage() * (float) maxHeight);
	}
	
	public float getFluidPercentage() {
		return (float) getTank().getFluidAmount() / (float) getTank().getCapacity();
	}

}
