package nuparu.sevendaystomine.client.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.elements.Animation;

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
	
	public static final Animation WINXP_LOADING = new Animation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_6.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_7.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_8.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_9.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_10.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_11.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winXp/frame_12.png")});
	
	public static final Animation WIN8_LOADING = new Animation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win8/frame_0.png")});
	
	public static final Animation MAC_LOADING = new Animation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_6.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_7.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_8.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_9.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_10.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_11.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_12.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_13.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_14.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_15.png")});
	
	public static final Animation LINUX_LOADING = new Animation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/linux/frame_0.png")});
}
