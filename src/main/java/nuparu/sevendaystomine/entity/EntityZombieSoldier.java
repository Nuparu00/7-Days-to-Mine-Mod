package nuparu.sevendaystomine.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModLootTables;

public class EntityZombieSoldier extends EntityBipedalZombie {

	public EntityZombieSoldier(World worldIn) {
		super(worldIn);
		this.lootTable = ModLootTables.ZOMBIE_SOLDIER;
		this.experienceValue = 20;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120F* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6D);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (rand.nextFloat() < 0.005F) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.NIGHT_VISION_DEVICE));
			 this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
		}
	}

}
