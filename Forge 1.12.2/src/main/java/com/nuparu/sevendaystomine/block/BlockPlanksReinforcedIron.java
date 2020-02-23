package com.nuparu.sevendaystomine.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPlanksReinforcedIron extends BlockUpgradeable {
	public BlockPlanksReinforcedIron() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setHardness(35f);
		setResistance(10f);
		setHarvestLevel("pickaxe", 1);
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }
}
