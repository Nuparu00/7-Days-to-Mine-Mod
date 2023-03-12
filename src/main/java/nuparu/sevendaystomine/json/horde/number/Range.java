package nuparu.sevendaystomine.json.horde.number;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import nuparu.sevendaystomine.util.MathUtils;

import java.util.Objects;

public record Range<T extends Number> (VariableNumber<T> min, VariableNumber<T> max){

    public static <T extends Number> Range<T> of(T number){
        VariableNumber<T> variableNumber = VariableNumber.of(number);
        return new Range<T>(variableNumber,variableNumber);
    }

    public Number get(ServerLevel level, RandomSource randomSource){
        Number min = min().get(level);
        Number max = max().get(level);

        if(Objects.equals(min, max)){
            return min;
        }

        if(min instanceof Integer || max instanceof Integer){
            return MathUtils.getIntInRange(randomSource,min.intValue(), max.intValue());
        }
        return MathUtils.getDoubleInRange(randomSource,min.doubleValue(), max.doubleValue());
    }
}
