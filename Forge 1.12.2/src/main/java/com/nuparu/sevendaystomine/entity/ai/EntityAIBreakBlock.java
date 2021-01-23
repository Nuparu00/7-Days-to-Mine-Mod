package com.nuparu.sevendaystomine.entity.ai;

import com.nuparu.sevendaystomine.block.repair.BreakData;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.entity.INoiseListener;
import com.nuparu.sevendaystomine.events.MobBreakEvent;
import com.nuparu.sevendaystomine.item.ItemQualityPickaxe;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAIBreakBlock extends EntityAIBase {
	public Block block;
	public BlockPos blockPosition = BlockPos.ORIGIN;
	public EntityZombieBase theEntity;
	private float stepSoundTickCounter;

	public EntityAIBreakBlock(EntityZombieBase entityIn) {
		this.theEntity = entityIn;
	}

	@Override
	public boolean shouldExecute() {
		if (theEntity.getAttackTarget() != null
				|| ((theEntity instanceof INoiseListener) && ((INoiseListener) theEntity).getCurrentNoise() != null)) {
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

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		super.resetTask();
	}

	public float blockStrength(IBlockState state, EntityLivingBase player, World world, BlockPos pos) {
		float hardness = state.getBlockHardness(world, pos);
		if (hardness < 0.0F) {
			return 0.0F;
		}
		return hardness;

	}

	@Override
	public void updateTask() {
		super.updateTask();
		World world = theEntity.world;
		BreakSavedData data = BreakSavedData.get(world);
		BreakData b = data.getBreakData(blockPosition, theEntity.world.provider.getDimension());

		IBlockState state = world.getBlockState(blockPosition);
		float hardness = state.getBlockHardness(world, blockPosition);
		if (state.getMaterial() != Material.AIR && hardness >= 0) {

			if (stepSoundTickCounter % 4.0F == 0.0F) {
				theEntity.swingArm(world.rand.nextInt(2) == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			}
			float m = (theEntity.getBlockBreakSpeed(state, blockPosition)/hardness)/32f;

			if (Utils.damageBlock(world, blockPosition, m, true, this.stepSoundTickCounter % 4.0F == 0.0F)) {
				world.playEvent(2001, blockPosition, Block.getStateId(world.getBlockState(blockPosition)));
				resetTask();
				MinecraftForge.EVENT_BUS.post(new MobBreakEvent(theEntity.world, blockPosition,
						theEntity.world.getBlockState(blockPosition), (EntityLivingBase) theEntity));

			}
			++this.stepSoundTickCounter;
		}
	}
}