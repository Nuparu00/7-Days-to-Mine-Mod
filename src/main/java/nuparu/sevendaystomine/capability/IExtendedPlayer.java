package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    void unlockRecipe(String rec);

    void forgetRecipe(String rec);

    boolean hasRecipe(String rec);

    List<String> getRecipes();

    boolean isInfected();

    int getInfectionTime();

    void setInfectionTime(int stage);

    IExtendedPlayer setOwner(Player player);
    Player getOwner();

    void readNBT(CompoundTag nbt);
    CompoundTag writeNBT(CompoundTag nbt);
    void copy(IExtendedPlayer iep);
    void onDataChanged();
    void onStartedTracking(Player tracker);
    void tick(Player player);

    void setLastHorde(ResourceLocation horde, long time);

    long getLastHorde(ResourceLocation horde);

    Optional<Tuple<ResourceLocation, Long>> getLastActualHordeHorde();
}
