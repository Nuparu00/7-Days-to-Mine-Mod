package nuparu.sevendaystomine.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;

public class BlockReinforcedConcrete extends BlockBase {

	public static final PropertyInteger PHASE = PropertyInteger.create("phase", 0, 3);

	public BlockReinforcedConcrete() {
		super(Material.ROCK);
		setHardness(10.0F);
		setResistance(30.0F);
		setHarvestLevel("pickaxe", 3);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PHASE, Integer.valueOf(0)));
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		tryToRemove(worldIn, pos);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (player != null) {
			if (player.isCreative()) {
				return world.setBlockToAir(pos);
			}
		}
		tryToRemove(world, pos);
		return false;


	}

	public boolean tryToRemove(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockReinforcedConcrete))
			return true;
		if (state.getValue(PHASE) == 0) {
			world.setBlockState(pos, ModBlocks.REINFORCED_CONCRETE.getDefaultState().withProperty(PHASE, 1));
			return false;
		}

		if (world.getBlockState(pos).getValue(PHASE) == 1) {
			world.setBlockState(pos, ModBlocks.REINFORCED_CONCRETE.getDefaultState().withProperty(PHASE, 2));
			return false;
		}

		if (world.getBlockState(pos).getValue(PHASE) == 2) {
			world.setBlockState(pos, ModBlocks.REINFORCED_CONCRETE.getDefaultState().withProperty(PHASE, 3));
			return false;
		}

		if (world.getBlockState(pos).getValue(PHASE) == 3) {
			return world.setBlockToAir(pos);
		}
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PHASE, Integer.valueOf((meta & 15) >> 2));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		byte b0 = 0;
		int i = b0;
		i |= ((Integer) state.getValue(PHASE)).intValue() << 2;
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PHASE });
	}

}
