package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.ItemSlabBase;

public class BlockDarkBricksSlab extends BlockSlabBase {

	public BlockDarkBricksSlab(boolean isdouble) {
		super(Material.ROCK, isdouble);
		setHardness(2);
		setResistance(6);
		if (!isdouble) {
			setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		}
	}

	@Override
	public ItemBlock createItemBlock() {
		if (this.isDouble())
			return null;
		return new ItemSlabBase(this, (BlockSlab) ModBlocks.DARK_BRICKS_SLAB,
				(BlockSlab) ModBlocks.DARK_BRICKS_SLAB_DOUBLE);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return ModBlocks.DARK_BRICKS_SLAB.getItem(world, pos, state);
	}
}
