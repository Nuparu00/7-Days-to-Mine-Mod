package nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.block.BlockStoneBrickWall;

public class ItemBlockStonebrickWall extends ItemBlockMetadata {

	public ItemBlockStonebrickWall(Block block) {
		super(block);
	}
	
	@Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
		return super.getUnlocalizedName() + "." + BlockStoneBrickWall.EnumVariant.byMetadata(itemStack.getMetadata()).getUnlocalizedName();
    }

}
