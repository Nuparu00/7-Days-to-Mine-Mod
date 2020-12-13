package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.tileentity.TileEntityCalendar;
import com.nuparu.sevendaystomine.tileentity.TileEntityToilet;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCalendar extends BlockTileProvider<TileEntityCalendar>{

	public BlockCalendar() {
		super(Material.GROUND);
		setHardness(0.05f);
		setResistance(0.05f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontalBase.FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCalendar();
	}

	@Override
	public TileEntityCalendar createTileEntity(World world, IBlockState state) {
		return new TileEntityCalendar();
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
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.F, 0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F,0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.0F, 0.75F, 1F, 0.0625F);
		case WEST:
			return new AxisAlignedBB(0.9375F, 0.4375F, 0.25F, 1F, 1F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.4375F, 0.25F, 0.0625F, 1F, 0.75F);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F,0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.0F, 0.75F, 1F, 0.0625F);
		case WEST:
			return new AxisAlignedBB(0.9375F, 0.4375F, 0.25F, 1F, 1F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.4375F, 0.25F, 0.0625F, 1F, 0.75F);
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}
	
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(BlockHorizontalBase.FACING,
				rot.rotate((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING,
				placer.getHorizontalFacing().getOpposite());
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING, EnumFacing.getHorizontal(meta));
	}

	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)).getHorizontalIndex();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontalBase.FACING });
	}

}