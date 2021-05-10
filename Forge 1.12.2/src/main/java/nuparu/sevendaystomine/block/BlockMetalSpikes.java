package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
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
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.tileentity.TileEntityMetalSpikes;

public class BlockMetalSpikes extends BlockTileProvider<TileEntityMetalSpikes> implements IScrapable {

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 6;
	
	
	public static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0F, 0.9375F, 0F, 1F, 1F, 1F);
	public static final AxisAlignedBB AABB_UP =  new AxisAlignedBB(0F, 0F, 0F, 1F, 0.0625F, 1F);
	public static final AxisAlignedBB AABB_NORTH=new AxisAlignedBB(0F, 0F, 0.9375F, 1, 1, 1F);
	public static final AxisAlignedBB AABB_SOUTH=new AxisAlignedBB(0F, 0F, 0.0F, 1, 1, 0.0625F);
	public static final AxisAlignedBB AABB_WEST=new AxisAlignedBB(0.9375F, 0, 0F, 1, 1F, 1F);
	public static final AxisAlignedBB AABB_EAST=new AxisAlignedBB(0.0F, 0, 0F, 0.0625F, 1F, 1F);

	public BlockMetalSpikes() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setHardness(3f);
		this.setResistance(2f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.NORTH));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entityIn) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMetalSpikes) {
			TileEntityMetalSpikes spikes = (TileEntityMetalSpikes) te;
			if (spikes.isRetracted())
				return;
			if (!(entityIn instanceof EntityLivingBase)) {
				return;
			}
			entityIn.setInWeb();
			if (entityIn instanceof EntityPlayer) {
				if (((EntityPlayer) entityIn).isCreative() || ((EntityPlayer) entityIn).isSpectator()) {
					return;
				}
			}
			entityIn.attackEntityFrom(DamageSource.GENERIC, 7);
			spikes.dealDamage(1);
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
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(BlockDirectional.FACING, facing);
    }

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (this.checkForDrop(world, pos, state) && !canPlaceBlock(world, pos, (EnumFacing)state.getValue(BlockDirectional.FACING)))
        {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMetalSpikes) {
			TileEntityMetalSpikes spikes = (TileEntityMetalSpikes) te;
			spikes.setRetracted(!world.isBlockPowered(pos));
		}
	}
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return canPlaceBlock(worldIn, pos, side);
    }

	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (canPlaceBlock(worldIn, pos, enumfacing))
            {
                return true;
            }
        }

        return false;
    }

    protected static boolean canPlaceBlock(World worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos blockpos = pos.offset(direction.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = iblockstate.getBlockFaceShape(worldIn, blockpos, direction) == BlockFaceShape.SOLID;
        Block block = iblockstate.getBlock();

        if (direction == EnumFacing.UP)
        {
            return iblockstate.isTopSolid() || !isExceptionBlockForAttaching(block) && flag;
        }
        else
        {
            return !isExceptBlockForAttachWithPiston(block) && flag;
        }
    }
    

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.canPlaceBlockAt(worldIn, pos))
        {
            return true;
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    } 

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockDirectional.FACING)) {
		default:
		case DOWN:
			return AABB_DOWN;
		case UP:
			return AABB_UP;
		case NORTH:
			return AABB_NORTH;
		case SOUTH:
			return AABB_SOUTH;
		case WEST:
			return AABB_WEST;
		case EAST:
			return AABB_EAST;
		}
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMetalSpikes();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(BlockDirectional.FACING)).getIndex();
	}

	@Override
	public TileEntityMetalSpikes createTileEntity(World world, IBlockState state) {
		return new TileEntityMetalSpikes();
	}

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(BlockDirectional.FACING,
				rot.rotate((EnumFacing) state.getValue(BlockDirectional.FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(BlockDirectional.FACING)));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockDirectional.FACING });
	}

}
