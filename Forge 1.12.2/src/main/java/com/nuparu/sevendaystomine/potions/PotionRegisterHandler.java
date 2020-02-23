package com.nuparu.sevendaystomine.potions;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.potion.Potion;

@Mod.EventBusSubscriber
public class PotionRegisterHandler {
@SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
         Potions.register(event.getRegistry());
    }
}