package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.ItemBlockMaterial;

public class BlockMetal extends BlockBase {
	
	private EnumMaterial enumMat = null;
	private int weight = 12;
	
	public BlockMetal() {
		this(Material.ROCK.getMaterialMapColor(), null);
	}
	public BlockMetal(EnumMaterial mat) {
		this(Material.ROCK.getMaterialMapColor(),mat);
	}
	public BlockMetal(EnumMaterial mat, int weight) {
		this(Material.ROCK,Material.ROCK.getMaterialMapColor(),mat, weight);
	}

	public BlockMetal(MapColor color) {
		this(Material.ROCK, color, null, 12);
	}
	
	public BlockMetal(MapColor color, EnumMaterial mat) {
		this(Material.ROCK, color, mat, 12);
	}
	
	public BlockMetal(Material materialIn, MapColor color, EnumMaterial mat, int weight) {
		super(materialIn, color);
		this.enumMat = mat;
		this.weight = weight;
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setSoundType(SoundType.METAL);
	}
	
	public ItemBlock createItemBlock() {
		ItemBlockMaterial item = new ItemBlockMaterial(this);
		item.setMaterial(enumMat);
		item.setCanBeScraped(true);
		item.setWeight(weight);
		return item;
	}

	public static ItemBlock createItemBlock(Block block) {
		if (!(block instanceof BlockMetal))
			return null;
		BlockMetal metal = (BlockMetal) block;
		ItemBlockMaterial item = new ItemBlockMaterial(metal);
		item.setMaterial(metal.enumMat);
		item.setCanBeScraped(true);
		item.setWeight(metal.weight);
		return item;
	}

}
