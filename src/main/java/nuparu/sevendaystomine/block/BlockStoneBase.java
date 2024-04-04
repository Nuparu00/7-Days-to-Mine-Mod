package nuparu.sevendaystomine.block;

import net.minecraft.block.material.Material;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockStoneBase extends BlockBase {

	public BlockStoneBase() {
		super(Material.ROCK);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHardness(2);
		setResistance(6);
	}

}
