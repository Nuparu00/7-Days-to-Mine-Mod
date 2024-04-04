package nuparu.sevendaystomine.item;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemChainsaw extends ItemFuelTool {

	@SuppressWarnings("rawtypes")
	public static final Set effectiveAgainst = Sets.newHashSet(new Block[] {Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG,
			Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER,
			Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE, Blocks.WEB, Blocks.LEAVES, Blocks.LEAVES2});

	@SuppressWarnings("unchecked")
	public ItemChainsaw(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveAgainst);
		setNoRepair();
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return com.google.common.collect.ImmutableSet.of("axe");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canHarvestBlock(IBlockState state) {

		Block block = state.getBlock();
		Material mat = block.getMaterial(state);
		if (effectiveAgainst.contains(block) || mat == Material.WOOD || mat == Material.PLANTS || mat == Material.VINE) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {

		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null || !nbt.hasKey("FuelCurrent")) {
			return 0f;
		}
		Block block = state.getBlock();
		Material mat = block.getMaterial(state);
		if (getAmmo(stack,null) > 0) {
			if (effectiveAgainst.contains(block) || mat == Material.WOOD || mat == Material.PLANTS
					|| mat == Material.VINE) {
				return this.efficiency * (1+(float) this.getQuality(stack) / 200f);
			} else {
				return efficiency/10f;
			}
		} else {
			return efficiency/10f;
		}

	}
	
	
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

		float damage = this.attackDamage;
		if (this.getAmmo(stack, null) <= 0) {
			damage = 1f;
		}
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage + (damage*(getQuality(stack)/500f)), 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}

		return multimap;
	}

}