package nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.DamageSources;

public class EntityMolotov extends EntityThrowable {

	public EntityMolotov(World worldIn) {
		super(worldIn);

	}

	public EntityMolotov(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityMolotov(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	protected void onImpact(RayTraceResult result) {

		if (world.isRemote) {
			return;
		}
		if (result.entityHit != null) {
			result.entityHit.attackEntityFrom(DamageSources.sharpGlass, 1);
		}
				
		this.setDead();
		world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + width / 2, posY + height / 2, posZ + width / 2, 0,
				0, 0);
		this.world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.AMBIENT,
				0.9f + this.rand.nextFloat() * 0.2f, 0.9f + this.rand.nextFloat() * 0.2f);
		
		EnumFacing facing = EnumFacing.UP;
		if(result.sideHit != null) {
			facing = result.sideHit;
		}
		Vec3i vec = facing.getDirectionVec();
		//
		if (!world.isRemote) {
			for (int i = 0; i < 128; i++) {
				EntityFlame flame = new EntityFlame(world, posX + width / 2, posY + height / 2, posZ + width / 2, 0, 0,
						0, 0);
				flame.setGravityVelocity(0.05f);
				flame.motionY = rand.nextDouble() * 0.2 - 0.1 + vec.getY()*0.25;
				flame.motionX = rand.nextDouble() * 0.8 - 0.4 + vec.getX()*0.5;
				flame.motionZ = rand.nextDouble() * 0.8 - 0.4 + vec.getZ()*0.5;
				world.spawnEntity(flame);
			}
		}
	}

}
