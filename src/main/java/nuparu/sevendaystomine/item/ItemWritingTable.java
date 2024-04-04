package nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemWritingTable extends ItemBlock {
	public ItemWritingTable(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
    
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
