package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.tileentity.TileEntityFlag;

public class BlockFlag extends BlockTileProvider<TileEntityFlag>{

	public BlockFlag() {
		super(Material.IRON);
		setHardness(0.7f);
		setResistance(1);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFlag();
	}

	@Override
	public TileEntityFlag createTileEntity(World world, IBlockState state) {
		return new TileEntityFlag();
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(BlockHorizontalBase.FACING).build();
	}
	
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
	        switch ((EnumFacing)blockState.getValue(BlockHorizontalBase.FACING))
	        {
	            case UP:
	            default:
	                return new AxisAlignedBB(0.375F, 0.125F, 0.7F, 0.625F, 0.375F, 1F);
	            case NORTH:
	            	return new AxisAlignedBB(0.375, 0.125F, 0.7F, 0.625F, 0.375F, 1F);
	            case SOUTH:
	            	return new AxisAlignedBB(0.375, 0.125F, 0.0F, 0.625F, 0.375F, 0.3F);
	            case WEST:
	            	return new AxisAlignedBB(0.7F, 0.125F, 0.375F, 1F, 0.375F, 0.625F);
	            case EAST:
	            	return new AxisAlignedBB(0.0F, 0.125F,0.375F, 0.3F, 0.375F, 0.625F);
	        }
	    }
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		switch ((EnumFacing)state.getValue(BlockHorizontalBase.FACING))
        {
            case UP:
            default:
                return new AxisAlignedBB(0.375F, 0.125F, 0.7F, 0.625F, 0.375F, 1F);
            case NORTH:
            	return new AxisAlignedBB(0.375, 0.125F, 0.7F, 0.625F, 0.375F, 1F);
            case SOUTH:
            	return new AxisAlignedBB(0.375, 0.125F, 0.0F, 0.625F, 0.375F, 0.3F);
            case WEST:
            	return new AxisAlignedBB(0.7F, 0.125F, 0.375F, 1F, 0.375F, 0.625F);
            case EAST:
            	return new AxisAlignedBB(0.0F, 0.125F,0.375F, 0.3F, 0.375F, 0.625F);
        }
    }
	
	/**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockHorizontalBase.FACING, rot.rotate((EnumFacing)state.getValue(BlockHorizontalBase.FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(BlockHorizontalBase.FACING)));
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(BlockHorizontalBase.FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BlockHorizontalBase.FACING, EnumFacing.getHorizontal(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(BlockHorizontalBase.FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {BlockHorizontalBase.FACING});
    }

}
