package nuparu.sevendaystomine.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemGun;

public class EnchantmentSmallMag extends Enchantment {

	protected EnchantmentSmallMag() {
		super(Rarity.UNCOMMON, ModEnchantments.GUNS,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		setRegistryName(SevenDaysToMine.MODID, "small_mag");
		setName("small_mag");
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemGun && ((ItemGun) stack.getItem()).getMaxAmmo() > 1;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 1 + 10 * (enchantmentLevel - 1);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return super.canApplyTogether(ench) && ench != ModEnchantments.big_mag;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean isCurse() {
		return true;
	}
}
