package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;

public class ItemBlueprint extends ItemRecipeBook {

	public ItemBlueprint(ResourceLocation data, String recipe) {
		super(data, recipe);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(SevenDaysToMine.proxy.localize(this.getRegistryName().getResourcePath() + ".title"));
		if(!GuiScreen.isShiftKeyDown()) return;
		if(Minecraft.getMinecraft().player == null) return;
		boolean known = CapabilityHelper.getExtendedPlayer(Minecraft.getMinecraft().player).hasRecipe(getRecipe());
		tooltip.add(known ? TextFormatting.GREEN +  SevenDaysToMine.proxy.localize("stat.known.name") : TextFormatting.RED + SevenDaysToMine.proxy.localize("stat.unknown.name"));
		tooltip.add(isRead(stack) ? TextFormatting.GREEN +  SevenDaysToMine.proxy.localize("stat.used.name") : TextFormatting.RED + SevenDaysToMine.proxy.localize("stat.unused.name"));
	}
}
