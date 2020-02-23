package com.nuparu.sevendaystomine.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityLootableCorpse extends Entity {

	private ItemStackHandler inventory = null;
	private boolean onEntity = false;
	public int health = 40;
	public long age = 0;

	private static final DataParameter<NBTTagCompound> ORIGINAL_NBT = EntityDataManager
			.<NBTTagCompound>createKey(EntityLootableCorpse.class, DataSerializers.COMPOUND_TAG);

	public EntityLootableCorpse(World worldIn) {
		super(worldIn);
		this.setSize(1f, 0.45f);
		initInventory();
	}

	public EntityLootableCorpse(World worldIn, Entity entity) {
		this(worldIn);
	}

	public void setOriginal(Entity entity) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase living = ((EntityLivingBase) entity);
			living.hurtTime = 0;
			living.limbSwing = 0;
		}

		this.rotationPitch = entity.rotationPitch;
		this.prevRotationPitch = entity.rotationPitch;
		this.rotationYaw = entity.rotationYaw;
		this.prevRotationYaw = entity.rotationYaw;
		this.setRotationYawHead(entity.getRotationYawHead());

		entity.motionX = 0;
		entity.motionY = 0;
		entity.motionZ = 0;
		entity.setSneaking(false);
		entity.extinguish();
		entity.setRotationYawHead(0);
		entity.rotationYaw = 0;
		entity.rotationPitch = 0;

		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound entityNbt = null;

		if (entity != null) {
			entityNbt = entity.writeToNBT(new NBTTagCompound());
			nbt.setTag("entity", entityNbt);
			nbt.setString("resourceLocation", EntityList.getKey(entity.getClass()).toString());
		}
		setOriginalNBT(nbt);
	}

	public Entity getOriginal() {
		NBTTagCompound nbt = getOriginalNBT();
		return Utils.getEntityByNBTAndResource(new ResourceLocation(nbt.getString("resourceLocation")),
				nbt.getCompoundTag("entity"), world);
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(ORIGINAL_NBT, new NBTTagCompound());
	}

	public void setOriginalNBT(NBTTagCompound nbt) {
		this.dataManager.set(ORIGINAL_NBT, nbt);
	}

	public NBTTagCompound getOriginalNBT() {
		return this.dataManager.get(ORIGINAL_NBT);
	}


	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("inventory")) {
			inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		}
		if (compound.hasKey("originalEntity")) {
			setOriginalNBT(compound.getCompoundTag("originalEntity"));
		}
		onEntity = compound.getBoolean("onEntity");
		age = compound.getLong("age");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (inventory != null) {
			compound.setTag("inventory", inventory.serializeNBT());
		}
		compound.setTag("originalEntity", getOriginalNBT());
		compound.setBoolean("onEntity", onEntity);
		compound.setLong("age", age);
		return compound;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		this.age++;
		
		if(this.age >= 48000) {
			this.setDead();
		}
		
		if (!onGround && !onEntity) {
			this.motionY = -0.0625;
		} else {
			this.motionY = 0;
		}
		if (this.onGround) {
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
		}
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

		if (this.posY < -64.0D) {
			this.outOfWorld();
		}

		boolean flag = false;
		for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this, getCollisionBoundingBox())) {
			if (!this.isPassenger(entity) && entity.canBeCollidedWith()) {
				flag = true;
			}
		}
		this.onEntity = flag;

	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	protected boolean canTriggerWalking()
    {
        return false;
    }

	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return getEntityBoundingBox();
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return getEntityBoundingBox();
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public int getInventorySize() {
		return 9;
	}

	protected void initInventory() {
		this.inventory = new ItemStackHandler(getInventorySize());
	}

	public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
		this.inventory.setStackInSlot(inventorySlot, itemStackIn);
		return true;
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

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (player.isSneaking() && hand == EnumHand.MAIN_HAND) {
			openGUI(player);
			return true;
		}
		return false;
	}

	public void openGUI(EntityPlayer playerEntity) {
		if (!this.world.isRemote) {
			playerEntity.openGui(SevenDaysToMine.instance, 9, this.world, (int) 0, (int) this.getEntityId(), (int) 0);
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
		this.health-=amount;
		if (this.world.isRemote) {
			world.playSound((double) this.posX, (double) this.posY, (double) this.posZ,
					SoundEvents.ENTITY_GENERIC_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
			for (int i = 0; i < (int) Math.round(MathUtils.getDoubleInRange(1, 5)*SevenDaysToMine.proxy.getParticleLevel()); i++) {
				double x = this.posX + MathUtils.getDoubleInRange(-1, 1) * this.width;
				double y = this.posY + MathUtils.getDoubleInRange(0, 1) * this.height;
				double z = this.posZ + MathUtils.getDoubleInRange(-1, 1) * this.width;
				SevenDaysToMine.proxy.spawnParticle(this.world, EnumModParticleType.BLOOD, x, y, z,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d)  / 7d, MathUtils.getDoubleInRange(-1d, 1d) / 7d);
			}
		}
		if(this.health <= 0) {
			for(int i = 0; i < inventory.getSlots(); i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				this.entityDropItem(stack, 0);
			}
			this.setDead();
		}
		return super.attackEntityFrom(source, amount);
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

}
