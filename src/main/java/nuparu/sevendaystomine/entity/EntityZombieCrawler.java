package nuparu.sevendaystomine.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.ItemUtils;

public class EntityZombieCrawler extends EntityZombieBase {

	public EntityZombieCrawler(World worldIn) {
		super(worldIn);
		setSize(0.75F, 0.33F);
		this.experienceValue = 8;
	}

	@Override
	public float getEyeHeight() {
		return this.height;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		range.setBaseValue(56.0D);
		speed.setBaseValue(0.125D);
		attack.setBaseValue(3.0D * ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D* ModConfig.players.balanceModifier);
		armor.setBaseValue(0.0D);
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);

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
		} else {
			super.onDeathUpdate();
		}
	}

	@Override
	public Vec3d corpseRotation() {
		return new Vec3d(1, 64, 1);
	}

	@Override
	public Vec3d corpseTranslation() {
		return new Vec3d(0, -0.25, 0);
	}

	@Override
	public boolean customCoprseTransform() {
		return true;
	}
}
