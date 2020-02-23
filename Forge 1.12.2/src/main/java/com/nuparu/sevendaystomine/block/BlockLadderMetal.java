package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLadderMetal extends BlockLadder implements IBlockBase, IScrapable {

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 3;

	@Override
	public boolean metaItemBlock() {
		return false;
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return null;
	}
	
	@Override
	public boolean hasCustomItemMesh() {
		return false;
	}

	@Override
	public ItemMeshDefinition getItemMesh() {
		return null;
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

}
