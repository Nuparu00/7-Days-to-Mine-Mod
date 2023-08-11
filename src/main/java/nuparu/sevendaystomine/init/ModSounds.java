package nuparu.sevendaystomine.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModSounds {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
			SevenDaysToMine.MODID);

	public static final RegistryObject<SoundEvent> UPGRADE_WOOD = SOUNDS.register("upgrade_wood",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "upgrade_wood")));
	public static final RegistryObject<SoundEvent> CAMPFIRE_CRACKLING = SOUNDS.register("campfire_crackling",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "campfire_crackling")));
	public static final RegistryObject<SoundEvent> HEARTBEAT = SOUNDS.register("heartbeat",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "heartbeat")));
	public static final RegistryObject<SoundEvent> PISTOL_SHOT = SOUNDS.register("pistol.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "pistol.shot")));
	public static final RegistryObject<SoundEvent> PISTOL_DRYSHOT = SOUNDS.register("pistol.dryshot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "dryshot")));
	public static final RegistryObject<SoundEvent> PISTOL_RELOAD = SOUNDS.register("pistol.reload",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "pistol.reload")));
	public static final RegistryObject<SoundEvent> AK47_SHOT = SOUNDS.register("ak47.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "ak47.shot")));
	public static final RegistryObject<SoundEvent> AK47_RELOAD = SOUNDS.register("ak47.reload",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "ak47.reload")));
	public static final RegistryObject<SoundEvent> DOOR_LOCKED = SOUNDS.register("door.locked",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "door.locked")));
	public static final RegistryObject<SoundEvent> CAMERA_TAKE = SOUNDS.register("camera.take",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "camera.take")));
	public static final RegistryObject<SoundEvent> HUNTING_RIFLE_SHOT = SOUNDS.register("hunting_rifle.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "hunting_rifle.shot")));
	public static final RegistryObject<SoundEvent> HUNTING_RIFLE_RELOAD = SOUNDS.register("hunting_rifle.reload",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "hunting_rifle.reload")));
	public static final RegistryObject<SoundEvent> WHITE_NOISE = SOUNDS.register("white_noise",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "white_noise")));
	public static final RegistryObject<SoundEvent> EAS = SOUNDS.register("emergency_alert_system",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "emergency_alert_system")));
	public static final RegistryObject<SoundEvent> RECIPE_UNLOCKED = SOUNDS.register("recipe_unlocked",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "recipe_unlocked")));
	public static final RegistryObject<SoundEvent> SHOTGUN_SHOT = SOUNDS.register("shotgun.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "shotgun.shot")));
	public static final RegistryObject<SoundEvent> MP5_SHOT = SOUNDS.register("mp5.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "mp5.shot")));
	public static final RegistryObject<SoundEvent> FLAMETHROWER_SHOT = SOUNDS.register("flamethrower.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "flamethrower.shot")));
	public static final RegistryObject<SoundEvent> SAFE_UNLOCK = SOUNDS.register("safe.unlock",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "safe.unlock")));
	public static final RegistryObject<SoundEvent> HUMAN_HURT = SOUNDS.register("entity.human.hurt",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "entity.human.hurt")));
	public static final RegistryObject<SoundEvent> HORDE = SOUNDS.register("horde",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "horde")));
	public static final RegistryObject<SoundEvent> GAS_LEAK = SOUNDS.register("gas_leak",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "gas_leak")));
	public static final RegistryObject<SoundEvent> CHAINSAW_IDLE = SOUNDS.register("chainsaw.idle",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "chainsaw.idle")));
	public static final RegistryObject<SoundEvent> CHAINSAW_CUT = SOUNDS.register("chainsaw.cut",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "chainsaw.cut")));
	public static final RegistryObject<SoundEvent> HONK = SOUNDS.register("honk",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "honk")));
	public static final RegistryObject<SoundEvent> MINIBIKE_IDLE = SOUNDS.register("minibike.idle",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "minibike.idle")));
	public static final RegistryObject<SoundEvent> CAR_ACCELERATE = SOUNDS.register("car.accelerate",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "car.accelerate")));
	public static final RegistryObject<SoundEvent> MAGNUM_SHOT = SOUNDS.register("magnum.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "magnum.shot")));
	public static final RegistryObject<SoundEvent> M4_SHOT = SOUNDS.register("m4.shot",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "m4.shot")));
	public static final RegistryObject<SoundEvent> MENU_DEFAULT = SOUNDS.register("main_menu_00",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "main_menu_00")));
	public static final RegistryObject<SoundEvent> MENU_REVERSE = SOUNDS.register("main_menu_01",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "main_menu_01")));
	public static final RegistryObject<SoundEvent> CAR_OPEN = SOUNDS.register("car.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "car.open")));
	public static final RegistryObject<SoundEvent> CAR_CLOSE = SOUNDS.register("car.close",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "car.close")));
	public static final RegistryObject<SoundEvent> GARBAGE_OPEN = SOUNDS.register("garbage.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "garbage.open")));
	public static final RegistryObject<SoundEvent> DRAWER_OPEN = SOUNDS.register("drawer.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "drawer.open")));
	public static final RegistryObject<SoundEvent> DRAWER_CLOSE= SOUNDS.register("drawer.close",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "drawer.close")));
	public static final RegistryObject<SoundEvent> FILE_CABINET_OPEN = SOUNDS.register("file_cabinet.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "file_cabinet.open")));
	public static final RegistryObject<SoundEvent> FILE_CABINET_CLOSE= SOUNDS.register("file_cabinet.close",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "file_cabinet.close")));
	public static final RegistryObject<SoundEvent> CABINET_OPEN = SOUNDS.register("cabinet.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "cabinet.open")));
	public static final RegistryObject<SoundEvent> CABINET_CLOSE= SOUNDS.register("cabinet.close",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "cabinet.close")));
	public static final RegistryObject<SoundEvent> FRIDGE_OPEN = SOUNDS.register("fridge.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "fridge.open")));
	public static final RegistryObject<SoundEvent> FRIDGE_CLOSE= SOUNDS.register("fridge.close",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "fridge.close")));

	public static final RegistryObject<SoundEvent> SOLAR_PANEL_HUM= SOUNDS.register("solar_panel_hum",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SevenDaysToMine.MODID, "solar_panel_hum")));
}
