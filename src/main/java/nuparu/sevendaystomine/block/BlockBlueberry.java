package nuparu.sevendaystomine.block;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.init.ModItems;

public class BlockBlueberry extends BlockFruitBush {

	public BlockBlueberry() {
		
	}
	@Override
    protected Item getCrop()
    {
        return ModItems.BLUEBERRY;
    }

}
