package nuparu.sevendaystomine.world.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.world.inventory.InventoryUtils;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class FuelDiggerItem extends DiggerItem implements IReloadableItem{
    public SoundEvent refillSound;
    //Units of fuel per canister
    public int reloadAmount = 5;
    
    public FuelDiggerItem(float p_204108_, float p_204109_, Tier p_204110_, TagKey<Block> p_204111_, Properties p_204112_) {
        super(p_204108_, p_204109_, p_204110_, p_204111_, p_204112_);
    }

    public FuelDiggerItem setReloadAmount(int amount){
        this.reloadAmount = amount;
        return this;
    }


    @Override
    public void onCraftedBy(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull Player player) {
        super.onCraftedBy(itemstack, world, player);
        initNBT(itemstack);

    }

    public void initNBT(ItemStack itemstack) {
        itemstack.getOrCreateTag().putInt("FuelMax", 1000);
        itemstack.getOrCreateTag().putInt("FuelCurrent", 0);
        itemstack.getOrCreateTag().putInt("ReloadTime", 0);
        itemstack.getOrCreateTag().putBoolean("Reloading", false);
    }

    @Override
    public void onReloadStart(Level world, Player player, ItemStack stack, int reloadTime) {
        stack.getOrCreateTag().putLong("NextFire",
                world.getGameTime() + (long) Math.ceil((reloadTime / 1000d) * 20));
    }

    @Override
    public void onReloadEnd(Level world, Player player, ItemStack stack, ItemStack bullet) {
        if (bullet != null && !bullet.isEmpty() && stack.getOrCreateTag().contains("FuelCurrent")
                && stack.getOrCreateTag().contains("FuelMax")) {

            stack.getOrCreateTag().putBoolean("Reloading", false);
            int toReload = getCapacity(stack,player) - getAmmo(stack,player);
            int reload = Math.min((int)Math.floor(toReload/reloadAmount), InventoryUtils.getItemCount(player.getInventory(), bullet.getItem()));

            setAmmo(stack, player, getAmmo(stack, player) + reload * reloadAmount);
            InventoryUtils.clearMatchingItems(player.getInventory(), bullet.getItem(), reload);
        }
    }

    @Override
    public int getAmmo(ItemStack stack, Player player) {
        if (stack == null || stack.isEmpty() || !stack.getOrCreateTag().contains("FuelCurrent"))
            return -1;
        return stack.getOrCreateTag().getInt("FuelCurrent");
    }

    @Override
    public int getCapacity(ItemStack stack, Player player) {
        return 1000;
    }

    @Override
    public void setAmmo(ItemStack stack, @Nullable Player player, int ammo) {
        stack.getOrCreateTag().putInt("FuelCurrent", ammo);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        stack.getOrCreateTag();
        if (stack.getOrCreateTag().contains("FuelCurrent") && stack.getOrCreateTag().getInt("FuelCurrent") < 0F) {

            stack.getOrCreateTag().putInt("FuelCurrent", 0);
        }

    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, @NotNull BlockState state, @NotNull BlockPos pos,
                             @NotNull LivingEntity entityLiving) {
        CompoundTag nbt = stack.getOrCreateTag();
        setAmmo(stack, null, getAmmo(stack, null) - 1);
        return !worldIn.isClientSide();
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack)
    {
        return Math.round((float)getAmmo(stack,null) * 13.0F / (float)getCapacity(stack,null));
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack)
    {
        return true;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip,
                                @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        tooltip.add(Component.literal(getAmmo(stack,null) + "/" + getCapacity(stack,null)));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        if (this.allowedIn(tab)) {
            Player player = Minecraft.getInstance().player;
            ItemStack stack = new ItemStack(this, 1);
            ((IQualityStack)(Object)stack).setQuality(SevenDaysToMine.proxy.getQualityForCurrentPlayer());
            if (player != null) {
                CompoundTag nbt = stack.getOrCreateTag();
                nbt.putInt("FuelMax", 1000);
                nbt.putInt("FuelCurrent", 0);
                nbt.putInt("ReloadTime", 90000);
                nbt.putBoolean("Reloading", false);
            }
            items.add(stack);
        }
    }

    @Override
    public Item getReloadItem(ItemStack stack) {
        return ModItems.GAS_CANISTER.get();
    }

    @Override
    public int getReloadTime(ItemStack stack) {
        return 200;
    }

    @Override
    public SoundEvent getReloadSound() {
        return SoundEvents.BUCKET_FILL;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return this.getAmmo(stack, null) <= 0 || super.onLeftClickEntity(stack, player, entity);
    }

}
