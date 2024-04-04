package nuparu.sevendaystomine.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.block.BlockCodeSafe;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.inventory.container.ContainerSmall;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import nuparu.sevendaystomine.item.ItemStethoscope;

public class TileEntityCodeSafe extends TileEntitySafe {

	private int correctCode = 000;
	private int selectedCode = 000;

	public TileEntityCodeSafe() {

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		correctCode = compound.getInteger("CorrectCode");
		selectedCode = compound.getInteger("SelectedCode");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("CorrectCode", correctCode);
		compound.setInteger("SelectedCode", selectedCode);
		return compound;
	}

	@Override
	public void update() {
		if (!world.isRemote) {
			super.update();
			if (!init) {
				while (correctCode == selectedCode && locked) {
					Random rand = world.rand;
					correctCode = rand.nextInt(1000);
					selectedCode = rand.nextInt(1000);
				}
				init = !init;
				markDirty();
			}
			tryToUnlock();
		}
	}

	public boolean tryToUnlock() {
		if (correctCode == selectedCode && locked) {
			unlock();
			return true;
		} else if (correctCode != selectedCode && !locked) {
			lock();
			return false;
		}
		return false;

	}

	public void unlock() {
		super.unlock();
		world.playSound(null, pos, SoundHelper.SAFE_UNLOCK, SoundCategory.BLOCKS, 0.9f + world.rand.nextFloat() / 4f,
				0.9f + world.rand.nextFloat() / 4f);
		locked = false;
		markDirty();
		BlockCodeSafe.setState(locked, world, pos);
	}

	public void lock() {
		locked = true;
		markDirty();
		BlockCodeSafe.setState(locked, world, pos);

	}

	public void setInit(boolean init) {
		this.init = init;
		markDirty();
	}

	public int getSelectedCode() {
		return this.selectedCode;
	}

	public int superSecretMethod() {
		return this.correctCode;
	}

	public void setSelectedCode(int code, @Nullable EntityPlayerMP player ) {
		this.selectedCode = code;
		boolean prevState = locked;
		tryToUnlock();
		if(player != null && prevState == true && !locked) {
			ModTriggers.SAFE_UNLOCK.trigger(player);
		}
		markDirty();
	}

	public void setCorrectCode(int code) {
		this.correctCode = code;
		markDirty();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		/*
		 * if (newState.getBlock() == Blocks.CODE_SAFE &&
		 * oldState.getProperties().get(BlockCodeSafe.FACING) ==
		 * newState.getProperties().get(BlockCodeSafe.FACING) ) { if(hasWorld()) {
		 * world.notifyBlockUpdate(pos, newState, newState, 3); } return false; }
		 */
		return true;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		nbtTag.removeTag("CorrectCode");
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTag);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		nbt.removeTag("CorrectCode");
		return nbt;
	}

	/*
	 * 0 = units, 2 = hundreds,...
	 */
	private int tryToGetDigit(EntityPlayer player, int digit) {
		if (!isUsableByPlayer(player))
			return -1;
		if (!(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemStethoscope))
			return -1;

		return (int) ((correctCode / Math.pow(10, digit)) % 10);
	}

	public int testDigit(EntityPlayer player, int guess, int numPos) {
		int digit = tryToGetDigit(player, numPos);
		return digit < guess ? -1 : (digit > guess ? 1 : 0);
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerSmall(playerInventoryWrapper, inventory, player, this);
	}

}
