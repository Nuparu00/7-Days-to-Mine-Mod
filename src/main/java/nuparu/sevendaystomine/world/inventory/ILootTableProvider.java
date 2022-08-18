package nuparu.sevendaystomine.world.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface ILootTableProvider {

    ResourceLocation getLootTable();

    boolean tryLoadLootTable(CompoundTag p_184283_1_);

    boolean trySaveLootTable(CompoundTag p_184282_1_);

    void unpackLootTable(@Nullable Player p_184281_1_);

    void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_);
    void setLootTable(ResourceLocation p_189404_1_);
}
