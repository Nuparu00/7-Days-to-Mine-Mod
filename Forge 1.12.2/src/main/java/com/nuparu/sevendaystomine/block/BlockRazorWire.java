package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;
import com.nuparu.sevendaystomine.tileentity.TileEntityWoodenSpikes;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRazorWire extends BlockTileProvider<TileEntityWoodenSpikes> implements IScrapable {

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 6;

	public BlockRazorWire() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setHardness(2f);
		this.setResistance(2f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontalBase.FACING, EnumFacing.NORTH));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!(entityIn instanceof EntityLivingBase)) {
			return;
		}
		entityIn.setInWeb();
		if (entityIn instanceof EntityPlayer) {
			if (((EntityPlayer) entityIn).isCreative() || ((EntityPlayer) entityIn).isSpectator()) {
				return;
			}
		}
		entityIn.attackEntityFrom(DamageSource.GENERIC, 5);
		if (this == ModBlocks.WOODEN_SPIKES) {
			degradeBlock(pos, worldIn);
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityWoodenSpikes) {
			TileEntityWoodenSpikes tileEntity = (TileEntityWoodenSpikes) te;
			tileEntity.dealDamage(1);
		}
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return null;
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

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.down());
		return state.isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !worldIn.getBlockState(pos.up()).getMaterial().isLiquid();
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!this.canBlockStay(worldIn, pos)) {
			dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 1);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWoodenSpikes();
	}

	@Override
	public TileEntityWoodenSpikes createTileEntity(World world, IBlockState state) {
		return new TileEntityWoodenSpikes();
	}

	public static void degradeBlock(BlockPos pos, World world) {
		Block block = world.getBlockState(pos).getBlock();
		if (block == ModBlocks.WOODEN_SPIKES) {
			world.setBlockState(pos, ModBlocks.WOODEN_SPIKES_BLOODED.getDefaultState());
		} else if (block == ModBlocks.WOODEN_SPIKES_BLOODED) {
			world.setBlockState(pos, ModBlocks.WOODEN_SPIKES_BROKEN.getDefaultState());
		} else if (block == ModBlocks.WOODEN_SPIKES_BROKEN) {
			world.destroyBlock(pos, false);
		}
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
