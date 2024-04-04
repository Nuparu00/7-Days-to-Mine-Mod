package nuparu.sevendaystomine.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUpgradeable extends BlockBase implements IUpgradeable {

	private ItemStack[] items = new ItemStack[0];
	private SoundEvent sound = null;
	private IBlockState result = null;
	private IBlockState prev = Blocks.AIR.getDefaultState();

	public BlockUpgradeable(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	public BlockUpgradeable(Material blockMaterialIn) {
		super(blockMaterialIn);
	}

	public ItemStack[] getItems() {
		return items;
	}

	public SoundEvent getSound() {
		return sound;
	}

	public IBlockState getResult(World world, BlockPos pos) {
		return result;
	}

	public BlockUpgradeable setItems(ItemStack[] items) {
		this.items = items;
		return this;
	}

	public BlockUpgradeable setSound(SoundEvent sound) {
		this.sound = sound;
		return this;
	}

	public BlockUpgradeable setResult(IBlockState result) {
		this.result = result;
		return this;    
	}

	public IBlockState getPrev(World world, BlockPos pos, IBlockState original) {
		return prev;
	}

	public void setPrev(IBlockState prev) {
		this.prev = prev;
	}

	@Override
	public void onUpgrade(World world, BlockPos pos, IBlockState oldState) {

	}

	@Override
	public void onDowngrade(World world, BlockPos pos, IBlockState oldState) {
		
	}

}
