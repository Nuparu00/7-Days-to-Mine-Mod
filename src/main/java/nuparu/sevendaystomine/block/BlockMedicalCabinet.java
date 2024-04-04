package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityMedicalCabinet;

public class BlockMedicalCabinet extends BlockTileProvider<TileEntityMedicalCabinet> {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockMedicalCabinet() {
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(1.8F);
		setResistance(3.0F);
		setHarvestLevel("pickaxe", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMedicalCabinet();
	}

	@Override
	public TileEntityMedicalCabinet createTileEntity(World world, IBlockState state) {
		return new TileEntityMedicalCabinet();
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
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
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
	        switch ((EnumFacing)blockState.getValue(FACING))
	        {
	            case UP:
	            default:
	                return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 1F, 1F);
	            case NORTH:
	            	return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 1F, 1F);
	            case SOUTH:
	            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1F, 0.3F);
	            case WEST:
	            	return new AxisAlignedBB(0.7F, 0.0F, 0F, 1F, 1F, 1F);
	            case EAST:
	            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.3F, 1F, 1F);
	        }
	    }
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		switch ((EnumFacing)state.getValue(FACING))
        {
            case UP:
            default:
                return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 1F, 1F);
            case NORTH:
            	return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 1F, 1F);
            case SOUTH:
            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1F, 0.3F);
            case WEST:
            	return new AxisAlignedBB(0.7F, 0.0F, 0F, 1F, 1F, 1F);
            case EAST:
            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.3F, 1F, 1F);
        }
    }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(playerIn.isSneaking()) return true;
        
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMedicalCabinet) {
			playerIn.openGui(SevenDaysToMine.instance, 5, worldIn, pos.getX(), pos.getY(), pos.getZ());

		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityMedicalCabinet) {
				((TileEntityMedicalCabinet) tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityMedicalCabinet) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityMedicalCabinet) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
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

}
