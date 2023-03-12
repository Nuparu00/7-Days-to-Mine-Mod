package nuparu.sevendaystomine.json.scrap;

import org.apache.commons.lang3.math.Fraction;

public class WeightWrapper {

    public static final WeightWrapper ZERO = new WeightWrapper(0);

    private final Number weight;

    @Override
    public String toString() {
        return weight.toString();
    }

    public WeightWrapper(Number weight){
        this.weight = weight;
    }

    public boolean isFraction(){
        return weight instanceof Fraction;
    }

    public Fraction asFraction(){
        return weight instanceof Fraction ? (Fraction) weight : Fraction.getFraction((double) weight);
    }

    public double asDouble(){
        return weight instanceof Double ? (double) weight : weight.doubleValue();
    }
    public Number asNumber(){
        return weight;
    }

    public Number divide(WeightWrapper other){
        return isFraction() || other.isFraction() ? asFraction().divideBy(other.asFraction()) : asDouble() / other.asDouble();
    }
}
