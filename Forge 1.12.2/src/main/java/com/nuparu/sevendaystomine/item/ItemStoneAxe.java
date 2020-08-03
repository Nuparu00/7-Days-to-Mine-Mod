package com.nuparu.sevendaystomine.item;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;

public class ItemStoneAxe extends ItemUpgrader {

	private static final Set<Block> EFFECTIVE_ON = com.google.common.collect.Sets.newHashSet(new Block[] {
			Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN,
			Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE });

	public ItemStoneAxe(ToolMaterial material, double speed) {
		super(material, EFFECTIVE_ON);
		setNoRepair();
		setUnlocalizedName("StoneAxe");
		this.setAttackDamage(material.getAttackDamage());
		this.setAttackSpeed(speed);
		effect = 0.25f;
	}	
	
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		float f = material != Material.WOOD && material != Material.PLANTS && material != Material.VINE
				? super.getDestroySpeed(stack, state)
				: this.efficiency;
		return f;
	}
	@Override
	public Set<String> getToolClasses(ItemStack stack) {
	    return ImmutableSet.of("axe");
	}
}
