package nuparu.sevendaystomine.item;

import net.minecraft.item.ItemStack;

public interface IQuality {

	public int getQuality(ItemStack stack);

	public EnumQuality getQualityTierFromStack(ItemStack stack);

	public EnumQuality getQualityTierFromInt(int quality);

	public void setQuality(ItemStack stack, int quality);

}
