package com.nuparu.sevendaystomine.block;

import java.util.Random;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.ItemBlockMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends BlockBase {

	private EnumMaterial enumMat = null;
	private int weight = 12;
	
	public BlockOre() {
		this(Material.ROCK.getMaterialMapColor(), null);
	}
	public BlockOre(EnumMaterial mat) {
		this(Material.ROCK.getMaterialMapColor(),mat);
	}
	public BlockOre(EnumMaterial mat, int weight) {
		this(Material.ROCK,Material.ROCK.getMaterialMapColor(),mat, weight);
	}

	public BlockOre(MapColor color) {
		this(Material.ROCK, color, null, 12);
	}
	
	public BlockOre(MapColor color, EnumMaterial mat) {
		this(Material.ROCK, color, mat, 12);
	}
	
	public BlockOre(Material materialIn, MapColor color, EnumMaterial mat, int weight) {
		super(materialIn, color);
		this.enumMat = mat;
		this.weight = weight;
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}
	@Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
        {
            int i = 0;

            if (this == Blocks.COAL_ORE)
            {
                i = MathHelper.getInt(rand, 0, 2);
            }
            else if (this == Blocks.DIAMOND_ORE)
            {
                i = MathHelper.getInt(rand, 3, 7);
            }
            else if (this == Blocks.EMERALD_ORE)
            {
                i = MathHelper.getInt(rand, 3, 7);
            }
            else if (this == Blocks.LAPIS_ORE)
            {
                i = MathHelper.getInt(rand, 2, 5);
            }
            else if (this == Blocks.QUARTZ_ORE)
            {
                i = MathHelper.getInt(rand, 2, 5);
            }

            return i;
        }
        return 0;
    }
	
	public ItemBlock createItemBlock() {
		ItemBlockMaterial item = new ItemBlockMaterial(this);
		item.setMaterial(enumMat);
		item.setCanBeScraped(true);
		item.setWeight(weight);
		return item;
	}

	public static ItemBlock createItemBlock(Block block) {
		if (!(block instanceof BlockOre))
			return null;
		BlockOre ore = (BlockOre) block;
		ItemBlockMaterial item = new ItemBlockMaterial(ore);
		item.setMaterial(ore.enumMat);
		item.setCanBeScraped(true);
		item.setWeight(ore.weight);
		return item;
	}
}
