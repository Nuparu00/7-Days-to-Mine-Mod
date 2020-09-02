package com.nuparu.sevendaystomine.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.inventory.itemhandler.AirdropInventoryHandler;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.ItemUtils;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.dialogue.Dialogues;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityAirdrop extends Entity {

	private ItemStackHandler inventory;
	public long age = 0;

	private static final DataParameter<Boolean> LANDED = EntityDataManager.<Boolean>createKey(EntityAirdrop.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Integer> SMOKE_TIME = EntityDataManager.<Integer>createKey(EntityAirdrop.class,
			DataSerializers.VARINT);

	private static final DataParameter<Integer> HEALTH = EntityDataManager.<Integer>createKey(EntityAirdrop.class,
			DataSerializers.VARINT);

	public EntityAirdrop(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
		this.inventory = new AirdropInventoryHandler(getInventorySize(), this);
		this.setHealth(40);
		ItemUtils.fillWithLoot(inventory, ModLootTables.AIRDROP, world, rand);
	}

	public EntityAirdrop(World worldIn, double x, double y, double z) {
		this(worldIn);
		setPosition(x, y, z);
		SevenDaysToMine.forceChunkLoad(world, new ChunkPos((int) posX >> 4, (int) posZ >> 4), this);
	}

	public EntityAirdrop(World worldIn, BlockPos pos) {
		this(worldIn, pos.getX(), pos.getY(), pos.getZ());
		System.out.println(posX + " " + posY + " " + posZ);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.age++;

		if (this.age >= 48000) {
			this.setDead();
		}

		if (!onGround) {
			if (!getLanded()) {
				this.motionY = -0.0625;
			} else {
				this.motionY = -0.1911;
			}
		} else {
			this.motionY = 0;
			if (!this.world.isRemote) {
				if (!getLanded()) {
					setSmokeTime(1200);
					setLanded(true);
					SevenDaysToMine.releaseChunkLoad(world, new ChunkPos((int) posX >> 4, (int) posZ >> 4));
				}
			}
		}
		if (this.onGround) {
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
		}
		if (getLanded() && getSmokeTime() > 0) {
			for (int i = 0; i < world.rand.nextInt(4) + 1; i++) {
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + height, this.posZ,
						MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.2f, 0.5f),
						MathUtils.getFloatInRange(-0.1f, 0.1f));
				world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY + height, this.posZ,
						MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.2f, 0.5f),
						MathUtils.getFloatInRange(-0.1f, 0.1f));
			}
			setSmokeTime(getSmokeTime() - 1);
		}
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return getEntityBoundingBox();
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return getEntityBoundingBox();
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(LANDED, false);
		this.dataManager.register(SMOKE_TIME, 0);
		this.dataManager.register(HEALTH, 50);
	}

	public void setLanded(boolean landed) {
		this.dataManager.set(LANDED, landed);
	}

	public boolean getLanded() {
		return this.dataManager.get(LANDED);
	}

	public void setSmokeTime(int ticks) {
		this.dataManager.set(SMOKE_TIME, ticks);
	}

	public int getSmokeTime() {
		return this.dataManager.get(SMOKE_TIME);
	}

	public void setHealth(int health) {
		this.dataManager.set(HEALTH, health);
	}

	public int getHealth() {
		return this.dataManager.get(HEALTH);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("inventory")) {
			inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
		}
		if (nbt.hasKey("landed")) {
			setLanded(nbt.getBoolean("landed"));
		}
		if (nbt.hasKey("smoke_time")) {
			setSmokeTime(nbt.getInteger("smoke_time"));
		}
		if (nbt.hasKey("age")) {
			age = nbt.getLong("age");
		}
		if (nbt.hasKey("health")) {
			setHealth(nbt.getInteger("health"));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		if (inventory != null) {
			compound.setTag("inventory", inventory.serializeNBT());
		}
		compound.setBoolean("landed", getLanded());
		compound.setInteger("smoke_time", getSmokeTime());
		compound.setLong("age", age);
		compound.setInteger("health", getHealth());
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		int i = (int) (getHealth() - amount);
		if (this.world.isRemote) {
			world.playSound((double) this.posX, (double) this.posY, (double) this.posZ, SoundEvents.ENTITY_GENERIC_HURT,
					SoundCategory.HOSTILE, 1.0F, 1.0F, false);
		}
		if (i <= 0) {
			for (int j = 0; j < inventory.getSlots(); j++) {
				ItemStack stack = inventory.getStackInSlot(j);
				this.entityDropItem(stack, 0);
			}
			this.setDead();
		} else {
			setHealth(i);
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void setDead() {
		SevenDaysToMine.releaseChunkLoad(world, new ChunkPos((int) posX >> 4, (int) posZ >> 4));
		this.isDead = true;
	}

	@Override
	public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStack) {
		this.inventory.setStackInSlot(inventorySlot, itemStack);
		return true;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {

		if (!player.isSneaking()) {
			player.openGui(SevenDaysToMine.instance, 21, this.world, (int) 0, (int) this.getEntityId(), (int) 0);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

		if (Double.isNaN(d0)) {
			d0 = 1.0D;
		}

		d0 = d0 * 64.0D * getRenderDistanceWeight() * (getLanded() ? 1 : 6);
		return distance < d0 * d0;
	}

	public void onInventoryChanged(AirdropInventoryHandler airdropInventoryHandler) {
		for (int j = 0; j < inventory.getSlots(); j++) {
			ItemStack stack = inventory.getStackInSlot(j);
			if (!stack.isEmpty()) {
				return;
			}
		}
		age = (long) MathUtils.clamp(age, 42000, 48000);
	}

	public int getInventorySize() {
		return 9;
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability,
			@Nullable net.minecraft.util.EnumFacing facing) {

		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) inventory;
		}
		return super.getCapability(capability, facing);
	}

}
