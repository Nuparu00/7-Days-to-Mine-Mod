package com.nuparu.sevendaystomine.block;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSofa extends BlockHorizontalBase implements IScrapable {
    /**
     * B: .. T: x.
     * B: .. T: x.
     */
    protected static final AxisAlignedBB AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.25D, 1.0D, 1.0D);
    /**
     * B: .. T: .x
     * B: .. T: .x
     */
    protected static final AxisAlignedBB AABB_QTR_TOP_EAST = new AxisAlignedBB(0.75D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    /**
     * B: .. T: xx
     * B: .. T: ..
     */
    protected static final AxisAlignedBB AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.25D);
    /**
     * B: .. T: ..
     * B: .. T: xx
     */
    protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0D, 0.5D, 0.75D, 1.0D, 1.0D, 1.0D);
    /**
     * B: .. T: x.
     * B: .. T: ..
     */
    protected static final AxisAlignedBB AABB_OCT_TOP_NW = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 0.25D, 1.0D, 0.25D);
    /**
     * B: .. T: .x
     * B: .. T: ..
     */
    protected static final AxisAlignedBB AABB_OCT_TOP_NE = new AxisAlignedBB(0.75D, 0.5D, 0.0D, 1.0D, 1.0D, 0.25D);
    /**
     * B: .. T: ..
     * B: .. T: x.
     */
    protected static final AxisAlignedBB AABB_OCT_TOP_SW = new AxisAlignedBB(0.0D, 0.5D, 0.75D, 0.25D, 1.0D, 1.0D);
    /**
     * B: .. T: ..
     * B: .. T: .x
     */
    protected static final AxisAlignedBB AABB_OCT_TOP_SE = new AxisAlignedBB(0.75D, 0.5D, 0.75D, 1.0D, 1.0D, 1.0D);
    /**
     * B: xx T: ..
     * B: xx T: ..
     */
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

	public static final PropertyEnum<BlockSofa.EnumShape> SHAPE = PropertyEnum.<BlockSofa.EnumShape>create("shape",
			BlockSofa.EnumShape.class);

	private EnumMaterial material = EnumMaterial.CLOTH;
	private int weight = 3;

	public BlockSofa() {
		super(Material.CLOTH);
		setSoundType(SoundType.CLOTH);
		setHardness(1.7F);
		setResistance(2.0F);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHarvestLevel("axe", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SHAPE,EnumShape.STRAIGHT));
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        if (!isActualState)
        {
            state = this.getActualState(state, worldIn, pos);
        }

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }

    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState bstate)
    {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        list.add(AABB_SLAB_BOTTOM);
        EnumShape shape = (EnumShape)bstate.getValue(SHAPE);

        if (shape == EnumShape.STRAIGHT || shape == EnumShape.INNER_LEFT || shape == EnumShape.INNER_RIGHT)
        {
            list.add(getCollQuarterBlock(bstate));
        }

        if (shape != EnumShape.STRAIGHT)
        {
            list.add(getCollEighthBlock(bstate));
        }

        return list;
    }

    /**
     * Returns a bounding box representing a quarter of a block (two eight-size cubes back to back).
     * Used in all stair shapes except OUTER.
     */
    private static AxisAlignedBB getCollQuarterBlock(IBlockState bstate)
    {
        switch ((EnumFacing)bstate.getValue(FACING))
        {
            case SOUTH:
            default:
                return AABB_QTR_TOP_NORTH;
            case NORTH:
                return AABB_QTR_TOP_SOUTH;
            case EAST:
                return AABB_QTR_TOP_WEST;
            case WEST:
                return AABB_QTR_TOP_EAST;
        }
    }

    /**
     * Returns a bounding box representing an eighth of a block (a block whose three dimensions are halved).
     * Used in all stair shapes except STRAIGHT (gets added alone in the case of OUTER; alone with a quarter block in
     * case of INSIDE).
     */
    private static AxisAlignedBB getCollEighthBlock(IBlockState bstate)
    {
        EnumFacing enumfacing = (EnumFacing)bstate.getValue(FACING);
        EnumFacing enumfacing1;

        switch ((EnumShape)bstate.getValue(SHAPE))
        {
            case OUTER_LEFT:
            default:
                enumfacing1 = enumfacing;
                break;
            case OUTER_RIGHT:
                enumfacing1 = enumfacing.rotateY();
                break;
            case INNER_RIGHT:
                enumfacing1 = enumfacing.getOpposite();
                break;
            case INNER_LEFT:
                enumfacing1 = enumfacing.rotateYCCW();
        }

        switch (enumfacing1)
        {
            case SOUTH:
            default:
                return AABB_OCT_TOP_NW;
            case NORTH:
                return AABB_OCT_TOP_SE;
            case EAST:
                return AABB_OCT_TOP_SW;
            case WEST:
                return AABB_OCT_TOP_NE;
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
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			return Utils.mountBlock(worldIn, pos, playerIn);
		}
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(SHAPE, getShape(state, worldIn, pos));
    }
	
	public EnumShape getShape(IBlockState state, IBlockAccess world, BlockPos pos) {
		
		EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        IBlockState iblockstate = world.getBlockState(pos.offset(enumfacing));

        if (isStateSofa(iblockstate))
        {
            EnumFacing enumfacing1 = (EnumFacing)iblockstate.getValue(FACING);

            if (enumfacing1.getAxis() != ((EnumFacing)state.getValue(FACING)).getAxis() && isDifferentSofa(state, world, pos, enumfacing1.getOpposite()))
            {
            	
            	 if (enumfacing1 == enumfacing.rotateYCCW())
                 {
                     return EnumShape.INNER_LEFT;
                 }
                return EnumShape.INNER_RIGHT;
            }
        }

        IBlockState iblockstate1 = world.getBlockState(pos.offset(enumfacing.getOpposite()));

        if (isStateSofa(iblockstate1))
        {
            EnumFacing enumfacing2 = (EnumFacing)iblockstate1.getValue(FACING);

            if (enumfacing2.getAxis() != ((EnumFacing)state.getValue(FACING)).getAxis() && isDifferentSofa(state, world, pos, enumfacing2))
            {
            	if (enumfacing2 == enumfacing.rotateYCCW())
                {
                    return EnumShape.OUTER_LEFT;
                }
                return EnumShape.OUTER_RIGHT;
            }
        }
		
		return EnumShape.STRAIGHT;
	}
	
	public static boolean isStateSofa(IBlockState state) {
		if(state == null) return false;
		return state.getBlock() instanceof BlockSofa;
	}
	
	private static boolean isDifferentSofa(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        IBlockState iblockstate = world.getBlockState(pos.offset(facing));
        return !isStateSofa(iblockstate) || iblockstate.getValue(FACING) != state.getValue(FACING);
    }
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, SHAPE});
    }

	public static enum EnumShape implements IStringSerializable {
		STRAIGHT("straight"),
        INNER_LEFT("inner_left"),
        INNER_RIGHT("inner_right"),
        OUTER_LEFT("outer_left"),
        OUTER_RIGHT("outer_right");

		private final String name;

		private EnumShape(String name) {
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
