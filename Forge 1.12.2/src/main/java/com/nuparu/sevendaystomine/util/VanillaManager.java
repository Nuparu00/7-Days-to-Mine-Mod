package com.nuparu.sevendaystomine.util;

import java.util.ArrayList;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.crafting.RecipeManager;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.EnumMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class VanillaManager {

	public static ArrayList<VanillaBlockUpgrade> vanillaUpgrades = new ArrayList<VanillaBlockUpgrade>();
	
	public static ArrayList<VanillaScrapableItem> vanillaScrapables = new ArrayList<VanillaScrapableItem>();

	public static void modifyVanilla() {
		editVanillaBlockProperties();
		removeVanillaRecipes();
		addVanillaBlockUpgrades();
		addVanillaScrapables();
	}

	public static void editVanillaBlockProperties() {
		Blocks.CONCRETE.setHardness(6.4f);
		/*
		Blocks.DIRT.setHardness(22.5F);
		Blocks.GRASS_PATH.setHardness(23F);
		Blocks.STONE.setHardness(35F);
		Blocks.QUARTZ_BLOCK.setHardness(35F);
		Blocks.PLANKS.setHardness(27.5F);
		Blocks.COBBLESTONE.setHardness(32.5F);
		Blocks.LOG.setHardness(30F);
		Blocks.LOG2.setHardness(30F);
		Blocks.PLANKS.setHardness(33F);
		Blocks.OAK_DOOR.setHardness(30F);
		Blocks.SPRUCE_DOOR.setHardness(30F);
		Blocks.BIRCH_DOOR.setHardness(30F);
		Blocks.JUNGLE_DOOR.setHardness(30F);
		Blocks.ACACIA_DOOR.setHardness(30F);
		Blocks.DARK_OAK_DOOR.setHardness(30F);
		Blocks.IRON_DOOR.setHardness(40F);
		Blocks.SAND.setHardness(18F);
		Blocks.GRAVEL.setHardness(18F);
		Blocks.GRASS.setHardness(23F);
		Blocks.BRICK_BLOCK.setHardness(35F);
		Blocks.STONEBRICK.setHardness(35F);
		Blocks.STONE_SLAB.setHardness(35F);
		Blocks.STONE_SLAB2.setHardness(35F);
		Blocks.COAL_ORE.setHardness(35F);
		Blocks.IRON_ORE.setHardness(35F);
		Blocks.GOLD_ORE.setHardness(35F);
		Blocks.LAPIS_ORE.setHardness(32F);
		Blocks.REDSTONE_ORE.setHardness(30F);
		Blocks.LIT_REDSTONE_ORE.setHardness(30F);
		Blocks.DIAMOND_ORE.setHardness(38F);
		Blocks.EMERALD_ORE.setHardness(38F);
		Blocks.QUARTZ_ORE.setHardness(35F);
		Blocks.IRON_BLOCK.setHardness(40F);
		Blocks.IRON_BARS.setHardness(35F);
		Blocks.CLAY.setHardness(18F);
		Blocks.CONCRETE.setHardness(39F);

		Blocks.DIRT.setResistance(1F);
		Blocks.STONE.setResistance(10F);
		Blocks.QUARTZ_BLOCK.setResistance(10F);
		Blocks.PLANKS.setResistance(5F);
		Blocks.COBBLESTONE.setResistance(10F);
		Blocks.LOG.setResistance(5F);
		Blocks.LOG2.setResistance(5F);
		Blocks.PLANKS.setResistance(4.5F);
		Blocks.OAK_DOOR.setResistance(5F);
		Blocks.SPRUCE_DOOR.setResistance(5F);
		Blocks.BIRCH_DOOR.setResistance(5F);
		Blocks.JUNGLE_DOOR.setResistance(5F);
		Blocks.ACACIA_DOOR.setResistance(5F);
		Blocks.DARK_OAK_DOOR.setResistance(5F);
		Blocks.IRON_DOOR.setResistance(10F);
		Blocks.SAND.setResistance(1F);
		Blocks.GRAVEL.setResistance(1F);
		Blocks.GRASS.setResistance(1F);
		Blocks.GRASS_PATH.setResistance(1F);
		Blocks.BRICK_BLOCK.setResistance(10F);
		Blocks.STONEBRICK.setResistance(10F);
		Blocks.STONE_SLAB.setResistance(10F);
		Blocks.STONE_SLAB2.setResistance(10F);
		Blocks.COAL_ORE.setResistance(5F);
		Blocks.IRON_ORE.setResistance(5F);
		Blocks.IRON_ORE.setResistance(5F);
		Blocks.GOLD_ORE.setResistance(5F);
		Blocks.LAPIS_ORE.setResistance(5F);
		Blocks.REDSTONE_ORE.setResistance(5F);
		Blocks.LIT_REDSTONE_ORE.setResistance(5F);
		Blocks.DIAMOND_ORE.setResistance(5F);
		Blocks.EMERALD_ORE.setResistance(5F);
		Blocks.QUARTZ_ORE.setResistance(5F);
		Blocks.IRON_BLOCK.setResistance(10F);
		Blocks.IRON_BARS.setResistance(10F);
		Blocks.CLAY.setResistance(1F);
		Blocks.CONCRETE.setResistance(20F);*/
		
		Items.PORKCHOP.setMaxStackSize(1);
		Items.COOKED_PORKCHOP.setMaxStackSize(1);
		Items.BEEF.setMaxStackSize(1);
		Items.COOKED_BEEF.setMaxStackSize(1);
		Items.APPLE.setMaxStackSize(1);;
		Items.BREAD.setMaxStackSize(1);
		Items.GOLDEN_APPLE.setMaxStackSize(1);
		Items.FISH.setMaxStackSize(1);
		Items.COOKED_FISH.setMaxStackSize(1);
		Items.CHICKEN.setMaxStackSize(1);
		Items.COOKED_CHICKEN.setMaxStackSize(1);
		Items.RABBIT.setMaxStackSize(1);
		Items.COOKED_RABBIT.setMaxStackSize(1);
		Items.MUTTON.setMaxStackSize(1);
		Items.COOKED_MUTTON.setMaxStackSize(1);
		Items.ROTTEN_FLESH.setMaxStackSize(1);
	}

	public static void removeVanillaRecipes() {
		RecipeManager.removeRecipe(Blocks.FURNACE);
		RecipeManager.removeRecipe(Blocks.PLANKS);
		RecipeManager.removeRecipe(Blocks.IRON_BARS);
		RecipeManager.removeRecipe(Blocks.IRON_TRAPDOOR);
		RecipeManager.removeRecipe(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
		RecipeManager.removeItem(net.minecraft.init.Items.WOODEN_SWORD);
		RecipeManager.removeItem(net.minecraft.init.Items.WOODEN_AXE);
		RecipeManager.removeItem(net.minecraft.init.Items.WOODEN_PICKAXE);
		RecipeManager.removeItem(net.minecraft.init.Items.WOODEN_SHOVEL);
		RecipeManager.removeItem(net.minecraft.init.Items.WOODEN_HOE);
		RecipeManager.removeItem(net.minecraft.init.Items.STONE_SWORD);
		RecipeManager.removeItem(net.minecraft.init.Items.STONE_AXE);
		RecipeManager.removeItem(net.minecraft.init.Items.STONE_PICKAXE);
		RecipeManager.removeItem(net.minecraft.init.Items.STONE_SHOVEL);
		RecipeManager.removeItem(net.minecraft.init.Items.STONE_HOE);
		RecipeManager.removeRecipe(net.minecraft.init.Items.IRON_INGOT);
		RecipeManager.removeRecipe(net.minecraft.init.Items.GOLD_INGOT);
		RecipeManager.removeRecipe(net.minecraft.init.Blocks.IRON_BLOCK);
		RecipeManager.removeRecipe(net.minecraft.init.Blocks.GOLD_BLOCK);
	}

	public static void addVanillaBlockUpgrades() {
		addVanillaBlockUpgrade(
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, SoundHelper.UPGRADE_WOOD,
				com.nuparu.sevendaystomine.init.ModBlocks.OAK_PLANKS_REINFORCED.getDefaultState(),
				com.nuparu.sevendaystomine.init.ModBlocks.OAK_FRAME.getDefaultState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, SoundHelper.UPGRADE_WOOD,
				com.nuparu.sevendaystomine.init.ModBlocks.BIRCH_PLANKS_REINFORCED.getDefaultState(),
				com.nuparu.sevendaystomine.init.ModBlocks.BIRCH_FRAME.getDefaultState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, SoundHelper.UPGRADE_WOOD,
				com.nuparu.sevendaystomine.init.ModBlocks.SPRUCE_PLANKS_REINFORCED.getDefaultState(),
				com.nuparu.sevendaystomine.init.ModBlocks.SPRUCE_FRAME.getDefaultState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, SoundHelper.UPGRADE_WOOD,
				com.nuparu.sevendaystomine.init.ModBlocks.JUNGLE_PLANKS_REINFORCED.getDefaultState(),
				com.nuparu.sevendaystomine.init.ModBlocks.JUNGLE_FRAME.getDefaultState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, SoundHelper.UPGRADE_WOOD,
				com.nuparu.sevendaystomine.init.ModBlocks.ACACIA_PLANKS_REINFORCED.getDefaultState(),
				com.nuparu.sevendaystomine.init.ModBlocks.ACACIA_FRAME.getDefaultState());
		addVanillaBlockUpgrade(
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK),
				new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) }, SoundHelper.UPGRADE_WOOD,
				com.nuparu.sevendaystomine.init.ModBlocks.DARKOAK_PLANKS_REINFORCED.getDefaultState(),
				com.nuparu.sevendaystomine.init.ModBlocks.DARKOAK_FRAME.getDefaultState());
	}

	public static void addVanillaBlockUpgrade(Block parent, ItemStack[] items, SoundEvent sound, Block result) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent.getDefaultState(), items, sound, result.getDefaultState(),
				(IBlockState) null));
	}

	public static void addVanillaBlockUpgrade(IBlockState parent, ItemStack[] items, SoundEvent sound,
			IBlockState result) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent, items, sound, result, (IBlockState) null));
	}

	public static void addVanillaBlockUpgrade(Block parent, ItemStack[] items, SoundEvent sound, Block result,
			Block prev) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent.getDefaultState(), items, sound, result.getDefaultState(),
				prev.getDefaultState()));
	}

	public static void addVanillaBlockUpgrade(IBlockState parent, ItemStack[] items, SoundEvent sound,
			IBlockState result, IBlockState prev) {
		vanillaUpgrades.add(new VanillaBlockUpgrade(parent, items, sound, result, prev));
	}

	public static VanillaBlockUpgrade getVanillaUpgrade(Block parent) {
		return getVanillaUpgrade(parent.getDefaultState());
	}

	public static VanillaBlockUpgrade getVanillaUpgrade(IBlockState parent) {
		for (VanillaBlockUpgrade upgrade : vanillaUpgrades) {
			if (upgrade.parent == parent)
				return upgrade;
		}
		return null;
	}
	
	public static void addVanillaScrapables() {
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Items.IRON_INGOT,EnumMaterial.IRON).setWeight(6));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Items.GOLD_INGOT,EnumMaterial.GOLD).setWeight(6));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Blocks.IRON_ORE,EnumMaterial.IRON).setWeight(12));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Blocks.GOLD_ORE,EnumMaterial.GOLD).setWeight(12));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Blocks.IRON_BLOCK,EnumMaterial.IRON).setWeight(54));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Blocks.GOLD_BLOCK,EnumMaterial.GOLD).setWeight(54));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Blocks.STONE,EnumMaterial.STONE).setWeight(9));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Blocks.GRAVEL,EnumMaterial.STONE).setWeight(4));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Blocks.COBBLESTONE,EnumMaterial.STONE).setWeight(6));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Items.FLINT,EnumMaterial.STONE).setWeight(2));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Items.COAL,EnumMaterial.CARBON).setWeight(1));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Items.IRON_NUGGET,EnumMaterial.IRON).setWeight(0));
		vanillaScrapables.add(new VanillaScrapableItem(net.minecraft.init.Items.GOLD_NUGGET,EnumMaterial.GOLD).setWeight(0));
		
		ItemUtils.INSTANCE.addSmallestBit(EnumMaterial.CARBON, net.minecraft.init.Items.COAL);
	}
	
	public static VanillaScrapableItem getVanillaScrapable(Item item) {
		for(VanillaScrapableItem vanillaScrapable : vanillaScrapables) {
			if(vanillaScrapable.getItem() == item) {
				return vanillaScrapable;
			}
		}
		return null;
	}

	public static class VanillaBlockUpgrade {

		private IBlockState parent;
		private ItemStack[] items;
		private SoundEvent sound;
		private IBlockState result;
		private IBlockState prev;

		public VanillaBlockUpgrade(IBlockState parent, ItemStack[] items, SoundEvent sound, IBlockState result,
				IBlockState prev) {
			this.parent = parent;
			this.items = items;
			this.sound = sound;
			this.result = result;
			this.prev = prev;
		}

		public IBlockState getParent() {
			return parent;
		}

		public ItemStack[] getItems() {
			return items;
		}

		public SoundEvent getSound() {
			return sound;
		}

		public IBlockState getResult() {
			return result;
		}

		public IBlockState getPrev() {
			return prev;
		}
	}

	public static class VanillaScrapableItem {
		
		private Item item = null;
		private EnumMaterial mat = EnumMaterial.NONE;
		private int weight = 1;
		private boolean canBeScraped = true;
		
		public VanillaScrapableItem(Block block, EnumMaterial mat) {
			this(Item.getItemFromBlock(block),mat);
		}

		public VanillaScrapableItem(Item item, EnumMaterial mat) {
			this.item = item;
			this.mat = mat;
		}
		
		public VanillaScrapableItem setMaterial(EnumMaterial mat) {
			this.mat = mat;
			return this;
		}
		
		public VanillaScrapableItem setScrap() {
			ItemUtils.INSTANCE.addScrapResult(mat, item);
			return this;
		}

		public EnumMaterial getMaterial() {
			return mat;
		}

		public VanillaScrapableItem setWeight(int newWeight) {
			this.weight = newWeight;
			return this;
		}

		public int getWeight() {
			return weight;
		}
		
		public VanillaScrapableItem setCanBeScraped(boolean canBeScraped) {
			this.canBeScraped = canBeScraped;
			return this;
		}

		public boolean canBeScraped() {
			return this.canBeScraped;
		}
		
		public Item getItem() {
			return this.item;
		}
		
	}

}
