package nuparu.sevendaystomine.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.util.Utils;

public class EntityAirplane extends Entity {

	private BlockPos startPoint = null;
	private double distanceToFly = Double.MAX_VALUE;
	private boolean hasDropped = false;

	private final float FLY_SPEED = 0.5F;
	private final double Y_HEIGHT = 200;

	public EntityAirplane(World worldIn) {
		super(worldIn);
		this.noClip = true;
		this.height = 1.5F;
		this.width = 7F;
		int x = (int) (this.posX / 16);
		int z = (int) (this.posZ / 16);
	}

	public EntityAirplane(World worldIn, double x, double y, double z) {
		this(worldIn);
		setPosition(x, y, z);
	}

	public EntityAirplane(World worldIn, BlockPos pos) {
		this(worldIn, pos.getX(), pos.getY(), pos.getZ());
	}

	public EntityAirplane(World world, BlockPos start, BlockPos center, double distance) {
		this(world);
		this.startPoint = start;
		this.distanceToFly = distance;

		Vec3d vec = new Vec3d(center.subtract(start));
		vec.normalize();

		setPosition(start.getX(), Y_HEIGHT, start.getZ());
		setThrowableHeading((double) vec.x, Y_HEIGHT, (double) vec.z, FLY_SPEED, 0f);
		Utils.getLogger().info(new BlockPos(this));

	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
		distanceToFly = tagCompund.getDouble("distance");
		startPoint = BlockPos.fromLong(tagCompund.getLong("start"));
		hasDropped = tagCompund.getBoolean("hasDropped");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		tagCompound.setDouble("distance", distanceToFly);
		tagCompound.setLong("start", startPoint.toLong());
		tagCompound.setBoolean("hasDropped", hasDropped);

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.posY = Y_HEIGHT;
		this.posX += this.motionX;
		this.posZ += this.motionZ;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		Utils.getLogger().info("Update");
		if (!world.isRemote) {
			if (startPoint == null) {
				this.setDead();
				Utils.getLogger().info(
						"EntityAirplane with id " + this.getEntityId() + " has null startPoint. Removing the entity!");
			}

			if (!hasDropped && world.rand.nextDouble() * distanceToFly <= 1d) {
				drop();
			}

			if (startPoint.distanceSq(new BlockPos(posX, posY, posZ)) >= distanceToFly) {
				if (!hasDropped) {
					drop();
				}
				this.setDead();

			}
		}

	}

	public void drop() {
		Utils.getLogger().info("Drop!");
		hasDropped = true;
		EntityAirdrop airdrop = new EntityAirdrop(world, new BlockPos(this));
		airdrop.copyLocationAndAnglesFrom(this);
		if (!world.isRemote) {
			world.spawnEntity(airdrop);
		}
	}

	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;

		double maxMotion = Math.abs(Math.max(Math.abs(x), Math.abs(z)));

		this.motionX = (x / maxMotion) * FLY_SPEED;
		// this.motionY = (y / maxMotion) * FLY_SPEED;
		this.motionZ = (z / maxMotion) * FLY_SPEED;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.atan2(x, z) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * 180.0D / Math.PI);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

		if (Double.isNaN(d0)) {
			d0 = 1.0D;
		}

		d0 = d0 * 64.0D * getRenderDistanceWeight() * 6;
		return distance < d0 * d0;
	}
	
	public static void spawnAirplane(World world) {
		BlockPos posCenter = Utils.getAirdropPos(world);
		BlockPos posStart = Utils.getAirDropStartPoint(world, posCenter);
		
		spawnAirplane(world, posCenter, posStart);
	}

	public static void spawnAirplane(World world, BlockPos posCenter, BlockPos posStart) {
		EntityAirplane airplane = new EntityAirplane(world, posStart, posCenter, posStart.distanceSq(posCenter) * 2f);
		if (!world.isRemote) {
			world.spawnEntity(airplane);
		}
	}

}
