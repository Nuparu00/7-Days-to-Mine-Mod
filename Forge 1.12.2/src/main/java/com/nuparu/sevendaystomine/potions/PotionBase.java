package com.nuparu.sevendaystomine.potions;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.Potion;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.client.Minecraft;

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