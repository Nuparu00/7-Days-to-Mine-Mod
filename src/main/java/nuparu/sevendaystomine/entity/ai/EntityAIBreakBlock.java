package nuparu.sevendaystomine.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.BreakSavedData;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityLootableCorpse;
import nuparu.sevendaystomine.entity.EntityZombieBase;
import nuparu.sevendaystomine.entity.INoiseListener;
import nuparu.sevendaystomine.events.MobBreakEvent;
import nuparu.sevendaystomine.util.Utils;

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
			if (!this.theEntity.world.getGameRules().getBoolean("mobGriefing") || !ModConfig.mobs.zombiesBreakBlocks) {
				return false;
			} else {
				if (theEntity.world.rand.nextInt(10) == 0) {
					RayTraceResult entityRay = Utils.raytraceEntities(theEntity, 2);
					if (entityRay != null) {
						if (entityRay.entityHit != null && entityRay.entityHit instanceof EntityLootableCorpse) {
							entityRay.entityHit.attackEntityFrom(DamageSource.GENERIC, 2);
							theEntity.swingArm(theEntity.world.rand.nextInt(2) == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
							theEntity.playSound(SoundEvents.ENTITY_GENERIC_EAT, theEntity.world.rand.nextFloat()*0.2f+0.9f, theEntity.world.rand.nextFloat()*0.2f+0.9f);
						}
					}
				}
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
			float m = (theEntity.getBlockBreakSpeed(state, blockPosition) / hardness) / 32f;

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