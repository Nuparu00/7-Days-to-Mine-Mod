package nuparu.sevendaystomine.event;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.json.drink.DrinkDataManager;
import nuparu.sevendaystomine.json.salvage.SalvageDataManager;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.json.upgrade.UpgradeDataManager;
import nuparu.sevendaystomine.json.upgrader.UpgraderToolDataManager;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataEventHandler {

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(ScrapDataManager.INSTANCE);
        event.addListener(DrinkDataManager.INSTANCE);
        event.addListener(UpgradeDataManager.INSTANCE);
        event.addListener(UpgraderToolDataManager.INSTANCE);
        event.addListener(SalvageDataManager.INSTANCE);

    }
}