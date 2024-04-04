package nuparu.sevendaystomine.entity;

import java.util.Calendar;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.ItemUtils;

public class EntityBipedalZombie extends EntityZombieBase {

	public EntityBipedalZombie(World worldIn) {
		super(worldIn);
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setEquipmentBasedOnDifficulty(difficulty);
		return livingdata;
	}

	@Override
	protected void onDeathUpdate() {
		if (ModConfig.mobs.zombieCorpses) {
			++this.deathTime;

			if (this.deathTime == 20) {
				if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot()
						&& this.world.getGameRules().getBoolean("doMobLoot"))) {
					int i = this.getExperiencePoints(this.attackingPlayer);
					i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
					while (i > 0) {
						int j = EntityXPOrb.getXPSplit(i);
						i -= j;
						this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
					}
				}

				this.setDead();
				EntityLootableCorpse lootable = new EntityLootableCorpse(world);
				lootable.setOriginal(this);
				lootable.setPosition(posX, posY, posZ);
				isDead = true;
				if (!world.isRemote) {
					ItemUtils.fillWithLoot(lootable.getInventory(), lootTable, world, rand);
					world.spawnEntity(lootable);
				}

				for (int k = 0; k < 20; ++k) {
					double d2 = this.rand.nextGaussian() * 0.02D;
					double d0 = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
							this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
							this.posY + (double) (this.rand.nextFloat() * this.height),
							this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2,
							d0, d1);
				}
			}
		}
		else {
			super.onDeathUpdate();
		}
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

		if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
			Calendar calendar = this.world.getCurrentDate();

			if (calendar.get(2) + 1 == 12 && this.rand.nextFloat() < 0.05F) {
				this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.CHRISTMAS_HAT));
				this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
			}
		}
	}

}
