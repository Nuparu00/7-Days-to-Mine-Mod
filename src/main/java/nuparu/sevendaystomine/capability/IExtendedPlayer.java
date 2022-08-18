package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface IExtendedPlayer {

    int getWaterLevel();
    int getMaxWaterLevel();
    void setWaterLevel(int level);
    void addWaterLevel(int delta);
    void consumeWaterLevel(int delta);
    void causeExhaustion(float delta);
    float getSaturationLevel();

    void setDrinkCounter(int value);
    int getDrinkCounter();

    IExtendedPlayer setOwner(Player player);
    Player getOwner();

    void readNBT(CompoundTag nbt);
    CompoundTag writeNBT(CompoundTag nbt);
    void copy(IExtendedPlayer iep);
    void onDataChanged();
    void onStartedTracking(Player tracker);
    void tick(Player player);

    boolean isInfected();

    int getInfectionTime();

    void setInfectionTime(int stage);
}
