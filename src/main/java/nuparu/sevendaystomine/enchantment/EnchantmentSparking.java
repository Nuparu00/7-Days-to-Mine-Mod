package nuparu.sevendaystomine.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemGun;

public class EnchantmentSparking extends Enchantment {

	protected EnchantmentSparking() {
		super(Rarity.RARE, ModEnchantments.GUNS,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		setRegistryName(SevenDaysToMine.MODID, "sparking");
		setName("sparking");
	}

	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemGun;
	}
	
	@Override
	public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + 10 * (enchantmentLevel - 1);
    }
	
	@Override
	public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
}
