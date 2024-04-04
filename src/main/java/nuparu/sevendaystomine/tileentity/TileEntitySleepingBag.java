package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.block.BlockSleepingBag;
import nuparu.sevendaystomine.init.ModItems;

public class TileEntitySleepingBag extends TileEntity {
	private EnumDyeColor color = EnumDyeColor.RED;

	public void setItemValues(ItemStack p_193051_1_) {
		this.setColor(EnumDyeColor.byMetadata(p_193051_1_.getMetadata()));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey("color")) {
			this.color = EnumDyeColor.byMetadata(compound.getInteger("color"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("color", this.color.getMetadata());
		return compound;
	}

	/**
	 * Get an NBT compound to sync to the client with SPacketChunkData, used for
	 * initial loading of the chunk or when many blocks change at once. This
	 * compound comes back to you clientside in {@link handleUpdateTag}
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	/**
	 * Retrieves packet to send to the client whenever this Tile Entity is resynced
	 * via World.notifyBlockUpdate. For modded TE's, this packet comes back to you
	 * clientside in {@link #onDataPacket}
	 */
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

	public EnumDyeColor getColor() {
		return this.color;
	}

	public void setColor(EnumDyeColor color) {
		this.color = color;
		world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 1);
		this.markDirty();
	}

	@SideOnly(Side.CLIENT)
	public boolean isHeadPiece() {
		return BlockBed.isHeadPiece(this.getBlockMetadata());
	}

	public ItemStack getItemStack() {
		return new ItemStack(ModItems.SLEEPING_BAG, 1, this.color.getMetadata());
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock())
				|| (oldState.getValue(BlockSleepingBag.PART) != newSate.getValue(BlockSleepingBag.PART));
	}
}