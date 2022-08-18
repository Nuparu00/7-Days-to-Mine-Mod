package nuparu.sevendaystomine.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEffects;
import nuparu.sevendaystomine.json.IngredientStack;
import nuparu.sevendaystomine.json.drink.DrinkDataManager;
import nuparu.sevendaystomine.json.drink.DrinkEntry;
import nuparu.sevendaystomine.json.salvage.SalvageDataManager;
import nuparu.sevendaystomine.json.salvage.SalvageEntry;
import nuparu.sevendaystomine.json.upgrade.UpgradeDataManager;
import nuparu.sevendaystomine.json.upgrade.UpgradeEntry;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Level level = player.level;
            RandomSource random = level.random;
            ItemStack stack = event.getItem();
            Item item = stack.getItem();
            if ((item.isEdible() || item.getUseAnimation(stack) == UseAnim.DRINK) && !level.isClientSide()) {
                if (DrinkDataManager.INSTANCE.hasDrinkData(item)) {
                    DrinkEntry drinkData = DrinkDataManager.INSTANCE.getDrinkData(item);
                    if (ServerConfig.thirst.get()) {
                        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);

                        if (extendedPlayer != null) {
                            extendedPlayer.addWaterLevel(drinkData.amount());
                            if (player.getRandom().nextDouble() <= drinkData.dirtiness()) {
                                player.addEffect(new MobEffectInstance(ModEffects.DYSENTERY.get(), MathUtils.getIntInRange(player.getRandom(), 600, 1200), 0, false, false, true));
                            }
                        }
                    }
                    if (drinkData.alcoholic()) {
                        MobEffectInstance effect = new MobEffectInstance(ModEffects.ALCOHOL_BUZZ.get(), 6000, 0, false, false, true);
                        if (player.hasEffect(ModEffects.ALCOHOL_POISON.get())) {
                            effect = new MobEffectInstance(ModEffects.ALCOHOL_POISON.get(), 3000, 0, false, false, true);
                        }
                        if (!player.hasEffect(ModEffects.ALCOHOL_POISON.get())) {
                            if (!player.hasEffect(ModEffects.DRUNK.get())) {
                                if (player.hasEffect(ModEffects.ALCOHOL_BUZZ.get())) {
                                    MobEffectInstance buzzEffect = player.getEffect(ModEffects.ALCOHOL_BUZZ.get());
                                    if (random.nextInt(Math.max(1, buzzEffect.getDuration() / 10)) > 10) {
                                        effect = new MobEffectInstance(ModEffects.DRUNK.get(), 3000, 0, false, false, true);
                                        player.removeEffect(ModEffects.ALCOHOL_BUZZ.get());
                                    }
                                }
                            } else {
                                MobEffectInstance drunkEffect = player.getEffect(ModEffects.DRUNK.get());
                                effect = drunkEffect;
                                if (random.nextInt(Math.max(1, drunkEffect.getDuration() / 10)) > 10) {
                                    effect = new MobEffectInstance(ModEffects.ALCOHOL_POISON.get(), 3000, 0, false, false, true);
                                    player.removeEffect(ModEffects.DRUNK.get());
                                } else {
                                    effect = new MobEffectInstance(ModEffects.DRUNK.get(), 3000, 0, false, false, true);
                                    player.removeEffect(ModEffects.ALCOHOL_BUZZ.get());
                                }
                            }
                        }
                        effect.setCurativeItems(new ArrayList<>());
                        player.addEffect(effect);
                    }
                    if(drinkData.caffeineBuzzDuration() > 0){
                        MobEffectInstance effect = new MobEffectInstance(ModEffects.CAFFEINE_BUZZ.get(), drinkData.caffeineBuzzDuration(), drinkData.caffeineBuzzAmplifier(), false, false, true);
                        effect.setCurativeItems(new ArrayList<>());
                        player.addEffect(effect);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!ServerConfig.quality.get()) return;
        float speed = (float) (event.getNewSpeed() / ServerConfig.blockToughnessModifier.get());
        if (event.getEntity() != null) {
            ItemStack stack = event.getEntity().getMainHandItem();
            IQualityStack qualityStack = (IQualityStack) (Object) stack;
            if (qualityStack.canHaveQuality()) {
                int quality = qualityStack.getQuality();
                speed *= 1 + (quality / (double) QualityManager.maxLevel) * (QualityManager.levels.size() - 1);
            }
        }
        event.setNewSpeed(speed);

    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!level.isClientSide()) {
            if (!player.isCrouching()) {
                double upgradeAmount = ItemUtils.getUpgradeAmount(stack);
                if (upgradeAmount > 0) {
                    UpgradeEntry entry = UpgradeDataManager.INSTANCE.getUpgradeFromEntry(state);
                    if (entry != null) {
                        HashMap<ItemStack, Integer[]> map = player.isCreative() ? new HashMap<>() : Utils.hasIngredients(entry.ingredients, player);
                        if (map != null) {

                            if (stack.getOrCreateTag().contains("7D2M_UpgradePos", Tag.TAG_LONG) && stack.getOrCreateTag().getLong("7D2M_UpgradePos") != pos.asLong()) {
                                ItemUtils.eraseUpgraderData(stack);
                            } else if (stack.getOrCreateTag().contains("7D2M_UpgradeDim", Tag.TAG_STRING) && !stack.getOrCreateTag().getString("7D2M_UpgradeDim").equals(level.dimension().location().toString())) {
                                ItemUtils.eraseUpgraderData(stack);
                            }

                            double progress = upgradeAmount + (stack.getOrCreateTag().contains("7D2M_UpgradeProgress") ? stack.getOrCreateTag().getDouble("7D2M_UpgradeProgress") : 0);
                            if (progress >= 1) {
                                ItemUtils.eraseUpgraderData(stack);
                                level.setBlockAndUpdate(pos, entry.getUpgradeTo().asBlockState(entry,state));
                                if (!player.isCreative()) {
                                    Utils.consumeIngredients(map, player);
                                }
                                stack.hurtAndBreak(1, player, (player1) -> {
                                    player1.broadcastBreakEvent(player.getUsedItemHand());
                                });
                            } else {
                                stack.getOrCreateTag().putDouble("7D2M_UpgradeProgress", progress);
                                stack.getOrCreateTag().putLong("7D2M_UpgradePos", pos.asLong());
                                stack.getOrCreateTag().putString("7D2M_UpgradeDim", level.dimension().location().toString());
                            }
                            if (entry.sound != null) {
                                level.playSound(null, player.getX(), player.getY(), player.getZ(), entry.sound, SoundSource.BLOCKS, MathUtils.getFloatInRange(0.9f, 1.1f), MathUtils.getFloatInRange(0.9f, 1.1f));
                            }
                            player.swing(event.getHand(), true);

                        }
                    }
                }
            } else {
                double salvageAmount = ItemUtils.getSalvageAmount(stack);
                if (salvageAmount > 0) {
                    SalvageEntry entry = SalvageDataManager.INSTANCE.getSalvageForBlock(block);
                    if (entry != null) {

                        if (stack.getOrCreateTag().contains("7D2M_UpgradePos", Tag.TAG_LONG) && stack.getOrCreateTag().getLong("7D2M_UpgradePos") != pos.asLong()) {
                            ItemUtils.eraseUpgraderData(stack);
                        } else if (stack.getOrCreateTag().contains("7D2M_UpgradeDim", Tag.TAG_STRING) && !stack.getOrCreateTag().getString("7D2M_UpgradeDim").equals(level.dimension().location().toString())) {
                            ItemUtils.eraseUpgraderData(stack);
                        }

                        double progress = salvageAmount + (stack.getOrCreateTag().contains("7D2M_UpgradeProgress") ? -stack.getOrCreateTag().getDouble("7D2M_UpgradeProgress") : 0);
                        if (progress >= 1) {
                            ItemUtils.eraseUpgraderData(stack);
                            for (IngredientStack ingredientStack : entry.ingredients) {
                                if(level.getRandom().nextDouble() > ingredientStack.chance()) continue;;
                                int count = level.getRandom().nextInt(ingredientStack.count() + 1);
                                if (count == 0) continue;
                                Ingredient ingredient = ingredientStack.ingredient();
                                ItemStack itemStack = ingredient.getItems()[level.getRandom().nextInt(ingredient.getItems().length)];
                                itemStack = itemStack.copy();
                                itemStack.setCount(count);
                                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
                            }
                            level.destroyBlock(pos,false);
                            stack.hurtAndBreak(1, player, (player1) -> {
                                player1.broadcastBreakEvent(player.getUsedItemHand());
                            });
                        } else {
                            stack.getOrCreateTag().putDouble("7D2M_UpgradeProgress", -progress);
                            stack.getOrCreateTag().putLong("7D2M_UpgradePos", pos.asLong());
                            stack.getOrCreateTag().putString("7D2M_UpgradeDim", level.dimension().location().toString());
                        }
                        if (entry.sound != null) {
                            level.playSound(null, player.getX(), player.getY(), player.getZ(), entry.sound, SoundSource.BLOCKS, MathUtils.getFloatInRange(0.9f, 1.1f), MathUtils.getFloatInRange(0.9f, 1.1f));
                        }
                        player.swing(event.getHand(), true);

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEatenEvent(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack stack = event.getItem();
            if (stack.getItem().getFoodProperties() != null && stack.getItem().getMaxDamage() > 0
                    && (stack.getMaxDamage() - stack.getDamageValue()) > 1) {
                stack.hurt(1, player.level.random, player);
                event.setResultStack(stack);
            }
        }
    }


}

