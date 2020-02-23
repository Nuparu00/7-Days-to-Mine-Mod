package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCatwalkStairs extends BlockStairsBase implements IScrapable{

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 3;
	
	public BlockCatwalkStairs() {
		super(ModBlocks.CATWALK.getDefaultState());
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.lightOpacity = 5;
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

	@Override
	public boolean isNormalCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return false;
    }
    
    
    
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	 public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return false;
    }
}
