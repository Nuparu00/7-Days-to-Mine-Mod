package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.tileentity.TileEntityRadio;
import com.nuparu.sevendaystomine.tileentity.TileEntitySirene;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockSirene extends BlockTileProvider<TileEntitySirene> {

	public BlockSirene() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setHardness(1f);
		this.setResistance(1f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySirene();
	}

	@Override
	public TileEntitySirene createTileEntity(World world, IBlockState state) {
		return new TileEntitySirene();
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
			@Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		if (worldIn.isRemote) {
			SevenDaysToMine.proxy.stopLoudSound(pos);
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (worldIn.isRemote) {
			SevenDaysToMine.proxy.stopLoudSound(pos);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntitySirene te = (TileEntitySirene) worldIn.getTileEntity(pos);
		te.cycle();
		return true;

	}

}
