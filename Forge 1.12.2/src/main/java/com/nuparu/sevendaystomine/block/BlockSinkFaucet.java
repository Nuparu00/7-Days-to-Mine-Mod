package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSinkFaucet extends BlockHorizontalBase implements IScrapable {

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 3;

	public BlockSinkFaucet() {
		super(Material.ROCK);
		this.setSoundType(SoundType.METAL);
		this.setHardness(1f);
		this.setResistance(2f);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.0F, 0.75F, 0.6875F, 0.3F);
		case WEST:
			return new AxisAlignedBB(0.7F, 0.375F, 0.25F, 1F, 0.6875F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.375F, 0.25F, 0.3F, 0.6875F, 0.75F);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.0F, 0.75F, 0.6875F, 0.3F);
		case WEST:
			return new AxisAlignedBB(0.7F, 0.375F, 0.25F, 1F, 0.6875F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.375F, 0.25F, 0.3F, 0.6875F, 0.75F);
		}
	}

	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING,
				facing);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		 EnumFacing enumfacing = (EnumFacing)state.getValue(BlockHorizontalBase.FACING);

	        if (!world.getBlockState(pos.offset(enumfacing.getOpposite())).getMaterial().isSolid())
	        {
	            this.dropBlockAsItem(world, pos, state, 0);
	            world.setBlockToAir(pos);
	        }

	        super.neighborChanged(state, world, pos, blockIn, fromPos);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
		return (side != EnumFacing.DOWN && side != EnumFacing.UP) && worldIn.getBlockState(pos.offset(side.getOpposite())).getMaterial().isSolid();
    }

}
