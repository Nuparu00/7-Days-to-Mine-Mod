package com.nuparu.sevendaystomine.entity.ai;

import com.nuparu.sevendaystomine.block.repair.BreakData;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.events.MobBreakEvent;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAIBreakBlock extends EntityAIBase {
	private int breakingTime;
	public Block block;
	public BlockPos blockPosition = BlockPos.ORIGIN;
	public EntityZombieBase theEntity;
	private float stepSoundTickCounter;
	public float blockHealth = 0f;
	public EntityAIBreakBlock(EntityZombieBase entityIn) {
		this.theEntity = entityIn;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */

	public boolean shouldExecute() {
		if (this.theEntity.getAttackTarget() != null) {
			if (!this.theEntity.world.getGameRules().getBoolean("mobGriefing")) {
				return false;
			} else {

				RayTraceResult ray = this.theEntity.rayTraceServer(2, 1F);
				if (ray != null) {
					if (ray.getBlockPos() != null) {
						this.blockPosition = ray.getBlockPos();
						this.block = this.theEntity.world.getBlockState(this.blockPosition).getBlock();
						return (block != null);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.breakingTime = 0;
		this.blockHealth = 0f;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		// double d0 = this.theEntity.getDistanceSq(this.doorPosition);

		if (this.theEntity.getAttackTarget() != null) {
			World world = theEntity.world;
			RayTraceResult ray = this.theEntity.rayTraceServer(2, 1F);
			if (ray != null) {
				if (ray.getBlockPos() != null) {

					if (this.breakingTime <= world.getBlockState(blockPosition).getBlockHardness(world, blockPosition)
							/ blockStrength(this.theEntity.world.getBlockState(this.blockPosition),
									this.theEntity, this.theEntity.world, this.blockPosition)) {

						if (ray.getBlockPos().getX() == this.blockPosition.getX()
								&& ray.getBlockPos().getY() == this.blockPosition.getY()
								&& ray.getBlockPos().getZ() == this.blockPosition.getZ()) {

							return true;

						}

					}
				}
			}
		}
		return false;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		super.resetTask();
		this.blockHealth = 0f;
		// this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(),
		// this.doorPosition, -1);
		// block = null;
		// this.breakingTime = 0;
	}

	public float getBreakSpeed(IBlockState state, BlockPos pos, EntityLivingBase player) {

		float f = 5f;

		if (player.isPotionActive(MobEffects.HASTE)) {
			f *= 1.0F + (float) (player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
		}

		if (player.isPotionActive(MobEffects.MINING_FATIGUE)) {
			float f1 = 1.0F;

			switch (player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
			case 0:
				f1 = 0.3F;
				break;
			case 1:
				f1 = 0.09F;
				break;
			case 2:
				f1 = 0.0027F;
				break;
			case 3:
			default:
				f1 = 8.1E-4F;
			}

			f *= f1;
		}

		if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player)) {
			f /= 5.0F;
		}

		if (!player.onGround) {
			f /= 5.0F;
		}

		// f = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(this, state, f,
		// pos);
		return f;
	}

	public float blockStrength(IBlockState state, EntityLivingBase player, World world, BlockPos pos) {
		float hardness = state.getBlockHardness(world, pos);
		if (hardness < 0.0F) {
			return 0.0F;
		}

		return getBreakSpeed(state, pos, player) / hardness / 3F;

	}

	/**
	 * Updates the task
	 */
	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public void playBreakSound() {
		SoundType soundType = block.getSoundType();
		Minecraft.getMinecraft().getSoundHandler()
				.playSound(new PositionedSoundRecord(soundType.getBreakSound(), SoundCategory.BLOCKS,
						(soundType.volume + 1.0F) / 8.0F, soundType.pitch * 0.5F,
						(float) this.blockPosition.getX() + 0.5F, (float) this.blockPosition.getY() + 0.5F,
						(float) this.blockPosition.getZ() + 0.5F));
	}

	public void updateTask() {
		super.updateTask();
		World world = this.theEntity.world;
		BreakSavedData data = BreakSavedData.get(world);
		BreakData b = data.getBreakData(this.blockPosition, this.theEntity.world.provider.getDimension());
		if (b != null) {
			this.blockHealth = b.getState();
		}
		IBlockState state = world.getBlockState(this.blockPosition);
		if (state.getMaterial() != Material.AIR && state.getBlockHardness(world,blockPosition) >= 0) {

			if (this.stepSoundTickCounter % 4.0F == 0.0F) {
//plSound();
				this.theEntity.swingArm(world.rand.nextInt(2) == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			}
			float m = world.getBlockState(this.blockPosition).getBlockHardness(world, blockPosition)
					/ blockStrength(world.getBlockState(this.blockPosition), this.theEntity,
							world, this.blockPosition);
			++this.stepSoundTickCounter;
			this.blockHealth += (1f / m);
			if (blockHealth >= 1f) {
				blockHealth = 1f;
			}
			
			Utils.damageBlock(world, blockPosition, blockHealth, false);
			if (blockHealth >= 1f) {
				world.playEvent(2001, blockPosition, Block.getStateId(world.getBlockState(this.blockPosition)));
				Utils.damageBlock(world, blockPosition, blockHealth, true);
				resetTask();
				MinecraftForge.EVENT_BUS.post(new MobBreakEvent(this.theEntity.world, this.blockPosition,
						this.theEntity.world.getBlockState(this.blockPosition),
						(EntityLivingBase) this.theEntity));

			}
		}
	}
}