package nuparu.sevendaystomine.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockBurntPlanksFence extends BlockFenceBase {

	public BlockBurntPlanksFence() {
		super(Material.WOOD, MapColor.BLACK);
		setSoundType(SoundType.WOOD);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

}
