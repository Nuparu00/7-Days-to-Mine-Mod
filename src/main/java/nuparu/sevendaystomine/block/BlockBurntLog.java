package nuparu.sevendaystomine.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockBurntLog extends BlockPillarBase {

	public BlockBurntLog() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setHardness(2F);
		setResistance(10F);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

}
