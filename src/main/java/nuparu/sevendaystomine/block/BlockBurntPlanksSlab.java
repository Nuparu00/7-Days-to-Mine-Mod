package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
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

public class BlockBurntPlanksSlab extends BlockSlabBase {

	public BlockBurntPlanksSlab(boolean isdouble) {
		super(Material.WOOD, isdouble);
		setSoundType(SoundType.WOOD);
		setHardness(2.5f);
		setResistance(7.5f);
		setHarvestLevel("axe", 0);
		if (!isdouble) {
			setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		}
	}

	@Override
	public ItemBlock createItemBlock() {
		if (this.isDouble())
			return null;
		return new ItemSlabBase(this, (BlockSlab) ModBlocks.BURNT_PLANKS_SLAB,
				(BlockSlab) ModBlocks.BURNT_PLANKS_SLAB_DOUBLE);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return ModBlocks.BURNT_PLANKS_SLAB.getItem(world, pos, state);
	}
}
