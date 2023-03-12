package nuparu.sevendaystomine.world.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.world.entity.EntityUtils;
import nuparu.sevendaystomine.world.entity.zombie.ZombieBaseEntity;
import nuparu.sevendaystomine.world.level.LevelUtils;

public class GoalBreakBlocks extends Goal {
	Block block;
	BlockPos blockPosition = BlockPos.ZERO;
	ZombieBaseEntity zombie;
	float stepSoundTickCounter;
	
	public GoalBreakBlocks(ZombieBaseEntity zombie){
		this.zombie = zombie;
	}

	@Override
	public boolean canUse() {
		if(!ServerConfig.zombiesBreakBlocks.get()) return false;
		if(zombie.getTarget() != null) {

			BlockHitResult blockRay = EntityUtils.rayTraceServer(zombie,1, 2.5f, ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE);
            this.blockPosition = blockRay.getBlockPos();
			if (!net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.zombie.level, this.blockPosition, this.zombie))  return false;
            this.block = zombie.level.getBlockState(this.blockPosition).getBlock();
            return true;
        }
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if(!(zombie.level instanceof ServerLevel world)) return;
		if(blockPosition == null) return;
		LevelChunk chunk = world.getChunkAt(blockPosition);
        IChunkData ichunkdata = CapabilityHelper.getChunkData(chunk);

		BlockState state = world.getBlockState(blockPosition);
		float hardness = state.getDestroySpeed(world, blockPosition);
		if (state.getMaterial() != Material.AIR && hardness >= 0) {

			if (stepSoundTickCounter % 4.0F == 0.0F) {
				zombie.swing(world.random.nextInt(2) == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
			}
			float m = (zombie.getDigSpeed(state, blockPosition) / hardness) / 32f;

			if (LevelUtils.damageBlock(world, blockPosition, m, true, this.stepSoundTickCounter % 4.0F == 0.0F)) {
				world.levelEvent(2001, blockPosition, Block.getId(world.getBlockState(this.blockPosition)));
				this.stop();

			}
			++this.stepSoundTickCounter;
		}
	}

}
