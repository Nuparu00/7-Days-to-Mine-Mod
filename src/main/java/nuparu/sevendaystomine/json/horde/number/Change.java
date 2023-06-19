package nuparu.sevendaystomine.json.horde.number;

import net.minecraft.server.level.ServerLevel;
import nuparu.sevendaystomine.world.level.LevelUtils;

public record Change<T extends Number>(T value, Number per, String unit, Number floor, Number ceil, String operation) {

    public static final Change<Integer> CONSTANT = new Change<>(0, 1, "none", 0, 0, "none");

    public int getUnitValue(ServerLevel level) {
        return switch (unit) {
            case "bloodmoon" -> LevelUtils.getBloomoonsHappened(level);
            case "day" -> LevelUtils.getDay(level);
            case "week" -> (int) Math.floor(LevelUtils.getDay(level) / 7f);
            case "tick" -> (int) level.getDayTime(); //Should change the types to long later on!
            default -> 0;
        };
    }
}
