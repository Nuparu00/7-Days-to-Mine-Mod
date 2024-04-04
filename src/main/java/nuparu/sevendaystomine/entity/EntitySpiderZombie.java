package nuparu.sevendaystomine.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.ItemUtils;

public class EntitySpiderZombie extends EntityZombieBase {

	private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntitySpiderZombie.class,
			DataSerializers.BYTE);
	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;

	public EntitySpiderZombie(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.5F);
		this.jumpHelper = new EntitySpiderZombie.SpiderZombieJumpHelper(this);
		this.moveHelper = new EntitySpiderZombie.SpiderZombieMoveHelper(this);
		isJumping = true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CLIMBING, Byte.valueOf((byte) 0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(55D);
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateClimber(this, worldIn);
	}

	@Override
	public boolean isOnLadder() {
		return this.isBesideClimbableBlock();
	}

	public boolean isBesideClimbableBlock() {
		return (((Byte) this.dataManager.get(CLIMBING)).byteValue() & 1) != 0;
	}

	public void setBesideClimbableBlock(boolean climbing) {
		byte b0 = ((Byte) this.dataManager.get(CLIMBING)).byteValue();

		if (climbing) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		this.dataManager.set(CLIMBING, Byte.valueOf(b0));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote) {
			this.setBesideClimbableBlock(this.collidedHorizontally);
		}
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier / 2f);
	}

	protected float getJumpUpwardsMotion() {
		if (!this.collidedHorizontally
				&& (!this.moveHelper.isUpdating() || this.moveHelper.getY() <= this.posY + 0.5D)) {
			Path path = this.navigator.getPath();

			if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength()) {
				Vec3d vec3d = path.getPosition(this);

				if (vec3d.y > this.posY + 0.5D) {
					return 0.5F;
				}
			}

			return this.moveHelper.getSpeed() <= 0.6D ? 0.2F : 0.3F;
		} else {
			return 0.5F;
		}
	}

	/**
	 * Causes this entity to do an upwards motion (jumping).
	 */
	protected void jump() {
		this.motionY = (double) this.getJumpUpwardsMotion();

		if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
			this.motionY += (double) ((float) (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1)
					* 0.1F);
		}

		float f = this.rotationYaw * 0.017453292F;
		this.motionX -= (double) (MathHelper.sin(f) * 0.6F);
		this.motionZ += (double) (MathHelper.cos(f) * 0.6F);

		this.isAirBorne = true;
		net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	}

	@SideOnly(Side.CLIENT)
	public float setJumpCompletion(float p_175521_1_) {
		return this.jumpDuration == 0 ? 0.0F : ((float) this.jumpTicks + p_175521_1_) / (float) this.jumpDuration;
	}

	public void setMovementSpeed(double newSpeed) {
		this.getNavigator().setSpeed(newSpeed);
		this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
	}

	public void setJumping(boolean jumping) {
		super.setJumping(jumping);
	}

	public void startJumping() {
		this.setJumping(true);
		this.jumpDuration = 10;
		this.jumpTicks = 0;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.jumpTicks != this.jumpDuration) {
			++this.jumpTicks;
		} else if (this.jumpDuration != 0) {
			this.jumpTicks = 0;
			this.jumpDuration = 0;
			this.setJumping(false);
		}
	}

	private void enableJumpControl() {
		((EntitySpiderZombie.SpiderZombieJumpHelper) this.jumpHelper).setCanJump(true);
	}

	private void disableJumpControl() {
		((EntitySpiderZombie.SpiderZombieJumpHelper) this.jumpHelper).setCanJump(false);
	}

	private void checkLandingDelay() {
		this.disableJumpControl();
	}

	@Override
	public float getEyeHeight() {
		return this.height - 0.1f;
	}

	public class SpiderZombieJumpHelper extends EntityJumpHelper {
		private final EntitySpiderZombie rabbit;
		private boolean canJump;

		public SpiderZombieJumpHelper(EntitySpiderZombie rabbit) {
			super(rabbit);
			this.rabbit = rabbit;
		}

		public boolean getIsJumping() {
			return this.isJumping;
		}

		public boolean canJump() {
			return this.canJump;
		}

		public void setCanJump(boolean canJumpIn) {
			this.canJump = canJumpIn;
		}

		/**
		 * Called to actually make the entity jump if isJumping is true.
		 */
		public void doJump() {
			if (this.isJumping) {
				this.rabbit.startJumping();
			}
		}
	}

	static class SpiderZombieMoveHelper extends EntityMoveHelper {
		private final EntitySpiderZombie rabbit;
		private double nextJumpSpeed;

		public SpiderZombieMoveHelper(EntitySpiderZombie rabbit) {
			super(rabbit);
			this.rabbit = rabbit;
		}

		public void onUpdateMoveHelper() {
			if (this.rabbit.onGround && !this.rabbit.isJumping
					&& !((EntitySpiderZombie.SpiderZombieJumpHelper) this.rabbit.getJumpHelper()).getIsJumping()) {
				this.rabbit.setMovementSpeed(0.0D);
			} else if (this.isUpdating()) {
				this.rabbit.setMovementSpeed(this.nextJumpSpeed);
			}

			super.onUpdateMoveHelper();
		}

		/**
		 * Sets the speed and location to move to
		 */
		public void setMoveTo(double x, double y, double z, double speedIn) {
			if (this.rabbit.isInWater()) {
				speedIn = 1.5D;
			}

			super.setMoveTo(x, y, z, speedIn);

			if (speedIn > 0.0D) {
				this.nextJumpSpeed = speedIn;
			}
		}
	}

	@Override
	protected void onDeathUpdate()
    {

		if (ModConfig.mobs.zombieCorpses) {
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
		
        else {
        super.onDeathUpdate();	
        }
    }

	@Override
	public Vec3d corpseRotation() {
		return new Vec3d(1.5, 0.6, 1.2);
	}

	@Override
	public Vec3d corpseTranslation() {
		return new Vec3d(-0.9, 0, -0.5);
	}

	@Override
	public boolean customCoprseTransform() {
		return true;
	}

}
