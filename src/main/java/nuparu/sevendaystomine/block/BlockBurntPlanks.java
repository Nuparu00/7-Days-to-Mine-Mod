package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public class BlockBurntPlanks extends BlockUpgradeable {
	public BlockBurntPlanks() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setResult(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK));
		setItems(new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) });
		setSound(SoundHelper.UPGRADE_WOOD);
		setHardness(2.5f);
		setResistance(7.5f);
		setHarvestLevel("axe", 0);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

	@Override
	public IBlockState getPrev(World world, BlockPos pos, IBlockState original) {
		return ModBlocks.BURNT_FRAME.getDefaultState();
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return true;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}
}
