package nuparu.sevendaystomine.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.horde.HordeManager;
import nuparu.sevendaystomine.world.level.WorldData;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class ServerTickEventHandler {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) return;
        HordeManager.getOrCreate(event.getServer()).tick();
        WorldData.getOrCreate(event.getServer()).tick();
    }
}
