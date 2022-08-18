package nuparu.sevendaystomine.world.item;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModEffects;

import java.util.ArrayList;

public class FirstAidKitItem extends BandageItem {

	public FirstAidKitItem(Properties properties) {
		super(properties);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player player = (Player) entityLiving;
			int dur = this.getUseDuration(stack) - timeLeft;
			if (dur >= 20) {
				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.getInventory().removeItem(stack);
					}
				}
				player.removeEffect(ModEffects.BLEEDING.get());
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
				MobEffectInstance brokeLegEffect = player.getEffect(ModEffects.BROKEN_LEG.get());
				if (brokeLegEffect != null) {
					player.removeEffect(ModEffects.BROKEN_LEG.get());
					MobEffectInstance effect = new MobEffectInstance(ModEffects.SPLINTED_LEG.get(), (int)Math.ceil(brokeLegEffect.getDuration()/2),
							brokeLegEffect.getAmplifier(), brokeLegEffect.isAmbient(),
							brokeLegEffect.isVisible());
					effect.setCurativeItems(new ArrayList<>());
					player.addEffect(effect);
				}
			}
		}
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand)
	{
		if(super.interactLivingEntity(stack, playerIn, target, hand) == InteractionResult.SUCCESS){
			target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
			MobEffectInstance brokeLegEffect = target.getEffect(ModEffects.BROKEN_LEG.get());
			if (brokeLegEffect != null) {
				target.removeEffect(ModEffects.BROKEN_LEG.get());
				MobEffectInstance effect = new MobEffectInstance(ModEffects.SPLINTED_LEG.get(), brokeLegEffect.getDuration(),
						brokeLegEffect.getAmplifier(), brokeLegEffect.isAmbient(),
						brokeLegEffect.isVisible());
				effect.setCurativeItems(new ArrayList<>());
				target.addEffect(effect);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
