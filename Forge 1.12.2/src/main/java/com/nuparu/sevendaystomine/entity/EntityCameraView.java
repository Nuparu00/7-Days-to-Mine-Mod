package com.nuparu.sevendaystomine.entity;

import com.nuparu.sevendaystomine.block.BlockHorizontalBase;
import com.nuparu.sevendaystomine.tileentity.TileEntityCamera;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCameraView extends Entity {

	private IBlockState blockState = null;
	private BlockPos blockPos = BlockPos.ORIGIN;

	public TileEntityCamera te;

	public float initRotation;
	public int direction = 1;

	public EntityCameraView(World worldIn) {
		super(worldIn);
		this.noClip = true;
		this.height = 0.1F;
		this.width = 0.1F;
	}

	public EntityCameraView(World worldIn, double x, double y, double z) {
		this(worldIn);
		setPosition(x + 0.5D, y + 0.1D, z + 0.5D);
		blockPos = new BlockPos(x, y, z);
		blockState = this.world.getBlockState(blockPos);
		switch (blockState.getValue(BlockHorizontalBase.FACING)) {
		default:
		case SOUTH:
			initRotation = 0;
			direction = 1;
			break;
		case NORTH:
			initRotation = 180;
			direction = 1;
			break;
		case EAST:
			initRotation = 270;
			direction = 1;
			break;
		case WEST:
			initRotation = 90;
			direction = 1;
			break;
		}
		TileEntity tile = world.getTileEntity(blockPos);
		if (tile instanceof TileEntityCamera) {
			this.te = (TileEntityCamera) tile;
		} else if(!world.isRemote){
			setDead();
		}
	}

	public EntityCameraView(World worldIn, double x, double y, double z, TileEntityCamera te) {
		this(worldIn);
		if (te == null && !world.isRemote){
			setDead();
			return;
		}
		setPosition(x + 0.5D, y + 0.1D, z + 0.5D);
		blockPos = new BlockPos(x, y, z);
		blockState = this.world.getBlockState(blockPos);
		this.te = te;

		switch (blockState.getValue(BlockHorizontalBase.FACING)) {
		default:
		case SOUTH:
			initRotation = 0;
			direction = 1;
			break;
		case NORTH:
			initRotation = 180;
			direction = 1;
			break;
		case EAST:
			initRotation = 270;
			direction = 1;
			break;
		case WEST:
			initRotation = 90;
			direction = 1;
			break;
		}

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		// System.out.println(System.currentTimeMillis() / 1000d);



	}

	@Override
	public void onEntityUpdate() {
		if (!this.world.isRemote) {
			if (this.world.getBlockState(blockPos) != blockState) {
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
