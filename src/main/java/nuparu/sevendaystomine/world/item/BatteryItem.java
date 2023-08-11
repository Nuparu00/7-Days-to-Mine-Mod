package nuparu.sevendaystomine.world.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryItem extends ItemBase implements IBattery{
    public static final int BASE_VOLTAGE = 2500;
    public BatteryItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return nbt.contains("Voltage", Tag.TAG_LONG);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack)
    {
        return Math.round(13f * (float) getVoltage(stack,null) / getCapacity(stack, null));
    }


    @Override
    public long getCapacity(ItemStack stack, @javax.annotation.Nullable Level world) {
        return (long) (BASE_VOLTAGE*(1+(double)((IQualityStack)(Object)stack).getQuality() / QualityManager.getMaxLevel()));
    }

    @Override
    public long getVoltage(ItemStack stack,@javax.annotation.Nullable Level world) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains("Voltage", Tag.TAG_LONG)) {
            return nbt.getLong("Voltage");
        }
        return 0;
    }

    @Override
    public void setVoltage(ItemStack stack, @javax.annotation.Nullable Level world, long voltage) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putLong("Voltage", Math.max(0,voltage));
    }

    @Override
    public long tryToAddVoltage(ItemStack stack, @javax.annotation.Nullable Level world, long deltaVoltage) {
        if(deltaVoltage < 0) return deltaVoltage;
        CompoundTag nbt = stack.getOrCreateTag();
        if(!nbt.contains("Voltage",Tag.TAG_LONG)) {
            setVoltage(stack,world,deltaVoltage);
        }
        long voltage = nbt.getLong("Voltage");
        long difference = getCapacity(stack,world)-voltage;
        long toAdd = Math.min(deltaVoltage, difference);
        nbt.putLong("Voltage", voltage+toAdd);
        return deltaVoltage-toAdd;
    }

    @Override
    public void drainVoltage(ItemStack stack, @javax.annotation.Nullable Level world, long deltaVoltage) {
        if(deltaVoltage < 0) return;
        CompoundTag nbt = stack.getOrCreateTag();
        if(!nbt.contains("Voltage", Tag.TAG_LONG)) {
            return;
        }
        nbt.putLong("Voltage", Math.max(0,nbt.getLong("Voltage")-deltaVoltage));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip,
                                @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.literal(this.getVoltage(stack, worldIn) + "/" + this.getCapacity(stack, worldIn)+" J"));
    }
}
