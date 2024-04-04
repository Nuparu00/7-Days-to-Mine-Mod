package nuparu.sevendaystomine.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IUpgradeable {

public ItemStack[] getItems();
public SoundEvent getSound();
public IBlockState getPrev(World world, BlockPos pos, IBlockState original);
public IBlockState getResult(World world, BlockPos pos);
public void onUpgrade(World world, BlockPos pos, IBlockState oldState);
public void onDowngrade(World world, BlockPos pos, IBlockState oldState);

}