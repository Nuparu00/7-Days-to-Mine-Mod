package nuparu.sevendaystomine.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.util.Utils;

public class EntityFeralZombie extends EntityBipedalZombie {

	public EntityFeralZombie(World worldIn) {
		super(worldIn);
		this.experienceValue = 40;
		this.lootTable = ModLootTables.ZOMBIE_FERAL;
		nightRun = false;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
		return super.attackEntityFrom(source, this.isBurning() ? amount*4.25f : amount/3);
    }
	
	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && Utils.isBloodmoon(world);
	}
	
	@Override
	public float getBlockBreakSpeed(IBlockState state, BlockPos pos) {
		return super.getBlockBreakSpeed(state, pos)*1.5f;
	}

}
