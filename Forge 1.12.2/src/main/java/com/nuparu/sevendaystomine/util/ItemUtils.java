package com.nuparu.sevendaystomine.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.EnumQuality;

public class ItemUtils {

	public static ItemUtils INSTANCE = new ItemUtils();
	public HashMap<EnumMaterial, Item> scrapResults = new HashMap<EnumMaterial, Item>();
	public HashMap<EnumMaterial, Item> smallestBit = new HashMap<EnumMaterial, Item>();
	//func_186463_a
	private static final Method m_shuffleItems = ObfuscationReflectionHelper.findMethod(LootTable.class, "func_186463_a", void.class, List.class, int.class, Random.class);

	public void addScrapResult(EnumMaterial mat, Item item) {
		scrapResults.put(mat, item);
		addSmallestBit(mat,item);
	}
	
	public void addSmallestBit(EnumMaterial mat, Item item) {
		smallestBit.put(mat, item);
	}

	public Item getScrapResult(EnumMaterial mat) {
		for (Map.Entry<EnumMaterial, Item> entry : scrapResults.entrySet()) {
			if (mat == entry.getKey()) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public Item getSmallestBit(EnumMaterial mat) {
		for (Map.Entry<EnumMaterial, Item> entry : smallestBit.entrySet()) {
			if (mat == entry.getKey()) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public static int getQuality(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			if (nbt.hasKey("Quality")) {
				return nbt.getInteger("Quality");
			}
		}
		return 0;
	}

	public static EnumQuality getQualityTierFromStack(ItemStack stack) {
		return getQualityTierFromInt(getQuality(stack));
	}

	public static EnumQuality getQualityTierFromInt(int quality) {
		return EnumQuality.getFromQuality(quality);
	}
	
	public static void fillWithLoot(IItemHandler inv, ResourceLocation res, World world, Random rand) {
		if(world.isRemote) return;
		LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(res);
	    LootContext context = new LootContext.Builder((WorldServer)world).build();
	    
	    final List<ItemStack> items = lootTable.generateLootForPools(rand, context);
        final List<Integer> slots = getRandomEmptySlots(inv, rand);
        
        try {
			m_shuffleItems.invoke(lootTable, items, slots.size(), rand);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
        for(ItemStack stack : items) {
        	if(slots.isEmpty()) break;
        	if(stack.isEmpty()) continue;
        	
        	int slot = slots.remove(slots.size()-1);
        	ItemStack rest = inv.insertItem(slot, stack, false);
        }
	}
	
	public static List<Integer> getRandomEmptySlots(IItemHandler inv, Random rand){
		final List<Integer> slots = new ArrayList<Integer>();
		for(int i = 0; i < inv.getSlots();i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) {
				slots.add(i);
			}
		}
		return slots;
	}

	public static void fillWithLoot(IInventory inv, ResourceLocation res, World world, Random rand) {
		if(world.isRemote) return;
		LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(res);
	    LootContext context = new LootContext.Builder((WorldServer)world).build();
	    
	    final List<ItemStack> items = lootTable.generateLootForPools(rand, context);
        final List<Integer> slots = getRandomEmptySlots(inv, rand);
        
        try {
			m_shuffleItems.invoke(lootTable, items, slots.size(), rand);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
        for(ItemStack stack : items) {
        	if(slots.isEmpty()) break;
        	if(stack.isEmpty()) continue;
        	
        	int slot = slots.remove(slots.size()-1);
        	inv.setInventorySlotContents(slot, stack);
        }
	}
	
	public static List<Integer> getRandomEmptySlots(IInventory inv, Random rand){
		final List<Integer> slots = new ArrayList<Integer>();
		for(int i = 0; i < inv.getSizeInventory() ;i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) {
				slots.add(i);
			}
		}
		return slots;
	}
}