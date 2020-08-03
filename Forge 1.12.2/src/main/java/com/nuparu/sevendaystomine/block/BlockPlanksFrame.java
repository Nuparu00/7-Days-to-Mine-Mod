package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPlanksFrame extends BlockUpgradeable {

	public BlockPlanksFrame(BlockPlanks.EnumType variant) {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setResult(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, variant));
		setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD, 6)});
		setSound(SoundHelper.UPGRADE_WOOD);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHardness(0.8f);
		setResistance(2f);
		setHarvestLevel("axe", 0);
		Blocks.FIRE.setFireInfo(this,5,20);
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