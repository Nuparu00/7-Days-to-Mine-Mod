package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCatwalkBase extends BlockBase implements IScrapable {

	public static final PropertyEnum<EnumBlockCatwalk> HALF = PropertyEnum.<EnumBlockCatwalk>create("half",
			BlockCatwalkBase.EnumBlockCatwalk.class);

	public static AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);
	public static AxisAlignedBB AABB_TOP = new AxisAlignedBB(0, 0.9375, 0, 1, 1, 1);

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 2;

	public BlockCatwalkBase() {
		super(Material.ROCK);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setDefaultState(this.blockState.getBaseState().withProperty(HALF, EnumBlockCatwalk.BOTTOM));
		this.lightOpacity = 5;
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
		IBlockState iblockstate = this.getDefaultState();

		return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D) ? iblockstate
				: iblockstate.withProperty(HALF, BlockCatwalkBase.EnumBlockCatwalk.TOP);
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
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumBlockCatwalk) blockState.getValue(HALF)) {
		default:
			return AABB_BOTTOM;
		case TOP:
			return AABB_TOP;
		case BOTTOM:
			return AABB_BOTTOM;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumBlockCatwalk) state.getValue(HALF)) {
		default:
			return AABB_BOTTOM;
		case TOP:
			return AABB_TOP;
		case BOTTOM:
			return AABB_BOTTOM;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumBlockCatwalk half = EnumBlockCatwalk.TOP;
		if (meta != 0) {
			half = EnumBlockCatwalk.BOTTOM;
		}
		return this.getDefaultState().withProperty(HALF, half);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumBlockCatwalk half = state.getValue(HALF);
		return half == EnumBlockCatwalk.TOP ? 0 : 1;

	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { HALF });
	}

	public static enum EnumBlockCatwalk implements IStringSerializable {
		TOP("top"), BOTTOM("bottom");

		private final String name;

		private EnumBlockCatwalk(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}
}
