package nuparu.sevendaystomine.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;

public class PotionInfection extends PotionBase {

	public PotionInfection(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(2, 0);
		setRegistryName(SevenDaysToMine.MODID, "infection");
		setPotionName("effect." + getRegistryName().getResourcePath());
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "96a8df9e-bf44-11e7-abc4-cec278b6b50a",
				-0.1D, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, net.minecraft.client.gui.Gui gui, int x, int y, float z) {
		super.renderInventoryEffect(effect, gui, x, y, z);
		if (effect.getAmplifier() > 3) {
			String s1 = I18n.format(effect.getPotion().getName());
			RenderUtils.drawString(I18n.format("enchantment.level."+(effect.getAmplifier()+1)), x + 28 + Minecraft.getMinecraft().fontRenderer.getStringWidth(s1 + " "), y + 6,
					0xffffff, true);
		}
	}

}
