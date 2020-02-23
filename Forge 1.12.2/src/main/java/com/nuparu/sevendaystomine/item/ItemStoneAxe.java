package com.nuparu.sevendaystomine.item;

import java.util.Set;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemStoneAxe extends ItemUpgrader {

	private static final Set<Block> EFFECTIVE_ON = com.google.common.collect.Sets.newHashSet(new Block[] {
			Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN,
			Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE });

	public ItemStoneAxe() {
		super(SevenDaysToMine.STONE_TOOLS, EFFECTIVE_ON);
		setNoRepair();
		setUnlocalizedName("StoneAxe");
		attackDamage = 8;
		attackSpeed = -1F;
		effect = 0.25f;
	}	
}
