package com.nuparu.sevendaystomine.enchantment;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(SevenDaysToMine.MODID)
public class ModEnchantments {
	public static final Enchantment fast_reload = null;
	public static final Enchantment marksman = null;

	@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Enchantment> event) {
			final IForgeRegistry<Enchantment> registry = event.getRegistry();
			registry.register(new EnchantmentFastReload());
			registry.register(new EnchantmentMarksman());
		}
	}
}