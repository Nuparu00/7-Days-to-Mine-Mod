package nuparu.sevendaystomine.json.horde.number;

import net.minecraft.server.level.ServerLevel;
import nuparu.sevendaystomine.world.level.LevelUtils;

public class Change<T extends Number> {

    public static final Change<Integer> CONSTANT = new Change<>(0, 1, "none", 0, 0,"none");
    private final T value;
    private final Number per;
    private final String unit;
    private final Number floor;
    private final Number ceil;
    private final String operation;

    public Change(T value, Number per, String unit, Number floor, Number ceil, String operation) {
        this.value = value;
        this.per = per;
        this.unit = unit;
        this.floor = floor;
        this.ceil = ceil;
        this.operation = operation;
    }

    public T getValue() {
        return value;
    }

    public Number getPer() {
        return per;
    }

    public String getUnit() {
        return unit;
    }

    public Number getFloor() {
        return floor;
    }

    public Number getCeil() {
        return ceil;
    }

    public String getOperation() {
        return operation;
    }

    public int getUnitValue(ServerLevel level){
        return switch (unit){
            case "bloodmoon" -> LevelUtils.getBloomoonsHappened(level);
            case "day" -> LevelUtils.getDay(level);
            case "week" -> (int) Math.floor(LevelUtils.getDay(level) / 7f);
            case "tick" -> (int) level.getDayTime(); //Should change the types to long later on!
            default -> 0;
        };
    }
}
