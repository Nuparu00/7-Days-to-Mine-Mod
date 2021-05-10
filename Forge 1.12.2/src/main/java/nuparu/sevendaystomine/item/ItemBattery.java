package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.electricity.IBattery;

public class ItemBattery extends ItemQualityScrapable implements IBattery{

	public static final int BASE_VOLTAGE = 2500;

	public ItemBattery(EnumMaterial mat, int weight) {
		super(mat, weight);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				setQualityForPlayer(stack, player);
			}
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound nbt = stack.getTagCompound();
			nbt.setLong("voltage", getCapacity(stack,null));
			items.add(stack);
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		return (nbt != null && nbt.hasKey("voltage", Constants.NBT.TAG_LONG));
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack The current ItemStack
	 * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged /
	 *         empty bar)
	 */
	public double getDurabilityForDisplay(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("voltage", Constants.NBT.TAG_LONG)) {
			return 1-((double) nbt.getLong("voltage") / getCapacity(stack,null));
		}
		return 0d;
	}

	@Override
	public long getCapacity(ItemStack stack, @Nullable World world) {
		return (long) (BASE_VOLTAGE*(1+(double)(ItemQuality.getQualityForStack(stack)/ModConfig.players.maxQuality)));
	}

	@Override
	public long getVoltage(ItemStack stack,@Nullable World world) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("voltage", Constants.NBT.TAG_LONG)) {
			return nbt.getLong("voltage");
		}
		return 0;
	}

	@Override
	public void setVoltage(ItemStack stack,@Nullable World world, long voltage) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setLong("voltage", Math.max(0,voltage));
	}

	@Override
	public long tryToAddVoltage(ItemStack stack,@Nullable World world,long deltaVoltage) {
		if(deltaVoltage < 0) return deltaVoltage;
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null || !nbt.hasKey("voltage",Constants.NBT.TAG_LONG)) {
			setVoltage(stack,world,deltaVoltage);
		}
		long voltage = nbt.getLong("voltage");
		long difference = getCapacity(stack,world)-voltage;
		long toAdd = Math.min(deltaVoltage, difference);
		nbt.setLong("voltage", voltage+toAdd);
		return deltaVoltage-toAdd;
	}
	
	@Override
	public void drainVoltage(ItemStack stack,@Nullable World world,long deltaVoltage) {
		if(deltaVoltage < 0) return;
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null || !nbt.hasKey("voltage",Constants.NBT.TAG_LONG)) {
			return;
		}
		nbt.setLong("voltage", Math.max(0,nbt.getLong("voltage")-deltaVoltage));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(this.getVoltage(stack, worldIn) + "/" + this.getCapacity(stack, worldIn)+" J");
	}
}
