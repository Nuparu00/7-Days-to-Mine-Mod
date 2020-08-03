package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTable extends BlockBase implements IScrapable{

	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	
	private EnumMaterial material = null;
	private int weight = 3;

	public BlockTable(Material materialIn) {
		super(materialIn);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false))
				.withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false))
				.withProperty(WEST, Boolean.valueOf(false)));

		if(materialIn == Material.WOOD) {
			this.setSoundType(SoundType.WOOD);
			this.material = EnumMaterial.WOOD;
			this.setHardness(1.5F);
			this.setResistance(4.0F);
		}
		else {
			this.setHardness(2.3F);
			this.setResistance(4.0F);
		}
		Blocks.FIRE.setFireInfo(this,5,20);
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
		return (material != null);
	}
	
	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.UP ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(NORTH, canConnectTo(worldIn, pos, EnumFacing.NORTH))
				.withProperty(SOUTH, canConnectTo(worldIn, pos, EnumFacing.SOUTH))
				.withProperty(WEST, canConnectTo(worldIn, pos, EnumFacing.WEST))
				.withProperty(EAST, canConnectTo(worldIn, pos, EnumFacing.EAST));
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return true;
	}

	public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing dir) {
		BlockPos off = pos.offset(dir);
		Block block = world.getBlockState(off).getBlock();
		return block == this;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, WEST, SOUTH });
	}

}
