package com.nuparu.sevendaystomine.client.sound;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.util.ResourceLocation;


/*
 * Handles all SoundEvents in the mod
 * Highly inspired by Choonster's testmod3 implementation
 */
@ObjectHolder(SevenDaysToMine.MODID)
public class SoundHelper {
    
	@ObjectHolder("upgrade_wood")
	public static final SoundEvent UPGRADE_WOOD = Utils.Null();
	@ObjectHolder("campfire_crackling")
	public static final SoundEvent CAMPFIRE_CRACKLING = Utils.Null();
	@ObjectHolder("heartbeat")
	public static final SoundEvent HEARTBEAT = Utils.Null();
	@ObjectHolder("main_menu_00")
	public static final SoundEvent MENU_00 = Utils.Null();
	@ObjectHolder("main_menu_01")
	public static final SoundEvent MENU_01 = Utils.Null();
	@ObjectHolder("pistol.shot")
	public static final SoundEvent PISTOL_SHOT = Utils.Null();
	@ObjectHolder("pistol.dryshot")
	public static final SoundEvent PISTOL_DRYSHOT = Utils.Null();
	@ObjectHolder("pistol.reload")
	public static final SoundEvent PISTOL_RELOAD = Utils.Null();
	@ObjectHolder("ak47.shot")
	public static final SoundEvent AK47_SHOT = Utils.Null();
	@ObjectHolder("ak47.reload")
	public static final SoundEvent AK47_RELOAD = Utils.Null();
	@ObjectHolder("door.locked")
	public static final SoundEvent DOOR_LOCKED  = Utils.Null();
	@ObjectHolder("camera.take")
	public static final SoundEvent CAMERA_TAKE  = Utils.Null();
	@ObjectHolder("hunting_rifle.shot")
	public static final SoundEvent HUNTING_RIFLE_SHOT = Utils.Null();
	@ObjectHolder("hunting_rifle.reload")
	public static final SoundEvent HUNTING_RIFLE_RELOAD = Utils.Null();
	@ObjectHolder("white_noise")
	public static final SoundEvent WHITE_NOISE = Utils.Null();
	@ObjectHolder("emergency_alert_system")
	public static final SoundEvent EAS = Utils.Null();
	@ObjectHolder("recipe_unlocked")
	public static final SoundEvent RECIPE_UNLOCKED = Utils.Null();
	
	private static List<SoundEvent> sounds = new ArrayList<SoundEvent>();
	
	@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
			final SoundEvent[] soundEvents = {
					createSoundEvent("upgrade_wood"),
					createSoundEvent("campfire_crackling"),
					createSoundEvent("heartbeat"),
					createSoundEvent("main_menu_00"),
					createSoundEvent("main_menu_01"),
					createSoundEvent("pistol.shot"),
					createSoundEvent("pistol.dryshot"),
					createSoundEvent("pistol.reload"),
					createSoundEvent("ak47.shot"),
					createSoundEvent("ak47.reload"),
					createSoundEvent("door.locked"),
					createSoundEvent("camera.take"),
					createSoundEvent("hunting_rifle.shot"),
					createSoundEvent("hunting_rifle.reload"),
					createSoundEvent("white_noise"),
					createSoundEvent("emergency_alert_system"),
					createSoundEvent("recipe_unlocked")
			};

			event.getRegistry().registerAll(soundEvents);
		}

		private static SoundEvent createSoundEvent(final String soundName) {
			final ResourceLocation soundID = new ResourceLocation(SevenDaysToMine.MODID, soundName);
			SoundEvent se = new SoundEvent(soundID).setRegistryName(soundID);
			sounds.add(se);
			return se;
		}
		
	}
	public static SoundEvent getSoundByName(String name) {
		for(SoundEvent sound : sounds) {
			if(sound.getRegistryName().getResourcePath().equals(name)) {
				return sound;
			}
		}
		return null;
	}
}
