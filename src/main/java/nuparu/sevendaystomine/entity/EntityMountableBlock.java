package nuparu.sevendaystomine.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMountableBlock extends Entity {

	private IBlockState blockState = null;
	private BlockPos blockPos = BlockPos.ORIGIN;

	public EntityMountableBlock(World worldIn) {
		super(worldIn);
		this.noClip = true;
		this.height = 0.01F;
		this.width = 0.01F;
	}

	public EntityMountableBlock(World worldIn, double x, double y, double z) {
		this(worldIn);
		setPosition(x + 0.5D, y + 0.1D, z + 0.5D);
		blockPos = new BlockPos(x, y, z);
		blockState = this.world.getBlockState(blockPos);
	}

	@SuppressWarnings("incomplete-switch")
	@Deprecated
	public EntityMountableBlock(World worldIn, int x, int y, int z, EnumFacing facing) {
		this(worldIn, x, y, z);
		switch (facing) {
		case NORTH:
			;
		case SOUTH:
			;
		case WEST:
			;
		case EAST:
			;
		}
	}

	@Override
	public void onEntityUpdate() {
		if (!this.world.isRemote) {
			if (!this.isBeingRidden() || this.world.getBlockState(blockPos) != blockState) {
				this.setDead();
			}
		}
	}

	@Override
	public double getMountedYOffset() {
		return this.height * 0.0D;
	}

	@Override
	protected boolean shouldSetPosAfterLoading() {
		return false;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

}
