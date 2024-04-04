package nuparu.sevendaystomine.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.item.ItemQuality;

public class EntityZombieMiner extends EntityBipedalZombie {

	public EntityZombieMiner(World worldIn) {
		super(worldIn);
		this.lootTable = ModLootTables.ZOMBIE_MINER;
		this.experienceValue = 17;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0D);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		if (rand.nextInt(3) != 0)
			return;
		this.setHeldItem(EnumHand.MAIN_HAND, ItemQuality.setQualityForStack(new ItemStack(rand.nextBoolean() ? ModItems.IRON_PICKAXE : ModItems.SLEDGEHAMMER), 1));
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && !this.world.canBlockSeeSky(this.getPosition()) && this.getPosition().getY() < 64;
	}

}
