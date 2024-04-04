package nuparu.sevendaystomine.client.particle;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.MathUtils;

@SideOnly(Side.CLIENT)
public class ParticleVomit extends Particle {

	private final ResourceLocation texture = new ResourceLocation(SevenDaysToMine.MODID, "entity/particles/vomit");

	public boolean collided = false;

	public ParticleVomit(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		this.particleGravity = 0.981F;
		this.particleMaxAge = (int) (16.0D / (this.rand.nextDouble() * 0.8D + 0.2D)) * 2;
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
				// 
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


	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
				double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
			return new ParticleVomit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}

}
