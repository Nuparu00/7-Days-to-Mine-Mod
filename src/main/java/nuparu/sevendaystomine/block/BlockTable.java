package nuparu.sevendaystomine.block;

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
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.EnumMaterial;


public class BlockTable extends BlockBase{

	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");

	public BlockTable(Material materialIn) {
		super(materialIn);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false))
				.withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false))
				.withProperty(WEST, Boolean.valueOf(false)));

		if(materialIn == Material.WOOD) {
			this.setSoundType(SoundType.WOOD);
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
