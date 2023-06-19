package nuparu.sevendaystomine.json.horde.number;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;

public record VariableNumber<T extends Number>(T base, Change<T> change) {

    public static <T extends Number> VariableNumber<T> of(T number){
        return new VariableNumber<T>(number,null);
    }

    public static VariableNumber<Integer> parseInt(JsonObject object){
        int base = GsonHelper.getAsInt(object,"base",0);
        Change<Integer> change = Change.CONSTANT;

        if(object.has("change")){
            JsonObject changeObject = object.getAsJsonObject("change");
            String operation = GsonHelper.getAsString(changeObject,"operation","none");
            int value = GsonHelper.getAsInt(changeObject,"value",1);
            int per = GsonHelper.getAsInt(changeObject,"per",1);
            String unit = GsonHelper.getAsString(changeObject,"unit","none");
            int floor = GsonHelper.getAsInt(changeObject,"floor",Integer.MIN_VALUE);
            int ceil = GsonHelper.getAsInt(changeObject,"ceil",Integer.MAX_VALUE);
            change = new Change<>(value,per,unit,floor,ceil,operation);
        }
        return new VariableNumber<>(base,change);
    }

    public Number get(ServerLevel level){
        Number result = base;
        if(change() != Change.CONSTANT && change() != null){
            int unit = change().getUnitValue(level);
            int pers = (int) Math.floor(unit / change().per().doubleValue());
            double deltaDouble = change().value().doubleValue() * pers;
            result = result.doubleValue() + deltaDouble;
            if(base instanceof Integer){
                result = result.intValue();
            }
        }
        return result;
    }
}
