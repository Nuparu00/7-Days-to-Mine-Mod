package nuparu.sevendaystomine.block;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.init.ModItems;

public class BlockBaneberry extends BlockFruitBush {

	public BlockBaneberry() {
		
	}
	@Override
    protected Item getCrop()
    {
        return ModItems.BANEBERRY;
    }
}
