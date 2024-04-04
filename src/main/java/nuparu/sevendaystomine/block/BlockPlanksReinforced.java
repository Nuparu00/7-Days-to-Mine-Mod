package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public class BlockPlanksReinforced extends BlockUpgradeable {
	public BlockPlanksReinforced(BlockPlanks.EnumType variant) {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setResult(getResult(variant));
		setPrev(net.minecraft.init.Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, variant));
		setItems(new ItemStack[]{new ItemStack(ModItems.IRON_SCRAP, 6)});
		setSound(SoundHelper.UPGRADE_WOOD);
		setHardness(2.5f);
		setResistance(7.5f);
		setHarvestLevel("axe", 0);
		Blocks.FIRE.setFireInfo(this,5,20);
	}
	public IBlockState getResult(BlockPlanks.EnumType variant) {
		switch(variant) {
		case OAK : ((BlockUpgradeable) ModBlocks.OAK_PLANKS_REINFORCED_IRON).setPrev(this.getDefaultState());return ModBlocks.OAK_PLANKS_REINFORCED_IRON.getDefaultState();
		case BIRCH : ((BlockUpgradeable) ModBlocks.BIRCH_PLANKS_REINFORCED_IRON).setPrev(this.getDefaultState());return ModBlocks.BIRCH_PLANKS_REINFORCED_IRON.getDefaultState();
		case SPRUCE : ((BlockUpgradeable) ModBlocks.SPRUCE_PLANKS_REINFORCED_IRON).setPrev(this.getDefaultState());return ModBlocks.SPRUCE_PLANKS_REINFORCED_IRON.getDefaultState();
		case JUNGLE : ((BlockUpgradeable) ModBlocks.JUNGLE_PLANKS_REINFORCED_IRON).setPrev(this.getDefaultState());return ModBlocks.JUNGLE_PLANKS_REINFORCED_IRON.getDefaultState();
		case ACACIA : ((BlockUpgradeable) ModBlocks.ACACIA_PLANKS_REINFORCED_IRON).setPrev(this.getDefaultState());return ModBlocks.ACACIA_PLANKS_REINFORCED_IRON.getDefaultState();
		case DARK_OAK : ((BlockUpgradeable) ModBlocks.DARKOAK_PLANKS_REINFORCED_IRON).setPrev(this.getDefaultState());return ModBlocks.DARKOAK_PLANKS_REINFORCED_IRON.getDefaultState();
		}
		return null;
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
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
