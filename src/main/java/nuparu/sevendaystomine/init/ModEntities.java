package nuparu.sevendaystomine.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.entity.item.*;
import nuparu.sevendaystomine.world.entity.zombie.*;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            SevenDaysToMine.MODID);


    //Items
    public static final RegistryObject<EntityType<StoneSpearEntity>> STONE_SPEAR = ENTITIES.register(
            "stone_spear",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<StoneSpearEntity>) StoneSpearEntity::new,
                            MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "stone_spear").toString()));

    public static final RegistryObject<EntityType<BoneSpearEntity>> BONE_SPEAR = ENTITIES.register(
            "bone_spear",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<BoneSpearEntity>) BoneSpearEntity::new,
                            MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "bone_spear").toString()));

    public static final RegistryObject<EntityType<LootableCorpseEntity>> LOOTABLE_CORPSE = ENTITIES.register(
            "lootable_corpse",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<LootableCorpseEntity>) LootableCorpseEntity::new,
                            MobCategory.MISC)
                    .sized(1.5f, 0.45f).fireImmune().setTrackingRange(128).setShouldReceiveVelocityUpdates(true).setUpdateInterval(2)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "lootable_corpse").toString()));


    public static final RegistryObject<EntityType<MountableBlockEntity>> MOUNTABLE_BLOCK = ENTITIES.register(
            "mountable_block",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<MountableBlockEntity>) MountableBlockEntity::new,
                            MobCategory.MISC)
                    .sized(0.01F, 0.01F)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "mountable_block").toString()));

    public static final RegistryObject<EntityType<AirdropEntity>> AIRDROP = ENTITIES.register(
            "airdrop",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<AirdropEntity>) AirdropEntity::new,
                            MobCategory.MISC)
                    .sized(1f, 1f).fireImmune().setTrackingRange(256).clientTrackingRange(128)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "airdorp").toString()));

    public static final RegistryObject<EntityType<MinibikeEntity>> MINIBIKE = ENTITIES.register(
            "minibike",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<MinibikeEntity>) MinibikeEntity::new,
                            MobCategory.MISC)
                    .sized(0.8F, 0.75F).setTrackingRange(128).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).updateInterval(1)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "minibike").toString()));
    public static final RegistryObject<EntityType<CarEntity>> CAR = ENTITIES.register(
            "car",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<CarEntity>) CarEntity::new,
                            MobCategory.MISC)
                    .sized(2F, 1.5F).setTrackingRange(128).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).updateInterval(1)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "car").toString()));


    //Zombies

    public static final RegistryObject<EntityType<ReanimatedCorpseEntity>> REANIMATED_CORPSE = ENTITIES.register(
            "reanimated_corpse",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<ReanimatedCorpseEntity>) ReanimatedCorpseEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "reanimated_corpse").toString()));

    public static final RegistryObject<EntityType<FrigidHunterEntity>> FRIGID_HUNTER = ENTITIES.register(
            "frigid_hunter",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<FrigidHunterEntity>) FrigidHunterEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "frigid_hunter").toString()));

    public static final RegistryObject<EntityType<FrostbittenWorkerEntity>> FROSTBITTEN_WORKER = ENTITIES.register(
            "frostbitten_worker",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<FrostbittenWorkerEntity>) FrostbittenWorkerEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "frostbitten_worker").toString()));

    public static final RegistryObject<EntityType<PlaguedNurseEntity>> PLAGUED_NURSE = ENTITIES.register(
            "plagued_nurse",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<PlaguedNurseEntity>) PlaguedNurseEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "plagued_nurse").toString()));

    public static final RegistryObject<EntityType<MinerZombieEntity>> MINER_ZOMBIE = ENTITIES.register(
            "miner_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<MinerZombieEntity>) MinerZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "miner_zombie").toString()));

    public static final RegistryObject<EntityType<BurntZombieEntity>> BURNT_ZOMBIE = ENTITIES.register(
            "burnt_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<BurntZombieEntity>) BurntZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f).fireImmune()
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "burnt_zombie").toString()));

    public static final RegistryObject<EntityType<SoulBurntZombieEntity>> SOUL_BURNT_ZOMBIE = ENTITIES.register(
            "soul_burnt_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<SoulBurntZombieEntity>) SoulBurntZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f).fireImmune()
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "soul_burnt_zombie").toString()));


    public static final RegistryObject<EntityType<BloatedZombieEntity>> BLOATED_ZOMBIE = ENTITIES.register(
            "bloated_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<BloatedZombieEntity>) BloatedZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "bloated_zombie").toString()));

    public static final RegistryObject<EntityType<SoldierZombieEntity>> SOLDIER_ZOMBIE = ENTITIES.register(
            "soldier_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<SoldierZombieEntity>) SoldierZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "soldier_zombie").toString()));

    public static final RegistryObject<EntityType<InfectedSurvivorEntity>> INFECTED_SURVIVOR = ENTITIES.register(
            "infected_survivor",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<InfectedSurvivorEntity>) InfectedSurvivorEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "infected_survivor").toString()));

    public static final RegistryObject<EntityType<FrozenLumberjackEntity>> FROZEN_LUMBERJACK = ENTITIES.register(
            "frozen_lumberjack",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<FrozenLumberjackEntity>) FrozenLumberjackEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "frozen_lumberjack").toString()));

    public static final RegistryObject<EntityType<FeralZombieEntity>> FERAL_ZOMBIE = ENTITIES.register(
            "feral_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<FeralZombieEntity>) FeralZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "feral_zombie").toString()));

    public static final RegistryObject<EntityType<TwistedZombieEntity>> TWISTED_ZOMBIE = ENTITIES.register(
            "twisted_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<TwistedZombieEntity>) TwistedZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.9f)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "twisted_zombie").toString()));

    public static final RegistryObject<EntityType<CrawlerZombieEntity>> CRAWLER_ZOMBIE = ENTITIES.register(
            "crawler_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<CrawlerZombieEntity>) CrawlerZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.9f, 0.33F)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "crawler_zombie").toString()));

    public static final RegistryObject<EntityType<ZombieWolfEntity>> ZOMBIE_WOLF = ENTITIES.register(
            "zombie_wolf",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<ZombieWolfEntity>) ZombieWolfEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 0.85F)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "zombie_wolf").toString()));

    public static final RegistryObject<EntityType<ZombiePigEntity>> ZOMBIE_PIG = ENTITIES.register(
            "zombie_pig",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<ZombiePigEntity>) ZombiePigEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.9F, 0.9F)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "zombie_pig").toString()));

    public static final RegistryObject<EntityType<SpiderZombieEntity>> SPIDER_ZOMBIE = ENTITIES.register(
            "spider_zombie",
            () -> EntityType.Builder
                    .of((EntityType.EntityFactory<SpiderZombieEntity>) SpiderZombieEntity::new,
                            MobCategory.MONSTER)
                    .sized(0.6F, 1.5F)
                    .build(new ResourceLocation(SevenDaysToMine.MODID, "spider_zombie").toString()));
}