package com.nuparu.sevendaystomine.util.client;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.Animation;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Animations {
	public static final Animation WIN10_LOADING = new Animation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_6.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_7.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_8.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_9.png") });

	public static final Animation WIN7_LOADING = new Animation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_1.png") });
}
