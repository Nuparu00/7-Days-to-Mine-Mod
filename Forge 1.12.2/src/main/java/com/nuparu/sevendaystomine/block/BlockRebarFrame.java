package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRebarFrame extends BlockUpgradeable implements IScrapable{

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 4;
	
	public BlockRebarFrame() {
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setItems(new ItemStack[]{new ItemStack(ModItems.PLANK_WOOD, 6)});
		setSound(SoundHelper.UPGRADE_WOOD);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHardness(2f);
		setResistance(10f);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public IBlockState getResult(World world, BlockPos pos) {
		return ModBlocks.REBAR_FRAME_WOOD.getDefaultState();
	}
}
