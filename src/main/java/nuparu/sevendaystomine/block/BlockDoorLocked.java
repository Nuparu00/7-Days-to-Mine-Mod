package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.MathUtils;

public class BlockDoorLocked extends BlockDoorBase {

	private static long nextSoundAllowed = 0;

	public BlockDoorLocked() {
		super(Material.WOOD);
		setHardness(3);
		setResistance(6);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(POWERED).build();
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
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModItems.LOCKED_DOOR_ITEM);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? net.minecraft.init.Items.AIR
				: ModItems.LOCKED_DOOR_ITEM;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (System.currentTimeMillis() >= nextSoundAllowed && !player.isSneaking()) {
			nextSoundAllowed = System.currentTimeMillis() + 500l;
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundHelper.DOOR_LOCKED, SoundCategory.BLOCKS,
					0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f), false);
		}
		return false;
	}

	public void unlock(World world, BlockPos pos, IBlockState state) {
		BlockPos bottom = pos;
		if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			bottom = pos.down();
		}
		
		EnumFacing facing = state.getValue(BlockDoor.FACING);
		boolean open = state.getValue(BlockDoor.OPEN);
		boolean powered = state.getValue(BlockDoor.POWERED);
		EnumHingePosition hinge = state.getValue(BlockDoor.HINGE);

		world.setBlockState(bottom.up(),
				Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER)
						.withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.OPEN, open)
						.withProperty(BlockDoor.POWERED, powered).withProperty(BlockDoor.HINGE, hinge));

		world.setBlockState(bottom,
				Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER)
						.withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.OPEN, open)
						.withProperty(BlockDoor.POWERED, powered).withProperty(BlockDoor.HINGE, hinge));
		
		world.markBlockRangeForRenderUpdate(bottom, bottom.up());
		world.notifyBlockUpdate(bottom, world.getBlockState(bottom), world.getBlockState(bottom), 3);
		world.scheduleBlockUpdate(bottom,this,0,0);
		world.notifyBlockUpdate(bottom.up(), world.getBlockState(bottom.up()), world.getBlockState(bottom.up()), 3);
		world.scheduleBlockUpdate(bottom.up(),this,0,0);

	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			BlockPos blockpos = pos.down();
			IBlockState iblockstate = worldIn.getBlockState(blockpos);

			if (iblockstate.getBlock() != this) {
				worldIn.setBlockToAir(pos);
			} else if (blockIn != this) {
				iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
			}
		} else {
			boolean flag1 = false;
			BlockPos blockpos1 = pos.up();
			IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

			if (iblockstate1.getBlock() != this) {
				worldIn.setBlockToAir(pos);
				flag1 = true;
			}

			if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) {
				worldIn.setBlockToAir(pos);
				flag1 = true;

				if (iblockstate1.getBlock() == this) {
					worldIn.setBlockToAir(blockpos1);
				}
			}

			if (flag1) {
				if (!worldIn.isRemote) {
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}
			} else {
				boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos1);

				if (blockIn != this && (flag || blockIn.getDefaultState().canProvidePower())
						&& flag != ((Boolean) iblockstate1.getValue(POWERED)).booleanValue()) {
					worldIn.setBlockState(blockpos1, iblockstate1.withProperty(POWERED, Boolean.valueOf(flag)), 2);

					/*
					 * if (flag != ((Boolean)state.getValue(OPEN)).booleanValue()) {
					 * worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)),
					 * 2); worldIn.markBlockRangeForRenderUpdate(pos, pos);
					 * worldIn.playEvent((EntityPlayer)null, flag ? this.getOpenSound() :
					 * this.getCloseSound(), pos, 0); }
					 */
				}
			}
		}
	}

}
