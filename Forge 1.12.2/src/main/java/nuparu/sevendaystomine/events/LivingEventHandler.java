package nuparu.sevendaystomine.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockMercury;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityBandit;
import nuparu.sevendaystomine.entity.EntityCar;
import nuparu.sevendaystomine.entity.EntityHuman;
import nuparu.sevendaystomine.entity.EntityMinibike;
import nuparu.sevendaystomine.entity.EntityZombieBase;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.EnumModParticleType;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public class LivingEventHandler {

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase livingEntity = event.getEntityLiving();
		World world = livingEntity.world;
		if (livingEntity.getHealth() < livingEntity.getMaxHealth()
				&& world.getBlockState(new BlockPos(livingEntity)).getBlock() instanceof BlockMercury) {
			livingEntity.addPotionEffect(new PotionEffect(Potions.mercuryPoison, 240));
		}
	}

	@SubscribeEvent
	public void onEntityAttack(LivingAttackEvent event) {
		DamageSource source = event.getSource();
		float amount = event.getAmount();
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;

		if (entity instanceof EntityMinibike || entity instanceof EntityCar)
			return;

		if (entity.getIsInvulnerable()) {
			return;
		}
		if (amount < 2)
			return;

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.isCreative() || player.isSpectator()) {
				return;
			}
			if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityZombieBase) {
				if (world.rand
						.nextInt(ModConfig.players.infectionChanceModifier * (getArmorItemsCount(player) + 1)) == 0) {
					Utils.infectPlayer(player, 0);
				}
			}
		}

		if (source == DamageSource.DROWN || source == DamageSource.FALL || source == DamageSource.HOT_FLOOR
				|| source == DamageSource.ON_FIRE || source == DamageSource.OUT_OF_WORLD
				|| source == DamageSource.STARVE || source == DamageSource.WITHER || source == DamageSource.MAGIC) {
			return;
		}

		if (entity instanceof AbstractSkeleton || entity instanceof EntitySquid || entity instanceof EntitySnowman
				|| entity instanceof EntityGuardian || entity instanceof EntityWither || entity instanceof EntityDragon
				|| entity instanceof EntityBlaze || entity instanceof EntityVex || entity instanceof EntitySlime
				|| entity instanceof EntityGolem || entity instanceof EntityEnderman
				|| entity instanceof EntityEndermite) {
			return;
		}

		if (world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isRemote
				&& amount >= (entity.getMaxHealth() / 100 * 20)
				&& world.rand.nextInt(ModConfig.mobs.bleedingChanceModifier * (getArmorItemsCount(entity) + 1)) == 0) {
			PotionEffect effect = new PotionEffect(Potions.bleeding, Integer.MAX_VALUE, 1, false, false);
			effect.setCurativeItems(new ArrayList<ItemStack>());
			entity.addPotionEffect(effect);
		}
		if (amount > 0) {
			for (int i = 0; i < (int) Math
					.round(MathUtils.getDoubleInRange(1, 5) * SevenDaysToMine.proxy.getParticleLevel()); i++) {
				double x = entity.posX + MathUtils.getDoubleInRange(-1, 1) * entity.width;
				double y = entity.posY + MathUtils.getDoubleInRange(0, 1) * entity.height;
				double z = entity.posZ + MathUtils.getDoubleInRange(-1, 1) * entity.width;
				SevenDaysToMine.proxy.spawnParticle(world, EnumModParticleType.BLOOD, x, y, z,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d) / 7d,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d);
			}
		}
	}

	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (ModConfig.players.fragileLegs && living instanceof EntityPlayer && !living.world.isRemote) {
			if (event.getDistance() > 4
					&& living.world.rand.nextFloat() * (Math.max(256 - (event.getDistance() * 25), 0)) <= 1) {
				living.removePotionEffect(Potions.splintedLeg);
				PotionEffect effect = new PotionEffect(Potions.brokenLeg,
						(int) (MathUtils.getFloatInRange(0.5f, 1.5f) * (500 * event.getDistance())), 0, false, false);
				effect.setCurativeItems(new ArrayList<ItemStack>());
				living.addPotionEffect(effect);
			}
		}

	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityPlayer || living instanceof EntityHuman || living instanceof EntityVillager) {
			World world = living.world;
			List<EntityZombieBase> list = world.getEntitiesWithinAABB(EntityZombieBase.class,
					new AxisAlignedBB(living.posX, living.posY, living.posZ, living.posX + 1, living.posY + 1,
							living.posZ + 1).grow(16, 16, 16));
			for (EntityZombieBase zombie : list) {
				if (zombie.getAttackTarget() == null) {
					zombie.setAttackTarget(living);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living.world.isRemote)
			return;
		if (living instanceof EntityBandit && living.world.rand.nextInt(30) == 0) {
			EntityHorse horse = new EntityHorse(living.world);
			horse.setPositionAndRotation(living.posX, living.posY, living.posZ, living.rotationYaw,
					living.rotationPitch);
			horse.setHorseTamed(true);

			living.world.spawnEntity(horse);
			living.startRiding(horse);
			horse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).insertItem(0,
					new ItemStack(Items.SADDLE), false);

		}
	}

	@SubscribeEvent
	public void onLivingDrop(LivingDropsEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityAnimal) {
			DamageSource source = event.getSource();
			if (source.getImmediateSource() instanceof EntityZombieBase) {
				Iterator<EntityItem> it = event.getDrops().iterator();
				while (it.hasNext()) {
					EntityItem entity = it.next();
					if (entity.getItem().getItem() instanceof ItemFood) {
						it.remove();
					}

				}
			}

		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if (ModConfig.mobs.zombiesAttackAnimals) {
			if (entity instanceof EntityAnimal || entity instanceof EntityVillager) {
				EntityCreature creature = (EntityCreature) entity;
				creature.tasks.addTask(3,
						new EntityAIAvoidEntity<EntityZombieBase>(creature, EntityZombieBase.class, 6.0F, 1.0D, 1.2D));
			}
		}
		if (ModConfig.mobs.monstersAttackHumanNPCs && entity instanceof EntityCreature) {
			EntityCreature creature = (EntityCreature) entity;
			Set<EntityAITaskEntry> tasks = new LinkedHashSet<EntityAITaskEntry>(creature.targetTasks.taskEntries);
			Iterator<EntityAITaskEntry> it = tasks.iterator();
			while (it.hasNext()) {
				EntityAITaskEntry entry = it.next();
				if (entry.action instanceof EntityAINearestAttackableTarget) {
					EntityAINearestAttackableTarget<?> ai = (EntityAINearestAttackableTarget<?>) entry.action;
					Class<?> targetClass = ObfuscationReflectionHelper
							.getPrivateValue(EntityAINearestAttackableTarget.class, ai, "field_75307_b");
					if (targetClass == EntityPlayer.class || targetClass == EntityPlayerMP.class) {
						boolean shouldCheckSight = ObfuscationReflectionHelper.getPrivateValue(EntityAITarget.class, ai,
								"field_75297_f");
						boolean nearbyOnly = ObfuscationReflectionHelper.getPrivateValue(EntityAITarget.class, ai,
								"field_75303_a");
						creature.targetTasks.addTask(2, new EntityAINearestAttackableTarget(creature, EntityHuman.class,
								shouldCheckSight, nearbyOnly));
					}

				}
			}

		}

	}

	public static int getArmorItemsCount(EntityLivingBase living) {
		return (living.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() ? 0 : 2)
				+ (living.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() ? 0 : 3)
				+ (living.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() ? 0 : 2)
				+ (living.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() ? 0 : 1);
	}

}
