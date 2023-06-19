package nuparu.sevendaystomine.json.horde;

import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import nuparu.sevendaystomine.json.horde.number.Range;
import nuparu.sevendaystomine.json.horde.pool.HordePool;
import nuparu.sevendaystomine.json.horde.time.Time;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record HordeEntry(ResourceLocation path, boolean checkForActiveHorde, Trigger trigger, Range<Integer> waves, int wavesDelay, List<HordePool> startPools, List<HordePool> normalPools, List<HordePool> endPools, AdvancementRewards rewards) {

    public record Trigger (Time time, boolean global, SoundEvent sound, int lifespan, boolean despawnEntities){

    }

    public static Optional<HordePool> getHordeFromPool(List<HordePool> pools, ServerLevel level, RandomSource randomSource){
        if(pools.isEmpty()) return Optional.empty();
        List<Object[]> calculatedPools = pools.stream().map(pool -> new Object[]{pool, pool.weight().get(level, randomSource).intValue()}).toList();
        int totalWeight = calculatedPools.stream().map(pair -> (int) pair[1]).reduce(0, Integer::sum);

        int weightLeft = randomSource.nextInt(totalWeight);

        for(Object[] pair : calculatedPools.stream().sorted(Comparator.comparingInt((pair) -> (int) pair[1])).toList()){
            weightLeft -= (int) pair[1];
            if(weightLeft < 0){
                return Optional.of((HordePool) pair[0]);
            }
        }
        return Optional.empty();
    }

    public Optional<HordePool> getStartPool(ServerLevel level, RandomSource randomSource){
        Optional<HordePool> pool = getHordeFromPool(startPools(),level,randomSource);
        return pool.isPresent() ? pool : getNormalPool(level,randomSource);
    }

    public Optional<HordePool> getNormalPool(ServerLevel level, RandomSource randomSource){
        return getHordeFromPool(normalPools(),level,randomSource);
    }

    public Optional<HordePool> getEndPool(ServerLevel level, RandomSource randomSource){
        Optional<HordePool> pool = getHordeFromPool(endPools(),level,randomSource);
        return pool.isPresent() ? pool : getNormalPool(level,randomSource);
    }

}