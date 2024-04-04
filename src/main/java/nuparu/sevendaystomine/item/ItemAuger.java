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

public class ItemAuger extends ItemFuelTool {

	@SuppressWarnings("rawtypes")
	public static final Set effectiveAgainst = Sets.newHashSet(new Block[] { Blocks.GRASS, Blocks.DIRT, Blocks.SAND,
			Blocks.GRAVEL, Blocks.SNOW_LAYER, Blocks.SNOW, Blocks.CLAY, Blocks.FARMLAND, Blocks.SOUL_SAND,
			Blocks.MYCELIUM, Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL,
			Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK,
			Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE,
			Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL,
			Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB });

	@SuppressWarnings("unchecked")
	public ItemAuger(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveAgainst);
		setNoRepair();
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return com.google.common.collect.ImmutableSet.of("pickaxe", "shovel");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canHarvestBlock(IBlockState state) {

		Block block = state.getBlock();
		Material mat = block.getMaterial(state);
		if (effectiveAgainst.contains(block) || mat == Material.IRON || mat == Material.GRASS || mat == Material.GROUND
				|| mat == Material.ROCK || mat == Material.ANVIL || mat == Material.SAND) {
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
			if (effectiveAgainst.contains(block) || mat == Material.IRON || mat == Material.GRASS
					|| mat == Material.GROUND || mat == Material.ROCK || mat == Material.ANVIL
					|| mat == Material.SAND) {
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
		float totalModfier = 1;
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null || !nbt.hasKey("FuelCurrent") || nbt.getFloat("FuelCurrent") <= 0) {
			damage = 1f;
			totalModfier = 0.33f;
		}
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", totalModfier* (this.attackDamage + (damage*(getQuality(stack)/500f))), 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}

		return multimap;
	}

}