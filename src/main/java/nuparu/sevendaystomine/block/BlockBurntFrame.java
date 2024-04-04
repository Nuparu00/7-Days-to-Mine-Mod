package nuparu.sevendaystomine.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public class BlockBurntFrame extends BlockUpgradeable {

	public BlockBurntFrame() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD, 3)});
		setSound(SoundHelper.UPGRADE_WOOD);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHardness(0.8f);
		setResistance(2f);
		setHarvestLevel("axe", 0);
	}
	@Override
	public IBlockState getResult(World world, BlockPos pos) {
		return ModBlocks.BURNT_PLANKS.getDefaultState();
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public boolean isTopSolid(IBlockState state)
    {
        return true;
    }
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }
}