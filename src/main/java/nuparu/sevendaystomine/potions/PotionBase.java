package nuparu.sevendaystomine.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class PotionBase extends Potion {

	public static final ResourceLocation textureAtlas = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/status-icons.png");

	public PotionBase(boolean badEffect, int color) {
		super(badEffect, color);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().renderEngine.bindTexture(textureAtlas);
		return true;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;

	}
}