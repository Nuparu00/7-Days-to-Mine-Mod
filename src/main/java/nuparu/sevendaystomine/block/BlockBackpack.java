package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
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
import net.minecraft.util.BlockRenderLayer;
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
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityBackpack;

public class BlockBackpack extends BlockTileProvider<TileEntityBackpack> {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockBackpack() {
		super(Material.CLOTH);
		setSoundType(SoundType.CLOTH);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setHardness(0.01F);
		this.setResistance(0.5F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBackpack();
	}

	@Override
	public TileEntityBackpack createTileEntity(World world, IBlockState state) {
		return new TileEntityBackpack();
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
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }
	
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
	        switch ((EnumFacing)blockState.getValue(FACING))
	        {
	            case UP:
	            default:
	            	return new AxisAlignedBB(0.25, 0.0F, 1.0F, 0.75, 0.8125F, 0.6875F);
	            case NORTH:
	            	return  new AxisAlignedBB(0.25, 0.0F, 1.0F, 0.75, 0.8125F, 0.6875F);
	            case SOUTH:
	            	return new AxisAlignedBB(0.25F, 0.0F, 0F, 0.75F, 0.875F, 0.3125F);
	            case WEST:
	            	return new AxisAlignedBB(1F, 0.0F, 0.25F, 0.6875F, 0.8125F, 0.75F);
	            case EAST:
	            	return new AxisAlignedBB(0F, 0.0F, 0.25F, 0.3125F, 0.8125F, 0.75F);
	        }
	    }
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		switch ((EnumFacing)state.getValue(FACING))
        {
            case UP:
            default:
            	return new AxisAlignedBB(0.25, 0.0F, 1.0F, 0.75, 0.8125F, 0.6875F);
            case NORTH:
            	return  new AxisAlignedBB(0.25, 0.0F, 1.0F, 0.75, 0.8125F, 0.6875F);
            case SOUTH:
            	return new AxisAlignedBB(0.25F, 0.0F, 0F, 0.75F, 0.875F, 0.3125F);
            case WEST:
            	return new AxisAlignedBB(1F, 0.0F, 0.25F, 0.6875F, 0.8125F, 0.75F);
            case EAST:
            	return new AxisAlignedBB(0F, 0.0F, 0.25F, 0.3125F, 0.8125F, 0.75F);
        }
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.isSneaking()) return true;
        
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityBackpack) {
			playerIn.openGui(SevenDaysToMine.instance, 5, worldIn, pos.getX(), pos.getY(), pos.getZ());

		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityBackpack) {
				((TileEntityBackpack) tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityBackpack) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBackpack) tileentity);
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
