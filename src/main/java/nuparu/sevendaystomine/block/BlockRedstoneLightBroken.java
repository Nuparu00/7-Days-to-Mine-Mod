package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRedstoneLightBroken extends BlockBase {

	public BlockRedstoneLightBroken() {
		super(Material.REDSTONE_LIGHT);
		setHardness(0.3F);
		setSoundType(SoundType.GLASS);
	}

	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if (world.isBlockPowered(pos)) {
			world.setBlockToAir(pos);
			world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 0.1f, true);
		}
	}

}
