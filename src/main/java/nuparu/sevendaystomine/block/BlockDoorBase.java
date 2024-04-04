package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockDoorBase extends BlockDoor implements IBlockBase {

	public BlockDoorBase(Material materialIn) {
		super(materialIn);
	}
	
	@Override
	public ItemBlock createItemBlock() {
		return null;
	}

	@Override
	public boolean metaItemBlock() {
		return false;
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
	

}
