package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityTorch;

public class BlockTorchEnhanced extends BlockTorch implements IBlockBase, ITileEntityProvider {

	public BlockTorchEnhanced() {
		super();
		this.setHardness(0.0F);
		this.setLightLevel(0.9375F);
		this.setSoundType(SoundType.WOOD);
	}

	public boolean metaItemBlock() {
		return false;
	}

	public ItemBlock createItemBlock() {
		return new ItemBlock(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTorch();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasCustomStateMapper() {
		return false;
	}
	@Override
    @SideOnly(Side.CLIENT)
    public IStateMapper getStateMapper(){
		return null;
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

	public static void extinguish(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if(!(state.getBlock() instanceof BlockTorch)) return;
		world.setBlockState(pos,
				ModBlocks.TORCH_UNLIT.getDefaultState().withProperty(BlockUnlitTorch.FACING, state.getValue(FACING)), 3);
		world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
				(double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
				2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, false);
		for (int i = 0; i < 5; ++i) {
			double d0 = (double) pos.getX() + world.rand.nextDouble() * 0.6D + 0.2D;
			double d1 = (double) pos.getY() + world.rand.nextDouble() * 0.6D + 0.2D;
			double d2 = (double) pos.getZ() + world.rand.nextDouble() * 0.6D + 0.2D;
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

}
