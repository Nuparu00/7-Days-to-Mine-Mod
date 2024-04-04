package nuparu.sevendaystomine.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.ItemUtils;

public class EntityBurntZombie extends EntityBipedalZombie {

	public EntityBurntZombie(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.14D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(65D* ModConfig.players.balanceModifier);
	}

	@Override
	public boolean isBurning() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return 15728880;
	}

	public float getBrightness() {
		return 1.0F;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (rand.nextDouble() < 0.3) {
			entityIn.setFire(8);
		}
		return super.attackEntityAsMob(entityIn);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_HUSK_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_HUSK_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_HUSK_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_HUSK_STEP;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		for (int i = 0; i < 1 + world.rand.nextInt(3); i++) {
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + world.rand.nextDouble() * 0.3 - 0.15,
					posY + height / 2, posZ + world.rand.nextDouble() * 0.3 - 0.15, 0.0D, 0.0D, 0.0D);
		}
		if (rand.nextDouble() < 0.1D) {
			world.playSound(posX, posY + height / 2, posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.HOSTILE,
					1.0F, 1.0F, false);
		}

		if (!this.world.isRemote) {
			int i = MathHelper.floor(this.posX);
			int j = MathHelper.floor(this.posY);
			int k = MathHelper.floor(this.posZ);

			if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
				return;
			}

			for (int l = 0; l < 4; ++l) {
				i = MathHelper.floor(this.posX + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
				j = MathHelper.floor(this.posY);
				k = MathHelper.floor(this.posZ + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(i, j, k);

				if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR
						&& Blocks.FIRE.canPlaceBlockAt(this.world, blockpos)) {
					this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
				}
			}
		}
	}

	@Override
	protected void updateAITasks() {
		if (this.isWet()) {
			this.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}

		super.updateAITasks();
	}
	
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		
	}
	
	@Override
	protected void onDeathUpdate()
    {
        ++this.deathTime;

        if (this.deathTime == 20)
        {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
            {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }

            this.setDead();
            EntityLootableCorpse lootable = new EntityLootableCorpse(world);
    		lootable.setOriginal(this);
    		lootable.setPosition(posX, posY, posZ);
    		if (!world.isRemote) {
    			ItemUtils.fillWithLoot(lootable.getInventory(), lootTable, world, rand);
    			world.spawnEntity(lootable);
    		}

            for (int k = 0; k < 20; ++k)
            {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
            }
        }
    }
}
