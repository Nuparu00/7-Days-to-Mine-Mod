package com.nuparu.sevendaystomine.block;

import java.util.Random;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.ItemCookware;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCookware extends BlockPickable {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private AxisAlignedBB AABB;

	private EnumMaterial enumMat;

	public BlockCookware(Material matBlock, EnumMaterial enumMat, SoundType sound, AxisAlignedBB aabb) {
		super(matBlock);
		this.enumMat = enumMat;
		this.AABB = aabb;
		setSoundType(sound);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setCreativeTab(SevenDaysToMine.TAB_MATERIALS);
		setHardness(0.2F);
		setResistance(1.0F);
		this.setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return AABB.offset(pos);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		if(enumMat == EnumMaterial.GLASS) {
		return BlockRenderLayer.TRANSLUCENT;
		}
		return super.getBlockLayer();
	}

	public IBlockState getRandomVariant(Random rand) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.Plane.HORIZONTAL.random(rand));
	}

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);

		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}

		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	public ItemBlock createItemBlock() {
		ItemCookware item = new ItemCookware(this);
		item.setMaterial(enumMat);
		item.setWeight(3);
		return item;
	}

	public static ItemBlock createItemBlock(Block block) {
		if (!(block instanceof BlockCookware))
			return null;
		BlockCookware cookware = (BlockCookware) block;
		ItemCookware item = new ItemCookware(cookware);
		item.setMaterial(cookware.enumMat);
		item.setWeight(3);
		return item;
	}
	

}
