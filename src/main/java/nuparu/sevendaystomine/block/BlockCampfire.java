package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityCampfire;
import nuparu.sevendaystomine.util.MathUtils;

public class BlockCampfire extends BlockTileProvider<TileEntityCampfire> {
	private final boolean isBurning;
	private static boolean keepInventory;

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.25D, 1D);

	public BlockCampfire(boolean isBurning) {
		super(Material.ROCK);
		this.isBurning = isBurning;
		setHardness(1.8F);
		setResistance(2.0F);
		setLightOpacity(0);
		setHarvestLevel("pickaxe", 0);
		setTickRandomly(false);
		useNeighborBrightness = false;
		if (isBurning) {
			setLightLevel(0.675F);
		} else {
			setLightLevel(0);
		}
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return AABB.offset(pos);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
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
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.CAMPFIRE);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModBlocks.CAMPFIRE);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCampfire();
	}

	@Override
	public TileEntityCampfire createTileEntity(World world, IBlockState state) {
		return new TileEntityCampfire();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityCampfire) {
				((TileEntityCampfire) tileentity).setDisplayName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityCampfire) {
				NonNullList<ItemStack> drops = ((TileEntityCampfire) tileentity).getDrops();
				for (ItemStack stack : drops) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			playerIn.openGui(SevenDaysToMine.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (this.isBurning) {

			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY() + 0.45D;
			double d2 = (double) pos.getZ() + 0.5D;
			double d3 = rand.nextDouble() * 0.2D - 0.1D;

			if (rand.nextInt(15) == 0) {
				worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
						SoundHelper.CAMPFIRE_CRACKLING, SoundCategory.BLOCKS,
						0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f), false);
			}
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2 + d3, 0.0D, 0.0D, 0.0D);

		}
	}

	public static void setState(boolean burning, World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (burning) {
			worldIn.setBlockState(pos, ModBlocks.CAMPFIRE_LIT.getDefaultState(), 3);
		} else {
			worldIn.setBlockState(pos, ModBlocks.CAMPFIRE.getDefaultState(), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!this.canBlockStay(worldIn, pos)) {
			worldIn.getBlockState(pos).getBlock().dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 1);
			worldIn.setBlockToAir(pos);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.down());
		return state.getMaterial() != Material.AIR && !worldIn.getBlockState(pos.up()).getMaterial().isLiquid();
	}

}
