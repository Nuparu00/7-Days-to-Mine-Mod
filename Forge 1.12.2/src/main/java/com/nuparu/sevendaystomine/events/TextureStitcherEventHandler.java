package com.nuparu.sevendaystomine.events;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureStitcherEventHandler {
	@SubscribeEvent
	  public void stitcherEventPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation(SevenDaysToMine.MODID,"entity/particles/blood"));
		event.getMap().registerSprite(new ResourceLocation(SevenDaysToMine.MODID,"entity/particles/vomit"));
		event.getMap().registerSprite(new ResourceLocation(SevenDaysToMine.MODID,"items/empty_backpack_slot"));
		event.getMap().registerSprite(new ResourceLocation(SevenDaysToMine.MODID,"items/empty_paper_slot"));
	  }
}
