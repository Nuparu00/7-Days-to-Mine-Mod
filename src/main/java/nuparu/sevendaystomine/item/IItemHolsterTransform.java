package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemHolsterTransform {

	@SideOnly(Side.CLIENT)
	public double getRotationX(ItemStack stack, EntityPlayer player);
	
	@SideOnly(Side.CLIENT)
	public double getRotationY(ItemStack stack, EntityPlayer player);
	
	@SideOnly(Side.CLIENT)
	public double getRotationZ(ItemStack stack, EntityPlayer player);
	
    public void setRotationX(double x);
	
	public void setRotationY(double y);
	
	public void setRotationZ(double z);
}
