package com.nuparu.sevendaystomine.client.particle;

import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.events.TextureStitcherEventHandler;
import com.nuparu.sevendaystomine.util.MathUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleBlood extends Particle {

	private final ResourceLocation texture = new ResourceLocation(SevenDaysToMine.MODID, "entity/particles/blood");

	public boolean collided = false;

	public ParticleBlood(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		this.particleGravity = 0.981F;
		this.particleMaxAge = (int) (256.0D / (this.rand.nextDouble() * 0.8D + 0.2D)) * 2;
		this.setSize(0.4F, 0.4F);
		this.multipleParticleScaleBy(MathUtils.getFloatInRange(0.2f, 1.4f));

		this.particleAlpha = MathUtils.getFloatInRange(0.7f, 0.95f);
		this.motionX = xSpeedIn;
		this.motionY = ySpeedIn;
		this.motionZ = zSpeedIn;
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
		this.setParticleTexture(sprite);
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		if (!collided) {
			this.motionY -= 0.04D * (double) this.particleGravity;
		}

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround) {
			this.motionX *= 0.69999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
		if (this.collided) {
			this.motionX = 0d;
			this.motionY = 0d;
			this.motionZ = 0d;
			if (!onGround) {
				// System.out.println("Hi");
			}
		}
	}

	@Override
	public void move(double x, double y, double z) {
		double d0 = y;
		double origX = x;
		double origZ = z;

		if (this.canCollide) {
			List<AxisAlignedBB> list = this.world.getCollisionBoxes((Entity) null,
					this.getBoundingBox().expand(x, y, z));

			for (AxisAlignedBB axisalignedbb : list) {
				y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
			}

			this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

			for (AxisAlignedBB axisalignedbb1 : list) {
				x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
			}

			this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

			for (AxisAlignedBB axisalignedbb2 : list) {
				z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
			}

			this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
		} else {
			this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		}

		this.resetPositionToBB();
		this.onGround = d0 != y && d0 < 0.0D;
		if (this.onGround) {
		}

		if (origX != x) {
			this.motionX = 0.0D;
		}

		if (origZ != z) {
			this.motionZ = 0.0D;
		}
		List<AxisAlignedBB> list = this.world.getCollisionBoxes((Entity) null, this.getBoundingBox().expand(x, y, z));

		boolean flag = true;
		for (AxisAlignedBB aabb : list) {
			if (aabb.intersects(this.getBoundingBox())) {
				flag = false;
				if(aabb.contains(this.getBoundingBox().getCenter())) {
					this.setExpired();
				}
			}
		}
		if (!flag) {
			this.collided = true;
		}
		else {
			this.collided = false;
		}
	}

	@Override
	protected void resetPositionToBB() {
		AxisAlignedBB axisalignedbb = this.getBoundingBox();
		this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
		this.posY = axisalignedbb.minY;
		this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f = (float) this.particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = (float) this.particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1F * this.particleScale;

		if (this.particleTexture != null) {
			f = this.particleTexture.getMinU();
			f1 = this.particleTexture.getMaxU();
			f2 = this.particleTexture.getMinV();
			f3 = this.particleTexture.getMaxV();
		}

		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY)
				+ 0.00625f;
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] {
				new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4),
						(double) (-rotationYZ * f4 - rotationXZ * f4)),
				new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4),
						(double) (-rotationYZ * f4 + rotationXZ * f4)),
				new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4),
						(double) (rotationYZ * f4 + rotationXZ * f4)),
				new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4),
						(double) (rotationYZ * f4 - rotationXZ * f4)) };

		if (this.particleAngle != 0.0F) {
			float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

			for (int l = 0; l < 4; ++l) {
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d))
						.add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d)))
						.add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
			}
		}

		buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z)
				.tex((double) f1, (double) f3)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k)
				.endVertex();
		buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z)
				.tex((double) f1, (double) f2)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k)
				.endVertex();
		buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z)
				.tex((double) f, (double) f2)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k)
				.endVertex();
		buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z)
				.tex((double) f, (double) f3)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k)
				.endVertex();
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
				double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
			return new ParticleBlood(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}

}
