package nuparu.sevendaystomine.potions;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PotionRegisterHandler {
@SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
         Potions.register(event.getRegistry());
    }
}