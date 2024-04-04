package nuparu.sevendaystomine.world.gen.prefab.buffered;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BufferedEntity {

	protected String name;
	protected double x;
	protected double y;
	protected double z;
	protected float yaw;
	protected float pitch;
	protected NBTTagCompound nbt;

	public BufferedEntity(String name, double x, double y, double z, float yaw, float pitch, NBTTagCompound nbt) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.nbt = nbt;
	}

	public BufferedEntity rotate(float angle) {
		if (angle % 360 == 0)
			return this;

		int x1 = (int) Math.round(x * Math.cos(Math.toRadians(angle)) - z * Math.sin(Math.toRadians(angle)));
		int z1 = (int) Math.round(x * Math.sin(Math.toRadians(angle)) + z * Math.cos(Math.toRadians(angle)));
		return new BufferedEntity(name, x1, y, z1, yaw + angle, pitch, nbt);
	}
	public BufferedEntity flipX() {
		return new BufferedEntity(name, -x, y, z, yaw, pitch, nbt);
	}
	public BufferedEntity flipZ() {
		return new BufferedEntity(name, x, y, -z, yaw, pitch, nbt);
	}
	public BufferedEntity flipY() {
		return new BufferedEntity(name, x, -y, z, yaw, pitch, nbt);
	}

	public BufferedEntity copy() {
		return new BufferedEntity(name, x, y, z, yaw, pitch, nbt);
	}

	public void spawn(World world, BlockPos origin) {
		Class<? extends Entity> clazz = EntityList.getClass(new ResourceLocation(name));
		Entity entity = EntityList.newEntity(clazz, world);
		entity.readFromNBT(this.nbt);
		entity.setPosition(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
		entity.rotationYaw = this.yaw;
		entity.rotationPitch = this.pitch;
		entity.setUniqueId(MathHelper.getRandomUUID(entity.world.rand));
		if (!world.isRemote) {
			world.spawnEntity(entity);
		}
	}
}
