package nuparu.sevendaystomine.world.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModEntities;

public class BoneSpearEntity extends ThrownTrident {
    public BoneSpearEntity(EntityType<? extends ThrownTrident> p_37561_, Level p_37562_) {
        super(p_37561_, p_37562_);
    }
    public BoneSpearEntity(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(ModEntities.BONE_SPEAR.get(),p_37569_);
        this.setPos(p_37570_.getX(), p_37570_.getEyeY() - (double)0.1F, p_37570_.getZ());
        this.setOwner(p_37570_);
        if (p_37570_ instanceof Player) {
            this.pickup = Pickup.ALLOWED;
        }

        this.tridentItem = p_37571_.copy();
        this.entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(p_37571_));
        this.entityData.set(ID_FOIL, p_37571_.hasFoil());
    }
}
