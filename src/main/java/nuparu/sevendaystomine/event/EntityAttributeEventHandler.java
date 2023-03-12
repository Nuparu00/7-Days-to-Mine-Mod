package nuparu.sevendaystomine.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.world.entity.item.CarEntity;
import nuparu.sevendaystomine.world.entity.item.MinibikeEntity;
import nuparu.sevendaystomine.world.entity.zombie.*;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributeEventHandler {
    @SubscribeEvent
    public static void onPlayerItemUseFinish(EntityAttributeCreationEvent event) {
        event.put(ModEntities.REANIMATED_CORPSE.get(), ReanimatedCorpseEntity.createAttributes().build());
        event.put(ModEntities.FRIGID_HUNTER.get(), FrigidHunterEntity.createAttributes().build());
        event.put(ModEntities.FROSTBITTEN_WORKER.get(), FrostbittenWorkerEntity.createAttributes().build());
        event.put(ModEntities.PLAGUED_NURSE.get(), PlaguedNurseEntity.createAttributes().build());
        event.put(ModEntities.MINER_ZOMBIE.get(), MinerZombieEntity.createAttributes().build());
        event.put(ModEntities.BURNT_ZOMBIE.get(), BurntZombieEntity.createAttributes().build());
        event.put(ModEntities.SOUL_BURNT_ZOMBIE.get(), SoulBurntZombieEntity.createAttributes().build());
        event.put(ModEntities.BLOATED_ZOMBIE.get(), BloatedZombieEntity.createAttributes().build());
        event.put(ModEntities.SOLDIER_ZOMBIE.get(), SoldierZombieEntity.createAttributes().build());
        event.put(ModEntities.INFECTED_SURVIVOR.get(), InfectedSurvivorEntity.createAttributes().build());
        event.put(ModEntities.FROZEN_LUMBERJACK.get(), FrozenLumberjackEntity.createAttributes().build());
        event.put(ModEntities.FERAL_ZOMBIE.get(), FeralZombieEntity.createAttributes().build());
        event.put(ModEntities.TWISTED_ZOMBIE.get(), TwistedZombieEntity.createAttributes().build());
        event.put(ModEntities.CRAWLER_ZOMBIE.get(), CrawlerZombieEntity.createAttributes().build());
        event.put(ModEntities.ZOMBIE_WOLF.get(), ZombieWolfEntity.createAttributes().build());
        event.put(ModEntities.ZOMBIE_PIG.get(), ZombiePigEntity.createAttributes().build());
        event.put(ModEntities.SPIDER_ZOMBIE.get(), SpiderZombieEntity.createAttributes().build());
        event.put(ModEntities.MINIBIKE.get(), MinibikeEntity.createAttributes().build());
        event.put(ModEntities.CAR.get(), CarEntity.createAttributes().build());
    }
}

