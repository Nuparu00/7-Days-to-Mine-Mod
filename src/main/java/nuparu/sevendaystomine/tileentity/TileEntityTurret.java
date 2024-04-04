package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.block.BlockTurretBase;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.entity.EntityShot;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

public abstract class TileEntityTurret extends TileEntityItemHandler<ItemHandlerNameable>
		implements ITickable, INetwork {

	private ArrayList<BlockPos> network = new ArrayList<BlockPos>();

	public static final double VIEW_DISTANCE = 16;
	public static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.turret");

	private EnumFacing facing;

	public float headRotation = 0f;
	public float headRotationPrev = 0f;
	public int headRotationMaximumReached = 0; // 0 == LEFT ;1== RIGHT
	public int maxMemory = 400;// In ticks
	public int memory = 0;
	public int maxDelay = 5;
	public int delay = 0;
	private boolean on = false;

	public Entity target;
	public AITurretTarget targetAI;
	public AITurretShoot shootAI;
	
	public List<String> whitelistedTypes = new ArrayList<String>();

	public TileEntityTurret() {
		targetAI = new AITurretTarget(this);
		shootAI = new AITurretShoot(this);
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(9, DEFAULT_NAME);
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {

	}

	@Override
	public void onContainerClosed(EntityPlayer player) {

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.headRotation = compound.getFloat("headRoation");
		this.headRotationPrev = compound.getFloat("headRoationPrev");
		this.headRotationMaximumReached = compound.getInteger("headRotationReached");
		if (compound.hasKey("target", Constants.NBT.TAG_STRING)) {

			if (this.world != null && this.world instanceof WorldServer) {
				this.target = ((WorldServer) this.world)
						.getEntityFromUuid(UUID.fromString(compound.getString("target")));
			}
		} else {
			this.target = null;
		}
		this.maxMemory = compound.getInteger("maxMemory");
		this.memory = compound.getInteger("memory");

		this.maxDelay = compound.getInteger("maxDelay");
		this.delay = compound.getInteger("delay");
		this.on = compound.getBoolean("on");

		if (world != null) {
			network.clear();
			NBTTagList list = compound.getTagList("network", Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagLong nbt = (NBTTagLong) list.get(i);
				BlockPos blockPos = BlockPos.fromLong(nbt.getLong());
				network.add(blockPos);
			}
		}
		
		whitelistedTypes.clear();
		NBTTagList list = compound.getTagList("whitelistedTypes", Constants.NBT.TAG_STRING);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagString nbt = (NBTTagString) list.get(i);
			whitelistedTypes.add(nbt.getString());
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setFloat("headRoation", headRotation);
		compound.setFloat("headRoationPrev", headRotationPrev);
		compound.setInteger("headRotationReached", headRotationMaximumReached);
		if (target != null) {
			compound.setString("target", target.getUniqueID().toString());
		}
		compound.setInteger("maxMemory", maxMemory);
		compound.setInteger("memory", memory);

		compound.setInteger("maxDelay", maxDelay);
		compound.setInteger("delay", delay);
		compound.setBoolean("on", on);
		NBTTagList list = new NBTTagList();
		for (BlockPos net : getConnections()) {
			list.appendTag(new NBTTagLong(net.toLong()));
		}
		compound.setTag("network", list);
		
		NBTTagList targets = new NBTTagList();
		for (String s : whitelistedTypes) {
			targets.appendTag(new NBTTagString(s));
		}
		compound.setTag("whitelistedTypes", targets);
		
		return compound;
	}

	@Override
	public void update() {
		if(world.isRemote) {
			return;
		}
		this.headRotationPrev = this.headRotation;
		if (delay < maxDelay) {
			delay++;
		}
		if (on) {
			if (this.world.getBlockState(this.pos).getBlock() instanceof BlockTurretBase) {
				facing = (EnumFacing) this.world.getBlockState(this.pos).getValue(BlockHorizontalBase.FACING);
			}
			if (target == null) {
				if (headRotationMaximumReached == 1) {
					headRotation += 1;
				}
				if (headRotationMaximumReached == 0) {
					headRotation -= 1;
				}
				if (headRotation >= 180) {
					headRotationMaximumReached = 0;
				}
				if (headRotation <= -180) {
					headRotationMaximumReached = 1;
				}
			} else {

				if (target.isDead) {
					target = null;
				} else {
					rotateTowards();
					if (delay == maxDelay && !world.isRemote) {
						shootAI.updateAITask();
						delay = 0;
					}
				}
			}

			RayTraceResult ray = rayTrace(this.world, VIEW_DISTANCE);

			if (ray != null && ray.typeOfHit == RayTraceResult.Type.ENTITY) {
				targetAI.setTarget(ray.entityHit);
			}
			/*
			 * else { world.setBlockState(new
			 * BlockPos(ray.hitVec.x,ray.hitVec.y-2,ray.hitVec.z),
			 * Blocks.BEDROCK.getDefaultState()); }
			 */
			targetAI.updateAITask();
			markForUpdate();
		}
	}

	public void rotateTowards() {
		double dX = pos.getX() - target.posX;
		double dY = pos.getY() - target.posY;
		double dZ = pos.getZ() - target.posZ;

		double yaw = Math.atan2(dZ, dX);
		double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;

		Vec3d position = getHeadPosition();
		Vec3d rotation = getHeadRotation();
		Vec3d entityPosition = new Vec3d(target.posX, target.posY, target.posZ);

		Vec3d neededRotation = entityPosition.subtract(position);
		float currentYaw = getYaw(rotation);
		float neededYaw = getYaw(neededRotation);

		float difference = currentYaw - neededYaw;

		if (Math.abs(difference) % 360 == 0) {
			difference = 0;
		}
		if (difference > 0) {
			if (difference >= 1) {
				headRotation -= 1;
			} else {
				headRotation -= difference;
			}
		}
		if (difference < 0) {
			if (difference <= -1) {
				headRotation += 1;
			} else {
				headRotation += difference;
			}

		}
	}

	public static float getYaw(Vec3d vec) {
		double deltaX = vec.x;
		double deltaZ = vec.z;
		double yaw = 0;
		if (deltaX != 0) {
			if (deltaX < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(deltaZ / deltaX);
		} else if (deltaZ < 0) {
			yaw = Math.PI;
		}
		return (float) (-yaw * 180 / Math.PI - 90);
	}

	public Vec3d getHeadPosition() {
		Vec3d rot = getHeadRotation();
		return new Vec3d(pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5)
				.add(new Vec3d(rot.x * 0.7, rot.y * 0.7, rot.z * 0.7));
	}

	public Vec3d getHeadRotation() {
		return getVectorFromYawPitch(headRotation
				+ getAngle((EnumFacing) world.getBlockState(this.pos).getValue(BlockHorizontalBase.FACING)), 0);
	}

	public RayTraceResult rayTrace(World worldIn, double distance) {

		Vec3d rotation = getHeadRotation();
		Vec3d position = getHeadPosition();
		Vec3d rayEnd = position.add(new Vec3d(rotation.x * distance, rotation.y * distance, rotation.z * distance));
		Vec3d finalVec = null;

		RayTraceResult mop = worldIn.rayTraceBlocks(position, rayEnd, false, false, true);
		RayTraceResult result = null;
		Entity pointedEntity = null;
		float f = 1.0F;

		List<Entity> list = worldIn.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)
						.expand(rotation.x * distance, rotation.y * distance, rotation.z * distance),
				Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
					public boolean apply(Entity p_apply_1_) {
						return p_apply_1_.canBeCollidedWith();
					}
				}));
		double d2 = distance;
		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = (Entity) list.get(j);
			if (entity1 instanceof EntityLivingBase) {
				float borderSize = entity1.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox();
				RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(position, rayEnd);

				if (axisalignedbb.contains(position)) {
					if (d2 >= 0.0D) {
						pointedEntity = entity1;
						finalVec = movingobjectposition == null ? position : movingobjectposition.hitVec;
						d2 = 0.0D;
					}
				} else if (movingobjectposition != null) {
					double d3 = position.distanceTo(movingobjectposition.hitVec);

					if (d3 < d2 || d2 == 0.0D) {
						pointedEntity = entity1;
						finalVec = movingobjectposition.hitVec;
						d2 = d3;

					}
				}
			}
		}

		if (pointedEntity != null && pointedEntity instanceof EntityLivingBase) {

			double distToEntity = Math
					.abs(position.distanceTo(new Vec3d(pointedEntity.posX, pointedEntity.posY, pointedEntity.posZ)));
			RayTraceResult rayFinal = worldIn.rayTraceBlocks(position,
					position.add(
							new Vec3d(rotation.x * distToEntity, rotation.y * distToEntity, rotation.z * distToEntity)),
					false, false, false);
			if (rayFinal == null) {
				result = new RayTraceResult(pointedEntity, finalVec);
			}
		}
		if (result == null) {
			result = mop;
		}

		return result;
	}

	public static Vec3d getVectorFromYawPitch(float yaw, float pitch) {
		float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3d((double) (f1 * f2), (double) f3, (double) (f * f2)).normalize();
	}

	public boolean canEntityBeSeen(Entity entity) {
		return world.rayTraceBlocks(new Vec3d(pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5),
				new Vec3d(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ)) == null;
	}

	public float getAngle(EnumFacing facing) {
		switch (facing) {
		default:
		case NORTH: {
			return 0;
		}
		case SOUTH: {
			return 180;
		}

		case WEST: {
			return 270;
		}

		case EAST: {
			return 90;
		}

		}

	}

	public class AITurretBase {
		TileEntityTurret te = null;

		public AITurretBase(TileEntityTurret te) {
			this.te = te;
		}

		public void updateAITask() {

		}

		public double distanceSqrtTo(Entity entity) {
			double d0 = te.getPos().getX() - entity.posX;
			double d1 = te.getPos().getY() - entity.posY;
			double d2 = te.getPos().getZ() - entity.posZ;
			return (d0 * d0 + d1 * d1 + d2 * d2);
		}
	}

	public class AITurretTarget extends AITurretBase {
		public AITurretTarget(TileEntityTurret te) {
			super(te);
		}

		public void updateAITask() {

			Entity target = te.target;
			if (target != null) {
				if (distanceSqrtTo(target) > 256) {
					te.target = null;
				}
				if (Math.abs(target.posY - te.getPos().getY()) > 1) {
					te.target = null;
				}

			}
		}

		public void setTarget(Entity seenEntity) {
			if (seenEntity == te.target) {
				return;
			}
			if (seenEntity instanceof EntityPlayer
					&& (((EntityPlayer) seenEntity).isCreative() || ((EntityPlayer) seenEntity).isSpectator())) {
				return;
			}
			ResourceLocation key = EntityList.getKey(seenEntity);
			if(key != null && te.whitelistedTypes.contains(key.toString())) return;
			
			if (te.target == null || distanceSqrtTo(seenEntity) < distanceSqrtTo(te.target)) {
				te.target = seenEntity;
			}
		}
	}

	public class AITurretShoot extends AITurretBase {
		public AITurretShoot(TileEntityTurret te) {
			super(te);
		}

		public void updateAITask() {

			Entity target = te.target;
			BlockPos pos = te.getPos();
			if (target != null && te.hasAmmo()) {

				double dX = pos.getX() - target.posX;
				double dY = pos.getY() - target.posY;
				double dZ = pos.getZ() - target.posZ;

				double yaw = Math.atan2(dZ, dX);
				double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;

				Vec3d position = getHeadPosition();
				Vec3d rotation = getHeadRotation();
				Vec3d entityPosition = new Vec3d(target.posX, target.posY, target.posZ);

				Vec3d neededRotation = entityPosition.subtract(position);
				float currentYaw = TileEntityTurret.getYaw(rotation);
				float neededYaw = TileEntityTurret.getYaw(neededRotation);

				float difference = currentYaw - neededYaw;
				float absDiff = Math.abs(difference);

				if (absDiff <= 16) {
					shoot();
				}

			}
		}
		
		public void shoot() {
			if(te.hasAmmo() && te.delay == te.maxDelay) {
				float currentYaw = TileEntityTurret.getYaw(getHeadRotation());
				shoot(getHeadPosition(), currentYaw + 90, 0f);
				te.consumeAmmo(1);
				te.delay = 0;
			}
		}

		public void shoot(Vec3d pos, float yaw, float pitch) {
			EntityShot shot = new EntityShot(te.getWorld(), pos, yaw, pitch, 1.5f, 0.25f);
			shot.setDamage(20f);

			if (!te.getWorld().isRemote) {
				te.getWorld().spawnEntity(shot);
			}
			world.playSound(null, te.pos, SoundHelper.AK47_SHOT, SoundCategory.BLOCKS, 0.3f, 1.0F / (te.world.rand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			te.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.x, pos.y - 0.2, pos.z, 0.0D, 0.075D, 0.0D,
					new int[0]);
			te.getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y - 0.2, pos.z, 0.0D, 0.0D, 0.0D,
					new int[0]);

		}
	}

	public boolean hasAmmo() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = this.getInventory().getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.SEVEN_MM_BULLET)
				return true;
		}
		return false;
	}

	public void consumeAmmo(int amount) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = this.getInventory().getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.SEVEN_MM_BULLET) {
				if (stack.getCount() >= amount) {
					stack.shrink(amount);
					if (stack.getCount() <= 0) {
						((ItemStackHandler) this.getInventory()).setStackInSlot(i, ItemStack.EMPTY);
					}
					break;
				} else {
					amount -= stack.getCount();
					((ItemStackHandler) this.getInventory()).setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
	}

	public void setDisplayName(String displayName) {
		inventory.setDisplayName(new TextComponentString(displayName));
	}

	@Override
	@Nullable
	public ITextComponent getDisplayName() {
		return inventory.getDisplayName();
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net,
			net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlockPos> getConnections() {
		return (List<BlockPos>) network.clone();
	}

	@Override
	public void connectTo(INetwork toConnect) {
		if (!isConnectedTo(toConnect)) {
			network.add(toConnect.getPosition());
			toConnect.connectTo(this);
			markDirty();
		}
	}

	@Override
	public void disconnect(INetwork toDisconnect) {
		if (isConnectedTo(toDisconnect)) {
			network.remove(toDisconnect.getPosition());
			toDisconnect.disconnect(this);
			markDirty();
		}
	}

	@Override
	public boolean isConnectedTo(INetwork net) {
		return network.contains(net.getPosition());
	}

	@Override
	public void disconnectAll() {
		for (BlockPos pos : getConnections()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof INetwork) {
				((INetwork) te).disconnect(this);
			}
		}
	}

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

	public void setOn(boolean on) {
		this.on = on;
		markForUpdate();
	}
	
	public void markForUpdate() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
	}

	public boolean isOn() {
		return on;
	}

	public boolean switchOn() {
		setOn(!isOn());
		return on;
	}

	@Override
	public void sendPacket(String packet, INetwork from, EntityPlayer playerFrom) {
		switch (packet) {
		case "switch":
			switchOn();
			break;
		case "shoot":
			this.shootAI.shoot();
			break;
		case "reset":
			this.whitelistedTypes.clear();
			markDirty();
			break;
		}
	}

}
