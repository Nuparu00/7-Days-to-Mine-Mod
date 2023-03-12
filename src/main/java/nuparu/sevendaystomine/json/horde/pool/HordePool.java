package nuparu.sevendaystomine.json.horde.pool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import nuparu.sevendaystomine.json.horde.number.Range;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record HordePool(String group, SoundEvent soundEvent, Range<Integer> rolls, Range<Integer> weight, List<Entry> entries) {
    public record Entry(int weight, EntityType<?> type, CompoundTag nbt) {

    }

    public Optional<Entry> getEntry(ServerLevel level, RandomSource randomSource){
        if(entries.isEmpty()) return Optional.empty();
        int totalWeight = entries.stream().map(Entry::weight).reduce(0, Integer::sum);

        int weightLeft = randomSource.nextInt(totalWeight);

        for(Entry entry : entries.stream().sorted(Comparator.comparingInt(Entry::weight)).toList()){
            weightLeft -= entry.weight;
            if(weightLeft < 0){
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }
}
