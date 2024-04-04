package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.BlockWallSign;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.ItemBlockBigSign;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignSlave;
import nuparu.sevendaystomine.util.Utils;

public class BlockBigSignMaster extends BlockWallSign implements IBlockBase {

	public BlockBigSignMaster() {
		super();
		this.setSoundType(SoundType.METAL);
		setHardness(2);
		setResistance(0);
	}

	protected static final AxisAlignedBB SIGN_EAST_AABB = new AxisAlignedBB(0.0D, 0D, 0, 0.125D, 1, 1D);
	protected static final AxisAlignedBB SIGN_WEST_AABB = new AxisAlignedBB(0.875D, 0.0, 0D, 1.0D, 1D, 1D);
	protected static final AxisAlignedBB SIGN_SOUTH_AABB = new AxisAlignedBB(0, 0.0, 0.0D, 1D, 1D, 0.125D);
	protected static final AxisAlignedBB SIGN_NORTH_AABB = new AxisAlignedBB(0, 0.0, 0.875D, 1D, 1D, 1.0D);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(FACING)) {
		case NORTH:
		default:
			return SIGN_NORTH_AABB;
		case SOUTH:
			return SIGN_SOUTH_AABB;
		case WEST:
			return SIGN_WEST_AABB;
		case EAST:
			return SIGN_EAST_AABB;
		}
	}

	@Override
	public boolean metaItemBlock() {
		return false;
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemBlockBigSign(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomItemMesh() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getItemMesh() {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBigSignMaster();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(FACING).build();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager) {
		return true;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModBlocks.BIG_SIGN_MASTER);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity TE = worldIn.getTileEntity(pos);
		if (!(TE instanceof TileEntityBigSignMaster)) {
			return;
		}
		TileEntityBigSignMaster masterTE = (TileEntityBigSignMaster) TE;

		EnumFacing facing = state.getValue(FACING);

		for (int width = -3; width <= 3; width++) {
			for (int height = 0; height < 3; height++) {
				if (width == 0 && height == 0) {
					continue;
				}
				BlockPos pos2 = pos.offset(facing.rotateY(), width).up(height);
				IBlockState state2 = ModBlocks.BIG_SIGN_SLAVE.getDefaultState().withProperty(BlockBigSignSlave.FACING,
						facing);
				worldIn.setBlockState(pos2, state2);
				worldIn.notifyBlockUpdate(pos2, Blocks.AIR.getDefaultState(), state2, 3);
				masterTE.addSlave(pos2);
				TileEntity TE2 = worldIn.getTileEntity(pos2);
				if (TE2 instanceof TileEntityBigSignSlave) {
					TileEntityBigSignSlave slave = (TileEntityBigSignSlave) TE2;
					slave.setParent(pos);
				}
			}
		}

	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityBigSignMaster) {
			TileEntityBigSignMaster master = (TileEntityBigSignMaster) tileentity;
			for (BlockPos pos2 : master.getSlaves()) {
				worldIn.setBlockToAir(pos2);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand == EnumHand.MAIN_HAND) {
			ItemStack stack = playerIn.getHeldItem(hand);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item instanceof ItemDye) {
					TileEntity tileentity = worldIn.getTileEntity(pos);

					if (tileentity instanceof TileEntityBigSignMaster) {
						TileEntityBigSignMaster master = (TileEntityBigSignMaster) tileentity;
						EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
						TextFormatting textColor = Utils.dyeColorToTextFormatting(enumdyecolor);
						master.setTextColor(textColor);
						if(!playerIn.isCreative()) {
							stack.shrink(1);
						}
						return true;
					}
				}
			}
		}
		return false;
	}

}
