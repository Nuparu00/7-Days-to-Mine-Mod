package com.nuparu.sevendaystomine.enchantment;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.ItemGun;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.common.util.EnumHelper;

public class ModEnchantments {
	@ObjectHolder(SevenDaysToMine.MODID+":fast_reload")
	public static final Enchantment fast_reload = null;
	@ObjectHolder(SevenDaysToMine.MODID+":marksman")
	public static final Enchantment marksman = null;
	@ObjectHolder(SevenDaysToMine.MODID+":explosive")
	public static final Enchantment explosive = null;
	@ObjectHolder(SevenDaysToMine.MODID+":sparking")
	public static final Enchantment sparking = null;
	@ObjectHolder(SevenDaysToMine.MODID+":big_mag")
	public static final Enchantment big_mag = null;
	@ObjectHolder(SevenDaysToMine.MODID+":multishot")
	public static final Enchantment multishot = null;
	@ObjectHolder(SevenDaysToMine.MODID+":small_mag")
	public static final Enchantment small_mag = null;
	@ObjectHolder(SevenDaysToMine.MODID+":strabismus")
	public static final Enchantment strabismus = null;
	
	public static final EnumEnchantmentType GUNS = EnumHelper.addEnchantmentType("GUNS", (item) -> item instanceof ItemGun);

	@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Enchantment> event) {
			final IForgeRegistry<Enchantment> registry = event.getRegistry();
			registry.register(new EnchantmentFastReload());
			registry.register(new EnchantmentMarksman());
			registry.register(new EnchantmentExplosive());
			registry.register(new EnchantmentSparking());
			registry.register(new EnchantmentBigMag());
			registry.register(new EnchantmentMultishot());
			registry.register(new EnchantmentSmallMag());
			registry.register(new EnchantmentStrabismus());
		}
	}
}