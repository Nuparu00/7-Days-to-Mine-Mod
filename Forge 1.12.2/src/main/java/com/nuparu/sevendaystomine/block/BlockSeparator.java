package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.tileentity.TileEntityBackpack;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSeparator extends BlockHorizontalBase {
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.125F, 0.0F, 0.3125F, 0.875F, 0.46875F, 0.75);
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.125F, 0.0F, 0.25F, 0.875F, 0.46875F, 0.6875F);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.3125F, 0.0F, 0.125F, 0.75F, 0.46875F, 0.875F);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.25F, 0.0F, 0.125F, 0.6875F, 0.46875F, 0.875F);

	public BlockSeparator() {
		super(Material.GLASS);
		this.setSoundType(SoundType.GLASS);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setHardness(2F);
		this.setResistance(3.0F);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(BlockHorizontalBase.FACING)) {
		default:
		case NORTH:
			return AABB_WEST;
		case SOUTH:
			return AABB_EAST;
		case WEST:
			return AABB_NORTH;
		case EAST:
			return AABB_SOUTH;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)) {
		default:
		case NORTH:
			return AABB_WEST;
		case SOUTH:
			return AABB_EAST;
		case WEST:
			return AABB_NORTH;
		case EAST:
			return AABB_SOUTH;
		}
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking())
			return true;

		//playerIn.openGui(SevenDaysToMine.instance, 11, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

}
